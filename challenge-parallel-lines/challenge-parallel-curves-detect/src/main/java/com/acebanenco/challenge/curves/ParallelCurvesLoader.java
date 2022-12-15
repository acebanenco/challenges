package com.acebanenco.challenge.curves;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.Setter;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

@Setter
public class ParallelCurvesLoader {

    private String fxmlResource = "fxml/ParallelCurvesPaint.fxml";

    Parent loadRoot() throws IOException {
        ClassLoader classLoader = ParallelCurvesApp.class.getClassLoader();
        URL resource = classLoader.getResource(fxmlResource);
        return FXMLLoader.load(Objects.requireNonNull(resource,
                "Resource not found: " + fxmlResource));
    }

}
