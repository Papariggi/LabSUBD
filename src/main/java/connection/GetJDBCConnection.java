package connection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class GetJDBCConnection {
    private static volatile Connection connection;
    private static String URL = "jdbc:mysql://localhost/test";
    private static String USERNAME = "root";
    private static String PASSWORD = "8891";
    private static String DRIVER = "com.mysql.jdbc.Driver";


    private static Connection getDatabaseConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    @Before
    public static Connection init() throws SQLException {
        return connection = getDatabaseConnection();
    }

    @After
    public static void close() throws SQLException{
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    public void shouldGetDatabaseConnection() throws SQLException{
        try(Connection connection = getDatabaseConnection()) {
            Assert.assertTrue(connection.isValid(1));
            Assert.assertFalse(connection.isClosed());
        }
    }
}
