/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fxproject;

import fxproject.graphics.gizmos.Gizmo;
import fxproject.graphics.Canvas;
import fxproject.graphics.CanvasEntity;
import fxproject.graphics.RawImage;
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
import javafx.stage.FileChooser.ExtensionFilter;
import org.opencv.core.Point;

public class imageEditorController implements Initializable {

    /**
     * Initializes the controller class.
     */
    ObservableList<String> algorithm = FXCollections.observableArrayList("Octree",
            "Mediancut");
    ObservableList<String> morphologyList = FXCollections.observableArrayList("Erosión",
            "Dilatación", "Apertura", "Cierre");
    ObservableList<String> methodList = FXCollections.observableArrayList("Interpolación "
            + "bi-lineal", "Interpolación bi-cúbica", "Vecino más cercano", "Gauss + Vecino más cercano");

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
    private ComboBox<String> quantization, morphology, method;

    @FXML
    private TextField indexColors;

    private ImageView imageV;

    private ProjectImages main;

    private ArrayList<ImageView> visualImages;

    private Canvas current;

    public boolean isCropped;
    public GizmoCrop gizmoCrop;

    void selectImage(Point p) {
        if (main.g != null && main.g.isEditing) {
            return;
        }
        current.setSelectedImage(p);
    }

    @FXML
    void clickPanel(MouseEvent event) {
        if (main.g != null && main.g.type != null) {
            saveState();
            refresh();
        }
        Point p = new Point(event.getX(), event.getY());
        selectImage(p);
    }

    @FXML
    void clickSelect(MouseEvent event) {
        Point p = new Point(event.getX(), event.getY());
        selectImage(p);
        refreshImage();
    }

    @FXML
    void changeInterpolation(ActionEvent event) {
        main.setInterpolation(getMethod());
        
    }

    public Canvas loadCurrentCanvas() {
        return main.getCurrentCanvas();
    }

    public Canvas getCurrentCanvas() {
        return current;
    }

    public void putFront() {
        current.images.add(current.getSelectedImage());
        current.images.remove(current.currentIndex);
        current.currentIndex = current.images.size() - 1;
        saveState();
        refresh();
    }

    public void putBack() {
        current.images.add(0, current.getSelectedImage());
        current.images.remove(current.currentIndex + 1);
        current.currentIndex = 0;
        saveState();
        refresh();
    }

    void enableToolsButtons() {
        undoButton.setDisable(!main.canUndo());
        redoButton.setDisable(!main.canRedo());
    }

    @FXML
    void undoAction() {
        main.undo();
        loadState();
        refresh();
    }

    @FXML
    void redoAction() {
        main.redo();
        loadState();
        refresh();
    }

