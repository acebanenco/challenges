package com.acebanenco.challenge.crypto_old.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DigestMessage {
    private final byte[] input;
    private final byte[] digest;
}
