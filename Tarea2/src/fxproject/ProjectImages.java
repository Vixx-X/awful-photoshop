/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject;

import fxproject.graphics.gizmos.Gizmo;
import fxproject.graphics.Canvas;
import fxproject.graphics.CanvasEntity;
import java.io.IOException;
import java.util.ArrayList;
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

public class ProjectImages extends Application {

    private static ProjectImages instance;
    private static Stage primaryStage;
    private static VBox mainLayout;
    private Canvas canvas;

    private final ArrayList<Canvas> record = new ArrayList<>();
    private int currentState;
    public Gizmo g;
    private int interpolation;
    public CanvasEntity currentImage;
    public imageEditorController mainController;

    public ProjectImages() {
        this.interpolation = 3;
        this.currentState = 0;
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

        record.add(canvas);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("imageEditor.fxml"));
        VBox informationPanel = loader.load();
        mainController = loader.getController();
        Stage informationView = new Stage();
        informationView.initModality(Modality.WINDOW_MODAL);
        informationView.initOwner(primaryStage);
        informationView.setMaximized(true);
        //informationView.setFullScreen(true);
        Scene scene = new Scene(informationPanel);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (currentImage != null) {
                    if (t.getCode() == KeyCode.F) {
                        mainController.putFront();
                    } else if (t.getCode() == KeyCode.B) {
                        mainController.putBack();
                    }
                }
                if (t.getCode() == KeyCode.Z && t.isControlDown()) {
                    mainController.undoAction();
                } else if (t.getCode() == KeyCode.Y && t.isControlDown()) {
                    mainController.redoAction();
                }

            }

        });

        informationView.setScene(scene);
        informationView.showAndWait();
    }

    public Canvas getCurrentCanvas() {
        return record.get(currentState);
    }

    public int getIndex() {
        return currentState;
    }

    public int getStateListSize() {
        return record.size();
    }

    public void pushCanvas(Canvas c) {
        if (currentState != (record.size() - 1)) {
            for (int i = currentState + 1; i < record.size(); i++) {
                record.remove(i);
            }
        }
        if (currentState == 9) {
            record.remove(0);
        } else {
            currentState++;
        }
        record.add(c);
    }

    public void undo() {
        if (currentState > 0) {
            currentState--;
        }
    }

    public void redo() {
        if ((record.size() - 1) > currentState) {
            currentState++;
        }
    }

    public int getInterpolation() {
        return interpolation;
    }

    public void setInterpolation(int i) {
        this.interpolation = i;
    }
}
