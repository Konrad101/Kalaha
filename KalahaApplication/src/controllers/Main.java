package controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/Menu.fxml"));
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("/resources/prototype.png"));
        primaryStage.setTitle("Kalaha");
        primaryStage.setScene(new Scene(root, 700, 480));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
