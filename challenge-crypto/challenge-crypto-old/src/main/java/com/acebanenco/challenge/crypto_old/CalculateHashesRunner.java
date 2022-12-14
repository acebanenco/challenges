package com.acebanenco.challenge.crypto_old;

import com.acebanenco.challenge.crypto_old.repository.DigestRepository;
import com.acebanenco.challenge.crypto_old.service.CalculateHashesService;
import com.acebanenco.challenge.crypto_old.service.InputGenerateService;
import com.acebanenco.challenge.crypto_old.service.ValidHashCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;

@RequiredArgsConstructor
public class CalculateHashesRunner implements CommandLineRunner {
    private final InputGenerateService generateService;
    private final CalculateHashesService service;
    private final ValidHashCondition condition;
    private final DigestRepository repository;

    @Override
    public void run(String... args) {
        long beforeMillis = System.currentTimeMillis();
        long validHashesCount = generateService.generateInput()
                // calculate digest for the input
                .map(service::digestForInput)
                // check if hash matches
                .filter(digestMessage ->
                        condition.testHash(digestMessage.getDigest()))
                /*.peek(digestMessage ->
                        repository.saveHash(digestMessage.getInput(), digestMessage.getDigest()))*/
                .count();
        long afterMillis = System.currentTimeMillis();
        System.out.printf("Matched %,d hashes in %,d milliseconds%n", validHashesCount, afterMillis - beforeMillis);
    }

}
