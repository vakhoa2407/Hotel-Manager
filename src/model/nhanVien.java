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
public class nhanVien {

    String MaNV;
    String TenNV;
    String NgaySinh;
    String GioiTinh;
    int ChucVu;
    String CMT;
    String SDT;

    ConnectDB con = new ConnectDB();
    Connection conn;

    public nhanVien() {
    }

    public nhanVien(String MaNV, String TenNV, String NgaySinh, String GioiTinh, int ChucVu, String CMT, String SDT) {
        this.MaNV = MaNV;
        this.TenNV = TenNV;
        this.NgaySinh = NgaySinh;
        this.GioiTinh = GioiTinh;
        this.ChucVu = ChucVu;
        this.CMT = CMT;
        this.SDT = SDT;
    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String MaNV) {
        this.MaNV = MaNV;
    }

    public String getTenNV() {
        return TenNV;
    }

    public void setTenNV(String TenNV) {
        this.TenNV = TenNV;
    }

    public String getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(String NgaySinh) {
        this.NgaySinh = NgaySinh;
    }

    public String getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(String GioiTinh) {
        this.GioiTinh = GioiTinh;
    }

    public int getChucVu() {
        return ChucVu;
    }

    public void setChucVu(int ChucVu) {
        this.ChucVu = ChucVu;
    }

    public String getCMT() {
        return CMT;
    }

    public void setCMT(String CMT) {
        this.CMT = CMT;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public List<nhanVien> findAll(String maNV) {
        List<nhanVien> list = new ArrayList<>();

        conn = con.getConnection();

        try {
            //query
            String sql = "SELECT * FROM dbo.tbl_nhanvien WHERE maNV like ?";

            PreparedStatement statement;
            statement = conn.prepareCall(sql);
            statement.setString(1, "%" + maNV + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                nhanVien std = new nhanVien(resultSet.getString("maNV"),
                        resultSet.getString("tenNV"), resultSet.getString("ngaySinh"), resultSet.getString("gioiTinh"),
                        resultSet.getInt("chucVu"), resultSet.getString("cmt"), resultSet.getString("sdt"));
                list.add(std);
            }
            System.out.println("Có dữ liệu nhân viên");
        } catch (Exception e) {

        } finally {

            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Đóng kết nối thành công");
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(nhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
        return list;
    }
}
