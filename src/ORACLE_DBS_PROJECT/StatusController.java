package ORACLE_DBS_PROJECT;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

public class StatusController {

    @FXML
    private TextField tfTNO;

    @FXML
    private void OnBtnStatusChk(ActionEvent event) throws IOException {
        String ticketNumber = tfTNO.getText().trim();

        if (ticketNumber.isEmpty()) {
            showAlert("Please enter a ticket number.");
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection()) {
            if (conn == null) {
                showAlert("Database connection failed.");
                return;
            }

            String sql = "SELECT source, destination, departure_time, arrival_time FROM Ticket WHERE ticket_number = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ticketNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String source = rs.getString("source");
                String destination = rs.getString("destination");
                Timestamp departure = rs.getTimestamp("departure_time");
                Timestamp arrival = rs.getTimestamp("arrival_time");

                String msg = "Booking Found: " + ticketNumber +
                             "\nFrom: " + source +
                             "\nTo: " + destination +
                             "\nDeparture: " + departure.toString() +
                             "\nArrival: " + arrival.toString();
                showAlert(msg);
            } else {
                showAlert("No booking found with Ticket Number: " + ticketNumber);
            }

        } catch (SQLException e) {
            showAlert("Error fetching ticket status: " + e.getMessage());
            e.printStackTrace();
        }

        // Return to main menu
        Parent root = FXMLLoader.load(getClass().getResource("mainscene.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ticket Status");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
