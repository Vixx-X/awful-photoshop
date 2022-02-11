/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject;

import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.Arrays;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import org.opencv.core.Point;

public final class Gizmo {

    public Rectangle selectRect;
    public Rectangle mobileRect;
    //insert padding
    public int pd;
    public Circle resizeHandleNW;
    public Circle resizeHandleSE;
    public Circle rotateHandle;
    public Line rotateLine;
    private final double handleRadius;
    public Point finalCorner;
    public String type;

    public Gizmo(Point[] corners, double w, double h) {
        pd = 0;
        handleRadius = 6.5;
        double x = corners[0].x;
        double y = corners[0].y;

        createLine(corners);

        selectRect = new Rectangle(x - pd, y - pd, w + 2 * pd, h + 2 * pd);
        selectRect.setStyle("-fx-fill: transparent; -fx-stroke: black; -fx-stroke-width: 1;");
        mobileRect = new Rectangle(x - pd, y - pd, w + 2 * pd, h + 2 * pd);
        mobileRect.setStyle("-fx-fill: transparent; -fx-stroke: red; -fx-stroke-width: 1;");
        resizeHandleNW = new Circle(x - pd, y - pd, handleRadius);
        resizeHandleSE = new Circle(corners[2].x + pd, corners[2].y + pd, handleRadius);
        resizeHandleNW.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 0.8;");
        resizeHandleSE.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 0.8;");

        //rotate(corners);
        addBorder();

    }

    private void createLine(Point[] corners) {
        double x1 = (corners[0].x + corners[1].x) / 2;
        double y1 = (corners[0].y + corners[1].y) / 2;
        double deltay = corners[1].y - corners[0].y;
        double deltax = corners[1].x - corners[0].x;
        double x2;
        double y2;

        double d = 20;

        if (deltay == 0) {
            x2 = x1;
            y2 = (deltax > 0) ? y1 - d : y1 + d;
        } else if (deltax == 0) {
            y2 = y1;
            x2 = (deltay > 0) ? x1 + d : x1 - d;
        } else {
            double m1 = -1 / (corners[1].y - corners[0].y / corners[1].x - corners[0].x);
            x2 = cos(atan(m1)) * d;
            y2 = sqrt(pow(d, 2) - pow(x2, 2));
        }
        rotateLine = new Line(x1, y1, x2, y2);
        rotateHandle = new Circle(x2, y2, 6.5);
        rotateHandle.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 0.8;");

    }

    public double getAngle(Point[] corner) {
        double deltaX = corner[1].x - corner[0].x;
        double deltaY = corner[1].y - corner[0].y;
        return Math.atan(deltaY / deltaX) * 180 / Math.PI;
    }

    public void rotate(Point[] corners) {
        double angle = getAngle(corners);
        selectRect.setRotate(angle);
        mobileRect.setRotate(angle);
    }

    public void addBorder() {
        Wrapper<Point2D> mouseLocation = new Wrapper<>();

        setUpDragging(resizeHandleNW, mouseLocation);
        setUpDragging(resizeHandleSE, mouseLocation);
        setUpDragging(selectRect, mouseLocation);
        setUpDragging(rotateHandle, mouseLocation);

        resizeHandleNW.setOnMouseDragged(event -> {
            if (mouseLocation.value != null) {
                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                double newX = mobileRect.getX() + (deltaX + deltaY) / 2;
                double newY = mobileRect.getY() + (deltaX + deltaY) / 2;
                if (newX >= handleRadius && newY >= handleRadius
                        && newX <= mobileRect.getX() + mobileRect.getWidth() - handleRadius
                        && newY <= mobileRect.getY() + mobileRect.getHeight() - handleRadius) {
                    mobileRect.setX(newX);
                    mobileRect.setWidth(mobileRect.getWidth() - (deltaX + deltaY) / 2);
                    mobileRect.setY(newY);
                    mobileRect.setHeight(mobileRect.getHeight() - (deltaX + deltaY) / 2);
                }
                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
            }
            type = "scale";
        });

        resizeHandleSE.setOnMouseDragged(event -> {
            if (mouseLocation.value != null) {
                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                double newMaxX = mobileRect.getX() + mobileRect.getWidth() + deltaX;
                double newMaxY = mobileRect.getY() + mobileRect.getHeight() + deltaY;
                if (newMaxX >= mobileRect.getX() && newMaxY >= mobileRect.getY()
                        && newMaxX <= mobileRect.getParent().getBoundsInLocal().getWidth() - handleRadius
                        && newMaxY <= mobileRect.getParent().getBoundsInLocal().getHeight() - handleRadius) {
                    mobileRect.setWidth(mobileRect.getWidth() + (deltaX + deltaY) / 2);
                    mobileRect.setHeight(mobileRect.getHeight() + (deltaX + deltaY) / 2);
                }

                /*if (newMaxY >= mobileRect.getY() && newMaxX >= mobileRect.getX()
                        && newMaxY <= mobileRect.getParent().getBoundsInLocal().getHeight() - handleRadius) {
                    
                }*/
                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
            }
            type = "scale";
        });

        rotateHandle.setOnMouseDragged(event -> {
            if (mouseLocation.value != null) {
                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                double newMaxX = mobileRect.getX() + mobileRect.getWidth() + deltaX;
                if (newMaxX >= mobileRect.getX()
                        && newMaxX <= mobileRect.getParent().getBoundsInLocal().getWidth() - handleRadius) {
                    mobileRect.setWidth(mobileRect.getWidth() + deltaX);
                }
                double newMaxY = mobileRect.getY() + mobileRect.getHeight() + deltaY;
                if (newMaxY >= mobileRect.getY()
                        && newMaxY <= mobileRect.getParent().getBoundsInLocal().getHeight() - handleRadius) {
                    mobileRect.setHeight(mobileRect.getHeight() + deltaY);
                }
                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
            }
            type = "scale";
        });

        selectRect.setOnMouseDragged(event -> {
            if (mouseLocation.value != null) {
                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                double newX = mobileRect.getX() + deltaX;
                double newMaxX = newX + mobileRect.getWidth();
                if (newX >= handleRadius
                        && newMaxX <= mobileRect.getParent().getBoundsInLocal().getWidth() - handleRadius) {
                    mobileRect.setX(newX);
                }
                double newY = mobileRect.getY() + deltaY;
                double newMaxY = newY + mobileRect.getHeight();
                if (newY >= handleRadius
                        && newMaxY <= mobileRect.getParent().getBoundsInLocal().getHeight() - handleRadius) {
                    mobileRect.setY(newY);
                }
                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
            }
            type = "translate";

        });
    }

    public Point setNewPoint() {
        finalCorner.x = mobileRect.getX();
        finalCorner.y = mobileRect.getY();
        return finalCorner;
    }

    private void setUpDragging(Shape circle, Wrapper<Point2D> mouseLocation) {

        circle.setOnDragDetected(event -> {
            circle.getParent().setCursor(Cursor.CLOSED_HAND);
            mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
        });

        circle.setOnMouseReleased(event -> {
            circle.getParent().setCursor(Cursor.DEFAULT);
            mouseLocation.value = null;
        });
    }

    void removeOnCanvas(Pane canvasLayout) {
        canvasLayout.getChildren().removeAll(mobileRect, selectRect, resizeHandleNW,
                resizeHandleSE, rotateLine, rotateHandle);
    }

    void addOnCanvas(Pane canvasLayout) {
        canvasLayout.getChildren().addAll(mobileRect, selectRect, resizeHandleNW,
                resizeHandleSE, rotateLine, rotateHandle);
    }

    static class Wrapper<T> {

        T value;
    }
}
