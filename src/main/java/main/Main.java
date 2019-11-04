package main;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    private static String URL = "jdbc:mysql://localhost:3306/test";
    private static String USERNAME = "root";
    private static String PASSWORD = "8891";
    private static String DRIVER = "com.mysql.jdbc.Driver";
    public static void main(String[] args) {

        try {
            Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
