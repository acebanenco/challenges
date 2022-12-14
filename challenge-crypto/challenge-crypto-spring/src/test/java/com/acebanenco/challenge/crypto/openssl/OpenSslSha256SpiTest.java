package com.acebanenco.challenge.crypto.openssl;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class OpenSslSha256SpiTest {

    @Test
    void testHashCompare() throws NoSuchAlgorithmException {
        MessageDigest openSslHasher = MessageDigest.getInstance("SHA-256", new OpenSslProvider());
        MessageDigest javaHasher = MessageDigest.getInstance("SHA-256");

        HexFormat hexFormat = HexFormat.ofDelimiter(" ");

        byte[] input = new byte[4];
        IntBuffer intBuffer = ByteBuffer.wrap(input).asIntBuffer();
        for (int i = 0; i < 1000; i++) {
            intBuffer.rewind().put(i);
            byte[] javaHash = javaHasher.digest(input);
            byte[] openSslHash = openSslHasher.digest(input);
            assertArrayEquals(javaHash, openSslHash, "For input " + hexFormat.formatHex(input));
        }

    }

}