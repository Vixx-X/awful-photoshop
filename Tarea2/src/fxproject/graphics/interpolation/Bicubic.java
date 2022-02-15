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
import org.opencv.core.Mat;

/**
 *
 * @author vixx_
 */
public class Bicubic {

    public static double[] checkValues(RawImage img, double y, double x) {
        return img.get((int) (max(min(img.height() - 1, y), 0)), (int) (max(min(img.width() - 1, x), 0)));
    }

    public static double[][] getRow(RawImage img, double y, double x) {
        double[][] points = {
            checkValues(img, y, floor(x) - 1),
            checkValues(img, y, floor(x)),
            checkValues(img, y, ceil(x)),
            checkValues(img, y, ceil(x) + 1)};
        return points;
    }

    public static double[] getValue(RawImage img, double x, double y) {
        double[] ret = img.get((int) y, (int) x);
        int xfloor = (int) x;
        int yfloor = (int) y;
        double xdelta = x - xfloor;
        double ydelta = y - yfloor;

        double[][][] points = new double[4][4][3];
        points[0] = getRow(img, ceil(y) + 1, x);
        points[1] = getRow(img, ceil(y), x);
        points[2] = getRow(img, floor(y), x);
        points[3] = getRow(img, floor(y) - 1, x);

        double[][] cols = new double[][]{
            getRGBColors(
            points[0][0], points[0][1], points[0][2],
            points[0][3], ydelta),
            getRGBColors(
            points[1][0], points[1][1], points[1][2],
            points[1][3], ydelta),
            getRGBColors(
            points[2][0], points[2][1], points[2][2],
            points[2][3], 1 - ydelta),
            getRGBColors(
            points[3][0], points[3][1], points[3][2],
            points[3][3], 1 - ydelta)
        };

        double[] copy = getRGBColors(cols[0], cols[1], cols[2], cols[3], 1 - xdelta);

        ret[0] = copy[0];
        ret[1] = copy[1];
        ret[2] = copy[2];

        return ret;

    }

    public static double cubic(
            double y0, double y1,
            double y2, double y3,
            double mu) {
        double a0, a1, a2, a3, mu2;

        mu2 = mu * mu;
        a0 = y3 - y2 - y0 + y1;
        a1 = y0 - y1 - a0;
        a2 = y2 - y0;
        a3 = y1;

        return (a0 * mu * mu2 + a1 * mu2 + a2 * mu + a3);
    }

    public static double[] getRGBColors(
            double[] y0, double[] y1,
            double[] y2, double[] y3,
            double mu) {
        return new double[]{
            cubic(y0[0], y1[0], y2[0], y3[0], mu),
            cubic(y0[1], y1[1], y2[1], y3[1], mu),
            cubic(y0[2], y1[2], y2[2], y3[2], mu)
        };
    }
}
