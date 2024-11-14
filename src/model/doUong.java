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
public class doUong {

    String maDoUong;
    String tenDoUong;
    int gia;

    ConnectDB con = new ConnectDB();
    Connection conn;

    public doUong() {
    }

    public doUong(String maDoUong, String tenDoUong, int gia) {
        this.maDoUong = maDoUong;
        this.tenDoUong = tenDoUong;
        this.gia = gia;
    }

    public String getMaDoUong() {
        return maDoUong;
    }

    public void setMaDoUong(String maDoUong) {
        this.maDoUong = maDoUong;
    }

    public String getTenDoUong() {
        return tenDoUong;
    }

    public void setTenDoUong(String tenDoUong) {
        this.tenDoUong = tenDoUong;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public List<doUong> findAll(String maDoUong) {
        List<doUong> list = new ArrayList<>();

        conn = con.getConnection();

        try {
            //query
            String sql = "SELECT * FROM dbo.tbl_doUong WHERE maDU like ?";

            PreparedStatement statement;
            statement = conn.prepareCall(sql);
            statement.setString(1, "%" + maDoUong + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                doUong std = new doUong(resultSet.getString("maDU"),
                        resultSet.getString("tenDU"),
                        resultSet.getInt("gia"));
                list.add(std);
            }
            System.out.println("Có dữ liệu đồ uống");
        } catch (Exception e) {

        } finally {

            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Đóng kết nối thành công");
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(doUong.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
        return list;
    }
}
