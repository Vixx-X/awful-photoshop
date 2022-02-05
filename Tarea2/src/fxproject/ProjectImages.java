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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
/**
 *
 * @author Gaby
 */
public class ProjectImages extends Application{
    
    private static ProjectImages instance;
    private static Stage primaryStage;
    private static VBox mainLayout;
    
    private float widthCanvas;
    private float heightCanvas;
    private int currentState;
    public int i;
    
    //ProjectImages.getInstance().showImagePanel(images[0]);
    

    
    public static ProjectImages getInstance() {
        if (instance == null) {
            instance = new ProjectImages();
        }
        return instance;
    }
        
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        mainLayout = FXMLLoader.load(getClass().getResource("start.fxml"));
        primaryStage.setTitle("Photoshop Básico 2.0");
        primaryStage.setScene(new Scene(mainLayout));
        primaryStage.show();
    }
    public static void main(String[] args){
        launch(args);
    }
    
    public float getWidth() {
        return widthCanvas;
    } 
    public void setWidth(float width) {
        widthCanvas = width;
    } 
    public float getHeight() {
        return heightCanvas;
    } 
    public void setHeight(float height) {
        heightCanvas = height;
    } 
            public void showDetails() throws IOException {
        
    }
    public void showPanel(float width, float height) throws IOException {
        setWidth(width);
        setHeight(height);
        //System.out.println("A veee ["+widthCanvas+", "+height+"]");
        VBox informationPanel = FXMLLoader.load(getClass().getResource("test.fxml"));
        Stage informationView = new Stage();
        informationView.initModality(Modality.WINDOW_MODAL);
        informationView.initOwner(primaryStage);
        informationView.setScene(new Scene(informationPanel));
        informationView.showAndWait();
    }
}