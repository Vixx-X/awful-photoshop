/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tarea2;

/**
 *
 * @author Gaby
 */
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
public class Tarea2 extends Application {

    private static Tarea2 instance;

    private static Stage primaryStage;
    private static BorderPane mainLayout;
    
    private int currentState;
    public int i;

    public Tarea2() {
        currentState = 0;
    }

    public static Tarea2 getInstance() {
        if (instance == null) {
            instance = new Tarea2();
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

    public void showImagePanel() throws IOException {
        currentState = 0;
        //imageChoose = image;
        //imageList = new ArrayList<>();
        //imageList.add(imageChoose);
        BorderPane imagePanel = FXMLLoader.load(getClass().getResource("ImageEditor.fxml"));
        Stage kernelView = new Stage();
        kernelView.initModality(Modality.WINDOW_MODAL);
        kernelView.initOwner(primaryStage);
        kernelView.setScene(new Scene(imagePanel));
        kernelView.showAndWait();
    }

    public int getIndex() {
        return currentState;
    }

    //public void pushImage(RawImage image) {
    //    if (currentState != (imageList.size() - 1)) {
    //        for (int i = currentState + 1; i < imageList.size(); i++) {
    //            imageList.remove(i);
    //        }
    //    }
    //    if (currentState == 9) {
    //        imageList.remove(1);
    //    } else {
    //        currentState++;
    //    }
    //    //System.out.println(currentState);
    //    imageList.add(image);
    //}

    //public RawImage undo() {
    //    if (currentState > 0) {
    //        currentState--;
    //        return imageList.get(currentState);
    //    }
    //    return null;
    //}

    //public RawImage redo() {
    //    if ((imageList.size() - 1) > currentState) {
    //        currentState++;
    //        return imageList.get(currentState);
    //    }
    //    return null;
    //}

    public static void main(String[] args) {
        launch(args);
    }
}
