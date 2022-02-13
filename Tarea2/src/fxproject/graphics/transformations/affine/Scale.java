/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics.transformations.affine;

import fxproject.graphics.RawImage;
import static java.lang.Math.round;
import org.opencv.core.Point;
import org.opencv.core.Size;

/**
 *
 * @author vixx_
 */
public class Scale {

    static public RawImage apply(RawImage src, double scale) {
        Size size = src.size();
        Size newDim = new Size(
                round(size.width * scale),
                round(size.height * scale)
        );

        System.out.println(size + " " + newDim);
        RawImage out = new RawImage(newDim, src.type());

        for (int i = 0; i < newDim.height; i++) {
            for (int j = 0; j < newDim.width; j++) {
                Point p1 = new Point(j / scale, i / scale);

                if (p1.x > 0 && p1.y > 0 && p1.x < size.width && p1.y < size.height) {
                    double[] data = src.get((int) p1.y, (int) p1.x);

                    if (data != null) {
                        out.put(i, j, data);
                    }
                } else {
                    double[] data = {0, 0, 0, 0};
                    out.put(i, j, data);
                }

            }
        }

        return out;
    }

}
