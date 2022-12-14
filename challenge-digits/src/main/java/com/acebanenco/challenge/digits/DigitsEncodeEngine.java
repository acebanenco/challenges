package com.acebanenco.challenge.digits;

import java.nio.ByteBuffer;

public class DigitsEncodeEngine {

    LastSymbolAndCount encode(
            LastSymbolAndCount lastSymbolAndCount,
            ByteBuffer input,
            ByteBuffer output) {
        while (input.hasRemaining() && output.remaining() >= 2) {
            byte b = input.get();
            encode(lastSymbolAndCount, b, output);
        }
        return lastSymbolAndCount;
    }

    boolean encode(
            LastSymbolAndCount lastSymbolAndCount,
            byte nextByte,
            ByteBuffer output) {
        if (lastSymbolAndCount.isLastSymbol(nextByte)) {
            lastSymbolAndCount.incrementCount();
            return true;
        }
        if (lastSymbolAndCount.writeTo(output)) {
            lastSymbolAndCount.start(nextByte);
            return true;
        }
        return false;
    }

    static class LastSymbolAndCount {
        private byte lastSymbol;
        private byte symbolCount;

        public LastSymbolAndCount() {
            lastSymbol = -1;
            symbolCount = 0;
        }

        public LastSymbolAndCount(byte lastSymbol, byte symbolCount) {
            this.lastSymbol = lastSymbol;
            this.symbolCount = symbolCount;
        }

        public boolean isLastSymbol(byte b) {
            return lastSymbol == b;
        }

        public void incrementCount() {
            symbolCount++;
        }

        public void start(byte symbol) {
            lastSymbol = symbol;
            symbolCount = 1;
        }

        public boolean writeTo(ByteBuffer output) {
            if (symbolCount == 0) {
                return true;
            }
            if (output.remaining() < 2) {
                return false;
            }
            output.put(lastSymbol);
            output.put((byte) ('0' + symbolCount));
            return true;
        }
    }
}
