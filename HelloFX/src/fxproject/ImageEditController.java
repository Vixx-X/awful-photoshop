/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fxproject;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Gaby
 */
public class ImageEditController implements Initializable {

    /**
     * Initializes the controller class.
     */
   
    @FXML
    private ImageView imageMain;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        imageMain.setImage(ProjectImages.getInstance().getImageChoose());
    }    
    
}
