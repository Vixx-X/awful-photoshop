/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fxproject;

import fxproject.graphics.gizmos.Gizmo;
import fxproject.graphics.Canvas;
import fxproject.graphics.CanvasEntity;
import fxproject.graphics.gizmos.GizmoCrop;
import fxproject.graphics.transformations.morphology.Closing;
import fxproject.graphics.transformations.morphology.Dilation;
import fxproject.graphics.transformations.morphology.Erosion;
import fxproject.graphics.transformations.morphology.Opening;
import fxproject.graphics.transformations.quantization.MedianCut;
import fxproject.graphics.transformations.quantization.OctTree;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.opencv.core.Point;

public class imageEditorController implements Initializable {

    /**
     * Initializes the controller class.
     */
    ObservableList<String> smoothedList = FXCollections.observableArrayList("Caja",
            "Cilíndrico", "Gauss");
    ObservableList<String> algorithm = FXCollections.observableArrayList("Octree",
            "Mediancut");
    ObservableList<String> morphologyList = FXCollections.observableArrayList("Erosión",
            "Dilatación", "Apertura", "Cierre");
    ObservableList<String> borderList = FXCollections.observableArrayList("Sobel",
            "Roberts", "Prewitt", "Perfilado");
    ObservableList<String> methodList = FXCollections.observableArrayList("Interpolación "
            + "bi-lineal", "Interpolación bi-cúbica", "Vecino más cercano");
    ObservableList<String> optionsThreshold = FXCollections.observableArrayList("Valor "
            + "constante", "Rango");

    @FXML
    private StackPane backgroundLayout, staticPaneSelected;

    @FXML
    private Pane canvasLayout, paneImageSelected;

    @FXML
    private ScrollPane leftPanel;

    @FXML
    private TabPane rightPanel;

    @FXML
    private MenuItem undoButton, redoButton;

    @FXML
    private RadioButton r3b, r5b, r7b;

    @FXML
    private ComboBox<String> smoothedFilters, borderFilters, threshold,
            quantization, morphology, method;

    @FXML
    private TextField indexColors;

    //private WritableImage layout;
    private ProjectImages main;

    private ArrayList<ImageView> visualImages;

    private CanvasEntity tmp;
    private Canvas c;
    public GizmoCrop gizmoCrop;

    private Point p;

