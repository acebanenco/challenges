package com.acebanenco.challenge.crypto_simple.encode;

public interface IntEncoder {

    /**
     * Write 4 bytes integer at the beginning of the buffer.
     * @param value 4 bytes input value
     * @param buffer at least 4 bytes long buffer for the output
     */
    void writeInt(int value, byte[] buffer);

}
