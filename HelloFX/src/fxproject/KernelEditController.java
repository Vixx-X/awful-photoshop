/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class KernelEditController implements Initializable {

    @FXML
    private TextField columns;

    @FXML
    private TextField rows;

    @FXML
    private BorderPane kernelPane;
    
    @FXML
    private Button buttonApply;

    private final ArrayList<TextField> fieldList = new ArrayList<>();
    //private final ArrayList<Integer> kernelNumbers = new ArrayList<>();
    public float[] kernelNumbers;

    private int numCols; 
    private int numRows;
    private final GridPane root = new GridPane();

    @FXML
    void createGridPane(ActionEvent event) {
        if (!(columns.getText().isEmpty())) {
            numCols = Integer.parseInt(columns.getText());
        }
        if (!(rows.getText().isEmpty())) {
            numRows = Integer.parseInt(rows.getText());
        }
        if (!(columns.getText().isEmpty()) && !(rows.getText().isEmpty())) {
            kernelNumbers = new float[numCols*numRows];
            root.setGridLinesVisible(true);
            for (int i = 0; i < numCols; i++) {
                ColumnConstraints colConst = new ColumnConstraints();
                colConst.setPercentWidth(100.0 / numCols);
                root.getColumnConstraints().add(colConst);
            }
            for (int i = 0; i < numRows; i++) {
                RowConstraints rowConst = new RowConstraints();
                rowConst.setPercentHeight(100.0 / numRows);
                root.getRowConstraints().add(rowConst);
            }

            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    TextField f = new TextField();
                    fieldList.add(f);
                    root.add(fieldList.get(i * numCols + j), j, i);
                }
            }

            kernelPane.setCenter(root);
            buttonApply.setDisable(false);
        }
        //for (TextField file : fieldList) {
        //    System.out.println(file.getText());
        //}
    }

    @FXML
    void applyKernel(ActionEvent event) {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (fieldList.get(i * numCols + j).getText().isEmpty()) {
                    kernelNumbers[i* numCols + j] = (float) 0.0;
                } else {
                    Float.parseFloat("23.6");
                    kernelNumbers[i* numCols + j] = Float.parseFloat(fieldList.get(i * numCols + j).getText());
                }
            }
        }
        System.out.println(Arrays.toString(kernelNumbers));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //TODO
    }
}
