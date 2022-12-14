package com.acebanenco.challenge.crypto.repository;

import com.acebanenco.challenge.crypto.repository.DigestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

@ConditionalOnMissingBean(com.acebanenco.challenge.crypto.repository.DigestRepository.class)
@Log4j2
@RequiredArgsConstructor
public class NullDigestRepository implements DigestRepository {

    @Override
    public void saveHash(byte[] input, byte[] digest) {
        // do nothing
    }
}
