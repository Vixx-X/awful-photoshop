/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics;

import java.util.ArrayList;
import org.opencv.core.Point;

import static fxproject.Vendors.Reversed.reversed;

/**
 *
 * @author vixx_
 */
public class Canvas {

    public ArrayList<CanvasEntity> images;
    public int w, h;
    public int currentIndex;

    public Canvas(int width, int height) {
        currentIndex = 0;
        this.w = width;
        this.h = height;
        images = new ArrayList<>();
    }

    public Canvas(Canvas currentCanvas) {
        currentIndex = currentCanvas.currentIndex;
        this.w = currentCanvas.w;
        this.h = currentCanvas.h;
        this.images = new ArrayList<>();

        for (CanvasEntity i : currentCanvas.images) {
            this.images.add(new CanvasEntity(i));
        }
    }

    private CanvasEntity getSelectedImage(Point p) {
        for (CanvasEntity image : reversed(images)) {
            if (image.contain(p)) {
                return image;
            }
        }
        return null;
    }

    public void setSelectedImage(Point p) {
        if (getSelectedImage(p) == null) {
            return;
        }
        currentIndex = images.indexOf(getSelectedImage(p));
    }

    public CanvasEntity getSelectedImage() {
        return images.get(currentIndex);
    }

    public boolean addImage(String filename) {
        RawImage img = new RawImage();
        if (!img.readImage(filename)) {
            return false;
        }
        int x = (w / 2) - (img.width() / 2), y = (h / 2) - (img.height() / 2);
        images.add(new CanvasEntity(x, y, img));
        return true;
    }
}
