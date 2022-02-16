/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics.transformations.affine;

import fxproject.graphics.RawImage;
import fxproject.graphics.interpolation.Bilinear;
import fxproject.graphics.interpolation.Method;
import fxproject.graphics.interpolation.NearestNeighbor;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sin;
import org.opencv.core.Point;
import org.opencv.core.Size;

/**
 *
 * @author vixx_
 */
public class Rotation {

    private static final double[] kernel = new double[4];

    static public Point getRotatedPoint(Point src, Point center, boolean transpose) {
        int dx = transpose ? 1 : 0;
        return new Point(
                kernel[0] * (src.x - center.x) + kernel[1 + dx] * (src.y - center.y) + center.x,
                kernel[2 - dx] * (src.x - center.x) + kernel[3] * (src.y - center.y) + center.y
        );
    }

    static public RawImage apply(RawImage src, double angle) {
        double rad = (angle / 180.0f * PI);

        double c = cos(rad);
        double s = sin(rad);

        kernel[0] = c;
        kernel[1] = -s;
        kernel[2] = s;
        kernel[3] = c;

        Size size = src.size();
        Point center = new Point(size.width / 2, size.height / 2);

        Point A = getRotatedPoint(new Point(0, 0), center, false);
        Point B = getRotatedPoint(new Point(size.width, 0), center, false);
        Point C = getRotatedPoint(new Point(size.width, size.height), center, false);
        Point D = getRotatedPoint(new Point(0, size.height), center, false);

        Size newDim = new Size(
                max(max(A.x, B.x), max(C.x, D.x)) - min(min(A.x, B.x), min(C.x, D.x)),
                max(max(A.y, B.y), max(C.y, D.y)) - min(min(A.y, B.y), min(C.y, D.y))
        );

        Point newCenter = new Point(newDim.width / 2, newDim.height / 2);

        RawImage out = new RawImage(newDim, src.type());

        for (int i = 0; i < newDim.height; i++) {
            for (int j = 0; j < newDim.width; j++) {
                Point p1 = getRotatedPoint(new Point(j, i), newCenter, true);
                p1.x += -newCenter.x + center.x;
                p1.y += -newCenter.y + center.y;

                if (p1.x >= 0 && p1.y >= 0 && p1.x < size.width && p1.y < size.height) {
                    double data[] = Method.getValue(src, p1.x, p1.y);
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
