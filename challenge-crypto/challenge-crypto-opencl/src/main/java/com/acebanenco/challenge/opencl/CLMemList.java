package com.acebanenco.challenge.opencl;

public class CLMemList implements AutoCloseable {

    final CLMem[] buffers;

    CLMemList(CLMem[] buffers) {
        this.buffers = buffers;
    }

    CLMem[] getBuffers() {
        return buffers;
    }

    @Override
    public void close() {
        for (CLMem buffer : buffers) {
            buffer.close();
        }
    }
}
