/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.filters.locals;

import fxproject.filters.locals.KernelFilter;
import fxproject.models.RawImage;

/**
 *
 * @author vixx_
 */
public class SobelFilter {

    public static RawImage apply(RawImage img, int width, int height, int x1, int y1, int x2, int y2) {
        float[] kernel = {1, 0, -1, 2, 0, -2, 1, 0, -1};
        return KernelFilter.apply(img, kernel, 3, 3, x1, y1, x2, y2, false);
    }

    public static RawImage apply(RawImage img, int width, int height) {
        RawImage another = SobelFilter.apply(img, width, height, 0, 0, img.width - 1, img.height - 1);
        return another;
    }
}
