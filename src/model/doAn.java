/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import connectDatabase.ConnectDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PC
 */
public class doAn {

    String maDoAn;
    String tenDoAn;
    int gia;

    ConnectDB con = new ConnectDB();
    Connection conn;

    public doAn() {
    }

    public doAn(String maDoAn, String TenDoAn, int gia) {
        this.maDoAn = maDoAn;
        this.tenDoAn = TenDoAn;
        this.gia = gia;
    }

    public String getMaDoAn() {
        return maDoAn;
    }

    public void setMaDoAn(String maDoAn) {
        this.maDoAn = maDoAn;
    }

    public String getTenDoAn() {
        return tenDoAn;
    }

    public void setTenDoAn(String TenDoAn) {
        this.tenDoAn = TenDoAn;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public List<doAn> findAll(String maDoAn) {
        List<doAn> list = new ArrayList<>();

        conn = con.getConnection();

        try {
            //query
            String sql = "SELECT * FROM dbo.tbl_doAn WHERE maDA like ?";

            PreparedStatement statement;
            statement = conn.prepareCall(sql);
            statement.setString(1, "%" + maDoAn + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                doAn std = new doAn(resultSet.getString("maDA"),
                        resultSet.getString("tenDA"),
                        resultSet.getInt("gia"));
                list.add(std);
            }
            System.out.println("Có dữ liệu đồ ăn");
        } catch (Exception e) {

        } finally {

            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Đóng kết nối thành công");
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(doAn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
        return list;
    }
}
