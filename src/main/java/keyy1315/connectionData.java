package keyy1315;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectionData {
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        String url = "jdbc:mysql://192.168.0.10:3306/hr";
        String id = "root";
        String pw = "cocolabhub";

        Connection conn = DriverManager.getConnection(url,id,pw);
        return conn;
    }
}
