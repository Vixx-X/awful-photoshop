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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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
    private Pane canvasLayout;

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

    public void putImage(ImagePortion c, Image i) {
        for (int x = c.x1; x < c.x2; x++) {
            for (int y = c.y1; y < c.y2; y++) {
                layout.getPixelWriter().setColor(x, y, i.getPixelReader().getColor(x - c.x1, y - c.y1));
            }
        }
    }
    
    public void fillBorder(int srcX, int destX, int srcY, int destY, int[] p) {
        for (int x = srcX; x < destX; x++) {
            for (int y = srcY; y < destY; y++) {
                if(x == p[0] || x == p[1] || y == p[2] || y == p[3]){
                    layout.getPixelWriter().setColor(x, y, Color.WHITE);
                }else{
                    layout.getPixelWriter().setColor(x, y, Color.DIMGRAY);
                }
            }
        }
    }
    
     //Pass state of image f.e rotate, normal, etc
    public void selectedImage(ImagePortion c, Image i) {
        // Define borderSize
        int bz = 4;
        int[] p =  {c.x1-1, c.x2+1, c.y1-1, c.y2+1};
        fillBorder(c.x1-bz, c.x1, c.y1-bz, c.y2+bz, p);
        fillBorder(c.x1, c.x2+bz, c.y1-bz, c.y1, p);
        fillBorder(c.x1, c.x2+bz, c.y2, c.y2+bz, p);
        fillBorder(c.x2, c.x2+bz, c.y1, c.y2, p);
     
    }

    public ImagePortion centerImage(Image image) {

        int wImage2 = (int) (image.getWidth() / 2);
        int hImage2 = (int) (image.getHeight() / 2);
        int w2 = width / 2;
        int h2 = height / 2;

        return new ImagePortion(w2 - wImage2, h2 - hImage2, (int) image.getWidth(), 
                (int) image.getHeight());
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
    
    public void addEdge(int x, int y){
        Circle circle = new Circle(x, y, 6.5);
        circle.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 0.8;");
        canvasLayout.getChildren().add(circle);
    }
    
    public void addBorder(double w, double h, ImagePortion c){
        Rectangle rect = new Rectangle(w, h);
       // rect.setStyle("-fx-background-color:white; -fx-border-style:solid; -fx-border-width:3; -fx-border-color:black;");
        rect.setStyle("-fx-fill: transparent; -fx-stroke: black; -fx-stroke-width: 1;");
        
        int i = c.x1-3;
        int f = c.y1-3;
        //rect.setFill(Color.WHITE);
        rect.relocate(i, f);
        canvasLayout.getChildren().add(rect);
        
        addEdge(c.x1-2, c.y1-2);
        addEdge(c.x2+2, c.y2+2);
        addEdge(c.x2+2, c.y1-2);
        addEdge(c.x1-2, c.y2+2);
        
        Rectangle rect2 = new Rectangle(w, h);
        rect2.setStyle("-fx-fill: transparent; -fx-stroke: black; -fx-stroke-width: 1;");
        rect2.relocate(0, 0);
        rect.setOnDragDetected(e-> {
            canvasLayout.getChildren().add(rect2);
        });
        
        rect.setOnMouseDragged(e -> {
            rect2.relocate(i+e.getX(), f+e.getY());
        });
        
        rect.setOnMouseReleased(e-> {
            System.out.println("[" + e.getY() + ", " + e.getX());
            canvasLayout.getChildren().remove(rect2);
        });
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        backgroundLayout.prefWidthProperty().bind(leftPanel.widthProperty());
        backgroundLayout.prefHeightProperty().bind(leftPanel.heightProperty());
        width = (int) ProjectImages.getInstance().getWidth();
        height = (int) ProjectImages.getInstance().getHeight();
        //layout = createLayout();
        //putImage(c, i);
        //System.out.println("coordenadas [" + c.x1 + ", " + c.y1 + "]");
        //System.out.println("coordenadas [" + c.x2 + ", " + c.y2 + "]");
        //selectedImage(c, i);
        canvasLayout.setPrefSize(width, height);
        canvasLayout.setMaxSize(width, height);
        canvasLayout.setStyle("-fx-background-color: #f5f5f5;");
        
        Image i = createExampleImage();
        ImagePortion c = centerImage(i);
        ImageView iv1 = new ImageView(i);
        iv1.relocate(c.x1, c.y1);
        //iv1.setImage(i);
        iv1.setFitWidth(i.getWidth());
        iv1.setFitHeight(i.getHeight());
        iv1.setPreserveRatio(true);
        canvasLayout.getChildren().addAll(iv1);
        
        iv1.setOnMouseClicked(e -> {
            addBorder(i.getWidth() + 6, i.getHeight() + 6, c);
        });
        
        
        //addBorder(i.getWidth() + 6, i.getHeight() + 6, c);
        //Rectangle rect = new Rectangle(i.getWidth() + 6, i.getWidth() + 6);
        
        
        /*iv1.setOnMouseDragEntered(e -> {
            System.out.println("[chao]");
           canvasLayout.getChildren().add(rect);
        }); */
       
        
        
        
    }

}
