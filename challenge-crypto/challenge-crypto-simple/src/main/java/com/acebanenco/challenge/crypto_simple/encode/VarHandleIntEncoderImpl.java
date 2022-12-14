package com.acebanenco.challenge.crypto_simple.encode;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.nio.ByteOrder;

public class VarHandleIntEncoderImpl implements IntEncoder {

    private static final VarHandle INT_ARRAY =
            MethodHandles.byteArrayViewVarHandle(
                            int[].class,
                            ByteOrder.BIG_ENDIAN)
                    .withInvokeExactBehavior();

    @Override
    public void writeInt(int value, byte[] buffer) {
        INT_ARRAY.set(buffer, 0, value);
    }
}
