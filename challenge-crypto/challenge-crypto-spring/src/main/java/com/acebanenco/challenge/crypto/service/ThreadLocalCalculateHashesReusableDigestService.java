package com.acebanenco.challenge.crypto.service;

import com.acebanenco.challenge.crypto.digest.MessageDigestFactory;
import com.acebanenco.challenge.crypto.model.DigestMessage;
import com.acebanenco.challenge.crypto.service.CalculateHashesService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.security.DigestException;
import java.security.MessageDigest;

@RequiredArgsConstructor
public class ThreadLocalCalculateHashesReusableDigestService implements CalculateHashesService {

    private final ThreadLocal<DigestContext> contextThreadLocal =
            ThreadLocal.withInitial(this::createDigestContext);

    private final MessageDigestFactory messageDigestFactory;

    DigestContext createDigestContext() {
        return new DigestContext(messageDigestFactory.getMessageDigest());
    }

    @Override
    public DigestMessage digestForSalt(byte[] input) {
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
            byte[] salt,
            byte[] sharedDigest) {
        messageDigest.reset();
        messageDigest.update(salt);
        try {
            messageDigest.digest(sharedDigest, 0, sharedDigest.length);
        } catch (DigestException e) {
            throw new RuntimeException(e);
        }
        return sharedDigest;
    }

    static class DigestContext {
        @Getter
        private final MessageDigest messageDigest;
        private final byte[] sharedDigest;

        public DigestContext(MessageDigest messageDigest) {
            this.messageDigest = messageDigest;
            this.sharedDigest = new byte[messageDigest.getDigestLength()];
        }
    }

}
