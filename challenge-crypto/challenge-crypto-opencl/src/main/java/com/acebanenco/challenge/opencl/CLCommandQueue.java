package com.acebanenco.challenge.opencl;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;

import static org.jocl.CL.*;

public class CLCommandQueue extends CLObject<cl_command_queue> {

    final CLDevice device;
    final CLContext context;

    CLCommandQueue(cl_command_queue peer, CLDevice device, CLContext context) {
        super(peer, CL::clReleaseCommandQueue);
        this.device = device;
        this.context = context;
    }

    public void readBlocking(CLMem buffer) {
        // Read the output data
        CLMemInfo memInfo = buffer.getInfo();
        long bufferSize = memInfo.getArrayLength() * (long) memInfo.getComponentSize();
        Pointer hostPtr = memInfo.getHostPtr();
        clEnqueueReadBuffer(
                getPeer(),
                buffer.getPeer(),
                CL_TRUE,
                0,
                bufferSize,
                hostPtr,
                0,
                null,
                null);

    }

}
