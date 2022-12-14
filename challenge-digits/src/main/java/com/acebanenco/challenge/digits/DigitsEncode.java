package com.acebanenco.challenge.digits;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class DigitsEncode {

    private final byte[] lineSeparator = System.getProperty("line.separator", "\n")
            .getBytes(StandardCharsets.US_ASCII);

    private static class DigitsEncodeContext {
        long count = 0L;
        int currentChar = -1;
        int charCount = 0;
        int lineLength = 0;
        int groupLength = 0;
    }


    public int encode(File input, File output, int maxIterations, long maxLength) throws IOException {
        File parentFile = output.getParentFile();
        String outputName = output.getName();
        String prefix = outputName.substring(0, outputName.indexOf('.'));
        String suffix = outputName.substring(outputName.indexOf('.') + 1);
        File currentFile = input;
        int iteration;
        for (iteration = 0; iteration < maxIterations; iteration++) {
            File tmpFile = new File(parentFile, prefix + "_" + String.format("%03d", iteration) + "." + suffix);

            try (InputStream fileInput = Files.newInputStream(currentFile.toPath());
                 OutputStream fileOutput = Files.newOutputStream(tmpFile.toPath())) {
                long count = encode(fileInput, fileOutput);
                if (count > maxLength) {
                    break;
                }
            } catch (IllegalStateException ise) {
                // ok, we stop here
                break;
            }
            if (currentFile != input) {
                Files.delete(currentFile.toPath());
            }
            currentFile = tmpFile;
        }
        Files.move(currentFile.toPath(), output.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return iteration;
    }

    long encode(InputStream input, OutputStream output) throws IOException {
        DigitsEncodeContext context = new DigitsEncodeContext();
        int nextChar;
        while ((nextChar = input.read()) != -1) {
            if (Character.isWhitespace(nextChar)) {
                continue;
            }
            checkWordEnd(output, context);
            checkLineEnd(output, context);
            nextChar(output, context, nextChar);
        }
        checkWordEnd(output, context);
        checkLineEnd(output, context);
        flush(output, context);
        return context.count;
    }

    private static void flush(OutputStream output, DigitsEncodeContext context) throws IOException {
        output.write(context.charCount + '0');
        output.write(context.currentChar);
        context.count += 2;
    }

    private static void nextChar(OutputStream output, DigitsEncodeContext context, int nextChar) throws IOException {
        if (nextChar == context.currentChar) {
            context.charCount++;
            if (context.charCount > 9) {
                onCharCountExceed(context);
            }
        } else {
            checkCharIsValid(nextChar);
            writeCurrentChar(output, context);

            context.currentChar = nextChar;
            context.charCount = 1;
        }
    }

    private static void checkCharIsValid(int nextChar) {
        if (nextChar < '0' || nextChar > '9') {
            throw new IllegalStateException("Unexpected symbol '" + nextChar + "'");
        }
    }

    private static void onCharCountExceed(DigitsEncodeContext context) {
        // TODO output by 9 digits, e.g. 9252 (9+5 digits '2')
        throw new IllegalStateException("There are more than 9 digits in the row: " +
                Character.toString(context.currentChar));
    }

    private static void writeCurrentChar(OutputStream output, DigitsEncodeContext context) throws IOException {
        if (context.charCount > 0) {
            output.write(context.charCount + '0');
            output.write(context.currentChar);
            context.groupLength += 2;
            context.lineLength += 2;
            context.count += 2;
        }
    }

    private void checkLineEnd(OutputStream output, DigitsEncodeContext context) throws IOException {
        if (context.lineLength >= 84) {
            output.write(lineSeparator);
            context.count += lineSeparator.length;

            context.lineLength = 0;
            context.groupLength = 0;
        }
    }

    private static void checkWordEnd(OutputStream output, DigitsEncodeContext context) throws IOException {
        if (context.groupLength > 4) {
            output.write(' ');
            context.count++;

            context.lineLength++;
            context.groupLength = 0;
        }
    }
}
