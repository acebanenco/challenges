package com.acebanenco.challenge.crypto_old.service;

import com.acebanenco.challenge.crypto_old.model.DigestMessage;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ThreadLocalCalculateHashesSharedDigestService implements CalculateHashesService {

    private final ThreadLocal<DigestContext> contextThreadLocal =
            ThreadLocal.withInitial(this::createDigestContext);

    private DigestContext createDigestContext() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return new DigestContext(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DigestMessage digestForInput(byte[] input) {
        DigestContext context = contextThreadLocal.get();
        byte[] digest = digest(context, input);
        return new DigestMessage(input, digest);
    }

    private byte[] digest(DigestContext context, byte[] input) {
        MessageDigest messageDigest = context.messageDigest;
        byte[] sharedDigest = context.sharedDigest;
        return digest(messageDigest, input, sharedDigest);
    }

    private static byte[] digest(
            MessageDigest messageDigest,
            byte[] input,
            byte[] sharedDigest) {
        messageDigest.reset();
        messageDigest.update(input);
        try {
            messageDigest.digest(sharedDigest, 0, sharedDigest.length);
        } catch (DigestException e) {
            throw new RuntimeException(e);
        }
        return sharedDigest;
    }

    private static class DigestContext {
        private final MessageDigest messageDigest;
        private final byte[] sharedDigest;

        public DigestContext(MessageDigest messageDigest) {
            this.messageDigest = messageDigest;
            this.sharedDigest = new byte[messageDigest.getDigestLength()];
        }
    }

}
