package com.acebanenco.challenge.crypto;

import com.acebanenco.challenge.crypto.repository.DigestRepository;
import com.acebanenco.challenge.crypto.service.ValidHashCondition;
import com.acebanenco.challenge.crypto.service.SaltGenerateService;
import com.acebanenco.challenge.crypto.service.CalculateHashesService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;

@RequiredArgsConstructor
public class CalculateHashesRunner implements CommandLineRunner {
    private final SaltGenerateService generateService;
    private final CalculateHashesService service;
    private final ValidHashCondition condition;
    private final DigestRepository repository;

    @Override
    public void run(String... args) {
        long beforeMillis = System.currentTimeMillis();
        long validHashesCount = generateService.generateSalt()
                // calculate digest for the input
                .map(service::digestForSalt)
                // check if hash matches
                .filter(digestMessage ->
                        condition.testHash(digestMessage.getDigest()))
                .peek(digestMessage ->
                        repository.saveHash(digestMessage.getInput(), digestMessage.getDigest()))
                .count();
        long afterMillis = System.currentTimeMillis();
        System.out.printf("Matched %d hashes in %.2f seconds%n", validHashesCount, (afterMillis - beforeMillis) / 1000d);
    }

}
