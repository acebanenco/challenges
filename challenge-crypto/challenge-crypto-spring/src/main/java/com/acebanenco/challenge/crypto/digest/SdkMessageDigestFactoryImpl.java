package com.acebanenco.challenge.crypto.digest;

import lombok.Setter;

import java.lang.reflect.Constructor;
import java.security.*;

public class SdkMessageDigestFactoryImpl implements MessageDigestFactory {

    @Setter
    private String providerName;
    @Setter
    private String providerClass;
    @Setter
    private String algorithm = "SHA-256";

    private MessageDigest prototype;

    @Override
    public MessageDigest getMessageDigest() {
        try {
            if ( prototype != null ) {
                return (MessageDigest) prototype.clone();
            }
            return newMessageDigest();
        } catch (CloneNotSupportedException |
                 NoSuchAlgorithmException |
                 NoSuchProviderException e) {
            throw new IllegalStateException(e);
        }
    }

    void createDigest() throws Exception {
        if ( providerName != null ) {
            if (Security.getProvider(providerName) == null ) {
                Security.insertProviderAt(createProvider(), 1);
            }
        }
        prototype = newMessageDigest();
        try {
            prototype.clone();
        } catch (CloneNotSupportedException ex) {
            prototype = null;
        }
    }

    private MessageDigest newMessageDigest()
            throws NoSuchAlgorithmException, NoSuchProviderException {
        if ( providerName == null ) {
            return MessageDigest.getInstance(algorithm);
        }
        return MessageDigest.getInstance(algorithm, providerName);
    }

    private Provider createProvider() throws Exception {
        Class<?> aClass = Class.forName(providerClass);
        Constructor<?> constructor = aClass.getConstructor();
        return (Provider) constructor.newInstance();
    }
}
