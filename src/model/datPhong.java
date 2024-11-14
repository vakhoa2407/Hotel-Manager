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
public class datPhong {

    String maKH;
    String maNV;
    String maPhong;
    String ngayDat;
    String ngayTra;
    String trangThai;
    int thanhToan;

    ConnectDB con = new ConnectDB();
    Connection conn;

    public datPhong() {
    }

    public datPhong(String maKH, String maNV, String maPhong, String ngayDat, String ngayTra, String trangThai, int thanhToan) {
        this.maKH = maKH;
        this.maNV = maNV;
        this.maPhong = maPhong;
        this.ngayDat = ngayDat;
        this.ngayTra = ngayTra;
        this.trangThai = trangThai;
        this.thanhToan = thanhToan;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayDat = ngayDat;
    }

    public String getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(String ngayTra) {
        this.ngayTra = ngayTra;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public int getThanhToan() {
        return thanhToan;
    }

    public void setThanhToan(int thanhToan) {
        this.thanhToan = thanhToan;
    }

    public List<datPhong> findAll(String maKH, String maPhong) {
        List<datPhong> list = new ArrayList<>();

        conn = con.getConnection();

        try {
            //query
            String sql = "SELECT * FROM dbo.tbl_datphong WHERE maKH like ? and maPhong like ?";

            PreparedStatement statement;
            statement = conn.prepareCall(sql);
            statement.setString(1, "%" + maKH + "%");
            statement.setString(2, "%" + maPhong + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                datPhong std = new datPhong(resultSet.getString("maKH"),
                        resultSet.getString("maNV"), resultSet.getString("maPhong"),
                        resultSet.getString("ngayDat"), resultSet.getString("ngayTra"),
                        resultSet.getString("trangthai"), resultSet.getInt("thanhtoan"));
                list.add(std);
            }
            System.out.println("Có dữ liệu đặt phòng");
        } catch (Exception e) {

        } finally {

            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Đóng kết nối thành công");
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(datPhong.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
        return list;
    }
}
