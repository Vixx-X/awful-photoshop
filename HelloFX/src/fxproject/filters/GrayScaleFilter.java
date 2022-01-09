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
public class GrayScaleFilter {

    public static RawImage apply(RawImage img) {
        RawImage another = img.copy();
        for (int i = 0; i<another.width * another.height; i++) {
            int R = img.getRedPixel(i);
            int G = img.getGreenPixel(i);
            int B = img.getBluePixel(i);
            another.setGrayPixel(i, (int) Math.round((0.21 * R) + (0.75 * G) + (0.07 * B)));
        }
        another.type = RawImage.Type.GrayScale;
        return another;
    }
}
