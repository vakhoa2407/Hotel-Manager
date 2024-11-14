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
public class datDoUong {

    String ngay;
    String maPhong;
    String maDU;
    int soLuong;

    ConnectDB con = new ConnectDB();
    Connection conn;

    public datDoUong() {
    }

    public datDoUong(String ngay, String maPhong, String maDU, int soLuong) {
        this.ngay = ngay;
        this.maPhong = maPhong;
        this.maDU = maDU;
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

    public String getMaDU() {
        return maDU;
    }

    public void setMaDU(String maDU) {
        this.maDU = maDU;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public List<datDoUong> findAll(String maDU) {
        List<datDoUong> list = new ArrayList<>();

        conn = con.getConnection();

        try {
            //query
            String sql = "SELECT * FROM dbo.tbl_dangkyDoUong WHERE maDU like ?";

            PreparedStatement statement;
            statement = conn.prepareCall(sql);
            statement.setString(1, "%" + maDU + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                datDoUong std = new datDoUong(resultSet.getString("ngay"),
                        resultSet.getString("maPhong"),
                        resultSet.getString("maDU"), resultSet.getInt("soLuong"));
                list.add(std);
            }
            System.out.println("Có dữ liệu đặt đồ uống");
        } catch (Exception e) {

        } finally {

            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Đóng kết nối thành công");
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(datDoUong.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
        return list;
    }
}
