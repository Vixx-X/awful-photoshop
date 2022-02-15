/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics.interpolation;

import fxproject.graphics.RawImage;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.round;

/**
 *
 * @author vixx_
 */
public class Bilinear {

    public static double[] checkValues(RawImage img, double y, double x) {
        return img.get((int) (max(min(img.height() - 1, y), 0)), (int) (max(min(img.width() - 1, x), 0)));
    }

    public static double[] getValue(RawImage img, double x, double y) {
        double[][] points = {checkValues(img, ceil(y), ceil(x)),
            checkValues(img, ceil(y), floor(x)),
            checkValues(img, floor(y), ceil(x)),
            checkValues(img, floor(y), floor(x))};

        return calculateAvg(calculateAvg(points[0], points[1], ceil(x) - x),
                calculateAvg(points[2], points[3], x - floor(x)),
                ceil(y) - y);
    }

    private static double[] calculateAvg(double[] p1, double[] p2, double pond) {
        double avg[] = new double[p1.length];
        for (int i = 0; i < p1.length; i++) {
            avg[i] = p1[i] * (1 - pond) + p2[i] * pond;
        }
        return avg;
    }
}
