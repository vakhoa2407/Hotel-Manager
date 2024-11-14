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
public class khachHang {

    private String maKH;
    private String tenKH;
    private String ngaySinh;
    private String gioiTinh;
    private String cmt;
    private String sdt;

    ConnectDB con = new ConnectDB();

    Connection conn;

    public khachHang() {
    }

    public khachHang(String maKH, String tenKH, String ngaySinh, String gioiTinh, String cmt, String sdt) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.cmt = cmt;
        this.sdt = sdt;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public List<khachHang> findAll(String maKH) {
        List<khachHang> list = new ArrayList<>();

        conn = con.getConnection();

        try {
            //lay tat ca danh sach sinh vien

            //query
            String sql = "select * from tbl_khachhang where maKH like ?";

            PreparedStatement statement;
            statement = conn.prepareCall(sql);
            statement.setString(1, "%" + maKH + "%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                khachHang std = new khachHang(resultSet.getString("maKH"),
                        resultSet.getString("tenKH"), resultSet.getString("ngaySinh"), resultSet.getString("gioiTinh"),
                        resultSet.getString("cmt"), resultSet.getString("sdt"));
                list.add(std);
            }
            System.out.println("Có dữ liệu khách hàng");
        } catch (Exception e) {

        } finally {

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(khachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
        //ket thuc.

        return list;
    }
}
