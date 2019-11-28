package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class splash implements Initializable {

    @FXML
    private AnchorPane root;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("alo");

        new SplashScreen().start();
        System.out.println("alo");


    }
    class SplashScreen extends Thread
    {
        @Override
        public void run()
        {
            try {
                Thread.sleep(3000);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        Parent parent = null;
                        try {
                            parent = FXMLLoader.load(getClass().getResource("v.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Stage stage=new Stage();
                        stage.setTitle("Agoha");
                        stage.setScene(new Scene(parent));
                        stage.show();
                        root.getScene().getWindow().hide();

                    }
                });

            }catch (Exception e)
            {

            }
        }
    }
}
