package com.acebanenco.challenge.curves;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import lombok.Getter;

@Getter
public class CanvasPane extends Pane {

    private Canvas canvas;

    // TODO Observable property on children?
    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        getChildren().setAll(canvas);
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        double x = snappedLeftInset();
        double y = snappedRightInset();
        double w = snapSizeX(getWidth() - snappedRightInset());
        double h = snapSizeY(getHeight() - snappedBottomInset());

        canvas.setLayoutX(x);
        canvas.setLayoutY(y);
        canvas.setWidth(w);
        canvas.setHeight(h);

    }
}
