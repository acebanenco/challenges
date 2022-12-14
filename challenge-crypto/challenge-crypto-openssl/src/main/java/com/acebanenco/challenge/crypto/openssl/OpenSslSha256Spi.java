package com.acebanenco.challenge.crypto.openssl;

import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.provider.MemoryManager;
import jnr.ffi.provider.jffi.ArrayMemoryIO;

import java.nio.ByteBuffer;
import java.security.MessageDigestSpi;
import java.util.Arrays;

public class OpenSslSha256Spi extends MessageDigestSpi {

    private final LibSha256 engine;
    private final Runtime runtime;
    private final MemoryManager memoryManager;
    private final LibSha256.Sha256StateStruct context;
    private final Pointer hashPointer;

    private final int digestLength;
    private byte[] oneByte;
    private final ByteBuffer hash;

    public OpenSslSha256Spi() {
        engine = LibSha256.instance();
        runtime = Runtime.getRuntime(engine);
        memoryManager = runtime.getMemoryManager();

        digestLength = 32; // 256 bits / 8 bits/byte
        hash = ByteBuffer.allocate(digestLength);

        hashPointer = memoryManager.newPointer(hash);
        context = new LibSha256.Sha256StateStruct(runtime);
        engine.SHA256_Init(context);
    }

    @Override
    protected int engineGetDigestLength() {
        return digestLength;
    }

    @Override
    protected void engineUpdate(byte input) {
        if (oneByte == null) {
            oneByte = new byte[1];
        }
        engineUpdate(oneByte, 0, 1);
    }

    @Override
    protected void engineUpdate(byte[] input, int offset, int len) {
        Pointer inputPointer = new ArrayMemoryIO(runtime, input, offset, len);
        engine.SHA256_Update(context, inputPointer, inputPointer.size());
    }

    @Override
    protected void engineUpdate(ByteBuffer input) {
        Pointer inputPointer = memoryManager.newPointer(input);
        engine.SHA256_Update(context, inputPointer, inputPointer.size());
    }

    @Override
    protected byte[] engineDigest() {
        engine.SHA256_Final(hashPointer, context);
        engine.SHA256_Init(context);
        // doFinal
        return hash.array();
    }

    @Override
    protected void engineReset() {
        Arrays.fill(hash.array(), (byte) 0);
    }
}
