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
import java.util.TreeSet;

/**
 *
 * @author vixx_
 */
public class MedianCut {

    static class Node implements Comparable<Node> {

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

        public String toString() {
            return "(" + (int) r + "," + (int) g + "," + (int) b + ")";
        }

        @Override
        public int compareTo(Node t) {
            return toString().compareTo(t.toString());
        }

    };

    static Node[] flat_array;
    static RawImage ret = new RawImage();
    static int K;
    static int maxDepth;
    static TreeSet<Node> map;

    static double getDistanceSquare(Node a) {
        return a.r * a.r + a.g * a.g + a.b * a.b;
    }

    static double compareDistance(Node a, Node b) {
        return (getDistanceSquare(a) - getDistanceSquare(b));
    }

    static void medianCutQuantize(int begin, int end) {

        double[] avg_color = getAvgColor(begin, end);

        if (K <= 0) {
            Node near = new Node(avg_color[0], avg_color[1], avg_color[2], 0, 0, 0);
            double _min = Double.POSITIVE_INFINITY;
            for (Node n : map) {
                double aux = compareDistance(n, near);
                if (aux < _min) {
                    near = n;
                    _min = aux;
                }
            }

            avg_color[0] = near.r;
            avg_color[1] = near.g;
            avg_color[2] = near.b;
        }

        K--;

        for (int idx = begin; idx < end; ++idx) {
            Node node = flat_array[idx];
            int x = node.x, y = node.y;
            double[] data = ret.get(y, x);
            data[2] = avg_color[2];
            data[1] = avg_color[1];
            data[0] = avg_color[0];
            ret.put(y, x, data);
        }

        map.add(new Node(avg_color[0], avg_color[1], avg_color[2], 0, 0, 0));
    }

    static void splitBuckets(int begin, int end, int depth) {
        if (end <= begin) {
            return;
        }

        if (depth == 0) {
            medianCutQuantize(begin, end);
            return;
        }

        double r_max = Double.NEGATIVE_INFINITY, r_min = Double.POSITIVE_INFINITY;
        double g_max = Double.NEGATIVE_INFINITY, g_min = Double.POSITIVE_INFINITY;
        double b_max = Double.NEGATIVE_INFINITY, b_min = Double.POSITIVE_INFINITY;

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

        char highestRange = 'b';
        if (r_range >= b_range && r_range >= g_range) {
            highestRange = 'r';
        } else if (g_range >= r_range && g_range >= b_range) {
            highestRange = 'g';
        }

        final char hR = highestRange;

        Arrays.sort(flat_array, begin, end, (a, b) -> {
            return (int) signum(a.get(hR) - b.get(hR));
        });
        int mid = begin + (end - begin + 1) / 2;

        splitBuckets(begin, mid, depth - 1);
        splitBuckets(mid, end, depth - 1);
    }

    static double[] getAvgColor(int begin, int end) {
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

        double[] _ret = new double[3];
        _ret[0] = r_avg;
        _ret[1] = g_avg;
        _ret[2] = b_avg;

        return _ret;
    }

    public static int binlog(int bits) {
        // fast log2 for integers
        // https://stackoverflow.com/questions/3305059/how-do-you-calculate-log-base-2-in-java-for-integers#3305710
        int log = 0;
        if ((bits & 0xffff0000) != 0) {
            bits >>>= 16;
            log = 16;
        }
        if (bits >= 256) {
            bits >>>= 8;
            log += 8;
        }
        if (bits >= 16) {
            bits >>>= 4;
            log += 4;
        }
        if (bits >= 4) {
            bits >>>= 2;
            log += 2;
        }
        return log + (bits >>> 1);
    }

    public static RawImage apply(RawImage img, int colors) {
        K = colors;
        img.copyTo(ret);
        map = new TreeSet<Node>();

        flat_array = new Node[img.width() * img.height()];
        for (int i = 0; i < img.height(); ++i) {
            for (int j = 0; j < img.width(); ++j) {
                double[] c = img.get(i, j);
                flat_array[i * img.width() + j] = new Node(c[0], c[1], c[2], c[3], j, i);
            }
        }

        int _log = binlog(colors);
        int _pow = 2 << (_log - 1);

        splitBuckets(0, flat_array.length, _pow != colors ? _log + 1 : _log);
        return ret;
    }

}
