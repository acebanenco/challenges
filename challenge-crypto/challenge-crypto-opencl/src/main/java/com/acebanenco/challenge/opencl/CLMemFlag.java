package com.acebanenco.challenge.opencl;

import static org.jocl.CL.*;

public enum CLMemFlag {

    READ_ONLY(CL_MEM_READ_ONLY),
    READ_WRITE(CL_MEM_READ_WRITE),
    WRITE_ONLY(CL_MEM_WRITE_ONLY),
    COPY_HOST_PTR(CL_MEM_COPY_HOST_PTR);

    private final long flag;

    CLMemFlag(long flag) {
        this.flag = flag;
    }

    long getFlag() {
        return flag;
    }

    boolean isReadable() {
        return switch (this) {
            case READ_ONLY, READ_WRITE -> true;
            case WRITE_ONLY, COPY_HOST_PTR -> false;
        };
    }

    boolean isWriteable() {
        return switch (this) {
            case READ_ONLY, COPY_HOST_PTR -> false;
            case READ_WRITE, WRITE_ONLY -> true;
        };
    }
}
