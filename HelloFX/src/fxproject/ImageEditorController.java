/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fxproject;

import fxproject.models.RawImage;
import fxproject.filters.globals.BlackWhiteFilter;
import fxproject.filters.globals.GrayScaleFilter;
import fxproject.filters.globals.NegativeFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * FXML Controller class
 *
 * @author Gaby
 */
public class ImageEditorController implements Initializable {

    ObservableList<String> smoothedList = FXCollections.observableArrayList("Caja", "Cilíndrico", "Gauss");
    ObservableList<String> borderList = FXCollections.observableArrayList("Sobel", "Roberts", "Prewitt", "Perfilado");
    ObservableList<String> optionsThreshold = FXCollections.observableArrayList("Valor constante", "Rango");
    /**
     * Initializes the controller class.
     */
    @FXML
    private Button buttonBorder;

    @FXML
    private TextField columnsBorder;

    @FXML
    private TextField rowsBorder;

    @FXML
    private ComboBox<String> borderFilters;

    @FXML
    private Button buttonSmoothed;

    @FXML
    private TextField columnsSmooth;

    @FXML
    private TextField rowsSmooth;

    @FXML
    private ComboBox<String> smoothedFilters;

    @FXML
    private ImageView imageMain;

    @FXML
    private ComboBox<String> threshold;

    @FXML
    private Label thresholdLabel;

    @FXML
    private TextField thresholdVal1;

    @FXML
    private TextField thresholdVal2;

    @FXML
    private Button thresholdButton;

    @FXML
    private MenuItem undoButton;

    @FXML
    private MenuItem redoButton;

    @FXML
    private CheckBox imagePortionBool;

    @FXML
    private TextField portionX;

    @FXML
    private TextField portionY;

    @FXML
    private TextField heightPortion;

    @FXML
    private TextField widthPortion;

    @FXML
    private Button kernelButtonApply;

    @FXML
    private TextField kernelColumns;

    @FXML
    private BorderPane kernelPane;

    @FXML
    private TextField kernelRows;

    @FXML
    private Button brightnessButton;

    @FXML
    private Slider brightnessSlide;

    @FXML
    private TextField brightnessTextfield;

    @FXML
    private Button contrastButton;

    @FXML
    private Slider contrastSlide;

    @FXML
    private TextField contrastTextfield;

    private final ArrayList<TextField> kernelFieldList = new ArrayList<>();
    //private final ArrayList<Integer> kernelNumbers = new ArrayList<>();
    public float[] kernelNumbers;

    private int kernelNumCols;
    private int kernelNumRows;
    private GridPane kernelRoot = new GridPane();

    @FXML
    void BlackWhiteFilter(ActionEvent event) {
        RawImage choose = ProjectImages.getInstance().getChoose();
        RawImage img;

        if (imagePortionBool.isSelected()) {
            ImagePortion temp = obtainDataPortion();
            img = BlackWhiteFilter.apply(choose, temp.x1, temp.y1, temp.x2, temp.y2);
        } else {
            img = BlackWhiteFilter.apply(choose);
        }

        imageMain.setImage(img.getImage());
        ProjectImages.getInstance().pushImage(img);
        enableToolsButtons();
    }

    @FXML
    void GrayScaleFilter(ActionEvent event) {
        RawImage choose = ProjectImages.getInstance().getChoose();
        RawImage img;
        if (imagePortionBool.isSelected()) {
            ImagePortion temp = obtainDataPortion();
            img = GrayScaleFilter.apply(choose, temp.x1, temp.y1, temp.x2, temp.y2);
        } else {
            img = GrayScaleFilter.apply(choose);
        }
        imageMain.setImage(img.getImage());
        ProjectImages.getInstance().pushImage(img);
        enableToolsButtons();
    }

