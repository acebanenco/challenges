package com.acebanenco.challenge.circle;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.util.converter.DoubleStringConverter;

public class DrawCircleControlPane extends VBox {
    private final Slider samplesPerPixelSlider;
    private final Slider circleSizeSlider;
    private final Slider zoomSlider;

    public DrawCircleControlPane() {
        samplesPerPixelSlider = new Slider(1.0, 10.0, 2.0);
        samplesPerPixelSlider.setBlockIncrement(1.0);
        samplesPerPixelSlider.setShowTickLabels(true);
        samplesPerPixelSlider.setShowTickMarks(true);
        samplesPerPixelSlider.setMajorTickUnit(1.0);

        circleSizeSlider = new Slider(0.0, 400.0, 100.0);
        circleSizeSlider.setBlockIncrement(20.0);
        circleSizeSlider.setShowTickLabels(true);
        circleSizeSlider.setShowTickMarks(true);
        circleSizeSlider.setMajorTickUnit(20.0);

        zoomSlider = new Slider(0.5, 10.0, 1.0);
        zoomSlider.setBlockIncrement(1.0);
        zoomSlider.setShowTickLabels(true);
        zoomSlider.setShowTickMarks(true);
        zoomSlider.setMajorTickUnit(3.0);

        getChildren().add(new Label("Samples per pixel"));
        getChildren().add(samplesPerPixelSlider);
        getChildren().add(new Label("Circle size"));
        getChildren().add(circleSizeSlider);
        getChildren().add(new Label("Zoom"));
        getChildren().add(zoomSlider);
    }

    public Slider getSamplesPerPixelSlider() {
        return samplesPerPixelSlider;
    }

    public Slider getCircleSizeSlider() {
        return circleSizeSlider;
    }

    public Slider getZoomSlider() {
        return zoomSlider;
    }
}
