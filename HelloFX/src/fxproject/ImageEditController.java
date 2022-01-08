/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fxproject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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
    
    @FXML
    void BlackWhiteFilter(ActionEvent event) {
        System.out.println("BlackWhiteFilter here");
    }

    @FXML
    void GrayScaleFilter(ActionEvent event) {
        System.out.println("GrayScaleFilter here");
    }

    @FXML
    void negativeFilter(ActionEvent event) {
        System.out.println("negativeFilter here");
    }
    
    @FXML
    void histogramView(ActionEvent event) throws IOException {
        ProjectImages.getInstance().showBarChart();
    }
    
    @FXML
    void informatioView(ActionEvent event) throws IOException {
        ProjectImages.getInstance().showDetails();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        imageMain.setImage(ProjectImages.getInstance().getImageChoose());
    }
    
}