    @FXML
    void negativeFilter(ActionEvent event) {
        RawImage choose = ProjectImages.getInstance().getChoose();
        RawImage img;
        if (imagePortionBool.isSelected()) {
            ImagePortion temp = obtainDataPortion();
            img = NegativeFilter.apply(choose, temp.x1, temp.y1, temp.x2, temp.y2);
        } else {
            img = NegativeFilter.apply(choose);
        }
        imageMain.setImage(img.getImage());
        ProjectImages.getInstance().pushImage(img);
        enableToolsButtons();
    }

    boolean validInputRowsColumns(TextField col, TextField row) {
        int numberCol = Integer.parseInt(col.getText());
        int numberRow = Integer.parseInt(row.getText());
        return (numberCol >= 1 && numberRow >= 2 || numberCol >= 2 && numberRow >= 1) && (numberCol <= 7 && numberRow <= 7);
    }

    void enableFilterButton(ComboBox filterChoose, TextField col, TextField row, int number) {
        if (!(filterChoose.getValue() == null || col.getText().isEmpty()
                || row.getText().isEmpty()) && validInputRowsColumns(col, row)) {
            if (number == 0) {
                buttonSmoothed.setDisable(false);
            } else if (number == 1) {
                buttonBorder.setDisable(false);
            }
        } else {
            if (number == 0) {
                buttonSmoothed.setDisable(true);
            } else if (number == 1) {
                buttonBorder.setDisable(true);
            }
        }
    }

    @FXML
    void selectFilters(ActionEvent event) {
        if (event.getSource().equals(smoothedFilters)) {
            enableFilterButton(smoothedFilters, rowsSmooth, columnsSmooth, 0);
        } else {
            enableFilterButton(borderFilters, rowsBorder, columnsBorder, 1);
        }
    }

    @FXML
    void columnsFilters(ActionEvent event) {
        if (event.getSource().equals(columnsSmooth)) {
            enableFilterButton(smoothedFilters, rowsSmooth, columnsSmooth, 0);
        } else {
            enableFilterButton(borderFilters, rowsBorder, columnsBorder, 1);
        }
    }

    @FXML
    void rowsFilters(ActionEvent event) {
        if (event.getSource().equals(rowsSmooth)) {
            enableFilterButton(smoothedFilters, rowsSmooth, columnsSmooth, 0);
        } else {
            enableFilterButton(borderFilters, rowsBorder, columnsBorder, 1);
        }
    }

    @FXML
    void applySmoothed(ActionEvent event) {
        int numberCol = Integer.parseInt(columnsSmooth.getText());
        int numberRow = Integer.parseInt(rowsSmooth.getText());
        System.out.println("Smooth filter here");
        System.out.println(numberCol);
        System.out.println(numberRow);
        System.out.println(smoothedFilters.getValue());
        //ProjectImages.getInstance().pushImage(img);
        //enableToolsButtons();
        //imageMain.setImage([**insertar imagen de tipo Image**]);
    }

    @FXML
    void applyBorder(ActionEvent event) {
        int numberCol = Integer.parseInt(columnsBorder.getText());
        int numberRow = Integer.parseInt(rowsBorder.getText());
        System.out.println("Border filter here");
        System.out.println(numberCol);
        System.out.println(numberRow);
        System.out.println(borderFilters.getValue());
        //ProjectImages.getInstance().pushImage(img);
        //enableToolsButtons();
        //imageMain.setImage([**insertar imagen de tipo Image**]);
    }

    @FXML
    void typeThreshold(ActionEvent event) {
        thresholdLabel.setText(threshold.getValue());
        if ("Rango".equals(threshold.getValue())) {
            thresholdVal1.setVisible(true);
            thresholdVal2.setVisible(true);
        } else {
            thresholdVal1.setVisible(true);
            thresholdVal2.setVisible(false);
        }
    }

