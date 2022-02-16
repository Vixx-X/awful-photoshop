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
public class GrayScaleFilter {

    public static int computeGray(double R, double G, double B) {
        return (int) Math.round((0.21 * R) + (0.75 * G) + (0.07 * B));
    }

    public static RawImage apply(RawImage img, int x1, int y1, int x2, int y2) {
        RawImage another = img.copy();

        for (int y = Math.max(0, y1); y < Math.min(another.height(), y2 + 1); y++) {
            for (int x = Math.max(0, x1); x < Math.min(another.width(), x2 + 1); x++) {
                double[] c = img.get(y, x);
                c[0] = computeGray(c[0], c[1], c[2]);
                c[1] = computeGray(c[0], c[1], c[2]);
                c[2] = computeGray(c[0], c[1], c[2]);
                another.put(y, x, c);
            }
        }
        return another;
    }

    public static RawImage apply(RawImage img) {
        RawImage another = GrayScaleFilter.apply(img, 0, 0, img.width() - 1, img.height() - 1);
        return another;
    }
}
