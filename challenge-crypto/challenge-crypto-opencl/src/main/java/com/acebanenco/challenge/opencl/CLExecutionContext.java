package com.acebanenco.challenge.opencl;

import java.lang.reflect.Method;

public class CLExecutionContext {
    final CLDevice device;
    final CLKernel kernel;
    final Method method;
    final Object[] args;

    CLExecutionContext(CLDevice device, CLKernel kernel, Method method, Object[] args) {
        this.device = device;
        this.kernel = kernel;
        this.method = method;
        this.args = args;
    }


    public CLDevice getDevice() {
        return device;
    }

    public CLKernel getKernel() {
        return kernel;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }
}
