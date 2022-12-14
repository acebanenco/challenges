package com.acebanenco.challenge.opencl;

import static org.jocl.CL.CL_PLATFORM_NAME;

public enum CLPlatformInfoKey {

    PLATFORM_NAME(CL_PLATFORM_NAME);

    private final int parameter;

    CLPlatformInfoKey(int parameter) {
        this.parameter = parameter;
    }

    int getParameter() {
        return parameter;
    }
}
