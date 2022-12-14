package com.acebanenco.challenge.opencl;

import java.util.List;
import java.util.NoSuchElementException;

public class CLDeviceSelector {

    public CLDevice getDevice(CLDeviceList list) {
        List<CLDevice> devices = list.getDevices();
        if (devices.isEmpty()) {
            throw new NoSuchElementException();
        }
        return devices.get(0);
    }
}
