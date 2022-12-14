package com.acebanenco.challenge.crypto.service;

import com.acebanenco.challenge.crypto.service.ValidHashCondition;

public class ValidHashFirstByteZeroCondition implements ValidHashCondition {

    @Override
    public boolean testHash(byte[] digest) {
        // no need to check bounds since SHA-256 digest is always 32 bytes long
        return digest[0] == 0;
    }

}
