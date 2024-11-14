/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package viewsLeft;

import connectDatabase.ConnectDB;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.*;
import viewsTop.*;
import viewsLeft.*;
import staticModify.*;

/**
 *
 * @author DELL
 */
public class main_datPhong extends javax.swing.JFrame {

    /**
     * Creates new form main_taiKhoan
     */
    ConnectDB con = new ConnectDB();
    Connection conn;

    DefaultTableModel tableModel;
    List<datPhong> list = new ArrayList<>();

    String txtTkMaKH = "";
    String txtTkMaPhong = "";
    Calendar cal = Calendar.getInstance();
    java.sql.Date ngayHienTai = new java.sql.Date(cal.getTime().getTime());
    java.sql.Date ngayDat = new java.sql.Date(cal.getTime().getTime());
    java.sql.Date ngayTra = new java.sql.Date(cal.getTime().getTime());

    public main_datPhong(String username) {
        initComponents();
        txt_TaiKhoan.setText(username);
    }

    public main_datPhong() {
        initComponents();
        txt_TaiKhoan.setText(staticModify.userName);
        tableModel = (DefaultTableModel) tblDatPhong.getModel();
        showDatPhong();
//        staticModify.maNV = "bibo";
//        System.out.println("date: " + ngayHienTai);
//        System.out.println("date: " + ngayDat);
//        System.out.println("date: " + cal.getTime());
//        System.out.println("date: " + dateNgayDat.getDate());

    }

    public void showDatPhong() {
        if (cbTimKiem.getSelectedIndex() == 0) {
            txtTkMaKH = "";
            txtTkMaPhong = txtTimKiem.getText().trim();
        } else if (cbTimKiem.getSelectedIndex() == 1) {
            txtTkMaPhong = "";
            txtTkMaKH = txtTimKiem.getText().trim();
        }

        datPhong test = new datPhong();
        list = test.findAll(txtTkMaKH, txtTkMaPhong);

        tableModel.setRowCount(0); // Đặt lại số hàng là 0 (xóa tất cả dữ liệu)

        list.forEach((datPhong) -> {
            tableModel.addRow(new Object[]{/*tableModel.getRowCount() + 1*/
                datPhong.getMaKH(), datPhong.getMaNV(), datPhong.getMaPhong(),
                datPhong.getNgayDat(), datPhong.getNgayTra(),
                datPhong.getTrangThai(), datPhong.getThanhToan()});
        });
    }

