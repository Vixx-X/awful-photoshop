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
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import static java.util.Collections.list;
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

    private ImageView imageV;

    //private WritableImage layout;
    private ProjectImages main;

    private ArrayList<ImageView> visualImages;

    private CanvasEntity copyImage;
    private Canvas copyCanvas;
    public GizmoCrop gizmoCrop;

    private Point p;

    void moveActions(String type, Canvas c) {
        if (main.g.type != null) {
            switch (main.g.type) {
                case "translate" -> {
                    changeState(c);
                    break;
                }
                case "scale" -> {
                    int i = getMethod();
                    //tmp.sclae(i);
                    changeState(c);
                    break;
                }
                case "rotate" -> {
                    int i = getMethod();
                    System.out.println("Rotaaaaaaaaaaaaaaa me encanta la vaca");
                    changeState(c);
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
        //System.out.println("BRRRRRRR " + main.getCurrentCanvas().currentIndex);
        if (main.g != null) {
            moveActions(main.g.type, copyCanvas);
            main.g.removeOnCanvas(canvasLayout);
            main.g = null;
        }
        main.getCurrentCanvas().setSelectedImage(p);
        copyCanvas = new Canvas(main.getCurrentCanvas());
        refreshImage();
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
        c.images.add(c.getSelectedImage());
        c.images.remove(c.currentIndex);
        c.currentIndex = c.images.size() - 1;
        changeState(c);
    }

    public void putBack() {
        Canvas c = new Canvas(main.getCurrentCanvas());
        System.out.println(c.currentIndex + " tamañito 2 " + c.images.size());

        for (int i = 0; i < c.images.size(); i++) {
            System.out.println(c.images.get(i));
        }
        c.images.add(0, c.getSelectedImage());
        c.images.remove(c.currentIndex + 1);
        c.currentIndex = 0;
        changeState(c);
    }

    void enableToolsButtons() {
        undoButton.setDisable(main.getIndex() == 0);
        redoButton.setDisable(main.getStateListSize() - 1 == main.getIndex());
    }

    @FXML
    void undoAction() {
        Canvas canvas = main.undo();
        if (canvas != null) {
            refresh();
        }
    }

    @FXML
    void redoAction() {
        Canvas canvas = main.redo();
        if (canvas != null) {
            refresh();
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
            c.currentIndex = c.images.size() - 1;
            main.currentImage = c.images.get(c.images.size() - 1);
            setImageSize();
            changeState(c);
        }
    }

    void setImageSize() {
        float w = main.currentImage.getUnrotatedUnscaledCroppedWidth();
        float h = main.currentImage.getUnrotatedUnscaledCroppedHeight();
        float scale = (w <= h) ? w / h : h / w;
        if (w <= main.getCurrentCanvas().w && h <= main.getCurrentCanvas().h) {
            return;
        }
        float width = ((w <= h) ? scale * main.getCurrentCanvas().w
                : main.getCurrentCanvas().w);
        float height = ((w <= h) ? main.getCurrentCanvas().h
                : scale * main.getCurrentCanvas().h);
        scale = ((width / w) <= (height / h)) ? width / w : height / h;
        main.currentImage.scale(scale);

    }

    private void changeImage(CanvasEntity tmp) {
        Canvas c = new Canvas(main.getCurrentCanvas());
        int index = c.images.indexOf(main.currentImage);
        c.images.set(index, tmp);
        main.currentImage = null;
        changeState(c);

    }

    private int getDimension() {
        return (r5b.isSelected()) ? 5 : (r7b.isSelected() ? 7 : 3);
    }

    @FXML
    void applyMorphology(ActionEvent event) {

        int dim = getDimension();
        Canvas c = new Canvas(main.getCurrentCanvas());
        main.currentImage = c.getSelectedImage();
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
        changeState(c);
    }

    @FXML
    void applyQuantization(ActionEvent event
    ) {
        int index = (!indexColors.getText().isEmpty())
                ? Integer.parseInt(indexColors.getText()) : 256;

        Canvas c = new Canvas(main.getCurrentCanvas());
        main.currentImage = c.getSelectedImage();
        if (main.currentImage == null) {
            return;
        }
        String type = quantization.getValue();
        if (type != null) {
            switch (type) {
                case "Octree" ->
                    System.out.println("Octree " + index);
                case "Mediancut" ->
                    System.out.println("MedianCut " + index);
                default -> {
                    break;
                }
            }
        } else {
            quantization.setValue("Octree");
            indexColors.setText(String.valueOf(index));
            System.out.println("Default Octree");
        }
        changeState(c);
        //tmp.translateImg(p1);
        //changeImage(tmp);
    }

    private void changeState(Canvas c) {
        main.pushCanvas(c);
        refresh();
    }

    public void refresh() {
        refreshCanvas();
        refreshImage();
        enableToolsButtons();
    }

    public void selectImage() {
        main.g = new Gizmo(main.currentImage);
        main.g.addOnCanvas(canvasLayout);
    }

    public void refreshCanvas() {
        copyCanvas = main.getCurrentCanvas();
        drawRaster();
        //main.currentImage = c.getSelectedImage(p);
    }

    private void refreshImage() {
        main.currentImage = copyCanvas.getSelectedImage();
        if (main.currentImage == null) {
            cleanSelectImage();
            return;
        }
        selectImage();
        compositeSelected();
    }

    private void cleanSelectImage() {
        paneImageSelected.getChildren().remove(imageV);
        paneImageSelected.setStyle("-fx-background-color: transparent;");
        if (gizmoCrop != null) {
            gizmoCrop.removeOnCanvas(paneImageSelected);
            gizmoCrop = null;
        }

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
        imageV = new ImageView(tmpImg.getImage());
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
