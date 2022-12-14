package com.acebanenco.challenge.crypto_old.repository;

public interface DigestRepository {

    void saveHash(byte[] input, byte[] hash);
}
