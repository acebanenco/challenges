package com.acebanenco.challenge.opencl.tutorial;

import com.acebanenco.challenge.opencl.CLMemFlag;
import com.acebanenco.challenge.opencl.CLNDRange;
import com.acebanenco.challenge.opencl.annotation.Kernel;
import com.acebanenco.challenge.opencl.annotation.BufferParameter;
import com.acebanenco.challenge.opencl.annotation.ProgramResource;

@ProgramResource({
        "kernels/pbkdf2_sha256_32.cl",
        "kernels/sha256.cl",
})
public interface Sha256Prog extends AutoCloseable {

    @Kernel("hash_main")
    void hashMain(
            CLNDRange ndRange,
            @BufferParameter(flags = {CLMemFlag.READ_ONLY, CLMemFlag.COPY_HOST_PTR})
            byte[] inbuffer,
            @BufferParameter(flags = {CLMemFlag.READ_WRITE})
            byte[] outbuffer);
}
