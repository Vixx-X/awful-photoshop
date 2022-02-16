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
public class SumFilter {

    public static RawImage apply(RawImage img, float dt, int x1, int y1, int x2, int y2) {
        RawImage another = img.copy();

        for (int y = Math.max(0, y1); y < Math.min(another.height(), y2 + 1); y++) {
            for (int x = Math.max(0, x1); x < Math.min(another.width(), x2 + 1); x++) {
                double[] c = img.get(y, x);
                c[0] = (int) Math.round(c[0] + dt);
                c[1] = (int) Math.round(c[1] + dt);
                c[2] = (int) Math.round(c[2] + dt);
                another.put(y, x, c);
            }
        }

        return another;
    }

    public static RawImage apply(RawImage img, float dt) {
        return SumFilter.apply(img, dt, 0, 0, img.width(), img.height());
    }
}
