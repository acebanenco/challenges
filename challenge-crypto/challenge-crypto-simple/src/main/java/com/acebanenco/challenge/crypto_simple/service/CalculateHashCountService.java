package com.acebanenco.challenge.crypto_simple.service;

import com.acebanenco.challenge.crypto_simple.model.CalculateHashCountRequest;

public interface CalculateHashCountService {

    int getHashCount(CalculateHashCountRequest request);

}
