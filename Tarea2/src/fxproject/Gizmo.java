/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject;

import java.util.Arrays;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public final class Gizmo {

    public Rectangle selectRect;
    public Rectangle mobileRect;
    //insert padding
    public int pd;
    public Circle resizeHandleNW;
    public Circle resizeHandleSE;
    private final double handleRadius;

    public Gizmo(int x, int y, double w, double h, Pane root) {
        pd = 3;
        handleRadius = 6.5;
        selectRect = new Rectangle(x - pd, y - pd, w + 2 * pd, h + 2 * pd);
        selectRect.setStyle("-fx-fill: transparent; -fx-stroke: black; -fx-stroke-width: 1;");
        mobileRect = new Rectangle(x - pd, y - pd, w + 2 * pd, h + 2 * pd);
        mobileRect.setStyle("-fx-fill: transparent; -fx-stroke: red; -fx-stroke-width: 1;");
        resizeHandleNW = new Circle(handleRadius);
        resizeHandleSE = new Circle(handleRadius);
        addBorder();
    }

    public void addBorder() {
        // rect.setStyle("-fx-background-color:white; -fx-border-style:solid; -fx-border-width:3; -fx-border-color:black;");

        final String style = "-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 0.8;";
        //Circle circle = new Circle(x, y, 6.5);
        //circle.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 0.8;");

        // bind to top left corner of Rectangle:
        resizeHandleNW.centerXProperty().bind(selectRect.xProperty());
        resizeHandleNW.centerYProperty().bind(selectRect.yProperty());
        resizeHandleNW.setStyle(style);

        // bottom right resize handle:
        // bind to bottom right corner of Rectangle:
        resizeHandleSE.centerXProperty().bind(selectRect.xProperty().add(selectRect.widthProperty()));
        resizeHandleSE.centerYProperty().bind(selectRect.yProperty().add(selectRect.heightProperty()));
        resizeHandleSE.setStyle(style);

        selectRect.parentProperty().addListener((ObservableValue<? extends Parent> obs, Parent oldParent, Parent newParent) -> {
            for (Circle c1 : Arrays.asList(resizeHandleNW, resizeHandleSE)) {
                Pane currentParent = (Pane) c1.getParent();
                if (currentParent != null) {
                    currentParent.getChildren().remove(c1);
                }
                ((Pane) newParent).getChildren().add(c1);
            }
        });

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

        });
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

    static class Wrapper<T> {

        T value;
    }
}