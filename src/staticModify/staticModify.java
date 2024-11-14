/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package staticModify;

import connectDatabase.ConnectDB;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.phong;

/**
 *
 * @author binhNC
 */
public class staticModify {

    static ConnectDB con = new ConnectDB();
    static Connection conn;
    public static String userName = "";
    public static String maNV = "";
    public static int chucVu = 0;

    public static void refresh() {
        conn = con.getConnection();
        try {
            //dbo.uPhongTrongs
            String sql = "{call dbo.uPhongTrongs (?)}";
            CallableStatement statement = conn.prepareCall(sql);

            statement.setInt(1, 1);
            statement.execute();
            System.out.println("Refresh thành công");

        } catch (Exception e) {
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Đóng kết nối thành công");
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(staticModify.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
    }
}
