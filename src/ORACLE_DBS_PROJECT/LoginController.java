package ORACLE_DBS_PROJECT;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private TextField tfUser;
    @FXML
    private PasswordField tfPwd;

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
        String username = tfUser.getText();
        String password = tfPwd.getText();

        // Basic check (you can later connect to a DB if needed)
        if (username.equals("SYSADMIN") && password.equals("0000")) {
            // Load main menu
            Parent root = FXMLLoader.load(getClass().getResource("mainscene.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            showAlert("Successful login");
        } else {
            // Invalid login logic here
            showAlert("Invalid credentials");
        }
    }
}
