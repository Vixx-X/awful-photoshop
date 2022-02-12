/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject;


import fxproject.graphics.CanvasEntity;
import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import org.opencv.core.Point;

public final class Gizmo {

    //public Rectangle selectRect;
    public Polygon selectRect;
    public Polygon mobileRect;
    //public Rectangle mobileRect;
    //insert padding
    public int pd;
    public Circle resizeHandleNW;
    public Circle resizeHandleSE;
    public Circle rotateHandle;
    public Line rotateLine;
    private double handleRadius;
    public Point finalCorner;
    public String type;
    public CanvasEntity currentImage;

    public Gizmo(CanvasEntity img) {
        currentImage = img;
        drawGizmo();
    }

    public void drawGizmo() {
        Point[] corners = currentImage.getCorners();
        pd = 0;
        handleRadius = 6.5;
        double x = corners[0].x;
        double y = corners[0].y;

        createLine(corners);

        double[] points = new double[]{
            corners[0].x, corners[0].y,
            corners[1].x, corners[1].y,
            corners[2].x, corners[2].y,
            corners[3].x, corners[3].y};

        selectRect = new Polygon(points);
        //selectRect = new Rectangle(x - pd, y - pd, w + 2 * pd, h + 2 * pd);
        selectRect.setStyle("-fx-fill: transparent; -fx-stroke: black; -fx-stroke-width: 1;");
        mobileRect = new Polygon(points);
        //mobileRect = new Rectangle(x - pd, y - pd, w + 2 * pd, h + 2 * pd);
        mobileRect.setStyle("-fx-fill: transparent; -fx-stroke: red; -fx-stroke-width: 1;");
        resizeHandleNW = new Circle(x - pd, y - pd, handleRadius);
        resizeHandleSE = new Circle(corners[2].x + pd, corners[2].y + pd, handleRadius);
        resizeHandleNW.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 0.8;");
        resizeHandleSE.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 0.8;");

        finalCorner = new Point(0, 0);
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

    public void setPoint(Polygon pol, int ind, double d) {
        pol.getPoints().set(ind, pol.getPoints().get(ind) + d);
    }

    public void setCorners(Point[] corners) {
        for (int i = 0; i < 4; i++) {
            mobileRect.getPoints().set(i * 2, corners[i].x);
            mobileRect.getPoints().set(i * 2 + 1, corners[i].y);
        }
    }

    public boolean checkBorder(Polygon p, double limiti, double limitf, double delta, int ind) {
        if (p.getPoints().get(ind) + delta >= limiti
                && p.getPoints().get(ind) + delta <= limitf) {
            return true;
        } else {
            return false;
        }
    }

    public double getSlope(double x1, double y1, double x2, double y2) {
        if (x2 - x1 != 0) {
            return (y2 - y1) / (x2 - x1);
        }
        return 0;
    }

    public Point getCoord(Point dest, double m1, double d) {
        //double m1 = getSlope(src.x, src.y, dest.x, dest.y);
        //double dist = sqrt(pow((dest.x - src.x),2) + pow((dest.y - src.y),2));
        double x2 = cos(atan(m1)) * d;
        double y2 = sqrt(pow(d, 2) - pow(x2, 2));
        return new Point(dest.x + x2, dest.y + y2);
        //double newdist = dist + inc;
    }

    public Point getIntersection(Point p1, Point p2, Point p3) {
        double m1 = getSlope(p1.x, p1.y, p2.x, p2.y);
        double m2 = getSlope(p2.x, p2.y, p3.x, p3.y);
        double b1 = p1.y - m1 * p1.x;
        double b2 = p2.y - m2 * p2.x;
        double x = -1;
        double y = -1;
        if (m1 - m2 != 0) {
            x = (b2 - b1) / (m1 - m2);
            y = m1 * p1.x + b1;
        }
        return new Point(x, y);
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

                float d = (float) ((abs(deltaX) > abs(deltaY)) ? deltaX : deltaY);
                Point p = new Point(mobileRect.getPoints().get(0) + d,
                        mobileRect.getPoints().get(1) + d);
                int w = currentImage.getWidth();
                int h = currentImage.getHeight();
                
                float newScale = (float) ((float)(p.x - currentImage.x)*(p.y - currentImage.y)/((float)w*h));
                currentImage.translate(p);
                currentImage.scale(newScale);
                setCorners(currentImage.getCorners());
                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());

            }
            type = "scale";
        });

        resizeHandleSE.setOnMouseDragged(event -> {
            if (mouseLocation.value != null) {
                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                float d = (float) ((abs(deltaX) > abs(deltaY)) ? deltaX : deltaY);
                Point p = new Point(mobileRect.getPoints().get(4) + d,
                        mobileRect.getPoints().get(5) + d);
                int w = currentImage.getWidth();
                int h = currentImage.getHeight();
                
                float newScale = (float) ((float)(p.x - currentImage.x)*(p.y - currentImage.y)/((float)w*h));
                //System.out.println(newScale);
                currentImage.scale(newScale);
                setCorners(currentImage.getCorners());
                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
            }
            type = "scale";
        });

        rotateHandle.setOnMouseDragged(event -> {
            if (mouseLocation.value == null) {
                return;
            }
            //double deltaX = event.getSceneX() - mouseLocation.value.getX();
            //double deltaY = event.getSceneY() - mouseLocation.value.getY();

            ObservableList<Double> points = mobileRect.getPoints();

            Point middle = new Point((points.get(0) + points.get(2)) / 2,
                    (points.get(1) + points.get(3)) / 2);
            double angle1;
            if (points.get(3) - points.get(1) == 0) {
                angle1 = 90;
            } else if (points.get(2) - points.get(0) == 0) {
                angle1 = 0;
            } else {
                double m = getSlope(points.get(0), points.get(1), points.get(2), points.get(3));
                double m2 = -1 / m;
                angle1 = toDegrees(atan(m2));

            }
            double m3 = getSlope(event.getX(), event.getY(), middle.x, middle.y);

            double angle = 360 - (angle1 - toDegrees(atan(m3)));

            System.out.println(angle + " " + atan(m3));
            //mobileRect.setRotate(angle);

            currentImage.rotate((int) angle);
            setCorners(currentImage.getCorners());

            /*double newMaxX = mobileRect.getX() + mobileRect.getWidth() + deltaX;
                if (newMaxX >= mobileRect.getX()
                        && newMaxX <= mobileRect.getParent().getBoundsInLocal().getWidth() - handleRadius) {
                    mobileRect.setWidth(mobileRect.getWidth() + deltaX);
                }
                double newMaxY = mobileRect.getY() + mobileRect.getHeight() + deltaY;
                if (newMaxY >= mobileRect.getY()
                        && newMaxY <= mobileRect.getParent().getBoundsInLocal().getHeight() - handleRadius) {
                    mobileRect.setHeight(mobileRect.getHeight() + deltaY);
                } */
            mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
            type = "rotate";
        });

        selectRect.setOnMouseDragged(event -> {
            if (mouseLocation.value != null) {
                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                currentImage.translate(new Point(currentImage.x + deltaX,
                        currentImage.y + deltaY));

                setCorners(currentImage.getCorners());
                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
            }
            type = "translate";

        }
        );
    }

    private void setUpDragging(Shape shape, Wrapper<Point2D> mouseLocation) {

        shape.setOnDragDetected(event -> {
            shape.getParent().setCursor(Cursor.CLOSED_HAND);
            mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
        });

        shape.setOnMouseReleased(event -> {
            shape.getParent().setCursor(Cursor.DEFAULT);
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
