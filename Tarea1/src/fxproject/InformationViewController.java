/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject;

import fxproject.models.RawImage;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
        int w = img.width, h = img.height;
        dimensions.setText(Integer.toString(w) + " x " + Integer.toString(h) + " pixeles");
        
        int uniqueColor = img.countDifferentColors();
        int maxColors = img.type == RawImage.Type.RGB ? img.colorMax * img.colorMax * img.colorMax : img.colorMax;
        colors.setText(Integer.toString(uniqueColor) + " colores unicos de " + maxColors + " disponibles");
        
        int screenSize = Toolkit.getDefaultToolkit().getScreenResolution();
        dpi.setText(Integer.toString(screenSize) + " dots / ich");
        
        
        bitsxpixel.setText(Integer.toString(img.bpp) + " bit");
    }
}
