package ORACLE_DBS_PROJECT;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CancelController {

    @FXML
    private TextField ticketNumberField;

    @FXML
    private void OnBtnCancel(ActionEvent event) throws IOException {
        String ticketNumber = ticketNumberField.getText().trim();

        if (ticketNumber.isEmpty()) {
            showAlert("Please enter a ticket number to cancel.");
            return;
        }

        // Database call to cancel the ticket
        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                showAlert("Failed to connect to the database.");
                return;
            }

            String checkQuery = "SELECT COUNT(*) FROM Ticket WHERE ticket_number = ?";
            try (var ps = conn.prepareStatement(checkQuery)) {
                ps.setString(1, ticketNumber);
                var rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    showAlert("No ticket found with number: " + ticketNumber);
                    Parent root = FXMLLoader.load(getClass().getResource("mainscene.fxml"));
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                    return;
                }
            }

            CallableStatement stmt = conn.prepareCall("{call Cancel_Ticket(?)}");
            stmt.setString(1, ticketNumber);
            stmt.execute();

            showAlert("Ticket " + ticketNumber + " has been successfully canceled.");

        } catch (SQLException e) {
            showAlert("Error cancelling ticket: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Navigate back to main scene
        Parent root = FXMLLoader.load(getClass().getResource("mainscene.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cancel Ticket");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
