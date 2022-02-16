/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author vixx_
 */
public class RawImage extends Mat {

    public String filename;

    public RawImage(Size newDim, int CV_16F) {
        super(newDim, CV_16F);
    }

    public RawImage() {
        super();
    }

    public RawImage copy() {
        RawImage aux = new RawImage();
        this.clone().assignTo(aux);
        aux.filename = this.filename;
        return aux;
    }

    public boolean readImage(String filename) {
        this.filename = filename;
        Imgcodecs.imread(filename, -1).assignTo(this);
        Imgproc.cvtColor(this, this, 0);

        return !this.empty();
    }

    public boolean writeImage(String filen) {
        return Imgcodecs.imwrite(filen, this);
    }
}
