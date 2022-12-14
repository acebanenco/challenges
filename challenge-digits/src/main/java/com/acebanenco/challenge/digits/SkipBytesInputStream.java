package com.acebanenco.challenge.digits;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class SkipBytesInputStream extends FilterInputStream {

    private final byte[] skipBytes;

    public SkipBytesInputStream(byte[] skipBytes, InputStream in) {
        super(in);
        this.skipBytes = Arrays.copyOf(skipBytes, skipBytes.length);
        Arrays.sort(this.skipBytes);
    }

    @Override
    public int read() throws IOException {
        while(true) {
            int nextByte = in.read();
            if (nextByte < 0) {
                return nextByte;
            }
            int byteIndex = Arrays.binarySearch(skipBytes, (byte) nextByte);
            if (byteIndex < 0) {
                return nextByte;
            }
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int bytesRead = in.read(b, off, len);
        if ( bytesRead < 0) {
            return bytesRead;
        }
        int actualBytesRead = 0;
        for (int readIndex = 0; readIndex < bytesRead; readIndex++) {
            byte nextByte = b[readIndex];
            int byteIndex = Arrays.binarySearch(skipBytes, nextByte);
            if ( byteIndex < 0 ) {
                b[off + (actualBytesRead++)] = nextByte;
            }
        }
        return actualBytesRead;
    }
}
