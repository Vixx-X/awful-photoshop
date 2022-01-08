/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Gaby
 */
public class ProjectImages extends Application {
    private static ProjectImages instance;

    private static BorderPane mainLayout;
    private static Image imageChoose;
    
    
   public static ProjectImages getInstance() {
        if (instance == null) {
            instance = new ProjectImages();     
        }
        return instance;
    }


    @Override
    public void start(Stage stage) throws Exception {
        mainLayout = FXMLLoader.load(getClass().getResource("mainView.fxml"));
        stage.setTitle("Photoshop Básico");
        stage.setScene(new Scene(mainLayout));
        stage.show();
    }
    

    public void showImagePanel(Image image) throws IOException {
        imageChoose = image;
        BorderPane imagePanel = FXMLLoader.load(getClass().getResource("imageEditor.fxml"));
        mainLayout.setCenter(imagePanel);

    }

    public Image getImageChoose() {
        return imageChoose ;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
