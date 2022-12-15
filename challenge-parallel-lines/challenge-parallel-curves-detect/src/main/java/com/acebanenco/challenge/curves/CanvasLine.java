package com.acebanenco.challenge.curves;

import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class CanvasLine {

    Color color;
    float[] coordinates;//pairs x and y
    int numCoordinates; // number of all coordinates
    int numSegments;

    public CanvasLine(CanvasLine line) {
        this.coordinates = Arrays.copyOf(line.coordinates, line.numCoordinates);
        this.numSegments = line.numSegments;
        this.numCoordinates = line.numCoordinates;
        this.color = line.color;
    }

    public CanvasLine() {
        coordinates = new float[6];
        numCoordinates = 0;
        numSegments = 0;
    }

    void moveTo(float x, float y) {
        if (numCoordinates != 0) {
            throw new IllegalStateException("moveTo should be called first");
        }
        grow();
        coordinates[numCoordinates++] = x;
        coordinates[numCoordinates++] = y;
        numSegments++;
    }

    void lineTo(float x, float y) {
        if (numCoordinates == 0) {
            throw new IllegalStateException("moveTo should be called first");
        }
        grow();
        coordinates[numCoordinates++] = x;
        coordinates[numCoordinates++] = y;
        numSegments++;
    }

    private void grow() {
        int grow = 2;
        int actualGrowth = Math.max(grow, numCoordinates);// double capacity
        int newSize = numCoordinates + actualGrowth;
        // TODO if extends 4K (1K coordinates) use pages to increase size
        coordinates = Arrays.copyOf(coordinates, newSize);
    }

    public LineIterator iterator() {
        return new LineIterator();
    }

    public void reset() {
        numSegments = 0;
        numCoordinates = 0;
        color = null;
    }

    class LineIterator {
        int currSegment = -1;
        int coordinatesIndex = 0;

        boolean hasNext() {
            return (currSegment + 1) < numSegments;
        }

        void next(float[] coords) {
            if ( !hasNext() ) {
                throw new NoSuchElementException();
            }
            int readIndex = (coordinatesIndex += segmentSize());
            currSegment++;

            coords[0] = coordinates[readIndex];
            coords[1] = coordinates[readIndex + 1];
        }

        private int segmentSize() {
            return currSegment < 0 ? 0 : 2;
        }

    }
}
