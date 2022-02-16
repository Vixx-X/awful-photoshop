/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics.filters.locals;

import fxproject.graphics.RawImage;

/**
 *
 * @author Gaby
 */
public class PrewittFilter {

    public static RawImage apply(RawImage img, int width, int height, int x1, int y1, int x2, int y2) {
        float[] kernelN = new float[width * height];
        float[] kernelE = new float[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (height % 2 == 1 && y == (int) (height / 2)) {
                    kernelN[y * width + x] = 0.0f;
                } else {
                    kernelN[y * width + x] = (y < (int) (height / 2)) ? 1.0f : -1.0f;
                }
                if (width % 2 == 1 && x == (int) (width / 2)) {
                    kernelE[y * width + x] = 0.0f;
                } else {
                    kernelE[y * width + x] = (x < (int) (width / 2)) ? -1.0f : 1.0f;
                }
            }
        }
        RawImage temp = KernelFilter.apply(img, kernelN, width, height, x1, y1, x2, y2, false);
        return KernelFilter.apply(temp, kernelE, width, height, x1, y1, x2, y2, false);

    }

    public static RawImage apply(RawImage img, int width, int height, int type) {
        RawImage another = PrewittFilter.apply(img, width, height, 0, 0, img.width() - 1, img.height() - 1);
        return another;
    }
}
