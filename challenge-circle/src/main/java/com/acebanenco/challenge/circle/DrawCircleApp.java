package com.acebanenco.challenge.circle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DrawCircleApp extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        BorderPane borderPane = new BorderPane();

        DrawCirclePane drawCirclePane = new DrawCirclePane();
        borderPane.setCenter(drawCirclePane);

        DrawCircleControlPane controlPane = new DrawCircleControlPane();
        controlPane.getSamplesPerPixelSlider()
                .valueProperty()
                .addListener((observableValue, number, t1) -> {
                    int value = number.intValue();
                    drawCirclePane.samplesPerPixelProperty().set(value);
                });
        controlPane.getCircleSizeSlider()
                .valueProperty()
                .addListener((observableValue, number, t1) -> {
                    int value = number.intValue();
                    drawCirclePane.circleSizeProperty().set(value);
                });
        controlPane.getZoomSlider()
                .valueProperty()
                .addListener((observableValue, number, t1) -> {
                    double value = number.doubleValue();
                    drawCirclePane.zoomProperty().set(value);
                });
        drawCirclePane.widthProperty().addListener((observableValue, number, t1) ->
                controlPane.getCircleSizeSlider().setMax(number.doubleValue()));
        borderPane.setRight(controlPane);

        Scene scene = new Scene(borderPane, 640, 480);
        stage.setScene(scene);
        stage.show();
    }
}