    void moveActions(String type, Canvas c) {
        if (main.g.type != null) {
            switch (main.g.type) {
                case "translate" -> {
                    refreshRaster(c);
                    break;
                }
                case "scale" -> {
                    int i = getMethod();
                    //tmp.sclae(i);
                    refreshRaster(c);
                    break;
                }
                case "rotate" -> {
                    int i = getMethod();
                    //tmp.rotate(i);
                    refreshRaster(c);
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
        p = new Point(event.getX(), event.getY());
        if (main.g != null) {
            moveActions(main.g.type, c);
            main.g.removeOnCanvas(canvasLayout);
            main.g = null;
        }
        c = new Canvas(main.getCurrentCanvas());
        main.currentImage = c.getSelectedImage(p);
        compositeSelected();
        //main.currentImage = c.getSelectedImage(p);
        if (main.currentImage != null) {
            //tmp = new CanvasEntity(main.currentImage);
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
        c.images.set(index, tmp);
        main.currentImage = null;
        refreshRaster(c);
    }

    private int getDimension() {
        return (r5b.isSelected()) ? 5 : (r7b.isSelected() ? 7 : 3);
    }

    @FXML
    void applyMorphology(ActionEvent event) {

        int dim = getDimension();
        Canvas c = new Canvas(main.getCurrentCanvas());
        main.currentImage = c.getSelectedImage(p);
        if (main.currentImage == null) {
            return;
        }
        String type = morphology.getValue();
        if (type != null) {
            switch (type) {
                case "Erosión" ->
                    main.currentImage.img = Erosion.apply(main.currentImage.img, dim);
                case "Dilatación" ->
                    main.currentImage.img = Dilation.apply(main.currentImage.img, dim);
                case "Apertura" ->
                    main.currentImage.img = Opening.apply(main.currentImage.img, dim);
                case "Cierre" ->
                    main.currentImage.img = Closing.apply(main.currentImage.img, dim);
                default -> {
                    break;
                }
            }
        } else {
            morphology.setValue("Erosión");
            main.currentImage.img = Erosion.apply(main.currentImage.img, dim);
        }
        refreshRaster(c);
    }

    @FXML
    void applyQuantization(ActionEvent event
    ) {
        int index = (!indexColors.getText().isEmpty())
                ? Integer.parseInt(indexColors.getText()) : 256;

        Canvas c = new Canvas(main.getCurrentCanvas());
        main.currentImage = c.getSelectedImage(p);
        if (main.currentImage == null) {
            return;
        }
        String type = quantization.getValue();
        if (type != null) {
            switch (type) {
                case "Octree" ->
                    main.currentImage.img = OctTree.apply(main.currentImage.img, index);
                case "Mediancut" ->
                    main.currentImage.img = MedianCut.apply(main.currentImage.img, index);
                default -> {
                    break;
                }
            }
        } else {
            quantization.setValue("Octree");
            indexColors.setText(String.valueOf(index));
            System.out.println("Default Octree");
        }
        refreshRaster(c);
        //tmp.translateImg(p1);
        //changeImage(tmp);
    }

    private void refreshRaster(Canvas c) {
        main.pushCanvas(c);
        enableToolsButtons();
        drawRaster();
    }

    private void drawRaster() {
        visualImages = new ArrayList<>();
        for (CanvasEntity i : main.getCurrentCanvas().images) {
            ImageView imageV = new ImageView(i.getImage());
            Point p = i.getRasterCoord();
            imageV.relocate(p.x, p.y);
            visualImages.add(imageV);
        }
        canvasLayout.getChildren().setAll(visualImages);
    }

    private void compositeSelected() {
        if (main.currentImage == null) {
            return;
        }
        //CanvasEntity tmpImage = new CanvasEntity(main.currentImage);
        float w = main.currentImage.getUnrotatedUnscaledCroppedWidth();
        float h = main.currentImage.getUnrotatedUnscaledCroppedHeight();
        float scale = (w <= h) ? w / h : h / w;

        int width = (int) ((w <= h) ? scale * staticPaneSelected.getWidth()
                : staticPaneSelected.getWidth());
        int height = (int) ((w <= h) ? staticPaneSelected.getHeight()
                : scale * staticPaneSelected.getHeight());
        paneImageSelected.setPrefSize(width, height);
        paneImageSelected.setMaxSize(width, height);
        paneImageSelected.setStyle("-fx-background-color: #f5f5f5;");

        scale = (float) w / width;
        drawImage(main.currentImage, scale, width, height);

    }

    private void drawImage(CanvasEntity img, float scale, int width, int height) {
        CanvasEntity tmpImg = new CanvasEntity(img);
        tmpImg.angle = 0;
        tmpImg.scale = (float) 1 / scale;
        ImageView imageV = new ImageView(tmpImg.getImage());
        imageV.relocate(0, 0);
        paneImageSelected.getChildren().setAll(imageV);

        imageV.setOnMouseClicked(event -> {
            if (gizmoCrop != null) {
                return;
            }
            gizmoCrop = new GizmoCrop(img, width, height);
            gizmoCrop.addOnCanvas(paneImageSelected);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO
        main = ProjectImages.getInstance();
        backgroundLayout.prefWidthProperty().bind(leftPanel.widthProperty());
        backgroundLayout.prefHeightProperty().bind(leftPanel.heightProperty());

        staticPaneSelected.prefWidthProperty().bind(rightPanel.widthProperty());
        staticPaneSelected.prefHeightProperty().bind(rightPanel.widthProperty());
        staticPaneSelected.setMaxSize(350, 350);

        //staticPaneSelected.setPrefSize(300, 300);
        int w = main.getCurrentCanvas().w;
        int h = main.getCurrentCanvas().h;
        canvasLayout.setPrefSize(w, h);
        canvasLayout.setMaxSize(w, h);
        canvasLayout.setStyle("-fx-background-color: #f5f5f5;");
        threshold.setItems(optionsThreshold);
        smoothedFilters.setItems(smoothedList);
        borderFilters.setItems(borderList);
        quantization.setItems(algorithm);
        morphology.setItems(morphologyList);
        method.setItems(methodList);
        ToggleGroup group = new ToggleGroup();
        r3b.setToggleGroup(group);
        r5b.setToggleGroup(group);
        r7b.setToggleGroup(group);
    }

}
