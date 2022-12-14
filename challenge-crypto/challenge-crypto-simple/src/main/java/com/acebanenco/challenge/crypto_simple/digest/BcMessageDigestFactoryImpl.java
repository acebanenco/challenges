package com.acebanenco.challenge.crypto_simple.digest;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;

public class BcMessageDigestFactoryImpl implements MessageDigestFactory {

    private final Provider provider = new BouncyCastleProvider();

    @Override
    public MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256", provider);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
