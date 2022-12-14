package com.acebanenco.challenge.opencl;

import static org.jocl.CL.*;

public enum CLDeviceType {

    ALL(CL_DEVICE_TYPE_ALL),
    CPU(CL_DEVICE_TYPE_CPU),
    ACCELERATOR(CL_DEVICE_TYPE_ACCELERATOR),
    GPU(CL_DEVICE_TYPE_GPU),
    DEFAULT(CL_DEVICE_TYPE_DEFAULT),
    CUSTOM(CL_DEVICE_TYPE_CUSTOM);

    private final long deviceType;

    CLDeviceType(long deviceType) {
        this.deviceType = deviceType;
    }

    long getDeviceType() {
        return deviceType;
    }
}
