/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.filters.locals;

import fxproject.models.RawImage;

/**
 *
 * @author vixx_
 */
public class GaussFilter {
    public static float[] createGaussKernel(int width, int height) {
        float[] kernel = new float[width * height];
        return kernel;
    }
    
    public static RawImage apply(RawImage img, int width, int height, int x1, int y1, int x2, int y2) {
        float[] kernel = GaussFilter.createGaussKernel(width, height);
        return KernelFilter.apply(img, kernel, width, height, x1, y1, x2, y2);
    }
    
    public static RawImage apply(RawImage img, int width, int height) {
        RawImage another = GaussFilter.apply(img, width, height, 0, 0, img.width - 1, img.height - 1);
        return another;
    }
}
