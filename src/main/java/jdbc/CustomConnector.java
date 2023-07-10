package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CustomConnector {
    public Connection getConnection(String url) {
        Connection c;
        try {
            c = DriverManager.getConnection(url);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return c;
    }

    public Connection getConnection(String url, String user, String password)  {
        Connection c;
        try {
            c = DriverManager.getConnection(url,user, password);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return c;
    }


}
