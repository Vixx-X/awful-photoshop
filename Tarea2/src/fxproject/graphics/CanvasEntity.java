/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics;

import fxproject.ProjectImages;
import fxproject.graphics.filters.locals.GaussFilter;
import fxproject.graphics.transformations.affine.Rotation;
import fxproject.graphics.transformations.affine.Scale;
import java.io.ByteArrayInputStream;
import static java.lang.Math.cos;
import static java.lang.Math.min;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import java.util.UUID;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Range;

/**
 *
 * @author vixx_
 */
public class CanvasEntity {

    public int x, y;
    public RawImage img;
    public double angle;
    public double scale;
    public int padTop;
    public int padRight;
    public int padBottom;
    public int padLeft;
    public UUID id;
    public int aliasing;

    public CanvasEntity(int x, int y, String filename) {
        this.padTop = 0;
        this.padRight = 0;
        this.padBottom = 0;
        this.padLeft = 0;
        this.x = x;
        this.y = y;
        this.img = new RawImage();
        this.img.readImage(filename);
        this.angle = 0;
        this.scale = 1;
        this.id = UUID.randomUUID();
    }

    public CanvasEntity(CanvasEntity other) {
        this.padTop = other.padTop;
        this.padRight = other.padRight;
        this.padBottom = other.padBottom;
        this.padLeft = other.padLeft;
        this.x = other.x;
        this.y = other.y;
        this.img = other.img.copy();
        this.angle = other.angle;
        this.scale = other.scale;
        this.id = other.id;
    }

    public CanvasEntity(int x, int y, RawImage img) {
        this.padTop = 0;
        this.padRight = 0;
        this.padBottom = 0;
        this.padLeft = 0;
        this.x = x;
        this.y = y;
        this.img = img;
        this.angle = 0;
        this.scale = 1;
        this.id = UUID.randomUUID();
    }

    public Point getCenter() {
        int width = this.getUnrotatedCroppedWidth();
        int height = this.getUnrotatedCroppedHeight();
        int _x = this.getUnrotatedCroppedX();
        int _y = this.getUnrotatedCroppedY();
        return new Point(width / 2 + _x, height / 2 + _y);
    }

    public Point getRasterCoord() {
        Point[] corners = this.getCorners();
        Point ret = new Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        for (Point i : corners) {
            ret.x = min(i.x, ret.x);
            ret.y = min(i.y, ret.y);
        }
        return ret;
    }

    static Point rotatePoint(Point p, Point center, double angle) {
        double c = cos(angle);
        double s = sin(angle);

        return new Point(
                c * (p.x - center.x) - s * (p.y - center.y) + center.x,
                s * (p.x - center.x) + c * (p.y - center.y) + center.y
        );
    }

    public int getUnrotatedCroppedWidth() {
        Size size = this.img.size();
        return (int) round((size.width - this.padLeft - this.padRight) * this.scale);
    }

    public int getUnrotatedCroppedHeight() {
        Size size = this.img.size();
        return (int) round((size.height - this.padTop - this.padBottom) * this.scale);
    }

    public int getUnrotatedUnscaledCroppedWidth() {
        Size size = this.img.size();
        return (int) round(size.width - this.padLeft - this.padRight);
    }

    public int getUnrotatedUnscaledCroppedHeight() {
        Size size = this.img.size();
        return (int) round(size.height - this.padTop - this.padBottom);
    }

    public int getUnrotatedCroppedX() {
        return this.x + this.padLeft;
    }

    public int getUnrotatedCroppedY() {
        return this.y + this.padTop;
    }

