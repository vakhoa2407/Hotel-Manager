package model;

import connectDatabase.ConnectDB;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author DELL
 */
public class phong {

    private String maPhong;
    private String tenPhong;
    private String loaiPhong;
    private String moTa;
    private int max;

    ConnectDB con = new ConnectDB();

    Connection conn;

    public phong() {

    }

    public phong(String maPhong, String tenPhong, String loaiPhong, String moTa, int max) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.loaiPhong = loaiPhong;
        this.moTa = moTa;
        this.max = max;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public String getLoaiPhong() {
        return loaiPhong;
    }

    public void setLoaiPhong(String loaiPhong) {
        this.loaiPhong = loaiPhong;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public List<phong> findAll(String maPhong) {
        List<phong> phongList = new ArrayList<>();

        conn = con.getConnection();

        try {
            //lay tat ca danh sach sinh vien

            //query
            String sql = "select * from tbl_phong where maPhong like ?";

            PreparedStatement statement;
            statement = conn.prepareCall(sql);
            statement.setString(1, "%" + maPhong + "%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                phong std = new phong(resultSet.getString("maPhong"),
                        resultSet.getString("tenPhong"), resultSet.getString("maLoaiPhong"), resultSet.getString("mota"),
                        resultSet.getInt("songuoitoida"));
                phongList.add(std);
            }
            System.out.println("Có dữ liệu phòng");
        } catch (Exception e) {

        } finally {

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(phong.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
        //ket thuc.

        return phongList;
    }
}
