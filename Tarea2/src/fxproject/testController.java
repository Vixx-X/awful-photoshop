/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fxproject;

import fxproject.graphics.CanvasEntity;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;

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

    @FXML
    void clickPanel(MouseEvent event) {
        System.out.println("[" + event.getX() + ", " + event.getY() + "]");
    }

    /*public Image createExampleImage() {
        int w = (int) (ProjectImages.getInstance().getWidth() / 3);
        int h = (int) (ProjectImages.getInstance().getHeight() / 3);
        System.out.println("[" + w + ", " + h + "]");
        WritableImage out = new WritableImage(w, h);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                out.getPixelWriter().setColor(x, y, Color.BLUE);
            }
        }
        return out;
    } */
 /*public ImagePortion centerImage(Image image) {

        int wImage2 = (int) (image.getWidth() / 2);
        int hImage2 = (int) (image.getHeight() / 2);
        int w2 = width / 2;
        int h2 = height / 2;

        return new ImagePortion(w2 - wImage2, h2 - hImage2, (int) image.getWidth(),
                (int) image.getHeight());
    } */
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

    public void addEdge(int x, int y, int type) {
        Circle circle = new Circle(x, y, 6.5);
        circle.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 0.8;");
        canvasLayout.getChildren().add(circle);
        circle.setOnDragDetected(e -> {
            //canvasLayout.getChildren().add(rect2);
        });
        /*switch (type) {
            case 1 ->
                img = SquareFilter.apply(choose, numberCol, numberRow,
                        temp.x1, temp.y1, temp.x2, temp.y2);
            case 2 ->
                img = CircleFilter.apply(choose, numberCol, numberRow,
                        temp.x1, temp.y1, temp.x2, temp.y2);
            case 3 ->
                img = GaussFilter.apply(choose, numberCol, numberRow,
                        temp.x1, temp.y1, temp.x2, temp.y2);
            default -> {
                return;
            }
        } */
    }

    public Rectangle addBorder(double w, double h, ImagePortion c, Rectangle rect2) {
        Rectangle rect = new Rectangle(c.x1 - 3, c.y1 - 3, w, h);
        // rect.setStyle("-fx-background-color:white; -fx-border-style:solid; -fx-border-width:3; -fx-border-color:black;");
        rect.setStyle("-fx-fill: transparent; -fx-stroke: black; -fx-stroke-width: 1;");

        //int i = c.x1 - 3;
        //int f = c.y1 - 3;
        //rect.setFill(Color.WHITE);
        //rect.relocate(i, f);
        //canvasLayout.getChildren().add(rect);
        final double handleRadius = 6.5;
        final String style = "-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 0.8;";
        //Circle circle = new Circle(x, y, 6.5);
        //circle.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 0.8;");

        Circle resizeHandleNW = new Circle(handleRadius);
        // bind to top left corner of Rectangle:
        resizeHandleNW.centerXProperty().bind(rect.xProperty());
        resizeHandleNW.centerYProperty().bind(rect.yProperty());
        resizeHandleNW.setStyle(style);

        // bottom right resize handle:
        Circle resizeHandleSE = new Circle(handleRadius);
        // bind to bottom right corner of Rectangle:
        resizeHandleSE.centerXProperty().bind(rect.xProperty().add(rect.widthProperty()));
        resizeHandleSE.centerYProperty().bind(rect.yProperty().add(rect.heightProperty()));
        resizeHandleSE.setStyle(style);

        rect.parentProperty().addListener((ObservableValue<? extends Parent> obs, Parent oldParent, Parent newParent) -> {
            for (Circle c1 : Arrays.asList(resizeHandleNW, resizeHandleSE)) {
                Pane currentParent = (Pane) c1.getParent();
                if (currentParent != null) {
                    currentParent.getChildren().remove(c1);
                }
                ((Pane) newParent).getChildren().add(c1);
            }
        });

        //addEdge(c.x1 - 2, c.y1 - 2, 1);
        //addEdge(c.x2 + 2, c.y2 + 2, 2);
        //addEdge(c.x2 + 2, c.y1 - 2, 3);
        //addEdge(c.x1 - 2, c.y2 + 2, 4);
        /*
        rect.setOnMouseDragged(e -> {
            rect2.relocate(i + e.getX(), f + e.getY());
        });

        rect.setOnMouseReleased(e -> {
            System.out.println("[" + e.getY() + ", " + e.getX());
            canvasLayout.getChildren().remove(rect2);
        }); */
        Wrapper<Point2D> mouseLocation = new Wrapper<>();

        setUpDragging(resizeHandleNW, mouseLocation);
        setUpDragging(resizeHandleSE, mouseLocation);
        setUpDragging(rect, mouseLocation);

        resizeHandleNW.setOnMouseDragged(event -> {
            if (mouseLocation.value != null) {
                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                double newX = rect2.getX() + deltaX;
                if (newX >= handleRadius
                        && newX <= rect2.getX() + rect2.getWidth() - handleRadius) {
                    rect2.setX(newX);
                    rect2.setWidth(rect2.getWidth() - deltaX);
                }
                double newY = rect2.getY() + deltaY;
                if (newY >= handleRadius
                        && newY <= rect2.getY() + rect2.getHeight() - handleRadius) {
                    rect2.setY(newY);
                    rect2.setHeight(rect2.getHeight() - deltaY);
                }
                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
            }
        });

        resizeHandleSE.setOnMouseDragged(event -> {
            if (mouseLocation.value != null) {
                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                double newMaxX = rect2.getX() + rect2.getWidth() + deltaX;
                if (newMaxX >= rect2.getX()
                        && newMaxX <= rect2.getParent().getBoundsInLocal().getWidth() - handleRadius) {
                    rect2.setWidth(rect2.getWidth() + deltaX);
                }
                double newMaxY = rect2.getY() + rect2.getHeight() + deltaY;
                if (newMaxY >= rect2.getY()
                        && newMaxY <= rect2.getParent().getBoundsInLocal().getHeight() - handleRadius) {
                    rect2.setHeight(rect2.getHeight() + deltaY);
                }
                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
            }
        });

        rect.setOnMouseDragged(event -> {
            if (mouseLocation.value != null) {
                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                double newX = rect2.getX() + deltaX;
                double newMaxX = newX + rect2.getWidth();
                if (newX >= handleRadius
                        && newMaxX <= rect2.getParent().getBoundsInLocal().getWidth() - handleRadius) {
                    rect2.setX(newX);
                }
                double newY = rect2.getY() + deltaY;
                double newMaxY = newY + rect2.getHeight();
                if (newY >= handleRadius
                        && newMaxY <= rect2.getParent().getBoundsInLocal().getHeight() - handleRadius) {
                    rect2.setY(newY);
                }
                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
            }

        });

        return rect;
    }

    private void setUpDragging(Shape circle, Wrapper<Point2D> mouseLocation) {

        circle.setOnDragDetected(event -> {
            circle.getParent().setCursor(Cursor.CLOSED_HAND);
            mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
        });

        circle.setOnMouseReleased(event -> {
            circle.getParent().setCursor(Cursor.DEFAULT);
            mouseLocation.value = null;
        });
    }

    public void drawRaster() {
        ArrayList<ImageView> visualImages = new ArrayList<>();
        System.out.println("AAAAAAA");
        for (CanvasEntity i : main.canvas.images) {
            System.out.println("PUTA");
            ImageView imageV = new ImageView(i.getImage());
            imageV.relocate(i.x, i.y);
            System.out.println(i.getImage());
            visualImages.add(imageV);
            canvasLayout.getChildren().add(imageV);
        }
        canvasLayout.getChildren().addAll(visualImages);
    }

    static class Wrapper<T> {

        T value;
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
        System.out.println("aquiii " + main.canvas.w);
        System.out.println("aquiii2 " + main.canvas.h);

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
