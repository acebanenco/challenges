package com.acebanenco.challenge.crypto.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HexFormat;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Input4ByteGenerateServiceImplTest {

    private final HexFormat hexFormat = HexFormat.ofDelimiter(" ");

    static Stream<Arguments> testEncodeBytes() {
        return Stream.of(
          Arguments.arguments(0x00000000, "00 00 00 00"),
          Arguments.arguments(0xF0F0F0F0, "f0 f0 f0 f0"),
          Arguments.arguments(0x0D0D0D0D, "0d 0d 0d 0d"),
          Arguments.arguments(0xFFFFFFFF, "ff ff ff ff")
        );
    }

    @DisplayName("Test encode integer as 4 bytes array")
    @ParameterizedTest(name = "{0} is encoded as ''{1}''")
    @MethodSource
    void testEncodeBytes(int number, String expectedHexBytes) {
        Salt4ByteGenerateServiceImpl service = new Salt4ByteGenerateServiceImpl();
        byte[] bytes = service.encode(number);
        String hexString = hexFormat.formatHex(bytes);
        assertEquals(expectedHexBytes, hexString);
    }

    @Test
    void testGenerateInput() {
        Salt4ByteGenerateServiceImpl service = new Salt4ByteGenerateServiceImpl();
        service.setMinValue(0);
        service.setMaxValue(1000);
        List<byte[]> inputs = service.generateSalt().toList();
        assertEquals(1001, inputs.size());
    }
}