    public void datPhong() {
        java.sql.Date ngayDat = new java.sql.Date(dateNgayDat.getDate().getTime());
        java.sql.Date ngayTra = new java.sql.Date(dateNgayTra.getDate().getTime());

        if (txtMaKH.getText().trim().equals("")) {
            System.out.println("Mã khách hàng không được trống");
            JOptionPane.showMessageDialog(this, "Mã khách hàng không được trống");
            txtMaKH.requestFocus(true);
            txtMaKH.selectAll();
        } else if (txtMaPhong.getText().trim().equals("")) {
            System.out.println("Mã phòng không được trống");
            JOptionPane.showMessageDialog(this, "Mã phòng không được trống");
            txtMaPhong.requestFocus(true);
            txtMaPhong.selectAll();
        } else {
            conn = con.getConnection();

            try {
                String sql = "{call datphong (?, ?, ?, ?, ?)}";
                CallableStatement statement = conn.prepareCall(sql);

                statement.setString(1, txtMaKH.getText());
                statement.setString(2, txtMaNV.getText());
                statement.setString(3, txtMaPhong.getText());
                statement.setString(4, ngayDat.toString());
                statement.setString(5, ngayTra.toString());

                ResultSet rs = statement.executeQuery();

                while (rs.next()) {
                    if (rs.getInt("ketqua") == -6) {
                        System.out.println("Mã khách hàng không tồn tại");
                        JOptionPane.showMessageDialog(this, "Mã khách hàng không tồn tại");
                        txtMaKH.requestFocus(true);
                        txtMaKH.selectAll();
                    } else if (rs.getInt("ketqua") == -5) {
                        System.out.println("Mã nhân viên không tồn tại");
                        JOptionPane.showMessageDialog(this, "Mã nhân viên không tồn tại");
                    } else if (rs.getInt("ketqua") == -4) {
                        System.out.println("Mã phòng không tồn tại");
                        JOptionPane.showMessageDialog(this, "Mã phòng không tồn tại");
                        txtMaPhong.requestFocus(true);
                        txtMaPhong.selectAll();
                    } else if (rs.getInt("ketqua") == -3) {
                        System.out.println("Ngày đặt phải là hiện tại / tương lai");
                        JOptionPane.showMessageDialog(this, "Ngày đặt phải là hiện tại / tương lai");
                        dateNgayDat.requestFocus(true);
                    } else if (rs.getInt("ketqua") == -2) {
                        System.out.println("Ngày trả phải > ngày đặt");
                        JOptionPane.showMessageDialog(this, "Ngày trả phải > ngày đặt");
                        dateNgayTra.requestFocus(true);
                    } else if (rs.getInt("ketqua") == -1) {
                        System.out.println("Phòng hiện tại chưa đặt được\nCó thể đặt phòng khác");
                        JOptionPane.showMessageDialog(this, "Phòng hiện tại chưa đặt được\nCó thể đặt phòng khác");
                    } else if (rs.getInt("ketqua") == 0) {
                        System.out.println("Đặt phòng không thành công");
                        JOptionPane.showMessageDialog(this, "Đặt phòng không thành công");
                    } else if (rs.getInt("ketqua") == 1) {
                        System.out.println("Đặt phòng thành công");
                        JOptionPane.showMessageDialog(this, "Đặt phòng thành công");
                        btnDatPhong.setEnabled(false);
                        btnTraPhong.setEnabled(false);
                    } else if (rs.getInt("ketqua") == 2) {
                        System.out.println("Hẹn phòng thành công");
                        JOptionPane.showMessageDialog(this, "Hẹn đặt phòng thành công");
                        btnDatPhong.setEnabled(false);
                        btnTraPhong.setEnabled(false);
                    }
                }
            } catch (Exception e) {
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                        System.out.println("Đóng kết nối thành công");
                    } catch (SQLException ex) {
                        Logger.getLogger(main_datPhong.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        showDatPhong();
    }

    public void traPhong() {
        conn = con.getConnection();

        try {
            String sql = "{call thanhToan (?, ?, ?)}";
            CallableStatement statement = conn.prepareCall(sql);

            statement.setString(1, txtMaKH.getText().trim());
            statement.setString(2, txtMaPhong.getText().trim());
            statement.setString(3, ngayDat.toString());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                if (rs.getInt("ketqua") == 0) {
                    System.out.println("Lỗi\n(Ngày hiện tại >= ngày trả)");
                    JOptionPane.showMessageDialog(this, "Lỗi\n(Ngày hiện tại >= ngày trả)");
                } else if (rs.getInt("ketqua") == 1) {
                    System.out.println("Trả phòng thành công");
                    JOptionPane.showMessageDialog(this, "Trả phòng thành công");
                    cbThanhToan.setSelectedIndex(1);
                    btnTraPhong.setEnabled(false);
                    btnDatPhong.setEnabled(false);
                }
            }
        } catch (Exception e) {
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Đóng kết nối thành công");
                } catch (SQLException ex) {
                    Logger.getLogger(main_datPhong.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        hoaDon();
        showDatPhong();
    }

    public void hoaDon() {
        conn = con.getConnection();
        try {
            String sql = "{call hoaDonTheoKhachHang (?)}";
            CallableStatement statement = conn.prepareCall(sql);

            statement.setString(1, txtMaKH.getText().trim());

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String hd = rs.getString("hoaDon");
                hd = String.valueOf(Float.valueOf(hd) * 1000.0f);
                txtHoaDon.setText(hd + " VNĐ");
            }
        } catch (Exception e) {
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Đóng kết nối thành công");
                } catch (SQLException ex) {
                    Logger.getLogger(main_datPhong.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lbMaPhong = new javax.swing.JLabel();
        txtMaPhong = new javax.swing.JTextField();
        lbNgayDat = new javax.swing.JLabel();
        lbNgayTra = new javax.swing.JLabel();
        lbMaKH = new javax.swing.JLabel();
        txtMaKH = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        dateNgayDat = new com.toedter.calendar.JDateChooser();
        dateNgayTra = new com.toedter.calendar.JDateChooser();
        btnTraPhong = new javax.swing.JButton();
        lbMaNV = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        lbTrangThai = new javax.swing.JLabel();
        cbTrangThai = new javax.swing.JComboBox<>();
        lbThanhToan = new javax.swing.JLabel();
        cbThanhToan = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDatPhong = new javax.swing.JTable();
        lbLuaChon = new javax.swing.JLabel();
        cbTimKiem = new javax.swing.JComboBox<>();
        lbTimKiem = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnDatPhong = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        lbHoaDon = new javax.swing.JLabel();
        txtHoaDon = new javax.swing.JTextField();
        btnTaiKhoan = new javax.swing.JButton();
        btnPhong = new javax.swing.JButton();
        btnNhanVien = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        btnKhachHang = new javax.swing.JButton();
        btnTrangThaiPhong = new javax.swing.JButton();
        btnDP = new javax.swing.JButton();
        btnDkdv = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        btnThongKe = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        txt_TaiKhoan = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnAUL = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ĐẶT PHÒNG");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("ĐĂNG KÝ PHÒNG:");

        lbMaPhong.setText("Mã phòng:");

        txtMaPhong.setEditable(false);
        txtMaPhong.setEnabled(false);
        txtMaPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaPhongActionPerformed(evt);
            }
        });

        lbNgayDat.setText("Ngày đặt:");

        lbNgayTra.setText("Ngày trả:");

        lbMaKH.setText("Mã khách hàng:");

        txtMaKH.setEditable(false);
        txtMaKH.setEnabled(false);

        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        dateNgayDat.setDateFormatString("yyyy-MM-dd");

        dateNgayTra.setDateFormatString("yyyy-MM-dd");

        btnTraPhong.setText("Trả phòng");
        btnTraPhong.setEnabled(false);
        btnTraPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraPhongActionPerformed(evt);
            }
        });

