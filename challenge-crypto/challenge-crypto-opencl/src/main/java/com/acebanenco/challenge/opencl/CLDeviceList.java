package com.acebanenco.challenge.opencl;

import java.util.List;

public class CLDeviceList implements AutoCloseable {

    List<CLDevice> devices;

    CLDeviceList(List<CLDevice> devices) {
        this.devices = devices;
    }

    List<CLDevice> getDevices() {
        return devices;
    }

    @Override
    public void close() {
        for (CLDevice device : devices) {
            device.close();
        }
    }
}
