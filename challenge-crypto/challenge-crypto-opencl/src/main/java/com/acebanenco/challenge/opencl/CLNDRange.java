package com.acebanenco.challenge.opencl;

public class CLNDRange {

    final int globalWorkSize;

    public CLNDRange(int globalWorkSize) {
        this.globalWorkSize = globalWorkSize;
    }

    int getGlobalWorkSize() {
        return globalWorkSize;
    }
}
