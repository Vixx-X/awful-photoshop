/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.filters.locals;

import fxproject.models.RawImage;
import java.util.ArrayList;

/**
 *
 * @author vixx_
 */
public class KernelFilter {
    public static RawImage apply(RawImage img, ArrayList<Integer> kernel, int kWidth, int kHeight, int x1, int y1, int x2, int y2) {
        RawImage another = img.copy();

        for (int y = Math.max(0, y1); y < Math.min(another.height, y2 + 1); y++) {
            for (int x = Math.max(0, x1); x < Math.min(another.width, x2 + 1); x++) {

            }
        }
        return another;
    }
    
    public static RawImage apply(RawImage img, ArrayList<Integer> kernel, int kWidth, int kHeight) {
        return KernelFilter.apply(img, kernel, kWidth, kHeight, 0, 0, img.width, img.height);
    }
}
