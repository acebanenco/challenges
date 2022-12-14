package com.acebanenco.challenge.crypto.service;

import java.util.stream.Stream;

public interface SaltGenerateService {

    Stream<byte[]> generateSalt();

}
