/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics.interpolation;

import fxproject.graphics.RawImage;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.round;

/**
 *
 * @author vixx_
 */
public class NearestNeighbor {

    public static double getValue(RawImage img, double x, double y) {
        return img.get(max(min(img.width() - 1, round(x)), 0), max(min(img.height() - 1, round(y)), 0));
    }

}
