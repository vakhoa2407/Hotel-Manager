/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package connectDatabase;

import java.sql.*;
import java.sql.SQLException;

/**
 * /**
 *
 * @author DELL
 */
public class ConnectDB {

    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost;encrypt=true;database=QLKS;encrypt=true;trustServerCertificate=true;";
            String username = "sa";
            String password = "123";
            conn = DriverManager.getConnection(connectionUrl, username, password);

            if (conn != null) {
                System.out.println("ket noi thanh cong");
            }
        } catch (ClassNotFoundException | SQLException e) {
        }
        return conn;
    }
}
