/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject;

import java.awt.image.DataBufferByte;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.scene.image.Image;
import javafx.embed.swing.SwingFXUtils;
import javafx.util.Pair;

/**
 *
 * @author vixx_
 */
public class RawImage {
	enum Type {
		RGB,
		GrayScale,
	}

	int[] mat;
	int width;
	int height;
	int bpp;
	int dpi;

	Type type;

	int colorMax; // just for netbpm
	String filename;
	String extension;

	public static boolean stringIsNetbpm(String string) {
		return "pbm".equals(string) || "pgm".equals(string) || "ppm".equals(string);
	}

	static String getLineWithoutComment(String line) {
		int idx = line.indexOf('#');
		if (idx != -1) return line;
		return line.substring(0, idx);
	}

	static String getNextLineFromNetbpm(Scanner reader) {
		String aux;
		while (reader.hasNextLine()) {
			aux = reader.nextLine();
			if (aux.charAt(0) != '#')
				return RawImage.getLineWithoutComment(aux).strip();
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

		while (idx <= maxsizes) {
			String[] sizes = RawImage.getNextLineFromNetbpm(reader).split("\\s+");
			for (String s: sizes) {
				aux[idx] = Integer.parseInt(s);
				idx++;
			}
		}

		int _width = aux[0], _height = aux[1], color = aux[2];
		int countPixels = _height * _width;
		int[] _mat = new int[countPixels];

		idx = 0;
		int div = _extension == 3 ? 3 : 1;
		int[] pixels = new int[div * countPixels];

		while (reader.hasNextLine()) {
			String[] line = RawImage.getNextLineFromNetbpm(reader).split("\\s+");
			for (String s: line) {
				int pixel = 0;
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
					pixel = Integer.parseInt(s);
					pixels[idx] = pixel;
					idx++;
				}
			}
		}

		for (int i=0; i<countPixels; ++i) {
			int pixel = i * div;
			if (div == 1) {
				_mat[i] = pixels[pixel];
			} else {
				int argb = 0;
				argb += -16777216; // 255 alpha
				argb += 255 * pixels[pixel] / color; // blue
				argb += 255 * pixels[pixel + 1] / color; // green
				argb += 255 * pixels[pixel + 2] / color; // red
				_mat[i] = argb;
			}
		}

		this.mat = _mat;
		this.width = _width;
		this.height = _height;
		this.colorMax = color;
	}

	void readOther(String filename) {
		// Stolen from https://stackoverflow.com/questions/6524196/java-get-pixel-array-from-image

		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(filename));
		} catch (IOException ex) {
			Logger.getLogger(RawImage.class.getName()).log(Level.SEVERE, null, ex);
		}
		if (image == null) return;
                
		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		final int _width = image.getWidth();
		final int _height = image.getHeight();
		final boolean hasAlphaChannel = image.getAlphaRaster() != null;

		int[] result = new int[_height * _width];
		if (hasAlphaChannel) {
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
			_extension = filename.substring(idx+1);
			if ("rle".equals(_extension)) {
				compressed = true;
				idx = filename.substring(0, idx).lastIndexOf(".");
				if (idx < 0) {
					System.out.println("File has no extension.");
					return new Pair<>("", false);
				} else {
					_extension = filename.substring(idx+1);
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
		BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
		final int[] px = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
                // System.out.println(Arrays.toString(this.mat));
		System.arraycopy(this.mat, 0, px, 0, this.width * this.height);
		return image;
	}

	public Image getImage() {
		return SwingFXUtils.toFXImage(this.getBufferedImage(), null);
	}

	public void writeImage() {
		this._writeImage(this.filename);
	}

	public void writeImage(String filename) {
		this._writeImage(filename);
	}

	void writeNetbpm(String filename, String extension, Boolean compressed) {

	}

	void writeOther(String filename, String extension) {
		BufferedImage bi = this.getBufferedImage();
		File outputFile = new File(filename);
		try {
			ImageIO.write(bi, extension, outputFile);
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

	int getGrayPixel(int x) {
		return this.mat[x];
	}

	int getGreenPixel(int x) {
		return this.mat[x];
	}

	int getRedPixel(int x) {
		return this.mat[x];
	}

	int getBluePixel(int x) {
		return this.mat[x];
	}

	int getGrayPixel(int x, int y) {
		return this.getGrayPixel(this.width * y + x);
	}

	int getGreenPixel(int x, int y) {
		return this.getGreenPixel(this.width * y + x);
	}

	int getRedPixel(int x, int y) {
		return this.getRedPixel(this.width * y + x);
	}

	int getBluePixel(int x, int y) {
		return this.getBluePixel(this.width * y + x);
	}

	public int[] getRedHistogram() {
		int[] histo = new int[this.colorMax];
		int pixels = this.width * this.height;
		for (int i = 0; i < pixels; i++) {
			histo[this.getRedPixel(i)]++;
		}
		return histo;
	}

	public int[] getBlueHistogram() {
		int[] histo = new int[this.colorMax];
		int pixels = this.width * this.height;
		for (int i = 0; i < pixels; i++) {
			histo[this.getBluePixel(i)]++;
		}
		return histo;
	}

	public int[] getGreenHistogram() {
		int[] histo = new int[this.colorMax];
		int pixels = this.width * this.height;
		for (int i = 0; i < pixels; i++) {
			histo[this.getGreenPixel(i)]++;
		}
		return histo;
	}

	public int[] getGrayHistogram() {
		int[] histo = new int[this.colorMax];
		int pixels = this.width * this.height;
		for (int i = 0; i < pixels; i++) {
			histo[this.getGrayPixel(i)]++;
		}
		return histo;
	}
}
