package com.acebanenco.challenge.crypto;

import com.acebanenco.challenge.crypto.repository.DigestRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class CalculateHashesAppTest {

    @MockBean
    DigestRepository repository;

    @Test
    void testAppRunSuccessful() {
        Mockito.verify(repository, Mockito.times(1))
                .saveHash(Mockito.any(), Mockito.any());
    }

}