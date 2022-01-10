/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject;

import fxproject.models.RawImage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

/**
 *
 * @author Gaby
 */
public class InformationViewController implements Initializable {
    
    @FXML
    private Text bitsxpixel;

    @FXML
    private Text colors;

    @FXML
    private Text dimensions;

    @FXML
    private Text dpi;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        RawImage img = ProjectImages.getInstance().getChoose();
        // TODO
        dimensions.setText(Integer.toString(img.width) + " x " + Integer.toString(img.height));
        colors.setText(Integer.toString(img.colorMax));
        dpi.setText(Integer.toString(img.dpi));
    }
}
