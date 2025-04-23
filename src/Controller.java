


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class Controller {

    @FXML
    private TextField tfRollno;

    @FXML
    private TextField tfName;

    @SuppressWarnings("unused")
	private Stage mainWindow;
    
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521/orclpdb";
    private static final String DB_USER = "SYSTEM";
    private static final String DB_PASSWORD = "Oracle123";

    // @FXML
    // public void OnBtn(ActionEvent event) {
    //     String title = tfTitle.getText();
    //     mainWindow.setTitle(title);
    //     // You can add DB logic here later
    // }

    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;     
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void InsertStudent(ActionEvent event) {
        String roll = tfRollno.getText();
        String name = tfName.getText();

        if (roll.isEmpty() || name.isEmpty()) {
            showAlert("Please enter both Roll Number and Name.");
            return;
        }

        try {
            int Rno = Integer.parseInt(roll);

            Class.forName("oracle.jdbc.OracleDriver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = "INSERT INTO student (rollno, name) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Rno);
            stmt.setString(2, name);

            stmt.executeUpdate();
            showAlert("Student inserted successfully!");

            conn.close();

        } catch (NumberFormatException e) {
            showAlert("Roll Number must be a number.");
        } catch (SQLIntegrityConstraintViolationException e) {
            showAlert("Roll Number already exists.");
        } catch (ClassNotFoundException e) {
            System.err.println("Oracle JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database error: " + e.getMessage());
        } 
    }

}
