package com.acebanenco.challenge.crypto_old.service;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Input4ByteParallelGenerateServiceImpl implements InputGenerateService {
    @Override
    public Stream<byte[]> generateInput() {
        return IntStream.rangeClosed(Integer.MIN_VALUE, Integer.MAX_VALUE)
                .mapToObj(number ->
                        new byte[]{
                                (byte) (number >>> 24),
                                (byte) (number >>> 16),
                                (byte) (number >>> 8),
                                (byte) (number)
                        });
    }
}
