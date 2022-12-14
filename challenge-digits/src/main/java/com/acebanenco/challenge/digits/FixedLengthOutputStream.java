package com.acebanenco.challenge.digits;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class FixedLengthOutputStream extends FilterOutputStream {

    private final byte[] separatorBytes;
    private final int maxLineLength;
    private int lineLength = 0;

    private FixedLengthOutputStream(OutputStream out, int maxLineLength, byte[] separatorBytes) {
        super(out);
        this.maxLineLength = maxLineLength;
        this.separatorBytes = separatorBytes;
    }

    public static FixedLengthOutputStream withFixedLineLength(OutputStream out, int lineLength) {
        String lineSeparator = System.getProperty("line.separator", "\n");
        byte[] lineSeparatorBytes = lineSeparator.getBytes(StandardCharsets.US_ASCII);
        return new FixedLengthOutputStream(out, lineLength, lineSeparatorBytes);
    }

    public static FixedLengthOutputStream withFixedWordLength(OutputStream out, int wordLength) {
        byte[] wordSeparator = " ".getBytes(StandardCharsets.US_ASCII);
        return new FixedLengthOutputStream(out, wordLength, wordSeparator);
    }

    @Override
    public void write(int b) throws IOException {
        if ( lineLength >= maxLineLength ) {
            newLine();
        }
        out.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        int bytesWritten = 0;
        while ( bytesWritten < len ) {
            int remaining = maxLineLength - lineLength;
            if (remaining <= 0) {
                newLine();
            } else if ( remaining >= len ) {
                out.write(b, off, len);
                bytesWritten += len;
            } else {
                out.write(b, off, remaining);
                bytesWritten += remaining;
                newLine();
            }
        }
    }

    private void newLine() throws IOException {
        out.write(separatorBytes);
        lineLength = 0;
    }
}
