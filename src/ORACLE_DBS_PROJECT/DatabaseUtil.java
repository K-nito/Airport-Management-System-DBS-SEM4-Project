package ORACLE_DBS_PROJECT;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/orclpdb";
    private static final String USER = "SYSTEM";
    private static final String PASSWORD = "Oracle123";
    
    public static Connection getConnection() {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Oracle JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return null;
    }
}
