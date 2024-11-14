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
public class loaiPhong {

    String maLoaiPhong;
    String moTa;
    int gia;

    ConnectDB con = new ConnectDB();
    Connection conn;

    public loaiPhong() {
    }

    public loaiPhong(String maLoaiPhong, String moTa, int gia) {
        this.maLoaiPhong = maLoaiPhong;
        this.moTa = moTa;
        this.gia = gia;
    }

    public String getMaLoaiPhong() {
        return maLoaiPhong;
    }

    public void setMaLoaiPhong(String maLoaiPhong) {
        this.maLoaiPhong = maLoaiPhong;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public List<loaiPhong> findAll(String maLoaiPhong) {
        List<loaiPhong> list = new ArrayList<>();

        conn = con.getConnection();

        try {
            //query
            String sql = "SELECT * FROM dbo.tbl_loaiphong WHERE maLoaiPhong like ?";

            PreparedStatement statement;
            statement = conn.prepareCall(sql);
            statement.setString(1, "%" + maLoaiPhong + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                loaiPhong std = new loaiPhong(resultSet.getString("maLoaiPhong"),
                        resultSet.getString("moTa"),
                        resultSet.getInt("gia"));
                list.add(std);
            }
            System.out.println("Có dữ liệu loại phòng");
        } catch (Exception e) {

        } finally {

            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Đóng kết nối thành công");
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(loaiPhong.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
        return list;
    }
}
