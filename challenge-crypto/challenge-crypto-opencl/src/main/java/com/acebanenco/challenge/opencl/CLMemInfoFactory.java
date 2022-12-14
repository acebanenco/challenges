package com.acebanenco.challenge.opencl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CLMemInfoFactory {

    private static final Map<Class<?>, Function<Object,CLMemInfo>> factoryMethods = factoryMethods();

    public static CLMemInfo getMemInfo(Object arr) {
        Function<Object, CLMemInfo> function = factoryMethods.get(arr.getClass());
        if ( function == null ) {
            throw new IllegalArgumentException();
        }
        return function.apply(arr);
    }

    private static Map<Class<?>, Function<Object,CLMemInfo>> factoryMethods() {
        return Arrays.stream(CLMemInfo.class.getMethods())
                .filter(m -> Modifier.isStatic(m.getModifiers()))
                .filter(m -> m.getReturnType() == CLMemInfo.class)
                .filter(m -> m.getParameterCount() == 1)
                .filter(m -> m.getParameterTypes()[0].isArray())
                .collect(Collectors.toMap(
                        m -> m.getParameterTypes()[0],
                        CLMemInfoFactory::factoryMethod
                ));
    }

    private static Function<Object, CLMemInfo> factoryMethod(Method method) {
        return arr -> {
            try {
                return (CLMemInfo) method.invoke(null, arr);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }

}
