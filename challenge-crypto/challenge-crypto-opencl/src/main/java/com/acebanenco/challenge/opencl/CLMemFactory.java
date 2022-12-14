package com.acebanenco.challenge.opencl;

import org.jocl.Pointer;
import org.jocl.cl_mem;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.jocl.CL.CL_MEM_COPY_HOST_PTR;
import static org.jocl.CL.clCreateBuffer;

public class CLMemFactory {

    final CLMemFlag[] flags;

    final CLContext context;

    CLMemFactory(CLContext context, CLMemFlag... flags) {
        this.flags = Arrays.copyOf(flags, flags.length);
        this.context = context;
    }

    private static long getMemFlags(CLMemFlag[] flags) {
        return Stream.of(flags)
                .mapToLong(CLMemFlag::getFlag)
                .reduce(0L, (left, right) -> left | right);
    }

    public CLMem getBuffer(Object input) {
        if ( !input.getClass().isArray() ) {
            throw new IllegalArgumentException();
        }
        CLMemInfo memInfo = CLMemInfoFactory.getMemInfo(input);
        return getClMem(memInfo);
    }

    private CLMem getClMem(CLMemInfo memInfo) {
        long memFlags = getMemFlags(flags);
        long bufferSize = memInfo.getComponentSize() * (long) memInfo.getArrayLength();
        boolean copyHostPtr = (memFlags & CL_MEM_COPY_HOST_PTR) != 0L;
        Pointer hostPtr = copyHostPtr ? memInfo.getHostPtr() : null;
        cl_mem clMem = clCreateBuffer(
                context.getPeer(),
                memFlags,
                bufferSize,
                hostPtr,
                null);
        return new CLMem(clMem, this, memInfo);
    }

    boolean isReadable() {
        for (CLMemFlag flag : flags) {
            if (flag.isReadable() ) {
                return true;
            }
        }
        return false;
    }

    boolean isWriteable() {
        for (CLMemFlag flag : flags) {
            if (flag.isWriteable() ) {
                return true;
            }
        }
        return false;
    }

}
