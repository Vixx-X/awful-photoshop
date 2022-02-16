/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics.interpolation;

import fxproject.ProjectImages;
import fxproject.graphics.RawImage;

/**
 *
 * @author Gaby
 */
public class Method {

    private static int interpolation;

    public static double[] getValue(RawImage img, double x, double y) {
        double data[];
        interpolation = ProjectImages.getInstance().getInterpolation();
        switch (interpolation) {
            case 1 ->
                data = NearestNeighbor.getValue(img, x, y);
            case 2 ->
                data = Bilinear.getValue(img, x, y);
            case 3 ->
                data = Bicubic.getValue(img, x, y);
            case 4 ->
                data = NearestNeighbor.getValue(img, x, y);
            default ->
                data = Bilinear.getValue(img, x, y);
        }
        return data;
    }
}
