/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.filters.locals;

import fxproject.models.RawImage;
import java.util.Arrays;

/**
 *
 * @author vixx_
 */
public class KernelFilter {
    
    static float[] norm(float[] kernel, int kWidth, int kHeight) {
        int size = kWidth * kHeight;
        float fsize = 0.0f;
        for (int idx = 0; idx < size; idx++) {
            fsize += kernel[idx] ;
        }
        for (int idx = 0; idx < size; idx++) {
            kernel[idx] /= fsize;
        }
        return kernel;
    }
    
    public static RawImage apply(RawImage img, float[] kernel, int kWidth, int kHeight) {
        return KernelFilter.apply(img, kernel, kWidth, kHeight, 0, 0, img.width - 1, img.height - 1, true);
    }
      
    public static RawImage apply(RawImage img, float[] kernel, int kWidth, int kHeight, boolean applyNorm) {
        return KernelFilter.apply(img, kernel, kWidth, kHeight, 0, 0, img.width - 1, img.height - 1, applyNorm);
    }
    
    public static RawImage apply(RawImage img, float[] kernel, int kWidth, int kHeight, int x1, int y1, int x2, int y2) {
        return KernelFilter.apply(img, kernel, kWidth, kHeight, x1, y1, x2, y2, true);
    }

    public static RawImage apply(RawImage img, float[] kernel, int kWidth, int kHeight, int x1, int y1, int x2, int y2, boolean applyNorm) {
        RawImage another = img.copy();
        
        if (applyNorm) {
            kernel = KernelFilter.norm(kernel, kWidth, kHeight);
        }

        int kXCenter = kWidth/2, kYCenter = kHeight/2;
        
        y1 = Math.max(0, y1);
        y2 = Math.min(another.height, y2 + 1);
        x1 = Math.max(0, x1);
        x2 = Math.min(another.width, x2 + 1);
        
        for (int y = y1; y < y2; y++) {
            for (int x = x1; x < x2; x++) {
                int R = 0, G = 0, B = 0;
                for (int i=0; i<kHeight; i++) {
                    for (int j=0; j<kWidth; j++) {
                        int ii = y1 + (((y - kYCenter + i) % (y2 - y1)) + (y2 - y1)) % (y2 - y1);
                        int jj = x1 + (((x - kXCenter + j) % (x2 - x1)) + (x2 - x1)) % (x2 - x1);
                        R += Math.round(kernel[i * kWidth + j] * img.getRedPixel(jj, ii));
                        G += Math.round(kernel[i * kWidth + j] * img.getGreenPixel(jj, ii));
                        B += Math.round(kernel[i * kWidth + j] * img.getBluePixel(jj, ii));
                    }
                }
                another.setRedPixel(x, y, R);
                another.setGreenPixel(x, y, G);
                another.setBluePixel(x, y, B);
            }
        }
        
        return another;
    }
}
