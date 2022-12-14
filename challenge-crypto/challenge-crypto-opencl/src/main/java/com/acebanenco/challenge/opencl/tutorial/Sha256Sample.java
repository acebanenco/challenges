package com.acebanenco.challenge.opencl.tutorial;

import com.acebanenco.challenge.opencl.CLContextNDRangeAction;
import com.acebanenco.challenge.opencl.CLExecution;
import com.acebanenco.challenge.opencl.CLNDRange;
import com.acebanenco.challenge.opencl.CLProxyHandlerFactory;

import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

public class Sha256Sample {
    final Sha256Prog program;
    static final int INBUF_SIZE = 4 * (1 + 32);
    static final int OUTBUF_SIZE = 4 * 8;

    final int globalWorkSize;
    byte[] inbuffer;
    byte[] outbuffer;

    public Sha256Sample(Sha256Prog program, int globalWorkSize) {
        this.program = program;
        this.globalWorkSize = globalWorkSize;
        this.inbuffer = getInbuffer(globalWorkSize);
        this.outbuffer = getOutbuffer(globalWorkSize);
    }

    private static byte[] getInbuffer(int globalWorkSize) {
        return new byte[INBUF_SIZE * globalWorkSize];
    }

    private static byte[] getOutbuffer(int globalWorkSize) {
        return new byte[OUTBUF_SIZE * globalWorkSize];
    }

    public int findMatchingHashCount(int globalWorkSize, int rangeStart, int rangeEnd) {
        int batchCount = Math.toIntExact((rangeEnd - rangeStart + globalWorkSize - 1) / globalWorkSize);
        return IntStream.range(0, batchCount)
                .map(batch -> {
                    int offset = rangeStart + batchCount * globalWorkSize;
                    int submittedBatches = writeBatch(globalWorkSize, rangeEnd, offset);
                    doFinal(submittedBatches);
                    return getMatchCount(submittedBatches);
                }).sum();
    }

    private int writeBatch(int globalWorkSize, int rangeEnd, int offset) {
        IntUnaryOperator values = batchId ->
                Math.toIntExact(offset + (long) batchId);
        IntPredicate takeWhilePredicate = batchId -> {
            try {
                return values.applyAsInt(batchId) < rangeEnd;
            } catch (ArithmeticException ex) {
                return false;
            }
        };
        return writeBatch(globalWorkSize, values, takeWhilePredicate);
    }

    private int writeBatch(int globalWorkSize, IntUnaryOperator values, IntPredicate takeWhilePredicate) {
        return (int) IntStream.range(0, globalWorkSize)
                .takeWhile(takeWhilePredicate)
                .map(batchId -> {
                    int value = values.applyAsInt(batchId);
                    putValue(batchId, value);
                    return value;
                })
                .count();
    }

    private void putValue(int batchId, int value) {
        int writeIndex = batchId * INBUF_SIZE;
        // length
        writeInt(4, inbuffer, writeIndex);
        // message
        writeInt(value, inbuffer, writeIndex + 4);
    }

    private void doFinal(int submittedBatches) {
        program.hashMain(new CLNDRange(submittedBatches), inbuffer, outbuffer);
    }

    private static void writeInt(int value, byte[] buffer, int offset) {
        // little endian
        buffer[offset] = (byte) value;
        buffer[offset + 1] = (byte) (value >>> 8);
        buffer[offset + 2] = (byte) (value >>> 16);
        buffer[offset + 3] = (byte) (value >>> 24);
    }

    private int getMatchCount(int submittedBatches) {
        int count = 0;
        for (int i = 0; i < submittedBatches; i++) {
            int readIndex = OUTBUF_SIZE * i;
            if (outbuffer[readIndex] == 0) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        int globalWorkSize = 100_000;
//        long rangeStart = Integer.MIN_VALUE;
//        long rangeEnd = Integer.MAX_VALUE;
        int rangeStart = 0;
        int rangeEnd = 100_000_000;

        int matchCount;
        Sha256Prog program = getSha256Prog();
        Sha256Sample sample = new Sha256Sample(program, globalWorkSize);
        matchCount = sample.findMatchingHashCount(globalWorkSize, rangeStart, rangeEnd);
        System.out.println("Found " + matchCount + " matching hashes");
    }

    private static Sha256Prog getSha256Prog() {
        CLExecution execution = new CLExecution();
        CLContextNDRangeAction contextAction = new CLContextNDRangeAction();
        CLProxyHandlerFactory<Sha256Prog> handlerFactory = execution.getHandlerFactory(Sha256Prog.class);
        return handlerFactory.getImplementation(contextAction);
    }

}
