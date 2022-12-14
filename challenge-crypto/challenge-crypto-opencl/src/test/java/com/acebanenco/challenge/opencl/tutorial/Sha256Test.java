package com.acebanenco.challenge.opencl.tutorial;

import com.acebanenco.challenge.opencl.CLContextNDRangeAction;
import com.acebanenco.challenge.opencl.CLExecution;
import com.acebanenco.challenge.opencl.CLProxyHandlerFactory;
import com.acebanenco.challenge.opencl.tutorial.Sha256Prog;
import com.acebanenco.challenge.opencl.tutorial.Sha256Sample;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Sha256Test {

    private static final CLProxyHandlerFactory<Sha256Prog> handlerFactory =
            new CLExecution().getHandlerFactory(Sha256Prog.class);

    private static final Sha256Sample sample = new Sha256Sample(
            handlerFactory.getImplementation(new CLContextNDRangeAction()), 1);
    private static final MessageDigest messageDigest = getMessageDigest();

    private static MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    static Stream<Arguments> testHashMatches() {
        int count = 10;
        return IntStream.of(0, 1000, 1_000_000, 100_000_000)
                .flatMap(i -> IntStream.range(i, i + count))
                .boxed()
                .map(Arguments::arguments);
    }

    @MethodSource
    @ParameterizedTest
    void testHashMatches(int number) {
        assertHashMatches(number);
    }

    private static void assertHashMatches(int number) {
        sample.findMatchingHashCount(sample.globalWorkSize, number, number + 1);
        byte[] expected = messageDigest.digest(getIntAsBytes(number));
        HexFormat hexFormat = HexFormat.of();
        Assertions.assertEquals(hexFormat.formatHex(expected), hexFormat.formatHex(sample.outbuffer));
    }

    private static byte[] getIntAsBytes(int number) {
        return new byte[]{
                (byte) (number >>> 24),
                (byte) (number >>> 16),
                (byte) (number >>> 8),
                (byte) number
        };
    }

}
