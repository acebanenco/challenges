package com.acebanenco.challenge.opencl;

import org.jocl.CL;
import org.jocl.cl_mem;

public class CLMem extends CLObject<cl_mem> implements AutoCloseable {

    final CLMemFactory factory;
    final CLMemInfo info;

    CLMem(cl_mem peer, CLMemFactory factory, CLMemInfo info) {
        super(peer, CL::clReleaseMemObject);
        this.factory = factory;
        this.info = info;
    }

    CLMemFactory getFactory() {
        return factory;
    }

    CLMemInfo getInfo() {
        return info;
    }
}
