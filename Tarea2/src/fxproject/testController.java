/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fxproject;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

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
    void clickPanel(MouseEvent event) {
        System.out.println("["+event.getX()+", "+event.getY()+"]");
    }
    
    public Image createLayout() {
        int width = (int) ProjectImages.getInstance().getWidth();
        int height = (int) ProjectImages.getInstance().getHeight();
        System.out.println("["+width+", "+height+"]");
        WritableImage out = new WritableImage(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                out.getPixelWriter().setColor(x, y, Color.WHITESMOKE);
            }
        }
        return out;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        backgroundLayout.prefWidthProperty().bind(leftPanel.widthProperty());
        backgroundLayout.prefHeightProperty().bind(leftPanel.heightProperty());
        canvasLayout.setImage(createLayout());
        canvasLayout.setFitWidth(ProjectImages.getInstance().getWidth());
        canvasLayout.setFitHeight(ProjectImages.getInstance().getHeight());
        canvasLayout.setPreserveRatio(true);
        
    }    
    
}
