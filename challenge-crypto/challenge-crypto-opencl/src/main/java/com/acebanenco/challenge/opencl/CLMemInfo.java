package com.acebanenco.challenge.opencl;

import org.jocl.Pointer;
import org.jocl.Sizeof;

public class CLMemInfo {
    final int componentSize;
    final int arrayLength;
    final Pointer hostPtr;

    CLMemInfo(int componentSize, int arrayLength, Pointer hostPtr) {
        this.componentSize = componentSize;
        this.arrayLength = arrayLength;
        this.hostPtr = hostPtr;
    }

    int getComponentSize() {
        return componentSize;
    }

    int getArrayLength() {
        return arrayLength;
    }

    Pointer getHostPtr() {
        return hostPtr;
    }

    public static CLMemInfo valueOf(int[] arr) {
        return new CLMemInfo(Sizeof.cl_int, arr.length, Pointer.to(arr));
    }

    public static CLMemInfo valueOf(long[] arr) {
        return new CLMemInfo(Sizeof.cl_long, arr.length, Pointer.to(arr));
    }

    public static CLMemInfo valueOf(float[] arr) {
        return new CLMemInfo(Sizeof.cl_float, arr.length, Pointer.to(arr));
    }

    public static CLMemInfo valueOf(double[] arr) {
        return new CLMemInfo(Sizeof.cl_double, arr.length, Pointer.to(arr));
    }

    public static CLMemInfo valueOf(byte[] arr) {
        return new CLMemInfo(Sizeof.cl_char, arr.length, Pointer.to(arr));
    }

    public static CLMemInfo valueOf(char[] arr) {
        return new CLMemInfo(Sizeof.cl_char2, arr.length, Pointer.to(arr));
    }

}
