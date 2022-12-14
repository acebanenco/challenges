package com.acebanenco.challenge.crypto_simple.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CalculateHashCountRequest {
    private final int rangeStart;
    private final int rangeEnd;
    private final int itemsPerTask;
    private final byte[] message;
}
