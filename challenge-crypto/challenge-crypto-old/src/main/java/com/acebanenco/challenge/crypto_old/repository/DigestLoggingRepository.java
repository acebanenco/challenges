package com.acebanenco.challenge.crypto_old.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.HexFormat;

@Log4j2
@RequiredArgsConstructor
public class DigestLoggingRepository implements DigestRepository {

    private final HexFormat hexFormat;

    @Override
    public void saveHash(byte[] input, byte[] digest) {
        String inputString = hexFormat.formatHex(input);
        String digestString = hexFormat.formatHex(digest);
        log.info("""
                        \s
                          INPUT: {}
                           HASH: {}""",
                inputString,
                digestString);
    }
}
