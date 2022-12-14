package com.acebanenco.challenge.opencl;

import static org.jocl.CL.CL_DEVICE_NAME;

public enum CLDeviceInfoKey {

    DEVICE_NAME(CL_DEVICE_NAME);

    private final int parameter;

    CLDeviceInfoKey(int parameter) {
        this.parameter = parameter;
    }

    int getParameter() {
        return parameter;
    }
}
