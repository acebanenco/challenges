package com.acebanenco.challenge.crypto.service;

import com.acebanenco.challenge.crypto.model.DigestMessage;

public interface CalculateHashesService {
    DigestMessage digestForSalt(byte[] input);

}
