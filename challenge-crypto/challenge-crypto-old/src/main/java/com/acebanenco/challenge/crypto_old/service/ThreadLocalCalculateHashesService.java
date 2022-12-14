package com.acebanenco.challenge.crypto_old.service;

import com.acebanenco.challenge.crypto_old.model.DigestMessage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ThreadLocalCalculateHashesService implements CalculateHashesService {

    private final ThreadLocal<MessageDigest> contextThreadLocal =
            ThreadLocal.withInitial(this::createDigest);

    private MessageDigest createDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DigestMessage digestForInput(byte[] input) {
        MessageDigest messageDigest = contextThreadLocal.get();
        byte[] digest = messageDigest.digest(input);
        return new DigestMessage(input, digest);
    }

}
