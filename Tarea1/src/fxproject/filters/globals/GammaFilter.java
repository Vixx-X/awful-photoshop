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
public class GammaFilter {
    public static RawImage apply(RawImage img, float gamma, int x1, int y1, int x2, int y2) {
        RawImage another = img.copy();
        for (int y = Math.max(0, y1); y < Math.min(another.height, y2 + 1); y++) {
            for (int x = Math.max(0, x1); x < Math.min(another.width, x2 + 1); x++) {
                float R = (float)another.getRedPixel(x, y);
                float G = (float)another.getGreenPixel(x, y);
                float B = (float)another.getBluePixel(x, y);

                another.setRedPixel(x, y, (int) Math.round(255 * Math.pow(R/255.0f, 1.0f/gamma)));
                another.setGreenPixel(x, y, (int) Math.round(255 * Math.pow(G/255.0f, 1.0f/gamma)));
                another.setBluePixel(x, y, (int) Math.round(255 * Math.pow(B/255.0f, 1.0f/gamma)));
            }
        }
        return another;
    }
    public static RawImage apply(RawImage img, float gama) {
        return GammaFilter.apply(img, gama, 0, 0, img.width - 1, img.height - 1);
    }
}
