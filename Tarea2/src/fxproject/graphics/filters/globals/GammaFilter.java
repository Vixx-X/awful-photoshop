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
public class GammaFilter {

    public static int computeGamma(double c, float gamma) {
        return (int) Math.round(255 * Math.pow(c / 255.0f, 1.0f / gamma));
    }

    public static RawImage apply(RawImage img, float gamma, int x1, int y1, int x2, int y2) {
        RawImage another = img.copy();
        for (int y = Math.max(0, y1); y < Math.min(another.height(), y2 + 1); y++) {
            for (int x = Math.max(0, x1); x < Math.min(another.width(), x2 + 1); x++) {
                double[] c = img.get(y, x);
                c[0] = computeGamma(c[0], gamma);
                c[1] = computeGamma(c[1], gamma);
                c[2] = computeGamma(c[2], gamma);
                another.put(y, x, c);
            }
        }
        return another;
    }

    public static RawImage apply(RawImage img, float gama) {
        return GammaFilter.apply(img, gama, 0, 0, img.width() - 1, img.height() - 1);
    }
}
