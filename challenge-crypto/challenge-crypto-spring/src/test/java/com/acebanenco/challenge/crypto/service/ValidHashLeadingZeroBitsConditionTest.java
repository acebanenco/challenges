package com.acebanenco.challenge.crypto.service;

import com.acebanenco.challenge.crypto.service.ValidHashLeadingZeroBitsCondition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidHashLeadingZeroBitsConditionTest {

    @Test
    void testLeadingZeroBitCountValid8Bit() {
        ValidHashLeadingZeroBitsCondition condition = new ValidHashLeadingZeroBitsCondition();
        condition.setLeadingZeroBitsThreshold(8);
        boolean valid = condition.testHash(new byte[]{0, 1, 2, 3});
        assertTrue(valid);
    }

    @Test
    void testLeadingZeroBitCountNotValid8Bit() {
        ValidHashLeadingZeroBitsCondition condition = new ValidHashLeadingZeroBitsCondition();
        condition.setLeadingZeroBitsThreshold(8);
        boolean valid = condition.testHash(new byte[]{1, 0, 2, 3});
        assertFalse(valid);
    }

    @Test
    void testLeadingZeroBitCountValid12Bit() {
        ValidHashLeadingZeroBitsCondition condition = new ValidHashLeadingZeroBitsCondition();
        condition.setLeadingZeroBitsThreshold(12);
        boolean valid = condition.testHash(new byte[]{0, 1, 2, 3});
        assertTrue(valid);
    }

    @Test
    void testLeadingZeroBitCountNotValid12Bit() {
        ValidHashLeadingZeroBitsCondition condition = new ValidHashLeadingZeroBitsCondition();
        condition.setLeadingZeroBitsThreshold(12);
        boolean valid = condition.testHash(new byte[]{0, 127, 2, 3});
        assertFalse(valid);
    }

}