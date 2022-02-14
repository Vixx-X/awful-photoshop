/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics.transformations.quantization;

import fxproject.graphics.RawImage;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.signum;
import java.util.Arrays;

/**
 *
 * @author vixx_
 */
public class MedianCut {

    static class Node {

        double r, g, b, a;
        int x, y;

        public Node(double _r, double _g, double _b, double _a, int _x, int _y) {
            r = _r;
            g = _g;
            b = _b;
            a = _a;
            x = _x;
            y = _y;
        }

        public double get(char rgb) {
            if (rgb == 'r') {
                return r;
            }
            if (rgb == 'g') {
                return g;
            }
            return b;
        }

    };

    static Node[] flat_array;
    static RawImage ret = new RawImage();
    static int K;
    static int maxDepth;

    static void splitBuckets(int begin, int end, int depth) {
        if (K <= 0) {
            return;
        }
        if (end - begin <= 0) {
            return;
        }
        if (depth == maxDepth) {
            medianCutQuantize(begin, end);
            K--;
            return;
        }

        double r_max = 0, r_min = Double.NEGATIVE_INFINITY;
        double g_max = 0, g_min = Double.NEGATIVE_INFINITY;
        double b_max = 0, b_min = Double.NEGATIVE_INFINITY;

        for (int idx = begin; idx < end; ++idx) {
            Node node = flat_array[idx];
            r_max = max(r_max, node.r);
            g_max = max(g_max, node.g);
            b_max = max(b_max, node.b);
            r_min = min(r_min, node.r);
            g_min = min(g_min, node.g);
            b_min = min(b_min, node.b);
        }

        double r_range = r_max - r_min;
        double g_range = g_max - g_min;
        double b_range = b_max - b_min;

        char highestRange = ' ';
        if (g_range >= r_range && g_range >= b_range) {
            highestRange = 'r';
        } else if (b_range >= r_range && b_range >= g_range) {
            highestRange = 'g';
        } else if (r_range >= b_range && r_range >= g_range) {
            highestRange = 'b';
        }

        final char hR = highestRange;

        Arrays.sort(flat_array, begin, end, (a, b) -> {
            return (int) signum(a.get(hR) - b.get(hR));
        });

        int mid = (end - begin) / 2;

        splitBuckets(begin, mid + 1, depth + 1);
        splitBuckets(mid + 1, end, depth + 1);
    }

    static void medianCutQuantize(int begin, int end) {
        double r_avg = 0;
        double g_avg = 0;
        double b_avg = 0;

        for (int idx = begin; idx < end; ++idx) {
            Node node = flat_array[idx];
            r_avg += node.r;
            g_avg += node.g;
            b_avg += node.b;
        }

        int N = end - begin;
        r_avg /= N;
        g_avg /= N;
        b_avg /= N;

        for (int idx = begin; idx < end; ++idx) {
            Node node = flat_array[idx];
            int x = node.x, y = node.y;
            double[] data = ret.get(y, x);
            data[0] = r_avg;
            data[1] = g_avg;
            data[2] = b_avg;
            ret.put(y, x, data);
        }
    }

    public static RawImage apply(RawImage img, int colors) {
        K = colors;
        img.copyTo(ret);

        flat_array = new Node[img.width() * img.height()];
        for (int i = 0; i < img.height(); ++i) {
            for (int j = 0; j < img.width(); ++j) {
                double[] c = img.get(i, j);
                flat_array[i * img.width() + j] = new Node(c[0], c[1], c[2], c[3], i, j);
            }
        }

        splitBuckets(0, flat_array.length, colors);

        return ret;
    }

}
