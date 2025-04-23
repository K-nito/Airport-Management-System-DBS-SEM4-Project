package ORACLE_DBS_PROJECT;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SuppressWarnings("unused")
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ORACLE_DBS_PROJECT/LoginScene.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("AIRPORT DATABASE MANAGER");
        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
