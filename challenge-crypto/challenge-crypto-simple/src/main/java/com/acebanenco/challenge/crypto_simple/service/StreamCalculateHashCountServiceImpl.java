package com.acebanenco.challenge.crypto_simple.service;

import com.acebanenco.challenge.crypto_simple.context.CalculateHashContext;
import com.acebanenco.challenge.crypto_simple.context.CalculateHashContextPool;
import com.acebanenco.challenge.crypto_simple.encode.IntEncoder;
import com.acebanenco.challenge.crypto_simple.model.CalculateHashCountRequest;
import lombok.RequiredArgsConstructor;

import java.security.DigestException;
import java.security.MessageDigest;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class StreamCalculateHashCountServiceImpl implements CalculateHashCountService {

    private final IntEncoder intEncoder;
    private final CalculateHashContextPool calculateHashContextPool;

    @Override
    public int getHashCount(CalculateHashCountRequest request) {
        int from = request.getRangeStart();
        int to = request.getRangeEnd();
        int itemsPerTask = request.getItemsPerTask();
        byte[] message = request.getMessage();

        int numberOfTasks = (to - from + itemsPerTask - 1) / itemsPerTask;
        return IntStream.rangeClosed(0, numberOfTasks)
                .map(number ->
                        from + itemsPerTask * number)
                .parallel()
                .map(rangeStart -> {
                    int rangeEnd = Math.min(rangeStart + itemsPerTask, to);
                    return getNumberOfMatches(message, rangeStart, rangeEnd);
                })
                .sum();
    }


    private int getNumberOfMatches(
            byte[] message,
            int rangeStart,
            int rangeEnd) {
        CalculateHashContext context = calculateHashContextPool.acquire();
        try {
            MessageDigest messageDigest = context.getMessageDigest();
            byte[] input = context.getInput();
            byte[] digest = context.getDigest();
            return getNumberOfMatches(messageDigest, message, rangeStart, rangeEnd, input, digest);
        } finally {
            calculateHashContextPool.release(context);
        }
    }


    private int getNumberOfMatches(
            MessageDigest messageDigest,
            byte[] message,
            int rangeStart,
            int rangeEnd,
            byte[] input,
            byte[] digest) {
        return (int) IntStream.range(rangeStart, rangeEnd)
                .filter(number ->
                        saltMatches(messageDigest, input, message, digest, number))
                .count();
    }

    private boolean saltMatches(
            MessageDigest messageDigest,
            byte[] input,
            byte[] message,
            byte[] digest,
            int number) {
        intEncoder.writeInt(number, input);
        try {
            return saltMatches(messageDigest, input, message, digest);
        } catch (DigestException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean saltMatches(
            MessageDigest messageDigest,
            byte[] input,
            byte[] message,
            byte[] digest)
            throws DigestException {
        messageDigest.update(input);
        messageDigest.update(message);
        messageDigest.digest(digest, 0, digest.length);
        return digest[0] == 0;
    }
}
