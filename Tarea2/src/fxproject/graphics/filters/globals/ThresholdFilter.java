/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics.filters.globals;

import fxproject.graphics.RawImage;

/**
 *
 * @author vixx_
 */
public class ThresholdFilter {

    public static int computeColor(double R, double G, double B, int min, int max) {
        return ((R + G + B) > (min * 3) && (R + G + B) < (max * 3) ? 255 : 0);
    }

    public static int computeColor(double R, double G, double B, int min) {
        return ((R + G + B) > (min * 3) ? 255 : 0);
    }

    public static RawImage apply(RawImage img, int min, int max, int x1, int y1, int x2, int y2) {
        RawImage another = img.copy();

        for (int y = Math.max(0, y1); y < Math.min(another.height(), y2 + 1); y++) {
            for (int x = Math.max(0, x1); x < Math.min(another.width(), x2 + 1); x++) {
                double[] c = img.get(y, x);
                c[0] = computeColor(c[0], c[1], c[2], min, max);
                c[1] = computeColor(c[0], c[1], c[2], min, max);
                c[2] = computeColor(c[0], c[1], c[2], min, max);
                another.put(y, x, c);
            }
        }
        return another;
    }

    public static RawImage apply(RawImage img, int min, int x1, int y1, int x2, int y2) {
        RawImage another = img.copy();

        for (int y = Math.max(0, y1); y < Math.min(another.height(), y2 + 1); y++) {
            for (int x = Math.max(0, x1); x < Math.min(another.width(), x2 + 1); x++) {
                double[] c = img.get(y, x);
                c[0] = computeColor(c[0], c[1], c[2], min);
                c[1] = computeColor(c[0], c[1], c[2], min);
                c[2] = computeColor(c[0], c[1], c[2], min);
                another.put(y, x, c);

            }
        }
        return another;
    }

    public static RawImage apply(RawImage img, int min, int max) {
        RawImage another = ThresholdFilter.apply(img, min, max, 0, 0, img.width(), img.height());
        return another;
    }

    public static RawImage apply(RawImage img, int min) {
        RawImage another = ThresholdFilter.apply(img, min, 0, 0, img.width(), img.height());
        return another;
    }

}
