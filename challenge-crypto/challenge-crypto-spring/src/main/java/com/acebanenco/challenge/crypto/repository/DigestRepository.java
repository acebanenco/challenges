package com.acebanenco.challenge.crypto.repository;

public interface DigestRepository {

    void saveHash(byte[] input, byte[] hash);
}
