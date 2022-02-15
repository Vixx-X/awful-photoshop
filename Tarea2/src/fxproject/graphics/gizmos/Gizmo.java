/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics.gizmos;

import fxproject.graphics.CanvasEntity;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import org.opencv.core.Point;

public final class Gizmo {

    public Polygon selectRect;
    public Polygon mobileRect;
    public Circle resizeHandleNW;
    public Circle resizeHandleSE;
    public Circle rotateHandle;
    public Line rotateLine;
    private double handleRadius;
    private double handleLine;
    private Point rotationHandlePoint;
    public String type;
    public CanvasEntity currentImage;
    public double handleAngle;
    public Circle[] cropBorders;
    public boolean isEditing = false;
    public Wrapper<Point> mouseLocation = new Wrapper<>();

    public Line proof;

    public Gizmo(CanvasEntity img, Point origin) {
        initGizmo(img);
        drawGizmo();
        mouseLocation.value = origin;
    }

    public Gizmo(CanvasEntity img) {
        initGizmo(img);
        drawGizmo();
    }

    public void drawGizmo() {
        drawInternalGizmo();
        addBorder();
    }

    private void initGizmo(CanvasEntity img) {
        cropBorders = null;
        currentImage = img;
        proof = new Line(0, 0, 0, 0);
        Point[] corners = currentImage.getCorners();
        handleRadius = 6.5;
        handleLine = 20;
        double x = corners[0].x;
        double y = corners[0].y;

        createLine(corners);

        double[] points = new double[]{
            corners[0].x, corners[0].y,
            corners[1].x, corners[1].y,
            corners[2].x, corners[2].y,
            corners[3].x, corners[3].y
        };

        selectRect = new Polygon(points);
        selectRect.setStyle("-fx-fill: transparent; -fx-stroke: black; -fx-stroke-width: 1;");

        mobileRect = new Polygon(points);
        mobileRect.setStyle("-fx-fill: transparent; -fx-stroke: red; -fx-stroke-width: 1;");

        resizeHandleNW = new Circle(x, y, handleRadius);
        resizeHandleSE = new Circle(corners[2].x, corners[2].y, handleRadius);

        resizeHandleNW.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 0.8;");
        resizeHandleSE.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 0.8;");
    }

    private void createLine(Point[] corners) {
        Point mid = new Point((corners[0].x + corners[1].x) / 2, (corners[0].y + corners[1].y) / 2);
        handleAngle = -(currentImage.angle + (corners[1].y < corners[2].y ? 90 : -90)) * PI / 180;

        rotationHandlePoint = new Point(handleLine * cos(handleAngle) + mid.x, handleLine * sin(handleAngle) + mid.y);

        rotateLine = new Line(mid.x, mid.y, rotationHandlePoint.x, rotationHandlePoint.y);
        rotateHandle = new Circle(rotationHandlePoint.x, rotationHandlePoint.y, handleRadius);
        rotateHandle.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 0.8;");
    }

    public void drawInternalGizmo() {
        Point[] corners = currentImage.getCorners();
        setCorners(corners);
    }

    public double getAngle(Point[] corner) {
        double deltaX = corner[1].x - corner[0].x;
        double deltaY = corner[1].y - corner[0].y;
        return Math.atan(deltaY / deltaX) * 180 / Math.PI;
    }

    public void setCorners(Point[] corners) {
        for (int i = 0; i < 4; i++) {
            mobileRect.getPoints().set(i * 2, corners[i].x);
            mobileRect.getPoints().set(i * 2 + 1, corners[i].y);
        }
    }

    public double getSlope(double x1, double y1, double x2, double y2) {
        if (x2 - x1 == 0) {
            return 0;
        }
        return (y2 - y1) / (x2 - x1);

    }

