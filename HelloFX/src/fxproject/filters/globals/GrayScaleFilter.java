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
public class GrayScaleFilter {
    public static RawImage apply(RawImage img, int x1, int y1, int x2, int y2) {
        RawImage another = img.copy();

        for (int y = Math.max(0, y1); y < Math.min(another.height, y2 + 1); y++) {
            for (int x = Math.max(0, x1); x < Math.min(another.width, x2 + 1); x++) {
                int R = img.getRedPixel(x, y);
                int G = img.getGreenPixel(x, y);
                int B = img.getBluePixel(x, y);
                another.setGrayPixel(x, y, (int) Math.round((0.21 * R) + (0.75 * G) + (0.07 * B)));
            }
        }
        return another;
    }
    
    public static RawImage apply(RawImage img) {
        RawImage another = GrayScaleFilter.apply(img, 0, 0, img.width, img.height);
        another.type = RawImage.Type.GrayScale;
        return another;
    }
}
