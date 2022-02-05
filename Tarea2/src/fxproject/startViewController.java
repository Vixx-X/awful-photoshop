/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fxproject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Gaby
 */
public class startViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField heightCanvas;

    @FXML
    private Label mainLabel;

    @FXML
    private VBox mainLayout;

    @FXML
    private TextField widthCanvas;
    
    private float width;
    private float height;

    /*Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
double width = resolution.getWidth();
double height = resolution.getHeight(); 
double w = width/1280;  // your window width
double h = height/720;  // your window height
Scale scale = new Scale(w, h, 0, 0);
root.getTransforms().add(scale); */

    @FXML
    void createCanvas(ActionEvent event) {
        if (widthCanvas.getText().isEmpty()) {
            width = (float) 1060;
        }else{
            width = Float.parseFloat(widthCanvas.getText());
        }
        if (heightCanvas.getText().isEmpty()) {
            height = (float) 640;
        }else{
            height = Float.parseFloat(heightCanvas.getText());
        }
        try {
            System.out.println("A veee ["+width+", "+height+"]");
            ProjectImages.getInstance().showPanel(width, height); 
        } catch (IOException ex) {
            Logger.getLogger(startViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
