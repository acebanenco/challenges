package com.acebanenco.challenge.crypto.service;

import com.acebanenco.challenge.crypto.digest.MessageDigestFactory;
import com.acebanenco.challenge.crypto.model.DigestMessage;
import com.acebanenco.challenge.crypto.service.CalculateHashesService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.DigestException;
import java.security.MessageDigest;

@RequiredArgsConstructor
public class ThreadLocalCalculateHashesReusableMessageDigestService implements CalculateHashesService {

    @Setter
    private Resource message;
    private byte[] messageBytes;

    private final ThreadLocal<DigestContext> contextThreadLocal =
            ThreadLocal.withInitial(this::createDigestContext);

    private final MessageDigestFactory messageDigestFactory;

    void loadMessage() throws IOException {
        if ( message == null ) {
            messageBytes = new byte[0];
        } else {
            try (InputStream input = message.getInputStream();) {
                messageBytes = FileCopyUtils.copyToByteArray(input);
            }
        }
    }

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
        return digest(messageDigest, input, messageBytes, sharedDigest);
    }

    private static byte[] digest(
            MessageDigest messageDigest,
            byte[] message,
            byte[] salt,
            byte[] sharedDigest) {
        messageDigest.reset();
        messageDigest.update(salt);
        messageDigest.update(message);
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
