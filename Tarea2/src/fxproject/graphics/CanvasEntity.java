/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics;

import java.io.ByteArrayInputStream;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author vixx_
 */
public class CanvasEntity {

    public int x, y;
    public RawImage img;
    public int angle;

    public CanvasEntity(int x, int y, String filename) {
        this.x = x;
        this.y = y;
        this.img = new RawImage();
        this.img.readImage(filename);
    }

    public boolean contain(Point p) {
        return false;
    }

    //   static Mat rotate(Mat src, int angle) {
    //      Mat rotMat = [[ cos(angle), -sin(angle)], [sin(angle), cos(angle)]];
    // }
    public Image getImage() {
        Mat tmpMat = this.img;
        if (this.angle > 0) {

        }
        MatOfByte byteMat = new MatOfByte();
        Imgcodecs.imencode(".bmp", tmpMat, byteMat);
        return new Image(new ByteArrayInputStream(byteMat.toArray()));
    }
}
