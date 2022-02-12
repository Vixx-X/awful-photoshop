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
import javafx.scene.control.TextField;

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
    private TextField widthCanvas;

    @FXML
    void createCanvas(ActionEvent event) {
        int width = (!widthCanvas.getText().isEmpty())
                ? Integer.parseInt(widthCanvas.getText()) : 1060;
        int height = (!heightCanvas.getText().isEmpty())
                ? Integer.parseInt(heightCanvas.getText()) : 640;

        try {
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
