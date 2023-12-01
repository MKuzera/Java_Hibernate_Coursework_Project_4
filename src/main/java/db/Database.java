package db;
import java.sql.*;
public class Database {
    static final String DB_URL = "jdbc:mysql://localhost:3306/bazaprobna";
    static final String USER = "root";
    static final String PASS = "";
    static final String QUERY = "SELECT id, first, last FROM employees";

    public static void init() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Załaduj sterownik
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);) {
            // Extract data from result set
            while (rs.next()) {
                // Retrieve by column name
                System.out.print("ID: " + rs.getInt("id"));
                System.out.print(", First: " + rs.getString("first"));
                System.out.println(", Last: " + rs.getString("last"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


//        try {
//            Class.forName("jdbc:mysql://localhost:3306/bazaProbna");
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }

    }

}
