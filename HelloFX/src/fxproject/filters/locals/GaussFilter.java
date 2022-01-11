/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.filters.locals;

import fxproject.models.RawImage;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author vixx_
 */
public class GaussFilter {

    // Stolen from https://stackoverflow.com/questions/8204645/implementing-gaussian-blur-how-to-calculate-convolution-matrix-kernel
    public static float gaussian(float x, float mu, float sigma) {
        final float a = (x - mu) / sigma;
        return (float) Math.exp(-0.5 * a * a);
    }

    public static float[] createGaussKernel(int size) {
        float[] kernel = new float[size];
        Random r = new Random(0);
        for (int i = 0; i < size; i++) {
            kernel[i] = (float) r.nextGaussian();
        }
        return kernel;
    }

    public static RawImage apply(RawImage img, int width, int height, int x1, int y1, int x2, int y2) {
        float[] kernelX = GaussFilter.createGaussKernel(width);
        float[] kernelY = GaussFilter.createGaussKernel(height);
        System.out.println(Arrays.toString(kernelX));
        System.out.println(Arrays.toString(kernelY));
        var img2 = KernelFilter.apply(img, kernelX, width, 1, x1, y1, x2, y2);
        return KernelFilter.apply(img2, kernelY, 1, height, x1, y1, x2, y2);
    }

    public static RawImage apply(RawImage img, int width, int height) {
        RawImage another = GaussFilter.apply(img, width, height, 0, 0, img.width - 1, img.height - 1);
        return another;
    }
}
