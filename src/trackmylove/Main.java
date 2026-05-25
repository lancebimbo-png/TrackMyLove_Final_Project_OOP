package trackmylove;

import tml_db.DatabaseConnection;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            System.out.println("Connected to TML database!");
        } else {
            System.out.println("Connection failed.");
        }
    }
}