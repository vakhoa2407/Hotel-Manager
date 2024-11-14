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
public class tinhTrangPhong {

    String maPhong;
    String ngay;
    String tinhTrang;

    ConnectDB con = new ConnectDB();
    Connection conn;

    public tinhTrangPhong() {
    }

    public tinhTrangPhong(String maPhong, String ngay, String tinhTrang) {
        this.maPhong = maPhong;
        this.ngay = ngay;
        this.tinhTrang = tinhTrang;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public List<tinhTrangPhong> findAll(String tinhTrang) {
        List<tinhTrangPhong> list = new ArrayList<>();

        conn = con.getConnection();

        try {
            //query
            String sql = "SELECT * FROM dbo.tbl_trangthaiphong WHERE tinhTrang like ?";

            PreparedStatement statement;
            statement = conn.prepareCall(sql);
            statement.setString(1, "%" + tinhTrang + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                tinhTrangPhong std = new tinhTrangPhong(resultSet.getString("maPhong"),
                        resultSet.getString("ngay"),
                        resultSet.getString("tinhTrang"));
                list.add(std);
            }
            System.out.println("Có dữ liệu tình trạng phòng");
        } catch (Exception e) {

        } finally {

            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Đóng kết nối thành công");
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(tinhTrangPhong.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
        return list;
    }
}
