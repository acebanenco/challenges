package com.acebanenco.challenge.opencl;

import org.jocl.*;

import java.util.List;
import java.util.function.Consumer;

import static org.jocl.CL.clCreateCommandQueueWithProperties;
import static org.jocl.CL.clGetDeviceInfo;

public class CLDevice extends CLObject<cl_device_id> {

    private final CLPlatform platform;

    CLDevice(cl_device_id peer, CLPlatform platform) {
        super(peer, CL::clReleaseDevice);
        this.platform = platform;
    }

    public CLPlatform getPlatform() {
        return platform;
    }

    public String getDeviceName() {
        return getInfo(CLDeviceInfoKey.DEVICE_NAME);
    }

    public String getInfo(CLDeviceInfoKey infoKey) {
        return getInfo(getPeer(), infoKey.getParameter());
    }


    private static String getInfo(cl_device_id device, int paramName) {
        // Obtain the length of the string that will be queried
        long[] size = new long[1];
        clGetDeviceInfo(device, paramName, 0, null, size);

        // Create a buffer of the appropriate size and fill it with the info
        byte[] buffer = new byte[(int)size[0]];
        clGetDeviceInfo(device, paramName, buffer.length, Pointer.to(buffer), null);

        // Create a string from the buffer (excluding the trailing \0 byte)
        return new String(buffer, 0, buffer.length-1);
    }

    public CLContext getContext() {
        return platform.getContext(List.of(this));
    }

    public CLCommandQueue getQueue(CLContext context) {
        cl_queue_properties properties = new cl_queue_properties();
        cl_command_queue commandQueue = clCreateCommandQueueWithProperties(
                context.getPeer(), getPeer(), properties, null);
        return new CLCommandQueue(commandQueue, this, context);
    }


    public void executeWithKernel(CLProgram program, String kernelName, Consumer<CLKernel> action) {
        try (CLKernel kernel = program.getKernel(kernelName)) {
            action.accept(kernel);
        }
    }

}
