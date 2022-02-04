package fxproject;

import java.io.File;
import java.io.FileInputStream;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Controller implements Initializable{
    
    @FXML
    private MenuItem btnCl;

    @FXML
    private ImageView imageView1;

    @FXML
    private ImageView imageView2;

    @FXML
    private Label lab;


    @FXML
    void buttonClickMe(ActionEvent event) {
        Image image = new Image(getClass().getResourceAsStream("../images/StarWars.png"));
        imageView1.setImage(image);
        imageView2.setImage(image);
        System.out.println(imageView1);
        lab.setText("Holi");
    }
    
    @FXML
    void multipleFileChooser(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new ExtensionFilter("PDF Files", "*.pdf"));
        List<File> f = fc.showOpenMultipleDialog(null);
        for (File file: f){
            System.out.println(file.getAbsolutePath());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}