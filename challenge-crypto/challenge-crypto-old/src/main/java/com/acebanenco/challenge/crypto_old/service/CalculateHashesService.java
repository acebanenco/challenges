package com.acebanenco.challenge.crypto_old.service;

import com.acebanenco.challenge.crypto_old.model.DigestMessage;

public interface CalculateHashesService {
    DigestMessage digestForInput(byte[] input);

}
