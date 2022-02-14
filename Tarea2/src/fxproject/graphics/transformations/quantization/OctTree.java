/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.graphics.transformations.quantization;

import fxproject.graphics.RawImage;
import java.util.ArrayList;

/**
 *
 * @author vixx_
 */
public class OctTree {

    static RawImage ret = new RawImage();

    static class Color {

        int r, g, b;

        public Color(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

    static class OctTreeNode {

        Color color;
        int count;
        int index;
        OctTreeNode[] children;

        public OctTreeNode(int level, OctTreeQuantizer parent) {
            color = new Color(0, 0, 0);
            count = 0;
            index = 0;
            children = new OctTreeNode[8];
            if (level < OctTreeQuantizer.MAX_DEPTH - 1) {
                parent.addLevelNode(level, this);
            }
        }

        public boolean isLeaf() {
            return count > 0;
        }

        public ArrayList<OctTreeNode> getLeafNodes() {
            ArrayList<OctTreeNode> leafNodes = new ArrayList<>();
            for (int i = 0; i < 8; ++i) {
                OctTreeNode node = children[i];
                if (node != null) {
                    if (node.isLeaf()) {
                        leafNodes.add(node);
                    } else {
                        leafNodes.addAll(node.getLeafNodes());
                    }
                }
            }
            return leafNodes;
        }

        public int getNodesCount() {
            int countAcc = count;
            for (int i = 0; i < 8; ++i) {
                OctTreeNode node = children[i];
                if (node != null) {
                    countAcc += node.count;
                }
            }
            return countAcc;
        }

        public void addColor(Color c, int level, OctTreeQuantizer parent) {
            if (level >= OctTreeQuantizer.MAX_DEPTH) {
                color.r += c.r;
                color.g += c.g;
                color.b += c.b;
                count++;
                return;
            }
            int idx = getColorIndexLevel(c, level);
            if (children[idx] == null) {
                children[idx] = new OctTreeNode(level, parent);
            }
            children[idx].addColor(c, level + 1, parent);
        }

        public int getPaletteIndex(Color c, int level) {
            if (isLeaf()) {
                return index;
            }
            int idx = getColorIndexLevel(c, level);
            if (children[idx] != null) {
                return children[idx].getPaletteIndex(c, level + 1);
            }
            for (int i = 0; i < 8; ++i) {
                if (children[i] != null) {
                    return children[i].getPaletteIndex(c, level + 1);
                }
            }
            System.out.println("XDDDDD");
            return -1;
        }

        public int removeLeaves() {
            int ret = 0;
            for (int i = 0; i < 8; ++i) {
                OctTreeNode node = children[i];
                if (node != null) {
                    color.r += node.color.r;
                    color.g += node.color.g;
                    color.b += node.color.b;
                    count += node.count;
                    ret += 1;
                }
            }
            return ret - 1;
        }

        public int getColorIndexLevel(Color c, int level) {
            int idx = 0;
            int mask = 0x80 >> level;
            if ((c.r & mask) != 0) {
                idx |= 4;
            }
            if ((c.g & mask) != 0) {
                idx |= 2;
            }
            if ((c.b & mask) != 0) {
                idx |= 1;
            }
            return idx;
        }

        Color getColor() {
            return new Color(
                    color.r / count,
                    color.g / count,
                    color.b / count
            );
        }

    }

    static class OctTreeQuantizer {

        static final int MAX_DEPTH = 8;
        ArrayList<OctTreeNode>[] levels;
        OctTreeNode root;

        public OctTreeQuantizer() {
            levels = new ArrayList[MAX_DEPTH];
            for (int i = 0; i < levels.length; ++i) {
                levels[i] = new ArrayList();
            }
            root = new OctTreeNode(0, this);
        }

        public ArrayList<OctTreeNode> getLeaves() {
            return root.getLeafNodes();
        }

        public void addLevelNode(int level, OctTreeNode node) {
            //System.out.println(level);
            levels[level].add(node);
        }

        public void addColor(Color c) {
            root.addColor(c, 0, this);
        }

        public Color[] makePalette(int colorN) {
            Color[] palette = new Color[colorN];
            int index = 0;
            int leaf_cnt = getLeaves().size();

            for (int level = MAX_DEPTH - 1; level > -1; --level) {
                if (levels[level] != null) {
                    for (OctTreeNode node : levels[level]) {
                        leaf_cnt -= node.removeLeaves();
                        if (leaf_cnt <= colorN) {
                            break;
                        }
                    }
                    if (leaf_cnt <= colorN) {
                        break;
                    }
                    levels[level].clear();
                }
            }

            for (OctTreeNode node : getLeaves()) {
                if (index >= colorN) {
                    break;
                }
                if (node.isLeaf()) {
                    palette[index] = node.getColor();
                }
                node.index = index;
                index++;
            }

            return palette;
        }

        public int getPaletteIndex(Color c) {
            return root.getPaletteIndex(c, 0);
        }
    }

    public static RawImage apply(RawImage img, int colors) {
        img.copyTo(ret);
        OctTreeQuantizer octree = new OctTreeQuantizer();

        for (int i = 0; i < img.height(); ++i) {
            for (int j = 0; j < img.width(); ++j) {
                double[] data = img.get(i, j);
                octree.addColor(new Color((int) data[0], (int) data[1], (int) data[2]));
            }
        }

        Color[] palette = octree.makePalette(colors);

        for (int i = 0; i < img.height(); ++i) {
            for (int j = 0; j < img.width(); ++j) {
                double[] data = img.get(i, j);
                int idx = octree.getPaletteIndex(new Color((int) data[0], (int) data[1], (int) data[2]));
                Color color = palette[idx];
                data[0] = color.r;
                data[1] = color.g;
                data[2] = color.b;
                ret.put(i, j, data);
            }
        }

        return ret;
    }
}
