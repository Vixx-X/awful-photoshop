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
public class Opening {

    static public RawImage apply(RawImage src, int dim) {
        RawImage erosion = Erosion.apply(src, dim);
        return Dilation.apply(erosion, dim);
    }
}
