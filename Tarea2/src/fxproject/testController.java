/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fxproject;

import fxproject.graphics.Canvas;
import fxproject.graphics.CanvasEntity;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.opencv.core.Point;

public class testController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private StackPane backgroundLayout;

    @FXML
    private Pane canvasLayout;

    @FXML
    private ScrollPane leftPanel;

    @FXML
    private MenuItem undoButton;

    @FXML
    private MenuItem redoButton;

    //private WritableImage layout;
    private ProjectImages main;

    private ArrayList<ImageView> visualImages;

    private CanvasEntity tmp;

    @FXML
    void clickPanel(MouseEvent event) {
        Point p = new Point(event.getX(), event.getY());
        Canvas c = new Canvas(main.getCurrentCanvas());
        if (main.g != null && tmp != null) {
            if (main.g.type != null) {
                switch (main.g.type) {
                    case "translate" -> {
                        Point p1 = new Point(main.g.mobileRect.getX(),
                                main.g.mobileRect.getY());
                        int index = c.images.indexOf(main.currentImage);
                        tmp.translateImg(p1);
                        c.images.set(index, tmp);
                        refreshRaster(c);
                        break;
                    }
                    case "scale" -> {
                        break;
                    }
                    default -> {
                        break;
                    }
                }
            }
            tmp = null;
            main.g.removeOnCanvas(canvasLayout);
            main.g = null;

        }
        main.currentImage = c.getSelectedImage(p);
        //main.currentImage = c.getSelectedImage(p);
        System.out.println(main.currentImage);
        if (main.currentImage != null) {
            tmp = new CanvasEntity(main.currentImage);
            System.out.println("holi");
            main.g = new Gizmo(main.currentImage.getCorners(),
                    main.currentImage.getImage().getWidth(),
                    main.currentImage.getImage().getHeight());

            main.currentImage.getCorners();
            main.g.addOnCanvas(canvasLayout);

        }

    }

    public void putFront() {
        Canvas c = new Canvas(main.getCurrentCanvas());
        int index = c.images.indexOf(main.currentImage);
        c.images.remove(index);
        c.images.add(main.currentImage);
        refreshRaster(c);
    }

    public void putBack() {
        Canvas c = new Canvas(main.getCurrentCanvas());
        int index = c.images.indexOf(main.currentImage);
        c.images.remove(index);
        c.images.add(0, main.currentImage);
        refreshRaster(c);
    }

    void enableToolsButtons() {
        undoButton.setDisable(main.getIndex() == 0);
        redoButton.setDisable(main.getStateListSize() - 1 == main.getIndex());
    }

    @FXML
    void undoAction() {
        Canvas canvas = main.undo();
        if (canvas != null) {
            drawRaster();
            enableToolsButtons();
        }
    }

    @FXML
    void redoAction() {
        Canvas canvas = main.redo();
        if (canvas != null) {
            drawRaster();
            enableToolsButtons();
        }
    }

    @FXML
    void saveAsAction(ActionEvent event) {

    }

    @FXML
    void saveImages(ActionEvent event) {

    }

    @FXML
    void uploadImage(ActionEvent event) {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(null);
        if (file != null) {
            Canvas c = new Canvas(main.getCurrentCanvas());
            c.addImage(file.getAbsolutePath());
            refreshRaster(c);
        }
    }

    private void refreshRaster(Canvas c) {
        main.pushCanvas(c);
        enableToolsButtons();
        drawRaster();
    }

    private void drawRaster() {
        visualImages = new ArrayList<>();
        System.out.println(main.getCurrentCanvas().images);
        for (CanvasEntity i : main.getCurrentCanvas().images) {
            ImageView imageV = new ImageView(i.getImage());
            imageV.relocate(i.x, i.y);
            System.out.println(i.getImage());
            visualImages.add(imageV);
        }
        canvasLayout.getChildren().setAll(visualImages);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO
        backgroundLayout.prefWidthProperty().bind(leftPanel.widthProperty());
        backgroundLayout.prefHeightProperty().bind(leftPanel.heightProperty());

        main = ProjectImages.getInstance();

        int w = main.getCurrentCanvas().w;
        int h = main.getCurrentCanvas().h;
        canvasLayout.setPrefSize(w, h);
        canvasLayout.setMaxSize(w, h);
        canvasLayout.setStyle("-fx-background-color: #f5f5f5;");
    }

}
