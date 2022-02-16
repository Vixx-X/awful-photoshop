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
public class NegativeFilter {

    public static RawImage apply(RawImage img, int x1, int y1, int x2, int y2) {
        RawImage another = img.copy();
        for (int y = Math.max(0, y1); y < Math.min(another.height(), y2 + 1); y++) {
            for (int x = Math.max(0, x1); x < Math.min(another.width(), x2 + 1); x++) {
                double[] c = img.get(y, x);
                c[0] = 255 - c[0];
                c[1] = 255 - c[1];
                c[2] = 255 - c[2];
                another.put(y, x, c);
            }
        }
        return another;
    }

    public static RawImage apply(RawImage img) {
        return NegativeFilter.apply(img, 0, 0, img.width() - 1, img.height() - 1);
    }
}
