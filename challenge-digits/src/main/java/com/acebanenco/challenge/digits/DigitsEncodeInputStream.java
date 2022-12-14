package com.acebanenco.challenge.digits;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DigitsEncodeInputStream extends FilterInputStream {

    private final byte[] buffer = new byte[2];
    private int count;
    private int position;
    private int next;

    public DigitsEncodeInputStream(InputStream in) throws IOException {
        super(in);
        next = in.read();
    }

    @Override
    public int read() throws IOException {
        if (position >= count) {
            fillBuffer();
            if (position >= count) {
                return -1;
            }
        }
        return buffer[position++] & 0xFF;
    }

    private void fillBuffer() throws IOException {
        count = 0;
        position = 0;
        while (count < buffer.length) {
            int curr = next;
            if (curr == -1) {
                return;
            }
            int currCount = 1;
            while (curr == (next = in.read()) && currCount < 10) {
                currCount++;
            }

            buffer[count++] = (byte) (currCount + '0');
            buffer[count++] = (byte) curr; // buffer length should be even
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        for (int pos = 0; pos < len; pos++) {
            int ch = read();
            if (ch < 0) {
                    return pos == 0 ? -1 : pos;
            }
            b[pos + off] = (byte) ch;
        }
        return len;
    }

    @Override
    public long skip(long n) throws IOException {
        return super.skip(n);
    }
}
