package com.acebanenco.challenge.crypto_simple;

import com.acebanenco.challenge.crypto_simple.service.CalculateHashCountService;
import com.acebanenco.challenge.crypto_simple.service.CalculateHashCountServiceFactory;
import com.acebanenco.challenge.crypto_simple.service.CalculateHashCountServiceFactory.Feature;
import org.openjdk.jmh.annotations.*;

import java.util.Random;

@State(Scope.Benchmark)
public class CalculateHashesExecutionPlan {

    private static final CalculateHashCountServiceFactory SERVICE_FACTORY = new CalculateHashCountServiceFactory();
    private static final Random RANDOM = new Random();


    @Param({
            "SERVICE_DEFAULT",
//            "ENCODER_VAR_HANDLE",
//            "CONTEXT_POOL_THREAD_LOCAL",
//            "MESSAGE_DIGEST_BOUNCYCASTLE",
//            "MESSAGE_DIGEST_OPENSSL",
            "SERVICE_OPENCL",
    })
    public Feature feature;

    public int rangeStart = 0;

    @Param({
//            "1000", // one thousand
//            "100000", // one hundred thousand
//            "1000000", // one million
            "10000000", // ten millions
//            "100000000", // one hundred millions
    })
    public int rangeEnd;

    @Param({
//            "1000", // one thousand
//            "10000", // ten thousands
            "100000", // one hundred thousands
    })
    public int itemsPerTask;

    @Param({
            "0",
//            "32",
//            "256",
//            "4096",
    })
    public int messageSize;

    public byte[] message;

    public CalculateHashCountService service;

    @Setup(Level.Invocation)
    public void setup() {
        this.service = getService(feature);
        this.message = getRandomMessage(messageSize);
    }


    private static CalculateHashCountService getService(Feature feature) {
        return SERVICE_FACTORY.createService(feature);
    }

    private static byte[] getRandomMessage(int messageSize) {
        byte[] bytes = new byte[messageSize];
        RANDOM.nextBytes(bytes);
        return bytes;
    }

}
