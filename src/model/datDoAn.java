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
 * @author binhNC
 */
public class datDoAn {

    String ngay;
    String maPhong;
    String maDA;
    int soLuong;

    ConnectDB con = new ConnectDB();
    Connection conn;

    public datDoAn() {
    }

    public datDoAn(String ngay, String maPhong, String maDA, int soLuong) {
        this.ngay = ngay;
        this.maPhong = maPhong;
        this.maDA = maDA;
        this.soLuong = soLuong;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getMaDA() {
        return maDA;
    }

    public void setMaDA(String maDA) {
        this.maDA = maDA;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public List<datDoAn> findAll(String maDA) {
        List<datDoAn> list = new ArrayList<>();

        conn = con.getConnection();

        try {
            //query
            String sql = "SELECT * FROM dbo.tbl_dangkyDoAn WHERE maDA like ?";

            PreparedStatement statement;
            statement = conn.prepareCall(sql);
            statement.setString(1, "%" + maDA + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                datDoAn std = new datDoAn(resultSet.getString("ngay"),
                        resultSet.getString("maPhong"),
                        resultSet.getString("maDA"), resultSet.getInt("soLuong"));
                list.add(std);
            }
            System.out.println("Có dữ liệu đặt đồ ăn");
        } catch (Exception e) {

        } finally {

            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Đóng kết nối thành công");
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(datDoAn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
        return list;
    }
}
