/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject;

import static java.lang.Math.asin;
import java.util.Arrays;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import org.opencv.core.Point;

public final class Gizmo {

    public Rectangle selectRect;
    public Rectangle mobileRect;
    //insert padding
    public int pd;
    public Circle resizeHandleNW;
    public Circle resizeHandleSE;
    private final double handleRadius;
    public Point finalCorner;
    public String type;

    public Gizmo(Point[] corners, double w, double h) {
        pd = 3;
        handleRadius = 6.5;
        double x = corners[0].x;
        double y = corners[0].y;
        selectRect = new Rectangle(x - pd, y - pd, w + 2 * pd, h + 2 * pd);
        selectRect.setStyle("-fx-fill: transparent; -fx-stroke: black; -fx-stroke-width: 1;");
        mobileRect = new Rectangle(x - pd, y - pd, w + 2 * pd, h + 2 * pd);
        mobileRect.setStyle("-fx-fill: transparent; -fx-stroke: red; -fx-stroke-width: 1;");
        resizeHandleNW = new Circle(x - pd, y - pd, handleRadius);
        resizeHandleSE = new Circle(corners[2].x + pd, corners[2].y + pd, handleRadius);
        resizeHandleNW.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 0.8;");
        resizeHandleSE.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 0.8;");

        rotate(corners);
        addBorder();

    }

    public double getAngle(Point[] corner){
        double deltaX = corner[1].x - corner[0].x;
        double deltaY = corner[1].y - corner[0].y;
        return Math.atan(deltaY/deltaX) * 180 / Math.PI;
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

        resizeHandleNW.setOnMouseDragged(event -> {
            if (mouseLocation.value != null) {
                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                double newX = mobileRect.getX() + deltaX;
                if (newX >= handleRadius
                        && newX <= mobileRect.getX() + mobileRect.getWidth() - handleRadius) {
                    mobileRect.setX(newX);
                    mobileRect.setWidth(mobileRect.getWidth() - deltaX);
                }
                double newY = mobileRect.getY() + deltaY;
                if (newY >= handleRadius
                        && newY <= mobileRect.getY() + mobileRect.getHeight() - handleRadius) {
                    mobileRect.setY(newY);
                    mobileRect.setHeight(mobileRect.getHeight() - deltaY);
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
    
    public Point setNewPoint(){
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
        canvasLayout.getChildren().remove(resizeHandleNW);
        canvasLayout.getChildren().remove(resizeHandleSE);
        canvasLayout.getChildren().remove(mobileRect);
        canvasLayout.getChildren().remove(selectRect);
    }

    void addOnCanvas(Pane canvasLayout) {
        canvasLayout.getChildren().add(mobileRect);
        canvasLayout.getChildren().add(selectRect);
        canvasLayout.getChildren().add(resizeHandleNW);
        canvasLayout.getChildren().add(resizeHandleSE);
    }

    static class Wrapper<T> {

        T value;
    }
}
