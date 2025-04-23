package ORACLE_DBS_PROJECT;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

public class BookingController {

    @FXML private TextField tfTNO;
    @FXML private TextField tfPNO;
    @FXML private TextField tfSOURCE;
    @FXML private TextField tfDEST;
    @FXML private TextField tfDEPT_T;
    @FXML private TextField tfDEPT_A;
    @FXML private TextField tfSEAT_NO;
    @FXML private TextField tfAIRLINE;
    @FXML private ComboBox<String> comboClass;

    @FXML
    private void initialize() {
        // Optional: default value
        comboClass.setValue("Economy");
    }

    @FXML
    private void OnBtnBook(ActionEvent event) throws IOException {
        String tno = tfTNO.getText();
        String pno = tfPNO.getText();
        String source = tfSOURCE.getText();
        String destination = tfDEST.getText();
        String deptTimeStr = tfDEPT_T.getText();
        String arrTimeStr = tfDEPT_A.getText();
        String seatNo = tfSEAT_NO.getText();
        String airline = tfAIRLINE.getText();
        String ticketClass = comboClass.getValue();

        // Parse departure and arrival times
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Timestamp deptTime = null;
        Timestamp arrTime = null;

        try {
            deptTime = new Timestamp(sdf.parse(deptTimeStr).getTime());
            arrTime = new Timestamp(sdf.parse(arrTimeStr).getTime());
        } catch (ParseException e) {
            showAlert("Invalid Date/Time format. Please use 'yyyy-MM-dd HH:mm'.");
            return;
        }

        long milliseconds = arrTime.getTime() - deptTime.getTime();
        int duration = (int) (milliseconds / (1000 * 60));

        if (duration <= 0) {
            showAlert("Arrival time must be after departure time.");
            return;
        }

        // Submit information as entry to database (Book the ticket)
        TicketBookingService bookingService = new TicketBookingService();
        double price = TicketBookingService.calculatePrice(duration, ticketClass);
        bookingService.bookTicket(pno, tno, source, destination, deptTime, arrTime, ticketClass, seatNo, price, airline);

        showAlert("Ticket Booked!\nTicket No: " + tno + "\nClass: " + ticketClass);
        
        // Navigate to the main scene after booking
        Parent root = FXMLLoader.load(getClass().getResource("mainscene.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

class TicketBookingService {
    public static double calculatePrice(int duration, String travelClass) {
        double baseRate = 0.5;
        double classMultiplier;
    
        switch (travelClass.toLowerCase()) {
            case "economy":
                classMultiplier = 1.0;
                break;
            case "business":
                classMultiplier = 1.5;
                break;
            case "first":
                classMultiplier = 2.0;
                break;
            default:
                throw new IllegalArgumentException("Invalid class: " + travelClass);
        }
    
        return baseRate * duration * classMultiplier;
    }
    

    public void bookTicket(String passportNo, String ticketNumber, String source, String destination, 
                           Timestamp departureTime, Timestamp arrivalTime, String flightClass, 
                           String seatNumber, double price, String airlineName) {
        String procedureCall = "{ call Book_Ticket(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        
        try (Connection conn = DatabaseUtil.getConnection();
             CallableStatement stmt = conn.prepareCall(procedureCall)) {
            
            stmt.setString(1, passportNo);
            stmt.setString(2, ticketNumber);
            stmt.setString(3, source);
            stmt.setString(4, destination);
            stmt.setTimestamp(5, departureTime);
            stmt.setTimestamp(6, arrivalTime);
            stmt.setString(7, flightClass);
            stmt.setString(8, seatNumber);
            stmt.setDouble(9, price);
            stmt.setString(10, airlineName);
            
            stmt.executeUpdate();
            System.out.println("Ticket booked successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
