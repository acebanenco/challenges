package com.acebanenco.challenge.crypto_simple.context;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ConcurrentCalculateHashContextPoolImpl implements CalculateHashContextPool {
    private final CalculateHashContextFactory factory;
    private final Queue<CalculateHashContext> contexts = new ConcurrentLinkedQueue<>();

    public ConcurrentCalculateHashContextPoolImpl(CalculateHashContextFactory factory) {
        this(factory, getDefaultCoreSize());
    }

    public ConcurrentCalculateHashContextPoolImpl(CalculateHashContextFactory factory, int coreSize) {
        this.factory = factory;
        for (int i = 0; i < coreSize; i++) {
            contexts.add(factory.newContext());
        }
    }

    private static int getDefaultCoreSize() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.availableProcessors();
    }

    @Override
    public CalculateHashContext acquire() {
        CalculateHashContext context = contexts.poll();
        if (context != null) {
            return context;
        }
        return factory.newContext();
    }

    @Override
    public void release(CalculateHashContext context) {
        contexts.add(context);
    }
}
