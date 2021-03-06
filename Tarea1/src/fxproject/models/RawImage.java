/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.models;

import java.awt.image.DataBufferByte;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.util.Pair;

/**
 *
 * @author vixx_
 */
public class RawImage {

    public enum Type {
        RGB,
        GrayScale,
    }

    public int[] mat;
    public int width;
    public int height;
    public int bpp;
    public int dpi;

    public Type type;

    public int colorMax;
    public String filename;
    String extension;

    public RawImage copy() {
        RawImage another = new RawImage();
        another.mat = this.mat.clone();

        another.width = this.width;
        another.height = this.height;
        another.bpp = this.bpp;
        another.dpi = this.dpi;

        another.type = this.type;

        another.colorMax = this.colorMax;
        another.filename = this.filename;
        another.extension = this.extension;

        return another;
    }

    public static boolean stringIsNetbpm(String string) {
        return "pbm".equals(string) || "pgm".equals(string) || "ppm".equals(string);
    }

    static String getLineWithoutComment(String line) {
        int idx = line.indexOf('#');
        if (idx == -1) {
            return line;
        }
        return line.substring(0, idx);
    }

    static String getNextLineFromNetbpm(Scanner reader) {
        String aux;
        while (reader.hasNextLine()) {
            aux = reader.nextLine();
            if (aux.length() > 0 && aux.charAt(0) != '#') {
                return RawImage.getLineWithoutComment(aux).strip();
            }
        }
        return "";
    }

    void readNetbpm(String filename, Boolean rleCompress) {
        File file = new File(filename);
        Scanner reader = null;

        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RawImage.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (reader == null) {
            System.out.println("File not exist");
            return;
        }

        // Read magic number
        String magicNumber = RawImage.getNextLineFromNetbpm(reader).toLowerCase();
        int _extension = magicNumber.charAt(1) - '0';

        // Read sizes
        int idx = 0;
        int maxsizes = _extension == 1 ? 2 : 3;
        int[] aux = {0, 0, 1};

        while (idx < maxsizes) {
            String[] sizes = RawImage.getNextLineFromNetbpm(reader).split("\\s+");
            for (String s : sizes) {
                aux[idx] = Integer.parseInt(s);
                idx++;
            }
        }

        int _width = aux[0], _height = aux[1], color = aux[2];
        int countPixels = _height * _width;

        // Read pixels
        int[] _mat = new int[countPixels];

        idx = 0;
        int div = _extension == 3 ? 3 : 1;
        int[] pixels = new int[div * countPixels];

        while (reader.hasNextLine()) {
            String[] line = RawImage.getNextLineFromNetbpm(reader).split("\\s+");
            for (String s : line) {
                int pixel;
                if (rleCompress) {
                    String[] pair = s.split("-");
                    int qty = pair.length > 1 ? Integer.parseInt(pair[0]) : 1;
                    pixel = pair.length > 1 ? Integer.parseInt(pair[1]) : Integer.parseInt(pair[0]);
                    while (qty > 0) {
                        pixels[idx] = pixel;
                        idx++;
                        qty--;
                    }
                } else {
                    if (_extension == 1 && s.length() > 1) {
                        for (int c = 0; c < s.length(); c++) {
                            pixels[idx] = s.charAt(c) - '0';
                            idx++;
                        }
                    } else {
                        pixel = Integer.parseInt(s);
                        pixels[idx] = pixel;
                        idx++;
                    }
                }
            }
        }

        for (int i = 0; i < countPixels; ++i) {
            int pixel = i * div;
            int argb = 0;
            if (div == 1) {
                argb += -16777216; // 255 alpha
                argb += ((255 * pixels[pixel] / color) & 0xff); // blue
                argb += ((255 * pixels[pixel] / color) & 0xff) << 8; // green
                argb += ((255 * pixels[pixel] / color) & 0xff) << 16; // red
            } else {
                argb += -16777216; // 255 alpha
                argb += ((255 * pixels[pixel] / color) & 0xff); // blue
                argb += ((255 * pixels[pixel + 1] / color) & 0xff) << 8; // green
                argb += ((255 * pixels[pixel + 2] / color) & 0xff) << 16; // red
            }
            _mat[i] = argb;
        }

        this.mat = _mat;
        this.width = _width;
        this.height = _height;
        this.colorMax = color;
        this.type = _extension == 3 ? Type.RGB : Type.GrayScale;
        this.bpp = this.type == Type.GrayScale ? 8 : 16;
    }

