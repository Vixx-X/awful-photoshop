/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject;

import fxproject.models.RawImage;
import fxproject.models.RawImage.Type;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import javafx.stage.Modality;

/**
 *
 * @author Gaby
 */
public class ProjectImages extends Application {

    private static ProjectImages instance;

    private static Stage primaryStage;
    private static BorderPane mainLayout;
    private static RawImage imageChoose;
    
    private ArrayList<RawImage> imageList = new ArrayList<>();
    private int currentState;
    public int i;

    public ProjectImages() {
        currentState = 0;
    }

    public static ProjectImages getInstance() {
        if (instance == null) {
            instance = new ProjectImages();
        }
        return instance;
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        mainLayout = FXMLLoader.load(getClass().getResource("MainView.fxml"));
        primaryStage.setTitle("Photoshop Básico");
        primaryStage.setScene(new Scene(mainLayout));
        primaryStage.show();
    }

    public void showImagePanel(RawImage image) throws IOException {
        currentState = 0;
        imageChoose = image;
        imageList = new ArrayList<>();
        imageList.add(imageChoose);
        BorderPane imagePanel = FXMLLoader.load(getClass().getResource("ImageEditor.fxml"));
        Stage kernelView = new Stage();
        kernelView.initModality(Modality.WINDOW_MODAL);
        kernelView.initOwner(primaryStage);
        kernelView.setScene(new Scene(imagePanel));
        kernelView.showAndWait();
    }

    public void showBarChart() throws IOException {
        if(getChoose().type == Type.GrayScale){
            i = 0;
            BorderPane imagePanel = FXMLLoader.load(getClass().getResource("BarChart.fxml"));
            Stage histogramView = new Stage();
            histogramView.initModality(Modality.WINDOW_MODAL);
            histogramView.initOwner(primaryStage);
            histogramView.setScene(new Scene(imagePanel));
            histogramView.showAndWait();
            
        }else {
            for(i=1; i<4; i++){
                BorderPane imagePanel = FXMLLoader.load(getClass().getResource("BarChart.fxml"));
                Stage histogramView = new Stage();
                histogramView.initModality(Modality.NONE);
                histogramView.initOwner(primaryStage);
                histogramView.setScene(new Scene(imagePanel));
                histogramView.show();
            }
        }

    }

    

    public void showDetails() throws IOException {
        BorderPane informationPanel = FXMLLoader.load(getClass().getResource("InformationView.fxml"));
        Stage informationView = new Stage();
        informationView.initModality(Modality.WINDOW_MODAL);
        informationView.initOwner(primaryStage);
        informationView.setScene(new Scene(informationPanel));
        informationView.showAndWait();
    }

    public Image getImageChoose() {
        return imageList.get(currentState).getImage();
    }

    public RawImage getChoose() {
        return imageList.get(currentState);
    }

    public int getIndex() {
        return currentState;
    }

    public int getStateListSize() {
        return imageList.size();
    }

    public void pushImage(RawImage image) {
        if (currentState != (imageList.size() - 1)) {
            for (int i = currentState + 1; i < imageList.size(); i++) {
                imageList.remove(i);
            }
        }
        if (currentState == 9) {
            imageList.remove(1);
        } else {
            currentState++;
        }
        //System.out.println(currentState);
        imageList.add(image);
    }

    public RawImage undo() {
        if (currentState > 0) {
            currentState--;
            return imageList.get(currentState);
        }
        return null;
    }

    public RawImage redo() {
        if ((imageList.size() - 1) > currentState) {
            currentState++;
            return imageList.get(currentState);
        }
        return null;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
