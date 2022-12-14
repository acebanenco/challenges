package com.acebanenco.challenge.crypto.openssl;

import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.provider.MemoryManager;
import jnr.ffi.provider.jffi.ArrayMemoryIO;

import java.nio.ByteBuffer;
import java.security.MessageDigestSpi;
import java.util.Arrays;

public class OpenSslEvpSpi extends MessageDigestSpi {

    private final LibEvp engine;
    private final Runtime runtime;
    private final MemoryManager memoryManager;
    private final Pointer hashPointer;

    private final int digestLength;
    private final Pointer context;
    private byte[] oneByte;
    private final ByteBuffer hash;

    public OpenSslEvpSpi() {
        engine = LibEvp.instance();
        runtime = Runtime.getRuntime(engine);
        memoryManager = runtime.getMemoryManager();

        digestLength = 32; // 256 bits / 8 bits/byte
        hash = ByteBuffer.allocate(digestLength);

        hashPointer = memoryManager.newPointer(hash);
        context = engine.EVP_MD_CTX_new();
        Pointer md = engine.EVP_sha256();
        // TODO
        //Pointer md = engine.EVP_get_digestbyname("SHA-256");
        engine.EVP_DigestInit_ex2(context, md, null);
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
        engine.EVP_DigestUpdate(context, inputPointer, inputPointer.size());
    }

    @Override
    protected void engineUpdate(ByteBuffer input) {
        Pointer inputPointer = memoryManager.newPointer(input);
        engine.EVP_DigestUpdate(context, inputPointer, inputPointer.size());
    }

    @Override
    protected byte[] engineDigest() {
        engine.EVP_DigestFinal_ex(context, hashPointer, null);
        // reuse context for the next operation
        // engine.EVP_DigestInit_ex2(context, null, null);
        return hash.array();
    }

    @Override
    protected void engineReset() {
        engine.EVP_DigestInit_ex2(context, null, null);
    }
}
