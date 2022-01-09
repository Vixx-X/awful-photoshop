package fxproject.filters;

import fxproject.RawImage;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author vixx_
 */
public class NegativeFilter {
    
    public static RawImage apply(RawImage img) {
        RawImage another = img.copy();
        for (int i = 0; i<another.width * another.height; i++) {
            int R = another.getRedPixel(i);
            another.setRedPixel(i, 255 - R);
            int G = another.getGreenPixel(i);
            another.setGreenPixel(i, 255 - G);
            int B = another.getBluePixel(i);
            another.setBluePixel(i, 255 - B);
        }
        return another;
    }
}
