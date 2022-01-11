package fxproject.filters.globals;

import fxproject.models.RawImage;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author vixx_
 */
public class NegativeFilter {
    public static RawImage apply(RawImage img, int x1, int y1, int x2, int y2) {
        RawImage another = img.copy();

        for (int y = Math.max(0, y1); y < Math.min(another.height, y2 + 1); y++) {
            for (int x = Math.max(0, x1); x < Math.min(another.width, x2 + 1); x++) {
                int R = another.getRedPixel(x, y);
                int G = another.getGreenPixel(x, y);
                int B = another.getBluePixel(x, y);

                another.setRedPixel(x, y, 255 - R);
                another.setGreenPixel(x, y, 255 - G);
                another.setBluePixel(x, y, 255 - B);
            }
        }
        return another;
    }
    
    public static RawImage apply(RawImage img) {
        return NegativeFilter.apply(img, 0, 0, img.width-1, img.height-1);
    }
}
