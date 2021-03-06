/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.filters.globals;

import fxproject.models.RawImage;

/**
 *
 * @author vixx_
 */
public class SumFilter {
    public static RawImage apply(RawImage img, float dt, int x1, int y1, int x2, int y2) {
        RawImage another = img.copy();

        for (int y = Math.max(0, y1); y < Math.min(another.height, y2 + 1); y++) {
            for (int x = Math.max(0, x1); x < Math.min(another.width, x2 + 1); x++) {
                int R = img.getRedPixel(x, y);
                int G = img.getGreenPixel(x, y);
                int B = img.getBluePixel(x, y);
                another.setRedPixel(x, y, (int) Math.round(R + dt));
                another.setGreenPixel(x, y, (int) Math.round(G + dt));
                another.setBluePixel(x, y, (int) Math.round(B + dt));
            }
        }
        return another;
    }
    
    public static RawImage apply(RawImage img, float dt) {
        return SumFilter.apply(img, dt, 0, 0, img.width, img.height);
    }
}
