/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.filters.globals;

/**
 *
 * @author vixx_
 */
public class ContrastFilter {
    /*
    public static RawImage apply(RawImage img, float beta, int x1, int y1, int x2, int y2) {
        float alpha = (255 + beta)/(255 - (beta == 0.0f ? 1.0f : beta));
        float avgB = img.avgBrightness(x1, y1, x2, y2);
        RawImage another = img.copy();
        for (int y = Math.max(0, y1); y < Math.min(another.height, y2 + 1); y++) {
            for (int x = Math.max(0, x1); x < Math.min(another.width, x2 + 1); x++) {
                float R = (float)another.getRedPixel(x, y);
                float G = (float)another.getGreenPixel(x, y);
                float B = (float)another.getBluePixel(x, y);

                another.setRedPixel(x, y, (int) Math.round(alpha*(R - avgB) + avgB));
                another.setGreenPixel(x, y, (int) Math.round(alpha*(G - avgB) + avgB));
                another.setBluePixel(x, y, (int) Math.round(alpha*(B - avgB) + avgB));
            }
        }
        return another;
    }
    public static RawImage apply(RawImage img, float beta) {
        return ContrastFilter.apply(img, beta, 0, 0, img.width - 1, img.height - 1);
    }
     */
}
