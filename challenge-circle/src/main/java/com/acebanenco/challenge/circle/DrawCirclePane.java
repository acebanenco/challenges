package com.acebanenco.challenge.circle;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class DrawCirclePane extends Pane {

    private final ImageView imageView;
    private IntegerProperty samplesPerPixelProperty;
    private IntegerProperty circleSizeProperty;
    private DoubleProperty zoomProperty;

    public DrawCirclePane() {
        imageView = new ImageView();
        getChildren().add(imageView);

        ChangeListener<Number> changeListener = (observableValue, number, t1) ->
                updateImage();
        heightProperty().addListener(changeListener);
        widthProperty().addListener(changeListener);
        circleSizeProperty().addListener(changeListener);
        samplesPerPixelProperty().addListener(changeListener);
        zoomProperty().addListener(changeListener);
    }

    public IntegerProperty samplesPerPixelProperty() {
        if ( samplesPerPixelProperty == null ) {
            samplesPerPixelProperty = new SimpleIntegerProperty(this, "samplesPerPixelProperty", 3);
        }
        return samplesPerPixelProperty;
    }

    public IntegerProperty circleSizeProperty() {
        if ( circleSizeProperty == null ) {
            circleSizeProperty = new SimpleIntegerProperty(this, "circleSizeProperty", 90);
        }
        return circleSizeProperty;
    }

    public DoubleProperty zoomProperty() {
        if ( zoomProperty == null ) {
            zoomProperty = new SimpleDoubleProperty(this, "zoomProperty", 1);
        }
        return zoomProperty;
    }

    private void updateImage() {
        Image scaledImage = getImage();
        imageView.setImage(scaledImage);
    }

    private Image getImage() {
        Image superSampledImage = drawCircle(); // super-sampled image
        if ( superSampledImage == null ) {
            return null;
        }
        Image antiAliasedImage = superSampleAntiAliasing(superSampledImage);
        if ( antiAliasedImage == null ) {
            return null;
        }

        double scale = zoomProperty().doubleValue();
        if ( scale == 0.0 ) {
            return null;
        }
        return scaleImageUp(antiAliasedImage, scale);
    }

    private Image superSampleAntiAliasing(Image image) {
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();

        PixelReader imageReader = image.getPixelReader();

        int samplesPerPixel = samplesPerPixelProperty().get();
        int width = (int) (imageWidth / samplesPerPixel);
        int height = (int) (imageHeight / samplesPerPixel);

        if ( width == 0 || height == 0 ) {
            return null;
        }

        WritableImage antiAliasedImage = new WritableImage(width, height);
        PixelWriter pixelWriter = antiAliasedImage.getPixelWriter();
        int[] subSampleColors = new int[samplesPerPixel * samplesPerPixel];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                fillSubSampleColors(imageReader, samplesPerPixel, subSampleColors, x, y);
                int mixedColor = mixColors(subSampleColors);
                pixelWriter.setArgb(x, y, mixedColor);
            }
        }
        return antiAliasedImage;
    }

    private void fillSubSampleColors(PixelReader imageReader, int samplesPerPixel, int[] colors, int x, int y) {
        for (int i = 0; i < colors.length; i++) {
            int x1 = samplesPerPixel * x + i / samplesPerPixel;
            int y1 = samplesPerPixel * y + i % samplesPerPixel;
            colors[i] = imageReader.getArgb(x1, y1);
        }
    }

    private int mixColors(int[] colors) {
        return mixColorComponent(colors, 24)
                | mixColorComponent(colors, 16)
                | mixColorComponent(colors, 8)
                | mixColorComponent(colors, 0);
    }

    private int mixColorComponent(int[] colors, int bits) {
        int sum = 0;
        for (int color : colors) {
            sum += 0xFF & (color >> bits);
        }
        return (0xFF & (sum / colors.length)) << bits;
    }

    private Image drawCircle() {
        int size = circleSizeProperty().get();
        double radius = size / 2.0 * 0.7;
        double x0 = size / 2.0;
        double y0 = size / 2.0;
        return drawCircle(size, size, radius, x0, y0);
    }

    private Image scaleImageUp(Image image, double scaleFactor) {
        PixelReader imageReader = image.getPixelReader();

        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();
        int scaledWidth = (int) (imageWidth * scaleFactor);
        int scaledHeight = (int) (imageHeight * scaleFactor);
        WritableImage scaledImage = new WritableImage(scaledWidth, scaledHeight);

        PixelWriter writer = scaledImage.getPixelWriter();

        for (int x = 0; x < imageWidth; x++) {
            for (int y = 0; y < imageHeight; y++) {
                Color color = imageReader.getColor(x, y);
                drawRect((int) (x * scaleFactor), (int) (y * scaleFactor), (int) scaleFactor, (int) scaleFactor, writer, color);
            }
        }

        return scaledImage;
    }

    private static void drawRect(
            int x0,
            int y0,
            int rectWidth,
            int rectHeight,
            PixelWriter writer,
            Color color) {
        for (int x = 0; x < rectWidth; x++) {
            for (int y = 0; y < rectHeight; y++) {
                writer.setColor(x0 + x, y0 + y, color);
            }
        }
    }

    private Image drawCircle(int w, int h, double r, double x0, double y0) {
        if ( w == 0 || h == 0 ) {
            return null;
        }
        WritableImage raster = new WritableImage(w, h);
        PixelWriter pixelWriter = raster.getPixelWriter();
        Color foregroundColor = Color.BLACK;

        for (int x = 0; x <= r; x++) {
            int y = (int) Math.round(Math.sqrt(r * r - x * x));
            if (y < x) {
                break;
            }
            draw8Pixels(x0, x, y0, y, pixelWriter, foregroundColor);
        }
        return raster;
    }

    private void draw8Pixels(double x0, int x, double y0, int y, PixelWriter pixelWriter, Color foregroundColor) {
        int sx1 = (int) (x0 + x);
        int sx2 = (int) (x0 - x);
        int sy1 = (int) (y0 + y);
        int sy2 = (int) (y0 - y);

        int sx3 = (int) (x0 + y);
        int sx4 = (int) (x0 - y);
        int sy3 = (int) (y0 + x);
        int sy4 = (int) (y0 - x);

        setColor(sx1, sy1, pixelWriter, foregroundColor);
        setColor(sx1, sy2, pixelWriter, foregroundColor);
        setColor(sx2, sy1, pixelWriter, foregroundColor);
        setColor(sx2, sy2, pixelWriter, foregroundColor);
        setColor(sx3, sy3, pixelWriter, foregroundColor);
        setColor(sx3, sy4, pixelWriter, foregroundColor);
        setColor(sx4, sy3, pixelWriter, foregroundColor);
        setColor(sx4, sy4, pixelWriter, foregroundColor);
    }

    private void setColor(int x, int y, PixelWriter pixelWriter, Color color) {
        pixelWriter.setColor(x, y, color);
    }



}
