/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fxproject;

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

    @FXML
    void clickPanel(MouseEvent event) {
        Point p = new Point(event.getX(), event.getY());
        if (main.g != null) {
            if (main.g.type != null) {
                switch (main.g.type) {
                    case "translate" -> {
                        Point p1 = new Point(main.g.mobileRect.getX(),
                                main.g.mobileRect.getY());
                        main.currentImage.translateImg(p1);
                        drawRaster();
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
            main.g.removeOnCanvas(canvasLayout);
            main.g = null;

        }
        main.currentImage = main.canvas.getSelectedImage(p);
        System.out.println(main.currentImage);
        if (main.currentImage != null) {
            System.out.println("holi");
            main.g = new Gizmo(main.currentImage.getCorners(), 
                    main.currentImage.getImage().getWidth(),
                    main.currentImage.getImage().getHeight());

            main.currentImage.getCorners();
            main.g.addOnCanvas(canvasLayout);

        }

    }
    
    public void putFront(){
        int index = main.canvas.images.indexOf(main.currentImage);
        main.canvas.images.remove(index);
        main.canvas.images.add(main.currentImage);
        drawRaster();
    }
    
    public void putBack(){
        int index = main.canvas.images.indexOf(main.currentImage);
        main.canvas.images.remove(index);
        main.canvas.images.add(0, main.currentImage);
        drawRaster();
    }

    

    void enableToolsButtons() {
        if (ProjectImages.getInstance().getIndex() == 0) {
            undoButton.setDisable(true);
        } else {
            undoButton.setDisable(false);
        }
        /* if (ProjectImages.getInstance().getStateListSize() - 1
                == ProjectImages.getInstance().getIndex()) {
            redoButton.setDisable(true);
        } else {
            redoButton.setDisable(false);
        } */
    }

    @FXML
    void undoAction(ActionEvent event) {
        /*RawImage image = ProjectImages.getInstance().undo();
        if (image != null) {
            imageMain.setImage(image.getImage());
        } */
        enableToolsButtons();
    }

    @FXML
    void redoAction(ActionEvent event) {
        /*RawImage image = ProjectImages.getInstance().redo();
        if (image != null) {
            imageMain.setImage(image.getImage());
        } */
        enableToolsButtons();
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
            System.out.println(main.canvas.addImage(file.getAbsolutePath()));
            System.out.println(file.getAbsolutePath());
        }
        drawRaster();
    }

    public void drawRaster() {
        //canvasLayout.getChildren().removeAll(visualImages);
        visualImages = new ArrayList<>();
        System.out.println(main.canvas.images);
        for (CanvasEntity i : main.canvas.images) {
            ImageView imageV = new ImageView(i.getImage());
            imageV.relocate(i.x, i.y);
            /*imageV.setOnMouseClicked(e -> {
                Gizmo g = new Gizmo(i.x, i.y, i.getImage().getWidth(),
                        i.getImage().getHeight(), canvasLayout);
                canvasLayout.getChildren().add(g.mobileRect);
                canvasLayout.getChildren().add(g.selectRect);
            }); */
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

        int w = main.canvas.w;
        int h = main.canvas.h;
        canvasLayout.setPrefSize(w, h);
        canvasLayout.setMaxSize(w, h);
        canvasLayout.setStyle("-fx-background-color: #f5f5f5;");
        //System.out.println("aquiii " + main.canvas.w);
        //System.out.println("aquiii2 " + main.canvas.h);

        /*Image i = createExampleImage();
        ImagePortion c = centerImage(i);
        ImageView iv1 = new ImageView(i);
        iv1.relocate(c.x1, c.y1);
        //iv1.setImage(i);
        iv1.setFitWidth(i.getWidth());
        iv1.setFitHeight(i.getHeight());
        iv1.setPreserveRatio(true);
        canvasLayout.getChildren().addAll(iv1);
        Rectangle rect2 = new Rectangle(c.x1 - 3, c.y1 - 3, i.getWidth() + 6, i.getHeight() + 6);
        Rectangle rect = addBorder(i.getWidth() + 6, i.getHeight() + 6, c, rect2);
        rect2.setStyle("-fx-fill: transparent; -fx-stroke: red; -fx-stroke-width: 1;");

        iv1.setOnMouseClicked(e -> {
            canvasLayout.getChildren().remove(rect2);
            //Rectangle rect = createDraggableRectangle(200, 200, 400, 300);
            canvasLayout.getChildren().add(rect2);
            canvasLayout.getChildren().add(rect);
        }); /
        // = createDraggableRectangle(200, 200, 400, 300);
        //rect.setFill(Color.NAVY);
        //});

        //Rectangle rect = createDraggableRectangle(200, 200, 400, 300);
        //rect.setFill(Color.NAVY);

        //canvasLayout.getChildren().add(rect);

        //addBorder(i.getWidth() + 6, i.getHeight() + 6, c);
        //Rectangle rect = new Rectangle(i.getWidth() + 6, i.getWidth() + 6);
        /*iv1.setOnMouseDragEntered(e -> {
            System.out.println("[chao]");
           canvasLayout.getChildren().add(rect);
        }); */
    }

}
