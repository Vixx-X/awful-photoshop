/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 *
 * @author vixx_
 */
public class RawImage extends Mat {

    public String filename;
    public int rotationAngle;

    public boolean readImage(String filename) {
        this.filename = filename;
        Imgcodecs.imread(filename).assignTo(this);

        return this.empty();
    }

    public boolean writeImage(String filename) {
        return Imgcodecs.imwrite(this.filename, this);
    }

}
