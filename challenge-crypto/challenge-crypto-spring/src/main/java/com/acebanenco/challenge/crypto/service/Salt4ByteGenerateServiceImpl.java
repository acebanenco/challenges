package com.acebanenco.challenge.crypto.service;

import lombok.Setter;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Salt4ByteGenerateServiceImpl implements SaltGenerateService {

    private final CalculateHashes4ByteEncodeContext encodeContext = new CalculateHashes4ByteEncodeContext();
    @Setter
    private int minValue = Integer.MIN_VALUE;
    @Setter
    private int maxValue = Integer.MAX_VALUE;

    @Override
    public Stream<byte[]> generateSalt() {
        return intStream()
                .mapToObj(this::encode);
    }

    byte[] encode(int number) {
        return encodeContext().encodeShared(number);
    }

    CalculateHashes4ByteEncodeContext encodeContext() {
        return encodeContext;
    }

    IntStream intStream() {
        return IntStream.rangeClosed(minValue, maxValue);
    }

    static class CalculateHashes4ByteEncodeContext {
        private final ByteBuffer byteBuffer;
        private final IntBuffer intBuffer;

        CalculateHashes4ByteEncodeContext() {
            byteBuffer = ByteBuffer.allocate(4);
            intBuffer = byteBuffer.asIntBuffer();
        }

        byte[] encodeShared(int number) {
            intBuffer.rewind().put(number);
            return byteBuffer.array();
        }
    }

}