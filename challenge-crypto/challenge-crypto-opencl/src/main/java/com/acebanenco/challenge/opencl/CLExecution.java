package com.acebanenco.challenge.opencl;

import com.acebanenco.challenge.opencl.annotation.ProgramResource;
import com.acebanenco.challenge.opencl.annotation.ProgramSource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CLExecution {

    private static final Map<Class<?>, CLProgram> programCache = new HashMap<>();

    final CLPlatformSelector platformSelector;
    final CLDeviceSelector deviceSelector;
    final CLResourceLoader resourceLoader;

    public CLExecution() {
        this(new CLPlatformSelector(), new CLDeviceSelector(), new CLResourceLoader());
    }

    public CLExecution(
            CLPlatformSelector platformSelector,
            CLDeviceSelector deviceSelector,
            CLResourceLoader resourceLoader) {
        this.platformSelector = platformSelector;
        this.deviceSelector = deviceSelector;
        this.resourceLoader = resourceLoader;
    }

    public <T extends AutoCloseable> CLProxyHandlerFactory<T> getHandlerFactory(Class<T> proxyClass) {
        return new CLProxyHandlerFactory<T>(this, proxyClass);
    }


}
