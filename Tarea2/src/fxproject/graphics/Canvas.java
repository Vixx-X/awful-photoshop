/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics;

import java.util.ArrayList;
import org.opencv.core.Point;

/**
 *
 * @author vixx_
 */
public class Canvas {

    ArrayList<CanvasEntity> images;

    public CanvasEntity getSelectedImage(Point p) {
        for (CanvasEntity image : images) {
            if (image.contain(p)) {
                return image;
            }
        }
        return null;
    }
}
