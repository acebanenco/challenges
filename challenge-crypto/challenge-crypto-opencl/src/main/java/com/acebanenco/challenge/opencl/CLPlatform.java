package com.acebanenco.challenge.opencl;

import org.jocl.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.jocl.CL.*;
import static org.jocl.CL.clCreateContext;

public class CLPlatform extends CLObject<cl_platform_id> {

    CLPlatform(cl_platform_id peer) {
        super(peer, p -> {});
    }

    public CLDeviceList getDevices() {
        return getDevices(CLDeviceType.ALL);
    }

    public CLDeviceList getDevices(CLDeviceType deviceType) {
        int[] numDevicesArray = new int[1];
        clGetDeviceIDs(getPeer(), deviceType.getDeviceType(), 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];

        // Obtain a device IDs
        cl_device_id[] deviceIds = new cl_device_id[numDevices];
        clGetDeviceIDs(getPeer(), deviceType.getDeviceType(), numDevices, deviceIds, null);

        List<CLDevice> devices = Arrays.stream(deviceIds)
                .map(devicePeer -> new CLDevice(devicePeer, this))
                .collect(Collectors.toList());
        return new CLDeviceList(devices);
    }

    public CLContext getContext(List<CLDevice> devices) {
        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, getPeer());

        // Create a context for the devices
        cl_device_id[] device_ids = devices.stream()
                .map(CLObject::getPeer)
                .toArray(cl_device_id[]::new);

        cl_context context = clCreateContext(
                contextProperties, devices.size(), device_ids, null, null, null);

        return new CLContext(context, this, devices);
    }

}
