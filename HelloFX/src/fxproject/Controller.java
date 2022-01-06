package fxproject;

import java.io.FileInputStream;
import java.io.InputStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Controller {
    
    @FXML
    private MenuItem btnCl;
    
    @FXML
    private Label lab;
 
    @FXML
    private ImageView imageView1;

    @FXML
    private ImageView imageView2;


    @FXML
    void buttonClickMe(ActionEvent event) {
        Image image = new Image(getClass().getResourceAsStream("../images/StarWars.png"));
        //imageView1.setImage(image);
        //InputStream stream = new FileInputStream("C:\\Users\\Gaby\\Pictures\\StarWars.png");
        //Image image = new Image(stream);
        System.out.println(image);
        lab.setText("Clickeadito el boton");
    }
}