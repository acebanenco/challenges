package com.acebanenco.challenge.crypto_simple.context;

public class ThreadLocalCalculateHashContextPoolImpl implements CalculateHashContextPool {

    private final ThreadLocal<CalculateHashContext> contextHolder;

    public ThreadLocalCalculateHashContextPoolImpl(CalculateHashContextFactory factory) {
        contextHolder = ThreadLocal.withInitial(factory::newContext);
    }

    @Override
    public CalculateHashContext acquire() {
        return contextHolder.get();
    }

    @Override
    public void release(CalculateHashContext context) {
        // do nothing
    }
}
