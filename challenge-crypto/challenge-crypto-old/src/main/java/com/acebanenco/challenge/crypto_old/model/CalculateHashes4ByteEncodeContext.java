package com.acebanenco.challenge.crypto_old.model;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class CalculateHashes4ByteEncodeContext {
    private final ByteBuffer byteBuffer;
    private final IntBuffer intBuffer;

    public CalculateHashes4ByteEncodeContext() {
        byteBuffer = ByteBuffer.allocate(4);
        intBuffer = byteBuffer.asIntBuffer();
    }

    public byte[] encodeShared(int number) {
        intBuffer.rewind().put(number);
        return byteBuffer.array();
    }
}
