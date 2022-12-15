package com.acebanenco.challenge.curves;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParallelCurvesController {
    @FXML
    private Canvas paintCanvas;

    private final List<CanvasLine> lines = new ArrayList<>();
    private final CanvasLine currentLine = new CanvasLine();

    Color[] possibleColors = new Color[] {
            Color.BLACK,
            Color.AQUA,
            Color.BEIGE,
            Color.BLUE,
            Color.BROWN,
            Color.CORAL,
            Color.CYAN,
            Color.DEEPPINK,
            Color.GOLD,
            Color.INDIGO,
            Color.MAGENTA,
            Color.YELLOW,
            Color.GREEN,
            Color.RED,
            Color.ORANGE,
    };

    public void paintCanvasOnMousePressed(MouseEvent mouseEvent) {
        GraphicsContext context = paintCanvas.getGraphicsContext2D();
        context.setStroke(Color.BLACK);
        context.beginPath();

        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        context.moveTo(x, y);
        currentLine.moveTo((float) x, (float) y);
    }

    public void paintCanvasOnMouseDragged(MouseEvent mouseEvent) {
        GraphicsContext context = paintCanvas.getGraphicsContext2D();
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        context.lineTo(x, y);
        currentLine.lineTo((float) x, (float) y);
        context.stroke();
    }

    public void paintCanvasOnMouseReleased() {
        GraphicsContext context = paintCanvas.getGraphicsContext2D();
        context.closePath();
        CanvasLine copy = new CanvasLine(currentLine);
        copy.color = randomColor();
        lines.add(copy);
        currentLine.reset();

        clearCanvas();
        float[] coordinates = new float[2];
        for (CanvasLine line : lines) {
            drawLine(coordinates, line);
        }
    }

    public void clearButtonOnAction() {
        clearCanvas();
        lines.clear();
    }

    public void changeColorButtonOnAction() {
        // nothing to do
    }

    private void drawLine(float[] coordinates, CanvasLine line) {
        GraphicsContext context = paintCanvas.getGraphicsContext2D();
        context.setStroke(line.color);

        CanvasLine.LineIterator li = line.iterator();
        if ( li.hasNext() ) {
            context.beginPath();
            li.next(coordinates);
            context.moveTo(coordinates[0], coordinates[1]);

            drawPolyline(coordinates, li);
            context.stroke();
            context.closePath();
        }
    }

    private void drawPolyline(float[] coordinates, CanvasLine.LineIterator li) {
        GraphicsContext context = paintCanvas.getGraphicsContext2D();
        while (li.hasNext()) {
            li.next(coordinates);
            context.lineTo(coordinates[0], coordinates[1]);
        }
    }

    private Color randomColor() {
        Random random = new Random();
        int colorIndex = random.nextInt(possibleColors.length);
        return possibleColors[colorIndex];
    }

    private void clearCanvas() {
        double w = paintCanvas.getWidth();
        double h = paintCanvas.getHeight();
        paintCanvas.getGraphicsContext2D().clearRect(0, 0, w, h);
    }
}