    @FXML
    void enableThresholdButton(ActionEvent event) {
        int min = Integer.parseInt(thresholdVal1.getText());
        int max = 255;
        if (!thresholdVal2.getText().isEmpty()) {
            max = Integer.parseInt(thresholdVal2.getText());
        }
        if ("Rango".equals(threshold.getValue()) && min >= 0 && min < 255
                && max > min && max > 0 && max <= 255) {
            thresholdButton.setDisable(false);
        } else if ("Valor constante".equals(threshold.getValue()) && min >= 0 && min <= 255) {
            thresholdButton.setDisable(false);
        } else {
            thresholdButton.setDisable(true);
        }
    }

    @FXML
    void applyThreshold(ActionEvent event) {
        System.out.println("Umbralizacion aqui");
        int min = Integer.parseInt(thresholdVal1.getText());
        if ("Rango".equals(threshold.getValue())) {
            int max = Integer.parseInt(thresholdVal2.getText());
            System.out.println("rango");
            System.out.println(min);
            System.out.println(max);
        } else {
            System.out.println("valor");
            System.out.println(min);
        }
        //ProjectImages.getInstance().pushImage(img);
        //enableToolsButtons();
    }

    @FXML
    void histogramView(ActionEvent event) throws IOException {
        ProjectImages.getInstance().showBarChart();
    }

    @FXML
    void informationView(ActionEvent event) throws IOException {
        ProjectImages.getInstance().showDetails();
    }

    @FXML
    void saveImages(ActionEvent event) {
        RawImage image = ProjectImages.getInstance().getChoose();
        if (image != null) {
            image.writeImage();
        }

        // Tu imagen es imageChoose para acceder = ProjectImages.getInstance().getImageChoose();
        // Se puede hacer un condicioal que si es nulo abrir una ventana donde diga que no se ha eligido una imagen pa salvar
    }

    @FXML
    void saveAsAction(ActionEvent event) {
        RawImage image = ProjectImages.getInstance().getChoose();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
        //
        File f = fileChooser.showSaveDialog(null);
        System.out.println(f.getAbsolutePath());

        if (image != null) {
            image.writeImage(f.getAbsolutePath());
        }
    }

    void enableToolsButtons() {
        if (ProjectImages.getInstance().getIndex() == 0) {
            undoButton.setDisable(true);
        } else {
            undoButton.setDisable(false);
        }
        if (ProjectImages.getInstance().getStateListSize() - 1
                == ProjectImages.getInstance().getIndex()) {
            redoButton.setDisable(true);
        } else {
            redoButton.setDisable(false);
        }
    }

    @FXML
    void undoAction(ActionEvent event) {
        //System.out.println("Deshacer cambios la imagen actual");
        RawImage image = ProjectImages.getInstance().undo();
        if (image != null) {
            imageMain.setImage(image.getImage());
        }
        enableToolsButtons();

        //Tu imagen es imageChoose para acceder = ProjectImages.getInstance().getImageChoose();
    }

    @FXML
    void redoAction(ActionEvent event) {
        //System.out.println("Rehacer cambios la imagen actual");
        RawImage image = ProjectImages.getInstance().redo();
        if (image != null) {
            imageMain.setImage(image.getImage());
        }
        enableToolsButtons();
        //Tu imagen es imageChoose para acceder = ProjectImages.getInstance().getImageChoose();
    }

    @FXML
    void enablePortionImage(ActionEvent event) {

        if (!widthPortion.getText().isEmpty() || !heightPortion.getText().isEmpty()) {
            if (widthPortion.getText().isEmpty()) {
                widthPortion.setText(heightPortion.getText());
            } else if (heightPortion.getText().isEmpty()) {
                heightPortion.setText(widthPortion.getText());
            }
            if (portionX.getText().isEmpty()) {
                portionX.setText("0");
            }
            if (portionY.getText().isEmpty()) {
                portionY.setText("0");
            }
            imagePortionBool.setDisable(false);
        } else {
            imagePortionBool.setDisable(true);
        }

    }

