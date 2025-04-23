package ORACLE_DBS_PROJECT;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterController {

    @FXML private TextField tfPNO;
    @FXML private TextField tfName;
    @FXML private TextField tfAddress;
    @FXML private TextField tfSex;
    @FXML private TextField tfDOB;
    @FXML private TextField tfAge;
    @FXML private TextField tfPhoneNo;
    @FXML private Button registerButton;

    private Connection conn;

    // Initialize method to setup connection (using DatabaseUtil)
    public void initialize() {
        conn = DatabaseUtil.getConnection();
    }

    @FXML
    public void OnBtnRegister(ActionEvent event) {
        // Get data from fields
        String passportNo = tfPNO.getText().trim();
        String name = tfName.getText().trim();
        String address = tfAddress.getText().trim();
        String sex = tfSex.getText().trim().toUpperCase();
        String dobStr = tfDOB.getText().trim();
        String ageStr = tfAge.getText().trim();
        String phoneNo = tfPhoneNo.getText().trim();

        // Validate input fields
        if (passportNo.isEmpty() || name.isEmpty() || address.isEmpty() || sex.isEmpty() || dobStr.isEmpty() || ageStr.isEmpty() || phoneNo.isEmpty()) {
            showAlert("All fields must be filled.");
            return;
        }

        // Validate sex (it should be 'M' or 'F')
        if (!sex.equals("M") && !sex.equals("F")) {
            showAlert("Sex should be either 'M' or 'F'.");
            return;
        }

        // Validate age (must be a number)
        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            showAlert("Age must be a number.");
            return;
        }

        // Validate Date of Birth format (MM-DD-YYYY)
        Date dob = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dob = dateFormat.parse(dobStr);
        } catch (Exception e) {
            showAlert("Date of birth should be in the format 'YYYY-MM-DD'.");
            return;
        }

        // Call the stored procedure to register the passenger
        try {
            CallableStatement stmt = conn.prepareCall("{call Register_Passenger(?, ?, ?, ?, ?, ?, ?)}");
            stmt.setString(1, passportNo);
            stmt.setString(2, name);
            stmt.setString(3, address);
            stmt.setString(4, sex);
            stmt.setDate(5, new java.sql.Date(dob.getTime()));
            stmt.setInt(6, age);
            stmt.setString(7, phoneNo);

            // Execute the procedure
            stmt.execute();
            stmt.close();

            // Show success message
            showAlert("Passenger registered successfully.");
            
            // Clear the input fields
            clearFields();

            // Switch back to the main scene after successful registration
            switchToMainScene(event);

        } catch (SQLException e) {
            showAlert("Error occurred while registering passenger: " + e.getMessage());
        }
    }

    // Helper method to show alerts
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // Helper method to clear the input fields
    private void clearFields() {
        tfPNO.clear();
        tfName.clear();
        tfAddress.clear();
        tfSex.clear();
        tfDOB.clear();
        tfAge.clear();
        tfPhoneNo.clear();
    }

    // Method to switch to the main scene after registration
    private void switchToMainScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Failed to load the main scene: " + e.getMessage());
        }
    }
}

