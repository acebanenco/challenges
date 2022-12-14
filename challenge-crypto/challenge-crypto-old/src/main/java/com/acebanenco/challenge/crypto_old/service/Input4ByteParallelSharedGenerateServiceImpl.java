package com.acebanenco.challenge.crypto_old.service;

import com.acebanenco.challenge.crypto_old.model.CalculateHashes4ByteEncodeContext;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Input4ByteParallelSharedGenerateServiceImpl implements InputGenerateService {
    private final ThreadLocal<CalculateHashes4ByteEncodeContext> contextThreadLocal =
            ThreadLocal.withInitial(CalculateHashes4ByteEncodeContext::new);

    @Override
    public Stream<byte[]> generateInput() {
        return IntStream.rangeClosed(0, 100_000_000)
                // use as many cores as available
                .parallel()
                .mapToObj(number -> contextThreadLocal.get().encodeShared(number));
    }
}
