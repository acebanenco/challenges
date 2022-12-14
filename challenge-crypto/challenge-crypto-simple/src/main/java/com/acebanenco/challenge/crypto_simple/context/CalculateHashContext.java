package com.acebanenco.challenge.crypto_simple.context;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.MessageDigest;

@Getter
@RequiredArgsConstructor
public class CalculateHashContext {
    final MessageDigest messageDigest;
    final byte[] input;
    final byte[] digest;
    int number;

    public CalculateHashContext(MessageDigest messageDigest, byte[] input) {
        this(messageDigest, input, new byte[messageDigest.getDigestLength()]);
    }
}
