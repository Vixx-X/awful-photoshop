package fxproject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javax.imageio.ImageIO;

public class Controller implements Initializable {

    @FXML
    private MenuItem btnCl1;

    @FXML
    private ImageView imageView1;

    @FXML
    private ImageView imageView2;

    @FXML
    private Label labelImage1;

    @FXML
    private Label labelImage2;
    
    @FXML
    private Label mainLabel;

    @FXML
    void buttonClickMe(ActionEvent event) throws FileNotFoundException {
        //Image image = new Image(getClass().getResourceAsStream("../images/StarWars.png"));
        FileChooser fc = new FileChooser();
        //fc.getExtensionFilters().add(new ExtensionFilter("*.pbm", "*.pgm", "*.bmp", "*.ppm", "*.pnm", "*.dib"));
        List<File> f = fc.showOpenMultipleDialog(null);
        Image[] images = new Image[2];
        String[] names = new String[2];
        int i = 0;
        for (File file : f) {
            names[i] = file.getName();
            images[i] = new Image(new FileInputStream(file.getAbsolutePath()));
            i++;
        }
        //System.out.println(file.getAbsolutePath());
        if (i > 0) {
            mainLabel.setText("Escoga una Imagen para editar");
            labelImage1.setText(names[0]);
            imageView1.setImage(images[0]);
        }
        if (i > 1) {
            labelImage2.setText(names[1]);
            imageView2.setImage(images[1]);
        }

        System.out.println(imageView1);
        imageView1.setOnMouseClicked(e -> {
            String clickedImgUrl = (String) ((ImageView) e.getSource()).getUserData();
            System.out.println("Image was clicked: 1");
        });
        
        imageView2.setOnMouseClicked(e -> {
            String clickedImgUrl = (String) ((ImageView) e.getSource()).getUserData();
            System.out.println("Image was clicked: 2");
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
