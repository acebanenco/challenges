package com.acebanenco.challenge.crypto_old.service;

public class ValidHashLeadingZeroLeadingByteCondition implements ValidHashCondition {

    @Override
    public boolean testHash(byte[] digest) {
        return digest[0] == 0;
    }
}
