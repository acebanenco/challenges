package com.acebanenco.challenge.digits;

import com.acebanenco.challenge.digits.DigitsEncodeEngine.LastSymbolAndCount;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

public class DigitsEncodeOutputStream extends FilterOutputStream {

    private final DigitsEncodeEngine engine = new DigitsEncodeEngine();
    private LastSymbolAndCount lastSymbolAndCount;
    private final ByteBuffer output;
    private final WritableByteChannel outputChannel;

    public DigitsEncodeOutputStream(OutputStream out) {
        super(out);
        lastSymbolAndCount = new LastSymbolAndCount();
        output = ByteBuffer.allocate(8192);
        outputChannel = Channels.newChannel(out);
    }

    @Override
    public void write(int b) throws IOException {
        flushWriteBufferIfFull();
        if ( !engine.encode(lastSymbolAndCount, (byte)b, output) ) {
            throw new IOException("Unexpected encode problem, " +
                    "make sure write buffer has space for at least 2 digits");
        }
    }

    @Override
    public void write(byte[] bb, int off, int len) throws IOException {
        ByteBuffer input = ByteBuffer.wrap(bb, off, len);
        while ( input.hasRemaining() ) {
            flushWriteBufferIfFull();
            lastSymbolAndCount = engine.encode(lastSymbolAndCount, input, output);
        }
    }

    @Override
    public void flush() throws IOException {
        flushWriteBuffer();
    }

    private void flushWriteBufferIfFull() throws IOException {
        if (output.remaining() >= 2) {
            return;
        }
        flushWriteBuffer();
    }

    private void flushWriteBuffer() throws IOException {
        // reset position to 0, keep limit
        output.rewind();
        // flush bytes to the underlying channel/stream
        outputChannel.write(output);
        // reset position, set limit to capacity
        output.clear();
        // flush downstream
        out.flush();
    }
}
