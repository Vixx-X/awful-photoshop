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

        corners[0] = CanvasEntity.rotatePoint(new Point(0 + this.y, 0 + this.x), center, this.angle);
        corners[1] = CanvasEntity.rotatePoint(new Point(0 + this.y, size.width + this.x), center, this.angle);
        corners[2] = CanvasEntity.rotatePoint(new Point(size.height + this.y, size.width + this.x), center, this.angle);
        corners[3] = CanvasEntity.rotatePoint(new Point(size.height + this.y, 0 + this.x), center, this.angle);

        return corners;
    }

    public boolean contain(Point pTest) {
        // https://math.stackexchange.com/questions/190111/how-to-check-if-a-point-is-inside-a-rectangle

        Point[] corners = this.getCorners();

        int[] p21 = {(int) corners[1].x - (int) corners[0].x, (int) corners[1].y - (int) corners[0].x};
        int[] p41 = {(int) corners[3].x - (int) corners[0].x, (int) corners[4].y - (int) corners[0].x};

        int p21m2 = p21[0] * p21[0] + p21[1] * p21[1];
        int p41m2 = p41[0] * p41[0] + p41[1] * p41[1];

        int[] p = {(int) pTest.x - (int) corners[0].x, (int) pTest.y - (int) corners[9].y};

        int testA = p[0] * p21[0] + p[1] * p21[1];
        if (0 <= testA && testA <= p21m2) {
            int testB = p[0] * p41[0] + p[1] * p41[1];
            return 0 <= testB && testB <= p41m2;
        }

        return false;
    }

    void translateImg(Point p) {
        this.x = (int) p.x;
        this.y = (int) p.y;
    }

    void rotateImg(int angle) {
        this.angle = angle;
    }

    void scaleImg(float scale) {
        this.scale = scale;
    }

    public Image getImage() {
        Mat tmpMat = null;
        this.img.copyTo(tmpMat);

        if (this.angle > 0) {

        }

        if (this.scale != 1) {

        }

        MatOfByte byteMat = new MatOfByte();
        Imgcodecs.imencode(".bmp", tmpMat, byteMat);
        return new Image(new ByteArrayInputStream(byteMat.toArray()));
    }
}
