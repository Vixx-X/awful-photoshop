/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.filters;

import fxproject.RawImage;

/**
 *
 * @author vixx_
 */
public class BlackWhiteFilter {
    public static RawImage apply(RawImage img) {
        RawImage another = img.copy();
        another.type = RawImage.Type.GrayScale;
        another.colorMax = 1;
        for (int i = 0; i<another.width * another.height; i++) {
            int R = img.getRedPixel(i);
            int G = img.getGreenPixel(i);
            int B = img.getBluePixel(i);
            another.setGrayPixel(i, (R + G + B) > (255*3/2) ? 255 : 0);
        }

        return another;
    }
}
