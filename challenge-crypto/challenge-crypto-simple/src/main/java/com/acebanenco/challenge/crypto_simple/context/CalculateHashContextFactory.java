package com.acebanenco.challenge.crypto_simple.context;

import com.acebanenco.challenge.crypto_simple.digest.MessageDigestFactory;
import lombok.RequiredArgsConstructor;

import java.security.MessageDigest;

@RequiredArgsConstructor
public class CalculateHashContextFactory {
    private final MessageDigestFactory messageDigestFactory;

    public CalculateHashContext newContext() {
        byte[] input = new byte[4];
        MessageDigest messageDigest = getMessageDigest();
        return new CalculateHashContext(messageDigest, input);
    }

    private MessageDigest getMessageDigest() {
        return messageDigestFactory.getMessageDigest();
    }

}
