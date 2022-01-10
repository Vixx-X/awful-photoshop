/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject;

import fxproject.models.RawImage.Type;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class BarChartController implements Initializable {
    
    @FXML
    private BarChart histogram;
    
    @FXML
    private Label labelHistogram;
    
    private int[] dataHistogram;
    private final int typeHistogram;
    
    public BarChartController(){
        this.typeHistogram = ProjectImages.getInstance().i;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        switch(typeHistogram){
            case 3 -> {
                dataHistogram = ProjectImages.getInstance().getChoose().getBlueHistogram();
                labelHistogram.setText("Histograma del Canal Azul");
            }
            case 2 -> {
                dataHistogram = ProjectImages.getInstance().getChoose().getGreenHistogram();
                labelHistogram.setText("Histograma del Canal Verde");
            }
            case 1 -> {
                dataHistogram = ProjectImages.getInstance().getChoose().getRedHistogram();
                labelHistogram.setText("Histograma del Canal Rojo");
            }
            case 0 -> dataHistogram = ProjectImages.getInstance().getChoose().getGrayHistogram();
            default -> dataHistogram = ProjectImages.getInstance().getChoose().getGrayHistogram();
        }
        
        
        //System.out.println("el histograma es" + Arrays.toString(dataHistogram));
        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.getData().add(new XYChart.Data("0", dataHistogram[0]));
        //dataSeries1.setName("2014");
        for(int i=1; i<dataHistogram.length -1; i++){
            dataSeries1.getData().add(new XYChart.Data(String.valueOf(i), dataHistogram[i]));
        }
        dataSeries1.getData().add(new XYChart.Data(String.valueOf(dataHistogram.length-1), dataHistogram[dataHistogram.length-1]));
        
        histogram.getData().add(dataSeries1);
    }
}
