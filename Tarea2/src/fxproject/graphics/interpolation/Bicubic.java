/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics.interpolation;

/**
 *
 * @author vixx_
 */
public class Bicubic {

    public static double getValue(double[] p, double x) {
        return p[1] + 0.5 * x * (p[2] - p[0] + x * (2.0 * p[0] - 5.0 * p[1] + 4.0 * p[2] - p[3] + x * (3.0 * (p[1] - p[2]) + p[3] - p[0])));
    }

    private static double[] arr = new double[4];

    public static double getValue(double[][] p, double x, double y) {
        arr[0] = getValue(p[0], y);
        arr[1] = getValue(p[1], y);
        arr[2] = getValue(p[2], y);
        arr[3] = getValue(p[3], y);
        return getValue(arr, x);
    }
}
