/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFilextem/Templates/Classes/Class.java to edit this template
 */
package fxproject.filters.locals;

import fxproject.models.RawImage;

/**
 *
 * @author vixx_
 */
public class CircleFilter {
    public static RawImage apply(RawImage img, int width, int height, int x1, int y1, int x2, int y2) {
        float[] kernel = new float[width * height];
        int centerX = width / 2, centerY = height / 2;
        int ww = width * width, hh = height * height;
        int r = ww * hh;
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                int xw = (x - centerX), yh = (y - centerY);
                kernel[y * width + x] = (xw*xw*hh + yh*yh*ww <= r) ? 1.0f : 0.0f;
            }
        }
        return KernelFilter.apply(img, kernel, width, height, x1, y1, x2, y2);
    }
    
    public static RawImage apply(RawImage img, int width, int height) {
        RawImage another = CircleFilter.apply(img, width, height, 0, 0, img.width - 1, img.height - 1);
        return another;
    }
}
