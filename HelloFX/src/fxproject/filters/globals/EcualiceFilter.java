/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.filters.globals;

import fxproject.models.RawImage;

/**
 *
 * @author vixx_
 */
public class EcualiceFilter {
    public static RawImage apply(RawImage img, int x1, int y1, int x2, int y2) {
        RawImage another = img.copy();
        
        int[] rHisto = img.getRedHistogram(x1, y1, x2, y2);
        int[] gHisto = img.getGreenHistogram(x1, y1, x2, y2);
        int[] bHisto = img.getBlueHistogram(x1, y1, x2, y2);
        
        int[] rF = new int[rHisto.length];
        int[] gF = new int[gHisto.length];
        int[] bF = new int[bHisto.length];
        int rAcc = rHisto[0];
        int gAcc = gHisto[0];
        int bAcc = bHisto[0];
        int np = (y2-y1)*(x2-x1);
        
        for (int i=0; i<img.colorMax; ++i) {
            rF[i] = rAcc*img.colorMax/np;
            gF[i] = gAcc*img.colorMax/np;
            bF[i] = bAcc*img.colorMax/np;
            rAcc += rHisto[i];
            gAcc += gHisto[i];
            bAcc += bHisto[i];
        }
        
        for (int y = y1; y < y2; y++) {
            for (int x = x1; x < x2; x++) {
                another.setTranslatedBluePixel(x, y, bF[img.getTranslatedBluePixel(x, y)]);
                another.setTranslatedGreenPixel(x, y, gF[img.getTranslatedGreenPixel(x, y)]);
                another.setTranslatedRedPixel(x, y, rF[img.getTranslatedRedPixel(x, y)]);
            }
        }

        return another;
    }
    
    public static RawImage apply(RawImage img) {
        return EcualiceFilter.apply(img, 0, 0, img.width - 1, img.height - 1);
    }
}
