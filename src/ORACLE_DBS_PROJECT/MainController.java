package ORACLE_DBS_PROJECT;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

public class MainController {

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    public void handleBooking(ActionEvent event) throws IOException {
        switchScene(event, "bookscene.fxml");
    }

    @FXML
    public void handleCanceling(ActionEvent event) throws IOException {
        switchScene(event, "cancelscene.fxml");
    }

    @FXML
    public void handleReschedule(ActionEvent event) throws IOException {
        switchScene(event, "reschedulescene.fxml");
    }

    @FXML
    public void handleStatusCheck(ActionEvent event) throws IOException {
        switchScene(event, "statusscene.fxml");
    }

    @FXML
    public void handleLogOut(ActionEvent event) throws IOException {
        switchScene(event, "LoginScene.fxml");
        showAlert("Successfully logged out");
    }

    @FXML
    public void handleRegister(ActionEvent event) throws IOException {
        switchScene(event, "RegisterScene.fxml");
    }

    private void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}