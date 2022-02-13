/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics.transformations.morphology;

import fxproject.graphics.RawImage;
import java.util.Arrays;

/**
 *
 * @author Gaby
 */
public class Erosion {

    static public RawImage apply(RawImage src, int dim) {

        int width = (int) src.size().width;
        int height = (int) src.size().height;
        RawImage imgCopy = new RawImage();
        src.copyTo(imgCopy);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double[] rgb = src.get(y, x);
                double minRed = rgb[0];
                double minBlue = rgb[1];
                double minGreen = rgb[2];
                for (int w = x - dim / 2; w <= x + dim / 2; w++) {
                    for (int z = y - dim / 2; z <= y + dim / 2; z++) {
                        if (w >= 0 && w < width && z >= 0 && z < height) {
                            double[] rgb2 = src.get(z, w);
                            minRed = Math.min(minRed, rgb2[0]);
                            minBlue = Math.min(minBlue, rgb2[1]);
                            minGreen = Math.min(minGreen, rgb2[2]);
                        }

                    }
                }
                imgCopy.put(y, x, new double[]{minRed, minBlue, minGreen});
            }
        }

        return imgCopy;
    }
}
