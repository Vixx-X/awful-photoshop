/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics.transformations.affine;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;

/**
 *
 * @author vixx_
 */
public class Rotation {

    static public Mat apply(Mat src, float angle) {
        float rad = (float) (angle / 180.0f * PI);

        Size size = src.size();
        int w = (int) size.width;
        int h = (int) size.height;
        Size newDim = new Size(
                round(w * abs(sin(rad)) + h * abs(cos(rad))),
                round(w * abs(cos(rad)) + h * abs(sin(rad)))
        );

        Point center = new Point((w - 1) / 2, (h - 1) / 2);
        Point newCenter = new Point((newDim.width - 1) / 2, (newDim.height - 1) / 2);

        Mat out = new Mat(newDim, src.type());
        Point p1 = new Point();

        int paddY = (int) (-newCenter.y * cos(rad) + newCenter.x + sin(rad) + center.y);
        int paddX = (int) (-newCenter.y * cos(rad) - newCenter.x + sin(rad) + center.x);

        for (int i = 0; i < newDim.height; i++) {
            for (int j = 0; j < newDim.width; j++) {
                p1.y = i * cos(rad) - j * sin(rad) + 0.5 + paddY;
                p1.x = i * cos(rad) + j * sin(rad) + 0.5 + paddX;

                if (p1.x > 0 && p1.y > 0 && p1.x < newDim.width && p1.y < newDim.height) {
                    double[] data = src.get((int) p1.y, (int) p1.x);

                    if (data != null) {
                        out.put(i, j, data);
                    }
                } else {
                    double[] data = {0, 0, 0, 255};
                    out.put(i, j, data);
                }

            }
        }

        return out;
    }
}

//     static public Mat apply(Mat src, float angle) {
//         float rad = (float) (angle / 180.0f * PI);
//         Size size = src.size();
//         int w = (int) size.width;
//         int h = (int) size.height;
//         Size newDim = new Size(
//                 round(w * abs(sin(rad)) + h * abs(cos(rad))),
//                 round(w * abs(cos(rad)) + h * abs(sin(rad)))
//         );
//         Point center = new Point(w / 2, h / 2);
//         Point newCenter = new Point(round(newDim.width / 2), round(newDim.height / 2));
//         double kernel[] = {cos(-angle), sin(-angle), -sin(-angle), cos(-angle)};
//         Mat out = new Mat(newDim, CvType.CV_16F);
//         Point p = new Point();
//         Point p1 = new Point();
//         System.out.println(newDim);
//         for (int i = 0; i < newDim.height; i++) {
//             for (int j = 0; j < newDim.width; j++) {
//                 p.y = i - newCenter.y;
//                 p.x = j - newCenter.x;
//                 p1.x = round(p.x * kernel[0] + p.y * kernel[1]) + center.x;
//                 p1.y = round(p.x * kernel[2] + p.y * kernel[3]) + center.y;
//                 if (p1.x > 0 && p1.y > 0 && p1.x < newDim.width && p1.y < newDim.height) {
//                     double[] data = src.get((int) p1.y, (int) p1.x);
//                     if (data != null) {
//                         out.put(i, j, data);
//                     }
//                 }
//             }
//         }
//         System.out.println("Terminó");
//         return out;
//     }
// }
