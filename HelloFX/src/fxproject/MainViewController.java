package fxproject;

//import java.awt.image.BufferedImage;
import java.io.File;
//import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
//import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
//import javafx.stage.FileChooser.ExtensionFilter;
//import javax.imageio.ImageIO;

public class MainViewController implements Initializable {

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
	void uploadImages(ActionEvent event) throws FileNotFoundException {
		//Image image = new Image(getClass().getResourceAsStream("../images/StarWars.png"));
		FileChooser fc = new FileChooser();
		//fc.getExtensionFilters().add(new ExtensionFilter("*.pbm", "*.pgm", "*.bmp", "*.ppm", "*.pnm", "*.dib"));
		List<File> f = fc.showOpenMultipleDialog(null);
		RawImage[] images = new RawImage[2];
		String[] names = new String[2];
		int i = 0;
		for (File file : f) {
			names[i] = file.getName();
			images[i] = new RawImage();
			images[i].readImage(file.getAbsolutePath());
			i++;
		}
		showImages(i, images, names);

	}

	void showImages(int i, RawImage[] images, String[] names){
		if (i > 0) {
			mainLabel.setText("Escoga una Imagen para editar");
			labelImage1.setText(names[0]);
                        Image img = images[0].getImage();
			imageView1.setImage(img);
                        System.out.println(img.getWidth() + " " + img.getHeight());
                        imageView1.setSmooth(false);
                       
		}
		if (i > 1) {
			labelImage2.setText(names[1]);
			imageView2.setImage(images[1].getImage());
                        imageView2.setSmooth(false);
		}

		System.out.println(imageView1);
		imageView1.setOnMouseClicked(e -> {
			//String clickedImgUrl = (String) ((ImageView) e.getSource()).getUserData();
			System.out.println("Image was clicked: 1");
			//System.out.println(mainClass);
			try {
				ProjectImages.getInstance().showImagePanel(images[0]);
			} catch (IOException ex) {
				Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
			}
		});

		imageView2.setOnMouseClicked(e -> {
			try {
				ProjectImages.getInstance().showImagePanel(images[1]);
			} catch (IOException ex) {
				Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
			}
		});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}
}