        lbMaNV.setText("Mã nhân viên:");

        txtMaNV.setEditable(false);
        txtMaNV.setEnabled(false);

        lbTrangThai.setText("Trạng thái:");

        cbTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đặt", "Hẹn" }));
        cbTrangThai.setEnabled(false);

        lbThanhToan.setText("Thanh toán:");

        cbThanhToan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1" }));
        cbThanhToan.setEnabled(false);

        tblDatPhong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã khách hàng", "Mã nhân viên", "Mã phòng", "Ngày đặt", "Ngày trả", "Trạng thái", "Thanh toán"
            }
        ));
        tblDatPhong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDatPhongMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDatPhong);

        lbLuaChon.setText("Lựa chọn:");

        cbTimKiem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mã phòng", "Mã khách hàng" }));
        cbTimKiem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbTimKiemItemStateChanged(evt);
            }
        });

        lbTimKiem.setText("Tìm kiếm:");

        btnDatPhong.setText("Đặt phòng");
        btnDatPhong.setEnabled(false);
        btnDatPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatPhongActionPerformed(evt);
            }
        });

        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        lbHoaDon.setText("Tổng số tiền thanh toán:");

        txtHoaDon.setEditable(false);
        txtHoaDon.setEnabled(false);
        txtHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHoaDonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(lbLuaChon, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lbTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnTimKiem)))
                        .addGap(18, 20, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtMaPhong)
                                .addComponent(txtMaNV)
                                .addComponent(lbMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbNgayDat, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(dateNgayDat, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lbMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtHoaDon)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbNgayTra, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbMaKH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtMaKH)
                                        .addComponent(dateNgayTra, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                        .addComponent(cbTrangThai, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(lbHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap(322, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(209, 209, 209)
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDatPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTraPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(221, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(2, 2, 2)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbMaNV)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(lbMaKH)
                                .addGap(12, 12, 12)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lbLuaChon)
                                    .addComponent(cbTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(lbMaPhong)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(txtMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(cbTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addComponent(lbTimKiem))
                                        .addGap(12, 12, 12))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnTimKiem))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addComponent(lbNgayDat)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateNgayDat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(lbTrangThai)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbNgayTra)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateNgayTra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbThanhToan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbThanhToan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lbHoaDon)
                        .addGap(28, 28, 28)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTraPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDatPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 120, -1, 510));

        btnTaiKhoan.setText("Tài Khoản");
        btnTaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaiKhoanActionPerformed(evt);
            }
        });
        getContentPane().add(btnTaiKhoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 110, -1));

        btnPhong.setText("Phòng");
        btnPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhongActionPerformed(evt);
            }
        });
        getContentPane().add(btnPhong, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 30, 110, -1));

        btnNhanVien.setText("Nhân Viên");
        btnNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNhanVienActionPerformed(evt);
            }
        });
        getContentPane().add(btnNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 30, 110, -1));

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        getContentPane().add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 30, 130, -1));

        btnKhachHang.setText("Khách Hàng");
        btnKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhachHangActionPerformed(evt);
            }
        });
        getContentPane().add(btnKhachHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 30, 110, -1));

        btnTrangThaiPhong.setText("Trạng thái phòng theo ngày");
        btnTrangThaiPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrangThaiPhongActionPerformed(evt);
            }
        });
        getContentPane().add(btnTrangThaiPhong, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 220, -1));

        btnDP.setText("Đặt phòng");
        btnDP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDPActionPerformed(evt);
            }
        });
        getContentPane().add(btnDP, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 390, 220, -1));

        btnDkdv.setText("Đặt đồ ăn / đồ uống");
        btnDkdv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDkdvActionPerformed(evt);
            }
        });
        getContentPane().add(btnDkdv, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 430, 220, -1));

        jLabel23.setBackground(new java.awt.Color(20, 56, 20));
        jLabel23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/rsz_xd.png"))); // NOI18N
        jLabel23.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel23.setOpaque(true);
        getContentPane().add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, 220, 200));

        btnThongKe.setText("Báo cáo - Thống kê");
        btnThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThongKeActionPerformed(evt);
            }
        });
        getContentPane().add(btnThongKe, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 470, 220, -1));

        btnLogout.setText("Đăng xuất");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        getContentPane().add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 560, 220, -1));

        btnExit.setText("Thoát");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        getContentPane().add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 600, 220, -1));

        txt_TaiKhoan.setAlignmentX(1.0F);
        txt_TaiKhoan.setAutoscrolls(false);
        txt_TaiKhoan.setPreferredSize(new java.awt.Dimension(120, 22));
        txt_TaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TaiKhoanActionPerformed(evt);
            }
        });
        getContentPane().add(txt_TaiKhoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 150, 30));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Tài khoản:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 76, 80, 20));

        btnAUL.setText("Đồ ăn / Đồ uống / Loại phòng");
        btnAUL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAULActionPerformed(evt);
            }
        });
        getContentPane().add(btnAUL, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 200, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/green.png"))); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(1280, 640));
        jLabel1.setMinimumSize(new java.awt.Dimension(1280, 640));
        jLabel1.setPreferredSize(new java.awt.Dimension(1280, 640));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 640));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTaiKhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaiKhoanActionPerformed
        // TODO add your handling code here:
        new main_taiKhoan().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnTaiKhoanActionPerformed

    private void btnPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhongActionPerformed
        // TODO add your handling code here:
        new main_phong().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnPhongActionPerformed

    private void btnNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNhanVienActionPerformed
        // TODO add your handling code here:
        new main_nhanVien().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnNhanVienActionPerformed

    private void btnKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhachHangActionPerformed
        // TODO add your handling code here:
        new main_khachHang().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnKhachHangActionPerformed

    private void btnTrangThaiPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrangThaiPhongActionPerformed
        // TODO add your handling code here:
        new main_trangThaiPhong().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnTrangThaiPhongActionPerformed

    private void btnDPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDPActionPerformed
        // TODO add your handling code here:
        new main_datPhong().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnDPActionPerformed

    private void btnDkdvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDkdvActionPerformed
        // TODO add your handling code here:
        new main_DkDichVu().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnDkdvActionPerformed

    private void btnThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongKeActionPerformed
        // TODO add your handling code here:
        new main_thongKe().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnThongKeActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        new LoginForm().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btnExitActionPerformed

    private void txt_TaiKhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TaiKhoanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_TaiKhoanActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        staticModify.refresh();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnAULActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAULActionPerformed
        // TODO add your handling code here:
        new main_AUL().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnAULActionPerformed

    private void btnTraPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraPhongActionPerformed
        // TODO add your handling code here:
        traPhong();
    }//GEN-LAST:event_btnTraPhongActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        showDatPhong();
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void txtMaPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaPhongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaPhongActionPerformed

    private void btnDatPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatPhongActionPerformed
        // TODO add your handling code here:
        datPhong();
    }//GEN-LAST:event_btnDatPhongActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        txtMaNV.setText(staticModify.maNV);

        txtMaKH.setEnabled(true);
        txtMaKH.setEditable(true);
        txtMaKH.requestFocus(true);
        txtMaKH.selectAll();
        txtMaKH.setText("");
        txtMaPhong.setText("");
        cbTrangThai.setSelectedIndex(0);
        cbThanhToan.setSelectedIndex(0);

        txtMaPhong.setEnabled(true);
        txtMaPhong.setEditable(true);
        //
        dateNgayDat.setDate(ngayHienTai);
        dateNgayTra.setDate(ngayHienTai);

        //
        btnDatPhong.setEnabled(true);
        btnTraPhong.setEnabled(false);
    }//GEN-LAST:event_btnThemActionPerformed

    private void cbTimKiemItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbTimKiemItemStateChanged
        // TODO add your handling code here:
        if (cbTimKiem.getSelectedIndex() == 0) {
            txtTkMaKH = "";
            txtTkMaPhong = txtTimKiem.getText().trim();
        } else if (cbTimKiem.getSelectedIndex() == 1) {
            txtTkMaPhong = "";
            txtTkMaKH = txtTimKiem.getText().trim();
        }
    }//GEN-LAST:event_cbTimKiemItemStateChanged

    private void tblDatPhongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDatPhongMouseClicked
        // TODO add your handling code here:
        hoaDon();

        btnDatPhong.setEnabled(false);
        btnTraPhong.setEnabled(true);

        txtMaKH.setEnabled(false);
        txtMaKH.setEditable(false);

        txtMaPhong.setEnabled(false);
        txtMaPhong.setEditable(false);

        int selectedIndex = tblDatPhong.getSelectedRow();
        if (selectedIndex >= 0) {
            datPhong std = list.get(selectedIndex);
            txtMaNV.setText(std.getMaNV());
            txtMaKH.setText(std.getMaKH());
            txtMaPhong.setText(std.getMaPhong());

            if (std.getTrangThai().trim().equals("dat")) {
                cbTrangThai.setSelectedIndex(0);
            } else if (std.getTrangThai().trim().equals("hen")) {
                cbTrangThai.setSelectedIndex(1);
            }
            try {
                java.util.Date dateTest = new SimpleDateFormat("yyyy-MM-dd").parse(std.getNgayDat());
                dateNgayDat.setDate(dateTest);
                //
                dateTest = new SimpleDateFormat("yyyy-MM-dd").parse(std.getNgayTra());
                dateNgayTra.setDate(dateTest);

            } catch (ParseException ex) {
                Logger.getLogger(main_nhanVien.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            if (std.getThanhToan() == 0) {
                cbThanhToan.setSelectedIndex(0);
            } else if (std.getThanhToan() == 1) {
                cbThanhToan.setSelectedIndex(1);
            }
        }
        //
        ngayDat.setTime(dateNgayDat.getDate().getTime());
        System.out.println("Ngày đặt dùng trong proc thanh toán: " + ngayDat.toString());
        ngayTra.setTime(dateNgayTra.getDate().getTime());
        System.out.println("Ngày trả dùng trong proc thanh toán: " + ngayTra.toString());
        //
    }//GEN-LAST:event_tblDatPhongMouseClicked

    private void txtHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHoaDonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHoaDonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(main_datPhong.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main_datPhong.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main_datPhong.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main_datPhong.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main_datPhong().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAUL;
    private javax.swing.JButton btnDP;
    private javax.swing.JButton btnDatPhong;
    private javax.swing.JButton btnDkdv;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnKhachHang;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnNhanVien;
    private javax.swing.JButton btnPhong;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnTaiKhoan;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThongKe;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnTraPhong;
    private javax.swing.JButton btnTrangThaiPhong;
    private javax.swing.JComboBox<String> cbThanhToan;
    private javax.swing.JComboBox<String> cbTimKiem;
    private javax.swing.JComboBox<String> cbTrangThai;
    private com.toedter.calendar.JDateChooser dateNgayDat;
    private com.toedter.calendar.JDateChooser dateNgayTra;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbHoaDon;
    private javax.swing.JLabel lbLuaChon;
    private javax.swing.JLabel lbMaKH;
    private javax.swing.JLabel lbMaNV;
    private javax.swing.JLabel lbMaPhong;
    private javax.swing.JLabel lbNgayDat;
    private javax.swing.JLabel lbNgayTra;
    private javax.swing.JLabel lbThanhToan;
    private javax.swing.JLabel lbTimKiem;
    private javax.swing.JLabel lbTrangThai;
    private javax.swing.JTable tblDatPhong;
    private javax.swing.JTextField txtHoaDon;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtMaPhong;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txt_TaiKhoan;
    // End of variables declaration//GEN-END:variables
}
