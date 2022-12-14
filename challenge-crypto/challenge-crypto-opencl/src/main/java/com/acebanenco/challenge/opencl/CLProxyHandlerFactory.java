package com.acebanenco.challenge.opencl;

import com.acebanenco.challenge.opencl.annotation.Kernel;
import com.acebanenco.challenge.opencl.annotation.ProgramResource;
import com.acebanenco.challenge.opencl.annotation.ProgramSource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;

public class CLProxyHandlerFactory<T> implements AutoCloseable {

    final CLExecution execution;
    final Class<T> proxyClass;
    final HashMap<Method, CLKernel> kernels;
    final CLDeviceList devices;
    final CLDevice device;
    final CLContext context;
    final CLProgram program;

    public CLProxyHandlerFactory(CLExecution execution, Class<T> proxyClass) {
        this.execution = execution;
        this.proxyClass = validateProxyClass(proxyClass);

        this.kernels = new HashMap<>();
        CLPlatformFactory platformFactory = new CLPlatformFactory(execution.platformSelector);
        CLPlatform platform = platformFactory.getPlatform();
        this.devices = platform.getDevices();
        this.device = execution.deviceSelector.getDevice(devices);
        this.context = device.getContext();
        this.program = getProgram(proxyClass, context);
    }

    public T getImplementation(CLContextAction contextAction) {
        ClassLoader classLoader = proxyClass.getClassLoader();
        Class<?>[] interfaces = {proxyClass};
        InvocationHandler handler = new CLExecutionHandler(this, contextAction);
        Object proxy = Proxy.newProxyInstance(classLoader, interfaces, handler);
        return proxyClass.cast(proxy);
    }

    private Class<T> validateProxyClass(Class<T> proxyClass) {
        for (Method method : proxyClass.getMethods()) {
            if (method.getDeclaringClass() == Object.class) {
                continue;
            }
            if (method.getDeclaringClass() == AutoCloseable.class) {
                continue;
            }
            if (!method.isAnnotationPresent(Kernel.class)) {
                throw new IllegalArgumentException("Not a kernel method: " + method);
            }
        }
        return proxyClass;
    }

    private CLProgram getProgram(Class<?> proxyClass, CLContext context) {
        String[] programSources = getProgramSources(proxyClass);
        return context.getProgram(programSources);
    }

    private String[] getProgramSources(Class<?> proxyClass) {
        ProgramSource sources = proxyClass.getAnnotation(ProgramSource.class);
        if (sources != null) {
            return sources.value();
        }
        ProgramResource resources = proxyClass.getAnnotation(ProgramResource.class);
        if (resources != null) {
            ClassLoader classLoader = proxyClass.getClassLoader();
            return Arrays.stream(resources.value())
                    .map(resource -> execution.resourceLoader.loadResource(classLoader, resource))
                    .toArray(String[]::new);
        }
        throw new IllegalArgumentException("Not a program class: " + proxyClass);
    }

    @Override
    public void close() {
        kernels.values().forEach(CLKernel::close);
        program.close();
        context.close();
        devices.close();
    }
}
