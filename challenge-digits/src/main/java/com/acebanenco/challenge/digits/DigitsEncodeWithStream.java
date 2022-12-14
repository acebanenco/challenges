package com.acebanenco.challenge.digits;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class DigitsEncodeWithStream {


    public int encode(File input, File output, int maxIterations, int level, long maxLength) throws IOException {
        int formatLength = ("" + maxIterations).length();
        String intermediateFileFormat = "%s_%0" + formatLength + "d.%s";

        File parentFile = output.getParentFile();
        String outputName = output.getName();
        String prefix = outputName.substring(0, outputName.indexOf('.'));
        String suffix = outputName.substring(outputName.indexOf('.') + 1);
        File currentFile = input;
        int iteration;
        for (iteration = 0; iteration < (maxIterations + level - 1) / level; iteration++) {
            String intermediateFileName = String.format(intermediateFileFormat, prefix, iteration, suffix);
            File intermediateFile = new File(parentFile, intermediateFileName);

            try (InputStream fileInput = Files.newInputStream(currentFile.toPath());
                 OutputStream fileOutput = Files.newOutputStream(intermediateFile.toPath())) {
                long count = encode(fileInput, fileOutput, Math.min(level, maxIterations - (iteration * level)));
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
            currentFile = intermediateFile;
        }
        Files.move(currentFile.toPath(), output.toPath(), REPLACE_EXISTING);
        return iteration;
    }

    long encode(InputStream input, OutputStream output) throws IOException {
        try (DigitsEncodeInputStream encodeInput = new DigitsEncodeInputStream(input);
             OutputStream linesOutput = getDecoratedOutput(output)) {
            return encodeInput.transferTo(linesOutput);
        }
    }

    long encode(InputStream input, OutputStream output, int level) throws IOException {
        try (InputStream skipBytesInputStream = new SkipBytesInputStream(" \r\n".getBytes(StandardCharsets.US_ASCII), input);
             OutputStream encodeOutput = getEncodeOutput(output, level);
             OutputStream linesOutput = getDecoratedOutput(encodeOutput)) {
            return skipBytesInputStream.transferTo(linesOutput);
        }
    }

    private static OutputStream getEncodeOutput(OutputStream output, int level) {
        OutputStream result = output;
        for (int i = 0; i < level; i++) {
            result = new DigitsEncodeOutputStream(result);
        }
        return result;
    }

    private static OutputStream getDecoratedOutput(OutputStream output) {
        int wordLength = 2 * 3;
        int lineLength = (80 / (6 + 1)) * (6 + 1);
        FixedLengthOutputStream wordsOutput = FixedLengthOutputStream.withFixedWordLength(output, wordLength);
        return FixedLengthOutputStream.withFixedLineLength(wordsOutput, lineLength);
    }

}
