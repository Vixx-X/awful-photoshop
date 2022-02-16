/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics.gizmos;

import fxproject.graphics.CanvasEntity;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.opencv.core.Point;

/**
 *
 * @author Gaby
 */
public final class GizmoCrop {

    public Rectangle moveRect;
    public Line[] cropPoints;
    public Point origin;
    double scale;
    CanvasEntity img;
    int width;
    int height;
    int li;
    double w;
    double h;
    int top;
    int left;
    int right;
    int bottom;
    public Circle cir1;

    public GizmoCrop(CanvasEntity img, int width, int height, float scale) {
        this.width = width;
        this.height = height;
        this.img = img;
        this.scale = scale;
        Point[] cp = calculateCoordenates();
        initCrop(cp);
        addEvents();
    }

    public Point[] calculateCoordenates() {
        origin = new Point(
                img.padLeft * scale,
                img.padTop * scale
        );

        w = width - img.padRight * scale - img.padLeft * scale;
        h = height - img.padBottom * scale - img.padTop * scale;

        Point[] cp = new Point[4];
        cp[0] = new Point(origin.x, origin.y);
        cp[1] = new Point(w + origin.x, origin.y);
        cp[2] = new Point(w + origin.x, h + origin.y);
        cp[3] = new Point(origin.x, h + origin.y);

        return cp;
    }

    public void initCrop(Point[] cp) {
        //lineSize
        li = 15;

        cropPoints = new Line[4];
        for (int i = 0; i < 4; i++) {
            cropPoints[i] = new Line();
        }
        setCropsBorders(cp);

        for (Line cropPoint : cropPoints) {
            cropPoint.setStyle("-fx-stroke: #212121; -fx-stroke-width: 3;");
        }

    }

    public void setCropsBorders(Point[] cp) {

        for (int i = 0; i < 4; i++) {
            cropPoints[i].setStartX(cp[i].x);
            cropPoints[i].setStartY(cp[i].y);
            cropPoints[i].setEndX(cp[(i + 1) % 4].x);
            cropPoints[i].setEndY(cp[(i + 1) % 4].y);
        }
    }

    public void drawGizmo() {
        Point[] cp = calculateCoordenates();
        setCropsBorders(cp);
        //setMoveRect();
    }

    public void addEvents() {
        for (int i = 0; i < 4; i++) {
            setUpDragging(cropPoints[i]);
        }

        cropPoints[0].setOnMouseDragged(event -> {
            this.top = (int) (min(max(0, (int) (event.getY() / scale)), img.img.height() - bottom));
            img.crop(top, right, bottom, left);
            drawGizmo();
        });
        cropPoints[1].setOnMouseDragged(event -> {
            this.right = (int) (min(max(0, ((img.img.width() - (int) (event.getX() / scale)))), img.img.width() - left));
            //this.right = (int) ((width - event.getX()) / scale);
            img.crop(top, right, bottom, left);
            drawGizmo();
        });
        cropPoints[2].setOnMouseDragged(event -> {
            this.bottom = (int) (min(max(0, ((img.img.height() - (int) (event.getY() / scale)))), img.img.height() - top));
            //this.bottom = (int) ((height - event.getY()) / scale);
            img.crop(top, right, bottom, left);
            drawGizmo();
        });
        cropPoints[3].setOnMouseDragged(event -> {
            System.out.println("TRASLACION " + event.getX() + " " + (event.getX() / scale));
            this.left = (int) (min(max(0, (int) (event.getX() / scale)), img.img.width() - right));
            img.crop(top, right, bottom, left);
            drawGizmo();
        });
    }

    private void setUpDragging(Shape shape) {

        shape.setOnDragDetected(event -> {
            shape.getParent().setCursor(Cursor.CLOSED_HAND);
        });

        shape.setOnMouseReleased(event -> {
            shape.getParent().setCursor(Cursor.DEFAULT);
        });
    }

    public void removeOnCanvas(Pane canvasLayout) {
        canvasLayout.getChildren().remove(moveRect);
        canvasLayout.getChildren().removeAll(cropPoints);
    }

    public void addOnCanvas(Pane canvasLayout) {
        //canvasLayout.getChildren().add(moveRect);
        canvasLayout.getChildren().addAll(cropPoints);
    }
}
