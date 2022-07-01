package helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    public static Connection getConnection(){
        Connection connection = null;
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connecting..");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/praktikum_java_proyek",
                    "root",
                    "");
            System.out.println("Connected!");
        } catch (ClassNotFoundException e) {
            System.out.println("Connection error!");
        } catch (SQLException e) {
            System.out.println("SQL error!");
        }
        return connection;
    }
}