    public void addBorder() {

        setUpSelecting(resizeHandleNW);
        setUpSelecting(resizeHandleSE);
        setUpSelecting(rotateHandle);

        setUpDragging(resizeHandleNW);
        setUpDragging(resizeHandleSE);
        setUpDragging(selectRect);
        setUpDragging(rotateHandle);

        resizeHandleNW.setOnMouseDragged(event -> {
            if (mouseLocation.value == null) {
                return;
            }

            double deltaX = event.getSceneX() - mouseLocation.value.x;
            double deltaY = event.getSceneY() - mouseLocation.value.y;

            float d = (float) ((abs(deltaX) > abs(deltaY)) ? deltaX : deltaY);
            Point p = new Point(mobileRect.getPoints().get(0) + d,
                    mobileRect.getPoints().get(1) + d);

            int w = currentImage.getUnrotatedCroppedWidth();
            int h = currentImage.getUnrotatedCroppedHeight();
            int x = currentImage.getUnrotatedCroppedX();
            int y = currentImage.getUnrotatedCroppedY();

            float newScale = (float) ((float) (p.x - x) * (p.y - y) / ((float) w * h));

            currentImage.translate(p);
            currentImage.scale(newScale);

            drawInternalGizmo();

            mouseLocation.value = new Point(event.getSceneX(), event.getSceneY());

            type = "scale";
        });

        resizeHandleSE.setOnMouseDragged(event -> {
            if (mouseLocation.value == null) {
                return;
            }

            double deltaX = event.getSceneX() - mouseLocation.value.x;
            double deltaY = event.getSceneY() - mouseLocation.value.y;

            float d = (float) ((abs(deltaX) > abs(deltaY)) ? deltaX : deltaY);

            Point p = new Point(mobileRect.getPoints().get(4) + d,
                    mobileRect.getPoints().get(5) + d);

            int w = currentImage.getUnrotatedCroppedWidth();
            int h = currentImage.getUnrotatedCroppedHeight();
            int x = currentImage.getUnrotatedCroppedX();
            int y = currentImage.getUnrotatedCroppedY();

            double newScale = (currentImage.scale * abs((p.x - x) * (p.y - y) / ((double) w * h)));

            currentImage.scale(abs(newScale) > 0.01 ? abs(newScale) : currentImage.scale);

            drawInternalGizmo();

            mouseLocation.value = new Point(event.getSceneX(), event.getSceneY());

            type = "scale";
        });

        rotateHandle.setOnMouseDragged(event -> {
            if (mouseLocation.value == null) {
                return;
            }

            Point mid = currentImage.getCenter();

            double x = event.getSceneX() - mid.x;
            double y = event.getSceneY() - mid.y;

            float angle = (float) ((handleAngle - atan2(y, x)) * 180 / PI);
            proof.setStartX(mid.x);
            proof.setStartY(mid.y);
            proof.setEndX(x + mid.x);
            proof.setEndY(y + mid.y);

            currentImage.rotate(-angle);

            drawInternalGizmo();
            mouseLocation.value = new Point(event.getSceneX(), event.getSceneY());

            type = "rotate";
        });

        selectRect.setOnMouseDragged(event -> {
            if (mouseLocation.value == null) {
                return;
            }

            double deltaX = event.getSceneX() - mouseLocation.value.x;
            double deltaY = event.getSceneY() - mouseLocation.value.y;
            currentImage.translate(
                    new Point(
                            currentImage.x + deltaX,
                            currentImage.y + deltaY
                    )
            );

            drawInternalGizmo();
            mouseLocation.value = new Point(event.getSceneX(), event.getSceneY());

            type = "translate";
        }
        );

    }

    private void setUpSelecting(Shape shape) {
        shape.setOnMouseClicked(event -> {
            System.out.println("AAAAA");
            isEditing = true;
        });
        shape.setOnMousePressed(event -> {
            System.out.println("BBBB");
            isEditing = true;
        });
        shape.setOnMouseReleased(event -> {
            System.out.println("CCCC");
            isEditing = false;
        });
    }

    private void setUpDragging(Shape shape) {

        shape.setOnDragDetected(event -> {
            shape.setCursor(Cursor.CLOSED_HAND);
            mouseLocation.value = new Point(event.getSceneX(), event.getSceneY());
            isEditing = true;
            System.out.println("DRAGGING");
        });

        shape.setOnMouseReleased(event -> {
            shape.setCursor(Cursor.DEFAULT);
            mouseLocation.value = null;
            isEditing = false;
            System.out.println("REALESED");
        });
    }

    public void removeOnCanvas(Pane canvasLayout) {
        canvasLayout.getChildren().removeAll(
                mobileRect, selectRect, resizeHandleNW,
                resizeHandleSE, rotateLine, rotateHandle);
    }

    public void addOnCanvas(Pane canvasLayout) {
        canvasLayout.getChildren().add(proof);
        canvasLayout.getChildren().addAll(
                mobileRect, selectRect, resizeHandleNW,
                resizeHandleSE, rotateLine, rotateHandle);

    }

    static class Wrapper<T> {

        T value;
    }
}
