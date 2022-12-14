package com.acebanenco.challenge.opencl;

import com.acebanenco.challenge.opencl.annotation.BufferParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

public class CLContextNDRangeAction implements CLContextAction {

    @Override
    public void executeWithContext(CLExecutionContext context) {
        Method method = context.getMethod();
        // TODO can be cached
        int ndRangeIndex = getClndRangeIndex(method);
        // TODO can be cached
        CLMemFlag[][] parameterFlags = getClMemFlags(method);

        CLKernel kernel = context.getKernel();
        CLDevice device = context.getDevice();
        CLNDRange ndRange = (CLNDRange) context.getArgs()[ndRangeIndex];
        CLProgramArgument[] programArguments = getProgramArguments(context, parameterFlags);

        kernel.executeNDRange(device, ndRange, programArguments);
    }

    private static CLProgramArgument[] getProgramArguments(CLExecutionContext context, CLMemFlag[][] parameterFlags) {
        Object[] args = context.getArgs();
        return getArguments(args, parameterFlags);
    }

    private static CLMemFlag[][] getClMemFlags(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        return Stream.of(parameterAnnotations)
                .map(annotations -> Stream.of(annotations)
                        .filter(BufferParameter.class::isInstance)
                        .map(annotation -> (BufferParameter) annotation)
                        .map(BufferParameter::flags)
                        .findFirst()
                        .orElse(null))
                .toArray(CLMemFlag[][]::new);
    }

    private static CLProgramArgument[] getArguments(Object[] args, CLMemFlag[][] parameterFlags) {
        CLProgramArgument[] programArguments = new CLProgramArgument[args.length];
        int paramCount = 0;
        for (int i = 0; i < args.length; i++) {
            CLMemFlag[] flags = parameterFlags[i];
            if (flags == null) {
                continue;
            }
            Object arg = args[i];
            programArguments[paramCount++] = new CLProgramArgument(arg, flags);
        }
        if (paramCount == programArguments.length) {
            return programArguments;
        }
        return Arrays.copyOf(programArguments, paramCount);
    }

    private static int getClndRangeIndex(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (CLNDRange.class == parameterTypes[i]) {
                return i;
            }
        }
        throw new IllegalArgumentException("CLNDRange is not specified in arguments");
    }

}
