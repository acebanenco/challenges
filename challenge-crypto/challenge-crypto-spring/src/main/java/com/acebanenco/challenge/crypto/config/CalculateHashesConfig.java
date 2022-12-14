package com.acebanenco.challenge.crypto.config;

import com.acebanenco.challenge.crypto.CalculateHashesRunner;
import com.acebanenco.challenge.crypto.digest.MessageDigestFactory;
import com.acebanenco.challenge.crypto.digest.SdkMessageDigestFactoryImpl;
import com.acebanenco.challenge.crypto.model.HexFormatBuilder;
import com.acebanenco.challenge.crypto.repository.DigestRepository;
import com.acebanenco.challenge.crypto.repository.LoggingDigestRepository;
import com.acebanenco.challenge.crypto.repository.NullDigestRepository;
import com.acebanenco.challenge.crypto.service.*;
import com.acebanenco.challenge.crypto.service.ValidHashCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HexFormat;

@Configuration
public class CalculateHashesConfig {

    @Bean
    public CalculateHashesRunner calculateHashesRunner(
            SaltGenerateService generateService,
            com.acebanenco.challenge.crypto.service.CalculateHashesService service,
            com.acebanenco.challenge.crypto.service.ValidHashCondition condition,
            DigestRepository repository) {
        return new CalculateHashesRunner(generateService, service, condition, repository);
    }

    @ConfigurationProperties("app.challenge.crypto.salt")
    @Bean
    public SaltGenerateService inputGenerateService() {
        return new Salt4ByteParallelGenerateServiceImpl();
    }

    @ConfigurationProperties("app.challenge.crypto.digest")
    @Bean(initMethod = "createDigest")
    public MessageDigestFactory messageDigestFactory() {
        return new SdkMessageDigestFactoryImpl();
    }

    @ConditionalOnProperty("app.challenge.crypto.input.message")
    @Bean(initMethod = "loadMessage")
    @ConfigurationProperties("app.challenge.crypto.input")
    public com.acebanenco.challenge.crypto.service.CalculateHashesService threadLocalCalculateHashesReusableMessageDigestService(MessageDigestFactory messageDigestFactory) {
        return new ThreadLocalCalculateHashesReusableMessageDigestService(messageDigestFactory);
    }

    @ConditionalOnMissingBean(com.acebanenco.challenge.crypto.service.CalculateHashesService.class)
    @Bean
    public com.acebanenco.challenge.crypto.service.CalculateHashesService threadLocalCalculateHashesReusableDigestService(MessageDigestFactory messageDigestFactory) {
        return new ThreadLocalCalculateHashesReusableDigestService(messageDigestFactory);
    }

    @Bean
    @ConditionalOnProperty("app.challenge.crypto.condition.leading-zero-bits-threshold")
    @ConfigurationProperties("app.challenge.crypto.condition")
    public com.acebanenco.challenge.crypto.service.ValidHashCondition validHashLeadingZeroBitsCondition() {
        return new com.acebanenco.challenge.crypto.service.ValidHashLeadingZeroBitsCondition();
    }

    @Bean
    @ConditionalOnMissingBean(com.acebanenco.challenge.crypto.service.ValidHashCondition.class)
    public ValidHashCondition validHashFirstByteZeroCondition() {
        return new ValidHashFirstByteZeroCondition();
    }

    @Bean
    @ConfigurationProperties("app.challenge.crypto.repository.logging.hex-format")
    public HexFormatBuilder hexFormatBuilder() {
        return new HexFormatBuilder();
    }

    @Profile("logging")
    @Bean
    public DigestRepository logginDigestRepository(HexFormatBuilder hexFormatBuilder) {
        HexFormat hexFormat = hexFormatBuilder.build();
        return new LoggingDigestRepository(hexFormat);
    }

    @Bean
    public DigestRepository nullDigestRepository() {
        return new NullDigestRepository();
    }

}
