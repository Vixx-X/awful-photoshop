/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fxproject;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * FXML Controller class
 *
 * @author Gaby
 */
public class testController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private StackPane backgroundLayout;

    @FXML
    private ImageView canvasLayout;

    @FXML
    private ScrollPane leftPanel;

    @FXML
    private MenuItem undoButton;

    @FXML
    private MenuItem redoButton;

    private WritableImage layout;
    private int width;
    private int height;

    @FXML
    void clickPanel(MouseEvent event) {
        System.out.println("[" + event.getX() + ", " + event.getY() + "]");
    }

    public WritableImage createLayout() {
        System.out.println("[" + width + ", " + height + "]");
        WritableImage out = new WritableImage(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                out.getPixelWriter().setColor(x, y, Color.WHITESMOKE);
            }
        }
        return out;
    }

    public Image createExampleImage() {
        int w = (int) (ProjectImages.getInstance().getWidth() / 3);
        int h = (int) (ProjectImages.getInstance().getHeight() / 3);
        System.out.println("[" + w + ", " + h + "]");
        WritableImage out = new WritableImage(w, h);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                out.getPixelWriter().setColor(x, y, Color.BLUE);
            }
        }
        return out;
    }

    public WritableImage putImage(int[] c, Image i) {
        //int w = c[2] - c[1] + 1;
        //int h = c[3] - c[1] + 1;
        for (int x = c[0]; x < c[2]; x++) {
            for (int y = c[1]; y < c[3]; y++) {
                //layout.getPixelWriter().setPixels(x, y, w, h, i.getPixelReader(), x - c[0], y - c[1]);
                layout.getPixelWriter().setColor(x, y, i.getPixelReader().getColor(x - c[0], y - c[1]));
            }
        }
        return layout;
    }

    public int[] centerImage(Image image) {

        int[] coordinates = new int[4];
        int wImage2 = (int) (image.getWidth() / 2);
        int hImage2 = (int) (image.getHeight() / 2);
        int w2 = width / 2;
        int h2 = height / 2;

        coordinates[0] = w2 - wImage2;
        coordinates[1] = h2 - hImage2;
        coordinates[2] = w2 + wImage2;
        coordinates[3] = h2 + hImage2;

        return coordinates;
    }

    public Image addBorder(Image image) {
        int w = (int) image.getWidth();
        int h = (int) image.getHeight();
        System.out.println("[" + width + ", " + height + "]");
        System.out.println(image.getPixelReader());
        WritableImage out = new WritableImage(w + 8, h + 8);
        for (int x = 0; x < w + 8; x++) {
            for (int y = 0; y < h + 8; y++) {
                if (x < 3 || y < 3 || x > w + 4 || y > h + 4) {
                    out.getPixelWriter().setColor(x, y, Color.DIMGRAY);
                } else if (x == 3 || y == 3 || x == w + 4 || y == h + 4) {
                    out.getPixelWriter().setColor(x, y, Color.WHITE);
                } else {
                    out.getPixelWriter().setColor(x, y, image.getPixelReader().getColor(x - 4, y - 4));
                    //System.out.println("(" + x + ", " +  y + ")" );
                    //out.getPixelWriter().setPixels(x, y, w+6, h+6, image.getPixelReader(), x-3, y-3);
                }
            }
        }
        return out;
    }

    void enableToolsButtons() {
        if (ProjectImages.getInstance().getIndex() == 0) {
            undoButton.setDisable(true);
        } else {
            undoButton.setDisable(false);
        }
        /* if (ProjectImages.getInstance().getStateListSize() - 1
                == ProjectImages.getInstance().getIndex()) {
            redoButton.setDisable(true);
        } else {
            redoButton.setDisable(false);
        } */
    }

    @FXML
    void undoAction(ActionEvent event) {
        /*RawImage image = ProjectImages.getInstance().undo();
        if (image != null) {
            imageMain.setImage(image.getImage());
        } */
        enableToolsButtons();
    }

    @FXML
    void redoAction(ActionEvent event) {
        /*RawImage image = ProjectImages.getInstance().redo();
        if (image != null) {
            imageMain.setImage(image.getImage());
        } */
        enableToolsButtons();
    }

    @FXML
    void saveAsAction(ActionEvent event) {

    }

    @FXML
    void saveImages(ActionEvent event) {

    }

    @FXML
    void uploadImage(ActionEvent event) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String file = "C:\\Users\\Gaby\\Documents\\UCV\\Semestre7\\PDI\\awful-photoshop\\Tarea2\\src\\images";
        //Mat image = Imgcodecs.imread(file);
        //Image obj = Mat2WritableImage(image);
        //ImageView mImageView;
        //mImageView = (ImageView) findViewById(R.id.imageViewId);
        //mImageView.setImageBitmap(BitmapFactory.decodeFile("pathToImageFile"));
        System.out.println(file);
    }

    /* public Image Mat2WritableImage(Mat mat){
      //Encoding the image
      MatOfByte matOfByte = new MatOfByte();
      Imgcodecs.imencode(".jpg", mat, matOfByte);
      //Storing the encoded Mat in a byte array
      byte[] byteArray = matOfByte.toArray();
      //Preparing the Buffered Image
      InputStream in = new ByteArrayInputStream(byteArray);
      BufferedImage bufImage = ImageIO.read(in);
      System.out.println("Image Loaded");
      WritableImage writableImage = SwingFXUtils.toFXImage(bufImage, null);
      return writableImage;
   }*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        backgroundLayout.prefWidthProperty().bind(leftPanel.widthProperty());
        backgroundLayout.prefHeightProperty().bind(leftPanel.heightProperty());
        width = (int) ProjectImages.getInstance().getWidth();
        height = (int) ProjectImages.getInstance().getHeight();
        Image i = createExampleImage();
        Image border = addBorder(i);
        int[] c = centerImage(border);
        layout = createLayout();
        Image put = putImage(c, border);
        canvasLayout.setImage(put);
        canvasLayout.setFitWidth(ProjectImages.getInstance().getWidth());
        canvasLayout.setFitHeight(ProjectImages.getInstance().getHeight());
        canvasLayout.setPreserveRatio(true);

    }

}
