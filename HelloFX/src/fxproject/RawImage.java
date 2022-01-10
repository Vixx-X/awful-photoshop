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
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
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
		if (idx == -1) return line;
		return line.substring(0, idx);
	}

	static String getNextLineFromNetbpm(Scanner reader) {
		String aux;
		while (reader.hasNextLine()) {
			aux = reader.nextLine();
			if (aux.length() > 0 && aux.charAt(0) != '#')
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

		while (idx < maxsizes) {
			String[] sizes = RawImage.getNextLineFromNetbpm(reader).split("\\s+");
			for (String s: sizes) {
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
			for (String s: line) {
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
						for (int c=0; c<s.length(); c++) {
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

		for (int i=0; i<countPixels; ++i) {
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
		this.type = maxsizes == 3 ? Type.RGB : Type.GrayScale;

		System.out.println(Arrays.toString(mat));
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
		final Type _type = BufferedImage.TYPE_BYTE_GRAY == image.getType() ? Type.GrayScale : Type.RGB;

		int[] result = new int[_height * _width];
		if (_type == Type.GrayScale) {
			final int pixelLength = 1;
			for (int pixel = 0, cnt = 0; pixel + 2 < pixels.length; pixel += pixelLength, cnt += 1) {
				int argb = 0;
				argb += -16777216; // 255 alpha
				argb += ((int) pixels[pixel] & 0xff); // blue
				argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
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
		return this._getBufferedImage(BufferedImage.TYPE_INT_ARGB);
	}

	public BufferedImage getBufferedImage(boolean typed) {
		if (!typed) return this.getBufferedImage();
		return this._getBufferedImage(this.type == Type.RGB ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_BYTE_GRAY);
	}

	private BufferedImage _getBufferedImage(int _type) {
		BufferedImage image = new BufferedImage(this.width, this.height, _type);
		final int[] px = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		// System.out.println(Arrays.toString(this.mat));
		System.arraycopy(this.mat, 0, px, 0, this.width * this.height);
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

			if (writer == null) {
				System.out.println("An error occured");
				return;
			}

			// Write magic number
			String magicNumber;
			if ("pbm".equals(extension)) magicNumber = "P1";
			else if ("pgm".equals(extension)) magicNumber = "P2";
			else if ("ppm".equals(extension)) magicNumber = "P3";
			else {
				System.out.println("Wrong extension " + extension);
				return;
			}
			writer.write(magicNumber + "\n");

			// Write sizes
			writer.write(width + " " + height + "\n");
			writer.write(colorMax + "\n");

			// Write pixels
			int div = type == Type.RGB? 3: 1;
			int[] arr = new int[width * height * div];
			for (int i=0; i<mat.length; i+=div) {
				if (type == Type.RGB) {
					arr[i] = getRedPixel(i);
					arr[i+1] = getGreenPixel(i);
					arr[i+2] = getBluePixel(i);
				} else {
					arr[i] = getGrayPixel(i);
				}
			}

			int px, idx = 0, j, cnt;
			while (idx < arr.length) {
				cnt = 1;
				px = arr[idx];
				for (j=idx; j < arr.length-1; j++) {
					if (arr[j] != arr[j+1])
						break;
					cnt++;
				}
				String aux = cnt == 1? cnt + "-" + px : "" + px;
				writer.write(aux + "\n");
				idx = j+1;
			}

			writer.close();
		} catch (IOException ex) {
			Logger.getLogger(RawImage.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	void writeOther(String filename, String extension) {
		BufferedImage bi = this.getBufferedImage(false);
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

	int translateTo255(int c) {
		return 255 * c / this.colorMax;
	}

	public void setGrayPixel(int x, int val) {
		this.setRedPixel(x, val);
		this.setGreenPixel(x, val);
		this.setBluePixel(x, val);
	}

	public void setRedPixel(int x, int val) {
                System.out.println(val);
		int c = this.translateTo255(val);
                System.out.println(c);
		this.mat[x] = (this.mat[x] & ~(0xff >> 16)) | ((c & 0xff) >> 16);
	}

	public void setGreenPixel(int x, int val) {
		int c = this.translateTo255(val);
		this.mat[x] = (this.mat[x] & ~(0xff >> 8)) | ((c & 0xff) >> 8);
	}

	public void setBluePixel(int x, int val) {
		int c = this.translateTo255(val);
		this.mat[x] = (this.mat[x] & ~0xff) | ((c & 0xff));
	}

	public void setGrayPixel(int x, int y, int val) {
		this.getGrayPixel(this.width * y + x, val);
	}

	public void setGreenPixel(int x, int y, int val) {
		this.getGreenPixel(this.width * y + x, val);
	}

	public void setRedPixel(int x, int y, int val) {
                System.out.println(x + " " + y + " " + val + " " + this.width * y + x);
		this.getRedPixel(this.width * y + x, val);
	}

	public void setBluePixel(int x, int y, int val) {
		this.getBluePixel(this.width * y + x, val);
	}


	int translateFrom255(int c) {
		return c * this.colorMax / 255;
	}

	public int getGrayPixel(int x) {
		int c = this.mat[x] & 0xff;
		return this.translateFrom255(c);
	}

	public int getRedPixel(int x) {
		int c = (this.mat[x] >> 16) & 0xff;
		return this.translateFrom255(c);
	}

	public int getGreenPixel(int x) {
		int c = (this.mat[x] >> 8) & 0xff;
		return this.translateFrom255(c);
	}

	public int getBluePixel(int x) {
		int c = this.mat[x] & 0xff;
		return this.translateFrom255(c);
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
