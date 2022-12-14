package com.acebanenco.challenge.crypto.service;

import java.util.stream.IntStream;

public class Salt4ByteParallelGenerateServiceImpl extends Salt4ByteGenerateServiceImpl {

    private final ThreadLocal<CalculateHashes4ByteEncodeContext> contextThreadLocal =
            ThreadLocal.withInitial(CalculateHashes4ByteEncodeContext::new);

    CalculateHashes4ByteEncodeContext encodeContext() {
        return contextThreadLocal.get();
    }

    IntStream intStream() {
        return super.intStream()
                // use as many cores as available
                .parallel();
    }
}
