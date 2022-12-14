package com.acebanenco.challenge.crypto_old.config;

import com.acebanenco.challenge.crypto_old.CalculateHashesRunner;
import com.acebanenco.challenge.crypto_old.model.HexFormatBuilder;
import com.acebanenco.challenge.crypto_old.repository.DigestLoggingRepository;
import com.acebanenco.challenge.crypto_old.repository.DigestRepository;
import com.acebanenco.challenge.crypto_old.service.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HexFormat;

@Configuration
public class CalculateHashesConfig {

    @Bean
    public CalculateHashesRunner calculateHashesRunner(
            InputGenerateService generateService,
            CalculateHashesService service,
            ValidHashCondition condition,
            DigestRepository repository) {
        return new CalculateHashesRunner(generateService, service, condition, repository);
    }

    @Bean
    public InputGenerateService inputGenerateService() {
        // MatchHashesGenerateService generateService = new MatchHashes4ByteParallelGenerateServiceImpl();
        // Use optimized version, if needed default version can be configured with profiles
        return new Input4ByteParallelSharedGenerateServiceImpl();
    }

    @Bean
    public CalculateHashesService calculateHashesService() {
        // MatchHashesService service = new ThreadLocalMatchHashesService();
        // Use optimized version, if needed default version can be configured with profiles
        return new ThreadLocalCalculateHashesSharedDigestService();
    }

//    @Bean
//    @ConfigurationProperties("app.challenge.crypto.condition")
//    public ValidHashCondition validHashCondition() {
//        return new ValidHashLeadingZeroBitsCondition();
//    }

    @Bean
    public ValidHashCondition validHashCondition() {
        return new ValidHashLeadingZeroLeadingByteCondition();
    }

    @Bean
    @ConfigurationProperties("app.challenge.crypto.repository.logging.hex-format")
    public HexFormatBuilder hexFormatBuilder() {
        return new HexFormatBuilder();
    }

    @Bean
    public DigestRepository digestRepository(HexFormatBuilder hexFormatBuilder) {
        HexFormat hexFormat = hexFormatBuilder.build();
        return new DigestLoggingRepository(hexFormat);
    }

}
