package com.acebanenco.challenge.opencl;

import org.jocl.CL;
import org.jocl.cl_kernel;
import org.jocl.cl_program;

import static org.jocl.CL.clCreateKernel;

public class CLProgram extends CLObject<cl_program> {

    final CLContext context;

    CLProgram(cl_program peer, CLContext context) {
        super(peer, CL::clReleaseProgram);
        this.context = context;
    }

    CLContext getContext() {
        return context;
    }

    public CLKernel getKernel(String kernelName) {
        cl_kernel kernel = clCreateKernel(getPeer(), kernelName, null);
        return new CLKernel(kernel, this);
    }
}