    ImagePortion obtainDataPortion() {
        int x = Integer.parseInt(portionX.getText());
        int y = Integer.parseInt(portionY.getText());
        int width = Integer.parseInt(widthPortion.getText());
        int height = Integer.parseInt(heightPortion.getText());
        return new ImagePortion(x, y, width, height);
    }

    @FXML
    void createGridPane(ActionEvent event) {
        kernelRoot = new GridPane();
        if (!(kernelColumns.getText().isEmpty())) {
            kernelNumCols = Integer.parseInt(kernelColumns.getText());
        }
        if (!(kernelRows.getText().isEmpty())) {
            kernelNumRows = Integer.parseInt(kernelRows.getText());
        }
        if (!(kernelColumns.getText().isEmpty()) && !(kernelRows.getText().isEmpty())) {
            kernelNumbers = new float[kernelNumCols * kernelNumRows];
            kernelRoot.setGridLinesVisible(true);
            for (int i = 0; i < kernelNumCols; i++) {
                ColumnConstraints colConst = new ColumnConstraints();
                colConst.setPercentWidth(100.0 / kernelNumCols);
                kernelRoot.getColumnConstraints().add(colConst);
            }
            for (int i = 0; i < kernelNumRows; i++) {
                RowConstraints rowConst = new RowConstraints();
                rowConst.setPercentHeight(100.0 / kernelNumRows);
                kernelRoot.getRowConstraints().add(rowConst);
            }

            for (int i = 0; i < kernelNumRows; i++) {
                for (int j = 0; j < kernelNumCols; j++) {
                    TextField f = new TextField();
                    kernelFieldList.add(f);
                    kernelRoot.add(kernelFieldList.get(i * kernelNumCols + j), j, i);
                }
            }

            kernelPane.setCenter(kernelRoot);
            kernelButtonApply.setDisable(false);
        }
        //for (TextField file : kernelFieldList) {
        //    System.out.println(file.getText());
        //}
    }

    @FXML
    void applyKernel(ActionEvent event) {
        for (int i = 0; i < kernelNumRows; i++) {
            for (int j = 0; j < kernelNumCols; j++) {
                if (kernelFieldList.get(i * kernelNumCols + j).getText().isEmpty()) {
                    kernelNumbers[i * kernelNumCols + j] = (float) 0.0;
                } else {
                    Float.parseFloat("23.6");
                    kernelNumbers[i * kernelNumCols + j] = Float.parseFloat(kernelFieldList.get(i * kernelNumCols + j).getText());
                }
            }
        }
        System.out.println(Arrays.toString(kernelNumbers));
    }

    @FXML
    void sliderContrast(MouseEvent event) {
        System.out.println(contrastSlide.getValue());
    }

    @FXML
    void setBrightnessButton(ActionEvent event) {
        float i = Float.parseFloat(brightnessTextfield.getText());
        if (i > 10) {
            i = 10;
        } else if (i < -10) {
            i = -10;
        }
        brightnessSlide.setValue(i);
    }

    @FXML
    void setContrastButton(ActionEvent event) {
        float i = Float.parseFloat(contrastTextfield.getText());
        if (i > 10) {
            i = 10;
        } else if (i < -10) {
            i = -10;
        }
        contrastSlide.setValue(i);
    }
    
    @FXML
    void applyBrightness(ActionEvent event) {
        brightnessTextfield.setText(String.valueOf(brightnessSlide.getValue()));
        System.out.println("brillito");
        System.out.println(brightnessSlide.getValue());
    }

    @FXML
    void applyContrast(ActionEvent event) {
        contrastTextfield.setText(String.valueOf(contrastSlide.getValue()));
        System.out.println("contrasteeee");
        System.out.println(contrastSlide.getValue());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        threshold.setItems(optionsThreshold);
        smoothedFilters.setItems(smoothedList);
        borderFilters.setItems(borderList);
        imageMain.setImage(ProjectImages.getInstance().getImageChoose());
    }

}
