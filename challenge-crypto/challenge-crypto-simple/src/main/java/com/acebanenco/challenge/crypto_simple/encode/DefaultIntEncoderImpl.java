package com.acebanenco.challenge.crypto_simple.encode;

public class DefaultIntEncoderImpl implements IntEncoder {

    @Override
    public void writeInt(int value, byte[] buffer) {
        buffer[0] = (byte) (value >>> 24);
        buffer[1] = (byte) (value >>> 16);
        buffer[2] = (byte) (value >>> 8);
        buffer[3] = (byte) value;
    }
}
