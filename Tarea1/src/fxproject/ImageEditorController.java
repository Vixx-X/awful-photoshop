/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fxproject;

import fxproject.filters.locals.SobelFilter;
import fxproject.models.RawImage;
import fxproject.filters.globals.BlackWhiteFilter;
import fxproject.filters.globals.ContrastFilter;
import fxproject.filters.globals.EcualiceFilter;
import fxproject.filters.globals.GammaFilter;
import fxproject.filters.globals.GrayScaleFilter;
import fxproject.filters.globals.NegativeFilter;
import fxproject.filters.globals.SumFilter;
import fxproject.filters.globals.ThresholdFilter;
import java.io.File;
import fxproject.filters.locals.CircleFilter;
import fxproject.filters.locals.GaussFilter;
import fxproject.filters.locals.KernelFilter;
import fxproject.filters.locals.PrewittFilter;
import fxproject.filters.locals.ProfileFilter;
import fxproject.filters.locals.RobertsFilter;
import fxproject.filters.locals.SquareFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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

    ObservableList<String> smoothedList = FXCollections.observableArrayList("Caja", "Cil??ndrico", "Gauss");
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
    private Slider brightnessSlide;

    @FXML
    private TextField brightnessTextfield;

    @FXML
    private Slider contrastSlide;

    @FXML
    private TextField contrastTextfield;

    @FXML
    private Slider gammaSlide;

    @FXML
    private TextField gammaTextfield;

    private final ArrayList<TextField> kernelFieldList = new ArrayList<>();
    //private final ArrayList<Integer> kernelNumbers = new ArrayList<>();
    public float[] kernelNumbers;

    private int kernelNumCols;
    private int kernelNumRows;
    private GridPane kernelRoot = new GridPane();

    ImagePortion getPair() {
        RawImage choose = ProjectImages.getInstance().getChoose();
        ImagePortion temp = new ImagePortion(0, 0, choose.width - 1, choose.height - 1);
        if (imagePortionBool.isSelected()) {
            temp = obtainDataPortion();
        }
        return temp;
    }

    @FXML
    void BlackWhiteFilter(ActionEvent event) {
        RawImage choose = ProjectImages.getInstance().getChoose();
        ImagePortion temp = getPair();
        RawImage img = BlackWhiteFilter.apply(choose, temp.x1, temp.y1, temp.x2, temp.y2);
        imageMain.setImage(img.getImage());
        ProjectImages.getInstance().pushImage(img);
        enableToolsButtons();
    }

    @FXML
    void GrayScaleFilter(ActionEvent event) {
        RawImage choose = ProjectImages.getInstance().getChoose();
        ImagePortion temp = getPair();
        RawImage img = GrayScaleFilter.apply(choose, temp.x1, temp.y1, temp.x2, temp.y2);
        imageMain.setImage(img.getImage());
        ProjectImages.getInstance().pushImage(img);
        enableToolsButtons();
    }

    @FXML
    void negativeFilter(ActionEvent event) {
        RawImage choose = ProjectImages.getInstance().getChoose();
        ImagePortion temp = getPair();
        RawImage img = NegativeFilter.apply(choose, temp.x1, temp.y1, temp.x2, temp.y2);
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

        RawImage choose = ProjectImages.getInstance().getChoose();

        String type = smoothedFilters.getValue();
        RawImage img = null;
        ImagePortion temp = getPair();

        switch (type) {
            case "Caja" ->
                img = SquareFilter.apply(choose, numberCol, numberRow,
                        temp.x1, temp.y1, temp.x2, temp.y2);
            case "Cil??ndrico" ->
                img = CircleFilter.apply(choose, numberCol, numberRow,
                        temp.x1, temp.y1, temp.x2, temp.y2);
            case "Gauss" ->
                img = GaussFilter.apply(choose, numberCol, numberRow,
                        temp.x1, temp.y1, temp.x2, temp.y2);
            default -> {
                return;
            }
        }
        if (img == null) {
            return;
        }

        ProjectImages.getInstance().pushImage(img);
        enableToolsButtons();
        imageMain.setImage(img.getImage());
    }

    @FXML
    void applyBorder(ActionEvent event) {
        int numberCol = Integer.parseInt(columnsBorder.getText());
        int numberRow = Integer.parseInt(rowsBorder.getText());

        RawImage choose = ProjectImages.getInstance().getChoose();

        String type = borderFilters.getValue();
        RawImage img = null;
        ImagePortion temp = getPair();

        switch (type) {
            case "Sobel" ->
                img = SobelFilter.apply(choose, numberCol, numberRow,
                        temp.x1, temp.y1, temp.x2, temp.y2);
            case "Roberts" ->
                img = RobertsFilter.apply(choose, numberCol, numberRow,
                        temp.x1, temp.y1, temp.x2, temp.y2);
            case "Prewitt" ->
                img = PrewittFilter.apply(choose, numberCol, numberRow,
                        temp.x1, temp.y1, temp.x2, temp.y2);
            case "Perfilado" ->
                img = ProfileFilter.apply(choose, numberCol, numberRow,
                        temp.x1, temp.y1, temp.x2, temp.y2);
            default -> {
                return;
            }
        }
        if (img == null) {
            return;
        }

        ProjectImages.getInstance().pushImage(img);
        enableToolsButtons();
        imageMain.setImage(img.getImage());

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
        RawImage choose = ProjectImages.getInstance().getChoose();
        RawImage img = null;

        int min = Integer.parseInt(thresholdVal1.getText());
        ImagePortion temp = getPair();
        if ("Rango".equals(threshold.getValue())) {
            int max = Integer.parseInt(thresholdVal2.getText());
            img = ThresholdFilter.apply(choose, min, max, temp.x1, temp.y1, temp.x2, temp.y2);
        } else {
            img = ThresholdFilter.apply(choose, min, temp.x1, temp.y1, temp.x2, temp.y2);
        }

        ProjectImages.getInstance().pushImage(img);
        enableToolsButtons();
        imageMain.setImage(img.getImage());
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
    }

    @FXML
    void saveAsAction(ActionEvent event) {
        RawImage image = ProjectImages.getInstance().getChoose();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));

        File f = fileChooser.showSaveDialog(null);

        if (image != null && f != null) {
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
        RawImage image = ProjectImages.getInstance().undo();
        if (image != null) {
            imageMain.setImage(image.getImage());
        }
        enableToolsButtons();
    }

    @FXML
    void redoAction(ActionEvent event) {
        RawImage image = ProjectImages.getInstance().redo();
        if (image != null) {
            imageMain.setImage(image.getImage());
        }
        enableToolsButtons();
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

        RawImage choose = ProjectImages.getInstance().getChoose();
        ImagePortion temp = getPair();
        RawImage img = KernelFilter.apply(choose, kernelNumbers, kernelNumCols, kernelNumRows, temp.x1, temp.y1, temp.x2, temp.y2, false);

        ProjectImages.getInstance().pushImage(img);
        enableToolsButtons();
        imageMain.setImage(img.getImage());
    }

    float limitsValue(float i, float limit) {
        if (i > limit) {
            i = limit;
        } else if (i < -limit) {
            i = -limit;
        }
        return i;
    }

    @FXML
    void setBrightnessButton(ActionEvent event) {
        float i = Float.parseFloat(brightnessTextfield.getText());
        brightnessSlide.setValue(limitsValue(i, 10));
    }

    @FXML
    void setContrastButton(ActionEvent event) {
        float i = Float.parseFloat(contrastTextfield.getText());
        contrastSlide.setValue(limitsValue(i, 10));
    }

    @FXML
    void setGamma(ActionEvent event) {
        float i = Float.parseFloat(gammaTextfield.getText());
        gammaSlide.setValue(limitsValue(i, 5));
    }

    @FXML
    void applyBrightness(ActionEvent event) {
        brightnessTextfield.setText(String.valueOf(brightnessSlide.getValue()));
        float gamma = (float) brightnessSlide.getValue();
        RawImage choose = ProjectImages.getInstance().getChoose();
        ImagePortion temp = getPair();
        RawImage img = SumFilter.apply(choose, gamma, temp.x1, temp.y1, temp.x2, temp.y2);

        ProjectImages.getInstance().pushImage(img);
        enableToolsButtons();
        imageMain.setImage(img.getImage());

    }

    @FXML
    void applyContrast(ActionEvent event) {
        contrastTextfield.setText(String.valueOf(contrastSlide.getValue()));
        float beta = (float) contrastSlide.getValue();
        RawImage choose = ProjectImages.getInstance().getChoose();
        ImagePortion temp = getPair();
        RawImage img = ContrastFilter.apply(choose, beta, temp.x1, temp.y1, temp.x2, temp.y2);

        ProjectImages.getInstance().pushImage(img);
        enableToolsButtons();
        imageMain.setImage(img.getImage());
    }

    @FXML
    void applyGamma(ActionEvent event) {
        gammaTextfield.setText(String.valueOf(gammaSlide.getValue()));
        float gamma = (float) gammaSlide.getValue();
        RawImage choose = ProjectImages.getInstance().getChoose();
        ImagePortion temp = getPair();
        RawImage img = GammaFilter.apply(choose, gamma, temp.x1, temp.y1, temp.x2, temp.y2);

        ProjectImages.getInstance().pushImage(img);
        enableToolsButtons();
        imageMain.setImage(img.getImage());

    }

    @FXML
    void applyEqualize(ActionEvent event) {
        RawImage choose = ProjectImages.getInstance().getChoose();
        ImagePortion temp = getPair();
        RawImage img = EcualiceFilter.apply(choose, temp.x1, temp.y1, temp.x2, temp.y2);
        ProjectImages.getInstance().pushImage(img);
        enableToolsButtons();
        imageMain.setImage(img.getImage());
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
