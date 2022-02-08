/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject;

import fxproject.graphics.Canvas;
import fxproject.graphics.CanvasEntity;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import org.opencv.core.Core;

/**
 *
 * @author Gaby
 */
public class ProjectImages extends Application {

    private static ProjectImages instance;
    private static Stage primaryStage;
    private static VBox mainLayout;
    public Canvas canvas;

    //private ArrayList<RawImage> imageList = new ArrayList<>();
    private int currentState;
    public int i;
    public Gizmo g;
    public CanvasEntity currentImage;

    public ProjectImages() {
        currentState = 0;
    }

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

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }

    public void showPanel(int width, int height) throws IOException {
        canvas = new Canvas(width, height);
        currentState = 0;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("test.fxml"));
        VBox informationPanel = loader.load();
        testController controller = loader.getController();
        Stage informationView = new Stage();
        informationView.initModality(Modality.WINDOW_MODAL);
        informationView.initOwner(primaryStage);
        Scene scene = new Scene(informationPanel);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (currentImage != null) {
                    if (t.getCode() == KeyCode.F) {
                        controller.putFront();
                    } else if (t.getCode() == KeyCode.B) {
                        controller.putBack();
                    }
                }

            }

        });

        informationView.setScene(scene);
        informationView.showAndWait();
    }

    public int getIndex() {
        return currentState;
    }

    /* public int getStateListSize() {
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
    } */
}
