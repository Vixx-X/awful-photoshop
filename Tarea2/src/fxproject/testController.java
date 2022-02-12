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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
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
    ObservableList<String> smoothedList = FXCollections.observableArrayList("Caja",
            "Cilíndrico", "Gauss");
    ObservableList<String> algorithm = FXCollections.observableArrayList("Octree",
            "Mediancut");
    ObservableList<String> dimensions = FXCollections.observableArrayList("3x3",
            "5x5", "7x7");
    ObservableList<String> borderList = FXCollections.observableArrayList("Sobel",
            "Roberts", "Prewitt", "Perfilado");
    ObservableList<String> methodList = FXCollections.observableArrayList("Interpolación "
            + "bi-lineal", "Interpolación bi-cúbica", "Vecino más cercano");
    ObservableList<String> optionsThreshold = FXCollections.observableArrayList("Valor "
            + "constante", "Rango");

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

    @FXML
    private ComboBox<String> smoothedFilters;

    @FXML
    private ComboBox<String> borderFilters;

    @FXML
    private ComboBox<String> threshold;

    @FXML
    private ComboBox<String> quantization;

    @FXML
    private ComboBox<String> morphology;

    @FXML
    private ComboBox<String> method;

    //private WritableImage layout;
    private ProjectImages main;

    private ArrayList<ImageView> visualImages;

    private CanvasEntity tmp;
    private Canvas c;

    void moveActions(String type, Canvas c) {
        if (main.g.type != null) {
            switch (main.g.type) {
                case "translate" -> {
                    System.out.println("lisasa " + main.g.mobileRect.getPoints().get(0) 
                    + " " + main.g.mobileRect.getPoints().get(1));
                    System.out.println("li2222 " + main.currentImage.x 
                    + " " + main.currentImage.y);
                    refreshRaster(c);
                    break;
                }
                case "scale" -> {
                    int i = getMethod();
                    //tmp.sclae(i);
                    break;
                }
                case "rotate" -> {
                    int i = getMethod();
                    //tmp.rotate(i);
                    break;
                }
                default -> {
                    break;
                }
            }
        }
    }

    @FXML
    void clickPanel(MouseEvent event) {
        Point p = new Point(event.getX(), event.getY());
        if (main.g != null) {
            moveActions(main.g.type, c);
            main.g.removeOnCanvas(canvasLayout);
            main.g = null;
        }
        c = new Canvas(main.getCurrentCanvas());
        main.currentImage = c.getSelectedImage(p);
        //main.currentImage = c.getSelectedImage(p);
        System.out.println(main.currentImage);
        if (main.currentImage != null) {
            //tmp = new CanvasEntity(main.currentImage);
            //System.out.println("holi");
            main.g = new Gizmo(main.currentImage);
            main.g.addOnCanvas(canvasLayout);

        }
    }

    private int getMethod() {
        String type = method.getValue();
        if (type != null) {
            switch (type) {
                case "Interpolación bi-lineal" -> {
                    System.out.println("Interpolación bi-lineal");
                    return 1;
                }
                case "Interpolación bi-cúbica" -> {
                    System.out.println("Interpolación bi-cúbica");
                    return 2;
                }
                case "Vecino más cercano" -> {
                    System.out.println("Interpolación bi-cúbica");
                    return 3;
                }
                default -> {
                    return 1;
                }
            }
        } else {
            return 1;
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

    private void changeImage(CanvasEntity tmp) {
        Canvas c = new Canvas(main.getCurrentCanvas());
        int index = c.images.indexOf(main.currentImage);
        //System.out.println(index);
        c.images.set(index, tmp);
        main.currentImage = null;
        refreshRaster(c);
    }

    @FXML
    void applyMorphology(ActionEvent event) {
        if (main.currentImage == null) {
            return;
        }
        tmp = new CanvasEntity(main.currentImage);
        String type = morphology.getValue();
        if (type != null) {
            switch (type) {
                case "3x3" ->
                    System.out.println("3x3");
                case "5x5" ->
                    System.out.println("5x5");
                case "7x7" ->
                    System.out.println("7x7");
                default -> {
                    break;
                }
            }
        } else {
            morphology.setValue("3x3");
            System.out.println("Default 3x3");
        }

        //tmp.translateImg(p1);
        changeImage(tmp);
    }

    @FXML
    void applyQuantization(ActionEvent event
    ) {
        if (main.currentImage == null) {
            return;
        }
        tmp = new CanvasEntity(main.currentImage);
        String type = quantization.getValue();
        if (type != null) {
            switch (type) {
                case "Octree" ->
                    System.out.println("Octree");
                case "Mediancut" ->
                    System.out.println("MedianCut");
                default -> {
                    break;
                }
            }
        } else {
            quantization.setValue("Octree");
            System.out.println("Default Octree");
        }
        //tmp.translateImg(p1);
        changeImage(tmp);
    }

    private void refreshRaster(Canvas c) {
        main.pushCanvas(c);
        System.out.println("eeeee" + c);
        System.out.println("uuuuu" + main.getCurrentCanvas());
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
        main = ProjectImages.getInstance();
        backgroundLayout.prefWidthProperty().bind(leftPanel.widthProperty());
        backgroundLayout.prefHeightProperty().bind(leftPanel.heightProperty());
        int w = main.getCurrentCanvas().w;
        int h = main.getCurrentCanvas().h;
        canvasLayout.setPrefSize(w, h);
        canvasLayout.setMaxSize(w, h);
        canvasLayout.setStyle("-fx-background-color: #f5f5f5;");
        threshold.setItems(optionsThreshold);
        smoothedFilters.setItems(smoothedList);
        borderFilters.setItems(borderList);
        quantization.setItems(algorithm);
        morphology.setItems(dimensions);
        method.setItems(methodList);
    }

}