    void readOther(String filename) {
        // Stolen from https://stackoverflow.com/questions/6524196/java-get-pixel-array-from-image

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(filename));
        } catch (IOException ex) {
            Logger.getLogger(RawImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (image == null) {
            return;
        }

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int _width = image.getWidth();
        final int _height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;
        final Type _type = BufferedImage.TYPE_BYTE_GRAY == image.getType() ? Type.GrayScale : Type.RGB;

        int[] result = new int[_height * _width];
        if (_type == Type.GrayScale) {
            final int pixelLength = 1;
            for (int pixel = 0, cnt = 0; pixel < pixels.length; pixel += pixelLength, cnt += 1) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel] & 0xff) << 8); // green
                argb += (((int) pixels[pixel] & 0xff) << 16); // red
                result[cnt] = argb;
            }
        } else if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, cnt = 0; pixel + 3 < pixels.length; pixel += pixelLength, cnt += 1) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[cnt] = argb;
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, cnt = 0; pixel + 2 < pixels.length; pixel += pixelLength, cnt += 1) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[cnt] = argb;
            }
        }

        this.width = _width;
        this.height = _height;
        this.mat = result;
        this.colorMax = 255;
        this.type = _type;
        this.bpp = this.type == Type.GrayScale ? 8 : 16;
    }

    public Pair<String, Boolean> getFormat() {
        return _getFormat(this.filename);
    }

    public Pair<String, Boolean> getFormat(String filename) {
        return _getFormat(filename);
    }

    Pair<String, Boolean> _getFormat(String filename) {
        String _extension;
        Boolean compressed = false;

        int idx = filename.lastIndexOf(".");

        if (idx < 0) {
            System.out.println("File has no extension.");
            return new Pair<>("", false);
        } else {
            _extension = filename.substring(idx + 1);
            if ("rle".equals(_extension)) {
                compressed = true;
                filename = filename.substring(0, idx);
                idx = filename.lastIndexOf(".");
                if (idx < 0) {
                    System.out.println("File has no extension.");
                    return new Pair<>("", false);
                } else {
                    _extension = filename.substring(idx + 1);
                }
            }
        }

        return new Pair<>(_extension, compressed);
    }

    public void readImage(String filename) throws FileNotFoundException {
        Pair<String, Boolean> aux = this.getFormat(filename);
        String _extension = aux.getKey();
        Boolean compressed = aux.getValue();

        Boolean isNetbpm = stringIsNetbpm(_extension);

        if (compressed && !isNetbpm) {
            System.out.println(".rle are supported only for Netbpm extensions.");
            return;
        }

        this.filename = filename;
        this.extension = _extension;

        if (isNetbpm) {
            this.readNetbpm(filename, compressed);
            return;
        }

        this.readOther(filename);
    }

    public BufferedImage getBufferedImage() {
        return this._getBufferedImage(BufferedImage.TYPE_INT_ARGB);
    }

    public BufferedImage getBufferedImage(boolean typed, boolean bmpOrJpg) {
        if (!typed) {
            return this.getBufferedImage();
        }
        if (bmpOrJpg) {
            return this._getBufferedImage(this.type == Type.RGB ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_BYTE_GRAY);
        }
        return this._getBufferedImage(this.type == Type.RGB ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_BYTE_GRAY);
    }

    private BufferedImage _getBufferedImage(int _type) {
        BufferedImage image = new BufferedImage(this.width, this.height, _type);

        if (_type == BufferedImage.TYPE_INT_ARGB) {
            final int[] px = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
            System.arraycopy(this.mat, 0, px, 0, this.width * this.height);
        } else if (_type == BufferedImage.TYPE_INT_RGB) {
            final int[] px = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
            for (int idx = 0; idx < this.width * this.height; idx++) {
                px[idx] = this.mat[idx] & 0xffffff;
            }
        } else {
            final byte[] px = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            for (int idx = 0; idx < this.width * this.height; idx++) {
                px[idx] = (byte) this.mat[idx];
            }
        }
        return image;
    }

    private Image resample(Image input, int scaleFactor) {
        final int W = (int) input.getWidth();
        final int H = (int) input.getHeight();
        final int S = scaleFactor;

        WritableImage output = new WritableImage(
                W * S,
                H * S
        );

        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                final int argb = reader.getArgb(x, y);
                for (int dy = 0; dy < S; dy++) {
                    for (int dx = 0; dx < S; dx++) {
                        writer.setArgb(x * S + dx, y * S + dy, argb);
                    }
                }
            }
        }

        return output;
    }

    public Image getImage() {
        if (this.mat == null || this.mat.length <= 0) {
            return null;
        }
        Image aux = SwingFXUtils.toFXImage(this.getBufferedImage(), null);
        return this.resample(aux, Math.max(1, 300 / Math.min(this.width, this.height)));
    }

    public void writeImage() {
        this._writeImage(this.filename);
    }

    public void writeImage(String filename) {
        this._writeImage(filename);
    }

    void writeNetbpm(String filename, String extension, Boolean rleCompress) {
        try {
            FileWriter writer = new FileWriter(filename);

            // Write magic number
            String magicNumber;
            switch (extension) {
                case "pbm" ->
                    magicNumber = "P1";
                case "pgm" ->
                    magicNumber = "P2";
                case "ppm" ->
                    magicNumber = "P3";
                default -> {
                    System.out.println("Wrong extension " + extension);
                    writer.close();
                    return;
                }
            }
            writer.write(magicNumber + "\n");

            // Write sizes
            writer.write(width + " " + height + "\n");

            if (!"pbm".equals(extension)) {
                writer.write(colorMax + "\n");
            }

            // Write pixels
            int div = type == Type.RGB ? 3 : 1;
            int[] arr = new int[width * height * div];
            for (int i = 0; i < mat.length; i += div) {
                if (type == Type.RGB) {
                    arr[i] = getTranslatedRedPixel(i);
                    arr[i + 1] = getTranslatedGreenPixel(i);
                    arr[i + 2] = getTranslatedBluePixel(i);
                } else {
                    arr[i] = getTranslatedGrayPixel(i);
                }
            }

            if (rleCompress) {
                int px, idx = 0, j, cnt;
                while (idx < arr.length) {
                    cnt = 1;
                    px = arr[idx];
                    for (j = idx; j < arr.length - 1; j++) {
                        if (arr[j] != arr[j + 1]) {
                            break;
                        }
                        cnt++;
                    }
                    String aux = cnt != 1 ? cnt + "-" + px : "" + px;
                    writer.write(aux + "\n");
                    idx = j + 1;
                }
            } else {
                for (int idx = 0; idx < arr.length; idx++) {
                    writer.write(arr[idx] + "\n");
                }
            }

            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(RawImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void writeOther(String filename, String extension) {
        extension = extension.toLowerCase();
        BufferedImage bi = this.getBufferedImage(true, extension.equals("bmp") || extension.equals("jpg") || extension.equals("jpeg"));
        File outputFile = new File(filename);
        try {
            boolean ret = ImageIO.write(bi, extension, outputFile);
        } catch (IOException ex) {
            Logger.getLogger(RawImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void _writeImage(String filename) {
        Pair<String, Boolean> aux = this.getFormat(filename);
        String _extension = aux.getKey();
        Boolean compressed = aux.getValue();

        Boolean isNetbpm = stringIsNetbpm(_extension);
        if (isNetbpm) {
            this.writeNetbpm(filename, _extension, compressed);
            return;
        }

        this.writeOther(filename, _extension);
    }

    int translateTo255(int c) {
        return 255 * c / this.colorMax;
    }

    public void setTranslatedGrayPixel(int x, int val) {
        this.setTranslatedRedPixel(x, val);
        this.setTranslatedGreenPixel(x, val);
        this.setTranslatedBluePixel(x, val);
    }

    public void setTranslatedRedPixel(int x, int val) {
        int c = this.translateTo255(val);
        this.setRedPixel(x, c);
    }

    public void setTranslatedGreenPixel(int x, int val) {
        int c = this.translateTo255(val);
        this.setGreenPixel(x, c);
    }

    public void setTranslatedBluePixel(int x, int val) {
        int c = this.translateTo255(val);
        this.setBluePixel(x, c);
    }

    public void setTranslatedGrayPixel(int x, int y, int val) {
        this.setTranslatedGrayPixel(this.width * y + x, val);
    }

    public void setTranslatedGreenPixel(int x, int y, int val) {
        this.setTranslatedGreenPixel(this.width * y + x, val);
    }

    public void setTranslatedRedPixel(int x, int y, int val) {
        this.setTranslatedRedPixel(this.width * y + x, val);
    }

    public void setTranslatedBluePixel(int x, int y, int val) {
        this.setTranslatedBluePixel(this.width * y + x, val);
    }

    public void setGrayPixel(int x, int val) {
        this.setRedPixel(x, val);
        this.setGreenPixel(x, val);
        this.setBluePixel(x, val);
    }

    public void setRedPixel(int x, int val) {
        val = Math.max(0, Math.min(255, val));
        this.mat[x] = (this.mat[x] & ~((int) 0xff << 16)) | ((val & 0xff) << 16);
    }

    public void setGreenPixel(int x, int val) {
        val = Math.max(0, Math.min(255, val));
        this.mat[x] = (this.mat[x] & ~((int) 0xff << 8)) | ((val & 0xff) << 8);
    }

    public void setBluePixel(int x, int val) {
        val = Math.max(0, Math.min(255, val));
        this.mat[x] = (this.mat[x] & ~((int) 0xff)) | ((val & 0xff));
    }

    public void setGrayPixel(int x, int y, int val) {
        this.setGrayPixel(this.width * y + x, val);
    }

    public void setGreenPixel(int x, int y, int val) {
        this.setGreenPixel(this.width * y + x, val);
    }

    public void setRedPixel(int x, int y, int val) {
        this.setRedPixel(this.width * y + x, val);
    }

    public void setBluePixel(int x, int y, int val) {
        this.setBluePixel(this.width * y + x, val);
    }

    int translateFrom255(int c) {
        return c * this.colorMax / 255;
    }

    public int getTranslatedGrayPixel(int x) {
        int c = this.getGrayPixel(x);
        return this.translateFrom255(c);
    }

    public int getTranslatedRedPixel(int x) {
        int c = this.getRedPixel(x);
        return this.translateFrom255(c);
    }

    public int getTranslatedGreenPixel(int x) {
        int c = this.getGreenPixel(x);
        return this.translateFrom255(c);
    }

    public int getTranslatedBluePixel(int x) {
        int c = this.getBluePixel(x);
        return this.translateFrom255(c);
    }

    public int getTraslatedGrayPixel(int x, int y) {
        return this.getGrayPixel(this.width * y + x);
    }

    public int getTranslatedGreenPixel(int x, int y) {
        return this.getTranslatedGreenPixel(this.width * y + x);
    }

    public int getTranslatedRedPixel(int x, int y) {
        return this.getTranslatedRedPixel(this.width * y + x);
    }

    public int getTranslatedBluePixel(int x, int y) {
        return this.getTranslatedBluePixel(this.width * y + x);
    }

    public int getGrayPixel(int x) {
        return this.mat[x] & 0xff;
    }

    public int getRedPixel(int x) {
        return (this.mat[x] >> 16) & 0xff;
    }

    public int getGreenPixel(int x) {
        return (this.mat[x] >> 8) & 0xff;
    }

    public int getBluePixel(int x) {
        return this.mat[x] & 0xff;
    }

    public int getGrayPixel(int x, int y) {
        return this.getGrayPixel(this.width * y + x);
    }

    public int getGreenPixel(int x, int y) {
        return this.getGreenPixel(this.width * y + x);
    }

    public int getRedPixel(int x, int y) {
        return this.getRedPixel(this.width * y + x);
    }

    public int getBluePixel(int x, int y) {
        return this.getBluePixel(this.width * y + x);
    }

    public int[] getRedHistogram(int x1, int y1, int x2, int y2) {
        y1 = Math.max(0, y1);
        y2 = Math.min(this.height, y2 + 1);
        x1 = Math.max(0, x1);
        x2 = Math.min(this.width, x2 + 1);
        int[] histo = new int[this.colorMax + 1];
        for (int y = y1; y < y2; ++y) {
            for (int x = x1; x < x2; ++x) {
                histo[this.getTranslatedRedPixel(x, y)]++;
            }
        }
        return histo;
    }

    public int[] getGreenHistogram(int x1, int y1, int x2, int y2) {
        y1 = Math.max(0, y1);
        y2 = Math.min(this.height, y2 + 1);
        x1 = Math.max(0, x1);
        x2 = Math.min(this.width, x2 + 1);
        int[] histo = new int[this.colorMax + 1];
        for (int y = y1; y < y2; ++y) {
            for (int x = x1; x < x2; ++x) {
                histo[this.getTranslatedGreenPixel(x, y)]++;
            }
        }
        return histo;
    }

    public int[] getBlueHistogram(int x1, int y1, int x2, int y2) {
        y1 = Math.max(0, y1);
        y2 = Math.min(this.height, y2 + 1);
        x1 = Math.max(0, x1);
        x2 = Math.min(this.width, x2 + 1);
        int[] histo = new int[this.colorMax + 1];
        for (int y = y1; y < y2; ++y) {
            for (int x = x1; x < x2; ++x) {
                histo[this.getTranslatedBluePixel(x, y)]++;
            }
        }
        return histo;
    }

    public int[] getRedHistogram() {
        int[] histo = new int[this.colorMax + 1];
        int pixels = this.width * this.height;
        for (int i = 0; i < pixels; i++) {
            histo[this.getTranslatedRedPixel(i)]++;
        }
        return histo;
    }

    public int[] getBlueHistogram() {
        int[] histo = new int[this.colorMax + 1];
        int pixels = this.width * this.height;
        for (int i = 0; i < pixels; i++) {
            histo[this.getTranslatedBluePixel(i)]++;
        }
        return histo;
    }

    public int[] getGreenHistogram() {
        int[] histo = new int[this.colorMax + 1];
        int pixels = this.width * this.height;
        for (int i = 0; i < pixels; i++) {
            histo[this.getTranslatedGreenPixel(i)]++;
        }
        return histo;
    }

    public int[] getGrayHistogram() {
        int[] histo = new int[this.colorMax + 1];
        int pixels = this.width * this.height;
        for (int i = 0; i < pixels; i++) {
            histo[this.getTranslatedGrayPixel(i)]++;
        }
        return histo;
    }

    public int countDifferentColors() {
        HashSet<Integer> colors = new HashSet<>();
        int pixels = this.width * this.height;
        for (int i = 0; i < pixels; i++) {
            colors.add(this.mat[i]);
        }
        return colors.size();
    }

    public float avgBrightness(int x1, int y1, int x2, int y2) {
        y1 = Math.max(0, y1);
        y2 = Math.min(this.height, y2 + 1);
        x1 = Math.max(0, x1);
        x2 = Math.min(this.width, x2 + 1);
        int cnt = 0;
        int pixels = (y2 - y1) * (x2 - x1);
        for (int y = y1; y < y2; ++y) {
            for (int x = x1; x < x2; ++x) {
                cnt += this.getRedPixel(x, y) + this.getBluePixel(x, y) + this.getGreenPixel(x, y);
            }
        }
        return ((float) cnt) / (3 * pixels);
    }

    public float avgBrightness() {
        return avgBrightness(0, 0, this.width - 1, this.height - 1);
    }
}
