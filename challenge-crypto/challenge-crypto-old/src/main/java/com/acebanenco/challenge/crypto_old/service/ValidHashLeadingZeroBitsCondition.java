package com.acebanenco.challenge.crypto_old.service;

import lombok.Setter;

public class ValidHashLeadingZeroBitsCondition implements ValidHashCondition {
    @Setter
    private int leadingZeroBitsThreshold;

    @Override
    public boolean testHash(byte[] digest) {
        int leadingZeroBitsCount = leadingZeroBitsCount(digest, leadingZeroBitsThreshold);
        return leadingZeroBitsCount >= leadingZeroBitsThreshold;
    }

    private int leadingZeroBitsCount(byte[] digest, int threshold) {
        int leadingZeroBitsCount = 0;
        for (byte b : digest) {
            if ( b == 0 ) {
                leadingZeroBitsCount += 8;
                // optimization to break out of the loop as soon as possible
                if (leadingZeroBitsCount < threshold) {
                    continue;
                }
            }
            if ( b > 0) {
                int count = numberOfLeadingZeros(b);
                leadingZeroBitsCount += count;
            }
            break;
        }
        return leadingZeroBitsCount;
    }

    public static int numberOfLeadingZeros(byte b) {
        return Integer.numberOfLeadingZeros(Byte.toUnsignedInt(b)) - 24;
    }

}