    public Point[] getCorners() {
        Point[] corners = new Point[4];
        int width = this.getUnrotatedCroppedWidth();
        int height = this.getUnrotatedCroppedHeight();
        int _x = this.getUnrotatedCroppedX();
        int _y = this.getUnrotatedCroppedY();
        Point center = this.getCenter();

        double _angle = this.angle * Math.PI / 180;

        corners[0] = CanvasEntity.rotatePoint(new Point(0 + _x, 0 + _y), center, _angle);
        corners[1] = CanvasEntity.rotatePoint(new Point(width + _x, 0 + _y), center, _angle);
        corners[2] = CanvasEntity.rotatePoint(new Point(width + _x, height + _y), center, _angle);
        corners[3] = CanvasEntity.rotatePoint(new Point(0 + _x, height + _y), center, _angle);

        return corners;
    }

    public boolean contain(Point pTest) {
        // https://math.stackexchange.com/questions/19011/how-to-check-if-a-point-is-inside-a-rectangle

        Point[] corners = this.getCorners();

        int[] p21 = {(int) corners[1].x - (int) corners[0].x, (int) corners[1].y - (int) corners[0].y};
        int[] p41 = {(int) corners[3].x - (int) corners[0].x, (int) corners[3].y - (int) corners[0].y};

        int p21m2 = p21[0] * p21[0] + p21[1] * p21[1];
        int p41m2 = p41[0] * p41[0] + p41[1] * p41[1];

        int[] p = {(int) pTest.x - (int) corners[0].x, (int) pTest.y - (int) corners[0].y};

        int testA = p[0] * p21[0] + p[1] * p21[1];
        if (0 <= testA && testA <= p21m2) {
            int testB = p[0] * p41[0] + p[1] * p41[1];
            return 0 <= testB && testB <= p41m2;
        }

        return false;
    }

    public void translate(Point p) {
        this.x = (int) p.x - this.padLeft;
        this.y = (int) p.y - this.padTop;
    }

    public void rotate(double angle) {
        this.angle = angle;
    }

    public void scale(double scale) {
        this.scale = scale;
    }

    public void crop(int padTop, int padRight, int padBot, int padLeft) {
        this.padTop = padTop;
        this.padRight = padRight;
        this.padBottom = padBot;
        this.padLeft = padLeft;
    }

    static public RawImage translateImg(RawImage img, Point p) {
        // if this was more low level, here we will translate using Mat.mult
        return img;
    }

    static public RawImage rotateImg(RawImage img, double angle) {
        if (angle % 360 == 0) {
            return img;
        }
        RawImage ret = Rotation.apply(img, angle);
        return ret;
    }

    static public RawImage scaleImg(RawImage img, double scale) {
        if (scale == 1) {
            return img;
        }
        RawImage ret = Scale.apply(img, scale);
        return ret;
    }

    static public RawImage cropImg(RawImage img, int padTop, int padRight, int padBot, int padLeft) {
        RawImage ret = new RawImage();
        img.submat(padTop, img.height() - padBot, padLeft, img.width() - padRight).copyTo(ret);
        return ret;
    }

    public Image getImage() {
        RawImage tmpMat = getRawImage();
        MatOfByte byteMat = new MatOfByte();
        Imgcodecs.imencode(".png", tmpMat, byteMat);
        return new Image(new ByteArrayInputStream(byteMat.toArray()));
    }

    public RawImage getRawImage() {
        int tmp = ProjectImages.getInstance().getInterpolation();
        if (this.aliasing == 0) {
            this.aliasing = tmp;
        }
        ProjectImages.getInstance().setInterpolation(this.aliasing);
        RawImage tmpMat = new RawImage();
        this.img.copyTo(tmpMat);
        tmpMat = cropImg(tmpMat, this.padTop, this.padRight, this.padBottom, this.padLeft);
        if (this.aliasing == 4) {
            tmpMat = GaussFilter.apply(tmpMat, tmpMat.width(), tmpMat.height());
        }
        tmpMat = rotateImg(tmpMat, this.angle);
        tmpMat = scaleImg(tmpMat, this.scale);

        ProjectImages.getInstance().setInterpolation(tmp);
        return tmpMat;
    }

}
