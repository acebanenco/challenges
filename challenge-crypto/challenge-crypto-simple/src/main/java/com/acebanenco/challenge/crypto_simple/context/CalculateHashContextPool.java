package com.acebanenco.challenge.crypto_simple.context;

public interface CalculateHashContextPool {

    CalculateHashContext acquire();

    void release(CalculateHashContext context);
}
