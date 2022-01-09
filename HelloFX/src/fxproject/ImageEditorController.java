/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package fxproject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
    void BlackWhiteFilter(ActionEvent event) {
        System.out.println("BlackWhiteFilter here");
        //imageMain.setImage([**insertar imagen de tipo Image**]);
    }

    @FXML
    void GrayScaleFilter(ActionEvent event) {
        System.out.println("GrayScaleFilter here");
        //imageMain.setImage([**insertar imagen de tipo Image**]);
    }

    @FXML
    void negativeFilter(ActionEvent event) {
        System.out.println("negativeFilter here");
        //imageMain.setImage([**insertar imagen de tipo Image**]);
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
        if(!thresholdVal2.getText().isEmpty()){
            max = Integer.parseInt(thresholdVal2.getText());
        }
        if ("Rango".equals(threshold.getValue()) && min >= 0 && min < 255
                && max > min && max > 0 && max <= 255) {
            thresholdButton.setDisable(false);
        } else if("Valor constante".equals(threshold.getValue()) && min >= 0 && min <= 255 ){
            thresholdButton.setDisable(false);
        }else{
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
    }

    @FXML
    void histogramView(ActionEvent event) throws IOException {
        ProjectImages.getInstance().showBarChart();
    }

    @FXML
    void informationView(ActionEvent event) throws IOException {
        ProjectImages.getInstance().showDetails();
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
