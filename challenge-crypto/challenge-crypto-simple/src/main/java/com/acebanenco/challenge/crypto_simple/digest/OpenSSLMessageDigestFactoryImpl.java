package com.acebanenco.challenge.crypto_simple.digest;

import com.acebanenco.challenge.crypto.openssl.OpenSslProvider;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;

public class OpenSSLMessageDigestFactoryImpl implements MessageDigestFactory {

    private final Provider provider = new OpenSslProvider();

    @Override
    public MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256", provider);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
