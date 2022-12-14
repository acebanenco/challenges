package com.acebanenco.challenge.crypto_simple.service;

import com.acebanenco.challenge.crypto_simple.context.CalculateHashContextFactory;
import com.acebanenco.challenge.crypto_simple.context.CalculateHashContextPool;
import com.acebanenco.challenge.crypto_simple.context.ConcurrentCalculateHashContextPoolImpl;
import com.acebanenco.challenge.crypto_simple.context.ThreadLocalCalculateHashContextPoolImpl;
import com.acebanenco.challenge.crypto_simple.digest.BcMessageDigestFactoryImpl;
import com.acebanenco.challenge.crypto_simple.digest.DefaultMessageDigestFactoryImpl;
import com.acebanenco.challenge.crypto_simple.digest.MessageDigestFactory;
import com.acebanenco.challenge.crypto_simple.digest.OpenSSLMessageDigestFactoryImpl;
import com.acebanenco.challenge.crypto_simple.encode.DefaultIntEncoderImpl;
import com.acebanenco.challenge.crypto_simple.encode.IntEncoder;
import com.acebanenco.challenge.crypto_simple.encode.VarHandleIntEncoderImpl;

public class CalculateHashCountServiceFactory {

    public CalculateHashCountService createService(Feature... features) {
        for (Feature feature : features) {
            switch (feature) {
                case SERVICE_OPENCL:
                    return new OpenCLCalculateHashCountService();
            }
        }

        IntEncoder intEncoder = getIntEncoder(features);
        MessageDigestFactory messageDigestFactory = getMessageDigestFactory(features);
        CalculateHashContextFactory contextFactory = new CalculateHashContextFactory(messageDigestFactory);
        CalculateHashContextPool contextPool = getContextPool(contextFactory, features);
        return getHashCountService(intEncoder, contextPool, features);
    }

    private static StreamCalculateHashCountServiceImpl getHashCountService(
            IntEncoder intEncoder,
            CalculateHashContextPool contextPool,
            Feature... features) {
        return new StreamCalculateHashCountServiceImpl(intEncoder, contextPool);
    }

    private static CalculateHashContextPool getContextPool(
            CalculateHashContextFactory contextFactory,
            Feature... features) {
        for (Feature feature : features) {
            switch (feature) {
                case CONTEXT_POOL_THREAD_LOCAL:
                    return new ThreadLocalCalculateHashContextPoolImpl(contextFactory);
            }
        }
        return new ConcurrentCalculateHashContextPoolImpl(contextFactory);
    }

    private static IntEncoder getIntEncoder(Feature... features) {
        for (Feature feature : features) {
            switch (feature) {
                case ENCODER_VAR_HANDLE:
                    return new VarHandleIntEncoderImpl();
            }
        }
        return new DefaultIntEncoderImpl();
    }

    private static MessageDigestFactory getMessageDigestFactory(Feature... features) {
        for (Feature feature : features) {
            switch (feature) {
                case MESSAGE_DIGEST_BOUNCYCASTLE:
                    return new BcMessageDigestFactoryImpl();
                case MESSAGE_DIGEST_OPENSSL:
                    return new OpenSSLMessageDigestFactoryImpl();
            }
        }
        return new DefaultMessageDigestFactoryImpl();
    }

    public enum Feature {
        SERVICE_DEFAULT,
        SERVICE_OPENCL,
        ENCODER_VAR_HANDLE,
        CONTEXT_POOL_THREAD_LOCAL,
        MESSAGE_DIGEST_BOUNCYCASTLE,
        MESSAGE_DIGEST_OPENSSL,
    }

}
