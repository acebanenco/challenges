package com.acebanenco.challenge.crypto_simple.digest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DefaultMessageDigestFactoryImpl implements MessageDigestFactory {
    @Override
    public MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
