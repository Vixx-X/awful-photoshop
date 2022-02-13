/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics.transformations.morphology;

import fxproject.graphics.RawImage;

/**
 *
 * @author Gaby
 */
public class Closing {

    static public RawImage apply(RawImage src, int dim) {
        RawImage dilation = Dilation.apply(src, dim);
        return Erosion.apply(dilation, dim);
    }
}
