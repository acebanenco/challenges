package com.acebanenco.challenge.crypto.service;

public interface ValidHashCondition {

    boolean testHash(byte[] digest);

}
