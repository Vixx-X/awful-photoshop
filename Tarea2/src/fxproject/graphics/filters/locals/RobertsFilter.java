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
public class RobertsFilter {

    public static RawImage apply(RawImage img, int width, int height, int x1, int y1, int x2, int y2) {
        float[] kernelN = {0, 0, 0, 0, 0, 1, 0, -1, 0};
        float[] kernelE = {-1, 0, 0, 0, 1, 0, 0, 0, 0};

        RawImage temp = KernelFilter.apply(img, kernelN, width, height, x1, y1, x2, y2, false);
        return KernelFilter.apply(temp, kernelE, width, height, x1, y1, x2, y2, false);
    }

    public static RawImage apply(RawImage img, int width, int height, int type) {
        RawImage another = PrewittFilter.apply(img, width, height, 0, 0, img.width() - 1, img.height() - 1);
        return another;
    }
}
