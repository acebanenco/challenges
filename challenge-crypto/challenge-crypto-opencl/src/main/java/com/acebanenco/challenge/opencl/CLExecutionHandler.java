package com.acebanenco.challenge.opencl;

import com.acebanenco.challenge.opencl.annotation.Kernel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

class CLExecutionHandler implements InvocationHandler {

    private final CLProxyHandlerFactory<?> handlerFactory;
    private final CLContextAction contextAction;

    CLExecutionHandler(CLProxyHandlerFactory<?> handlerFactory, CLContextAction contextAction) {
        this.handlerFactory = handlerFactory;
        this.contextAction = contextAction;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
        }
        if (method.getDeclaringClass() == AutoCloseable.class) {
            return method.invoke(this, args);
        }

        CLKernel kernel = handlerFactory.kernels.computeIfAbsent(method, mthd -> {
            String kernelName = getKernelName(mthd);
            return handlerFactory.program.getKernel(kernelName);
        });

        CLExecutionContext executionContext = new CLExecutionContext(handlerFactory.device, kernel, method, args);
        contextAction.executeWithContext(executionContext);
        return null;
    }

    private static String getKernelName(Method method) {
        Kernel kernelAnnotation = method.getAnnotation(Kernel.class);
        if (kernelAnnotation == null) {
            throw new IllegalStateException();
        }
        return kernelAnnotation.value();
    }

}