    @FXML
    void saveAsAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));

        File f = fileChooser.showSaveDialog(null);

        if (f != null && main.currentImage.img != null) {
            String name = f.getAbsolutePath();
            int i = name.indexOf(".");
            if (i == -1) {
                return;
            }
            String ext = name.substring(i);
            if (".png".equals(ext) || ".bmp".equals(ext) || ".jpg".equals(ext)) {
                main.currentImage.getRawImage().writeImage(f.getAbsolutePath());
            } else {
                System.out.println("Extension no permitida");
            }
        }
    }

    @FXML
    void uploadImage(ActionEvent event) {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(null);
        if (file != null) {
            current.addImage(file.getAbsolutePath());
            current.currentIndex = current.images.size() - 1;
            main.currentImage = current.images.get(current.images.size() - 1);
            setImageSize();
            saveState();
            refresh();
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

    private int getDimension() {
        return (r5b.isSelected()) ? 5 : (r7b.isSelected() ? 7 : 3);
    }

    private int getMethod() {
        String type = method.getValue();
        if (type != null) {
            switch (type) {
                case "Interpolación bi-lineal" -> {
                    main.currentImage.aliasing = 2;
                    return 2;
                }
                case "Interpolación bi-cúbica" -> {
                    main.currentImage.aliasing = 3;
                    return 3;
                }
                case "Vecino más cercano" -> {
                    main.currentImage.aliasing = 1;
                    return 1;
                }
                case "Gauss + Vecino más cercano" -> {
                    main.currentImage.aliasing = 4;
                    return 4;
                }
                default -> {
                    return 0;
                }
            }
        } else {
            return 0;
        }
    }

    @FXML
    void applyMorphology(ActionEvent event) {
        int dim = getDimension();
        main.currentImage = current.getSelectedImage();
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
        saveState();
        refresh();
    }

    @FXML
    void applyQuantization(ActionEvent event) {
        int index = (!indexColors.getText().isEmpty())
                ? Integer.parseInt(indexColors.getText()) : 128;

        main.currentImage = current.getSelectedImage();
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
        }
        saveState();
        refresh();
    }

    private void saveState() {
        main.pushCanvas(new Canvas(current));
    }

    private void loadState() {
        current = loadCurrentCanvas();
    }

    public void refresh() {
        refreshCanvas();
        refreshImage();
        enableToolsButtons();
    }

    public void addGizmo() {
        main.g = new Gizmo(main.currentImage);
        main.g.addOnCanvas(canvasLayout);
    }

    public void removeGizmo() {
        main.g.removeOnCanvas(canvasLayout);
        main.g = null;
    }

    public void refreshCanvas() {
        drawRaster(current.images);
    }

    private void refreshImage() {
        main.currentImage = current.getSelectedImage();

        if (main.g != null && (main.currentImage == null || (main.currentImage.id != main.g.currentImage.id))) {
            removeGizmo();
        }

        if (main.currentImage == null) {
            cleanSelectImage();
            return;
        }

        if (main.g == null) {
            addGizmo();
        } else {
            main.g.currentImage = main.currentImage;
            main.g.drawGizmo();
        }

        compositeSelected();
    }

    private void removeGizmoCrop() {
        gizmoCrop.removeOnCanvas(paneImageSelected);
        gizmoCrop = null;
    }

    private void addGizmoCrop(CanvasEntity img, int width, int height, float scale) {
        gizmoCrop = new GizmoCrop(img, width, height, scale);
        gizmoCrop.addOnCanvas(paneImageSelected);
        isCropped = true;
    }

    private void cleanSelectImage() {
        paneImageSelected.getChildren().remove(imageV);
        paneImageSelected.setStyle("-fx-background-color: transparent;");
        if (gizmoCrop != null) {
            removeGizmoCrop();
        }

    }

    @FXML
    void cropAction(MouseEvent event) {
        if (imageV.isPressed() || !isCropped) {
            return;
        }
        isCropped = false;
        removeGizmoCrop();
        saveState();
        refresh();
    }

    private void drawRaster(ArrayList<CanvasEntity> images) {
        visualImages = new ArrayList<>();
        for (CanvasEntity i : images) {
            ImageView _imageV = new ImageView(i.getImage());
            Point p = i.getRasterCoord();
            _imageV.relocate(p.x, p.y);
            visualImages.add(_imageV);
        }
        canvasLayout.getChildren().setAll(visualImages);
        if (main.g != null) {
            main.g.addOnCanvas(canvasLayout);
        }
    }

    private void compositeSelected() {
        if (main.currentImage == null) {
            return;
        }
        //CanvasEntity tmpImage = new CanvasEntity(main.currentImage);
        float w = main.currentImage.img.width();
        float h = main.currentImage.img.height();
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
        tmpImg.padBottom = 0;
        tmpImg.padRight = 0;
        tmpImg.padLeft = 0;
        tmpImg.padTop = 0;
        imageV = new ImageView(tmpImg.getImage());
        imageV.relocate(0, 0);
        paneImageSelected.getChildren().setAll(imageV);

        imageV.setOnMouseClicked(event -> {
            if (gizmoCrop != null) {
                return;
            }
            addGizmoCrop(img, width, height, (float) tmpImg.scale);

        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        main = ProjectImages.getInstance();
        current = loadCurrentCanvas();
        isCropped = false;
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
        quantization.setItems(algorithm);
        morphology.setItems(morphologyList);
        method.setItems(methodList);
        ToggleGroup group = new ToggleGroup();
        r3b.setToggleGroup(group);
        r5b.setToggleGroup(group);
        r7b.setToggleGroup(group);
    }

}
