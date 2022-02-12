/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics;

import java.io.ByteArrayInputStream;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author vixx_
 */
public class CanvasEntity {

    public int x, y;
    public RawImage img;
    public int angle;
    public float scale;

    public CanvasEntity(int x, int y, String filename) {
        this.x = x;
        this.y = y;
        this.img = new RawImage();
        this.img.readImage(filename);
        this.angle = 0;
        this.scale = 1;
    }

    public CanvasEntity(CanvasEntity other) {
        this.x = other.x;
        this.y = other.y;
        this.img = other.img;
        this.angle = other.angle;
        this.scale = other.scale;
    }

    public CanvasEntity(int x, int y, RawImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.angle = 0;
        this.scale = 1;
    }

    static Point rotatePoint(Point p, Point center, int angle) {
        return new Point(
                cos(angle) * (p.x - center.x) - sin(angle) * (p.y - center.y) + center.x,
                sin(angle) * (p.x - center.x) + cos(angle) * (p.y - center.y) + center.y
        );
    }

    public Point[] getCorners() {
        Point[] corners = new Point[4];

        Size size = this.img.size();
        Point center = new Point(size.height / 2, size.width / 2);

        corners[0] = CanvasEntity.rotatePoint(new Point(0 + this.x, 0 + this.y), center, this.angle);
        corners[1] = CanvasEntity.rotatePoint(new Point(size.width + this.x, 0 + this.y), center, this.angle);
        corners[2] = CanvasEntity.rotatePoint(new Point(size.width + this.x, size.height + this.y), center, this.angle);
        corners[3] = CanvasEntity.rotatePoint(new Point(0 + this.x, size.height + this.y), center, this.angle);

        return corners;
    }

    public boolean contain(Point pTest) {
        // https://math.stackexchange.com/questions/190111/how-to-check-if-a-point-is-inside-a-rectangle

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
        this.x = (int) p.x;
        this.y = (int) p.y;
    }

    public void rotate(int angle) {
        this.angle = angle;
    }

    public void scale(float scale) {
        this.scale = scale;
    }

    static public Mat translateImg(Mat img, Point p) {
        // if this was more low level, here we will translate using Mat.mult
        return img;
    }

    static public Mat rotateImg(Mat img, int angle) {
        if (angle % 360 == 0) {
            return img;
        }
        return img;
    }

    static public Mat scaleImg(Mat img, float scale) {
        if (scale == 1) {
            return img;
        }
        return img;
    }

    public Image getImage() {
        Mat tmpMat = new Mat();
        this.img.copyTo(tmpMat);

        //tmpMat = translateImg(tmpMat, point);
        tmpMat = rotateImg(tmpMat, this.angle);
        tmpMat = scaleImg(tmpMat, this.scale);

        MatOfByte byteMat = new MatOfByte();
        Imgcodecs.imencode(".bmp", tmpMat, byteMat);
        return new Image(new ByteArrayInputStream(byteMat.toArray()));
    }
}
