package ORACLE_DBS_PROJECT;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RescheduleController {

    @FXML private TextField tfTNO;
    @FXML private TextField tfARR_TIME;
    @FXML private TextField tfDEPT_TIME;

    @FXML
    private void OnBtnReschedule(ActionEvent event) throws IOException {
        String ticketNumber = tfTNO.getText().trim();
        String newArrival = tfARR_TIME.getText().trim();
        String newDeparture = tfDEPT_TIME.getText().trim();

        if (ticketNumber.isEmpty() || newArrival.isEmpty() || newDeparture.isEmpty()) {
            showAlert("Please fill all fields.");
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                showAlert("Failed to connect to the database.");
                return;
            }

            // Convert string to TIMESTAMP
            Timestamp newArrivalTs = Timestamp.valueOf(newArrival);
            Timestamp newDepartureTs = Timestamp.valueOf(newDeparture);

            CallableStatement stmt = conn.prepareCall("{call Reschedule_Ticket(?, ?, ?)}");
            stmt.setString(1, ticketNumber);
            stmt.setTimestamp(2, newDepartureTs);
            stmt.setTimestamp(3, newArrivalTs);
            stmt.execute();

            showAlert("Ticket " + ticketNumber + " has been rescheduled to:\nArrival: " + newArrival + "\nDeparture: " + newDeparture);

        } catch (IllegalArgumentException e) {
            showAlert("Invalid datetime format. Please use format: yyyy-mm-dd hh:mm:ss");
        } catch (SQLException e) {
            showAlert("Error rescheduling ticket: " + e.getMessage());
            e.printStackTrace();
        }

        // Navigate back to main scene
        Parent root = FXMLLoader.load(getClass().getResource("mainscene.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reschedule Ticket");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
