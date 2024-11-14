/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package test;

import connectDatabase.ConnectDB;
import java.util.List;
import model.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import viewsLeft.*;
import staticModify.*;
import viewsTop.*;

/**
 *
 * @author DELL
 */
public class main_doUong extends javax.swing.JFrame {

    /**
     * Creates new form main_taiKhoan
     */
    ConnectDB con = new ConnectDB();
    Connection conn;
    DefaultTableModel tableModel;

    List<doUong> list = new ArrayList<>();

    public main_doUong(String username) {
        initComponents();
        txt_TaiKhoan.setText(username);

        tableModel = (DefaultTableModel) tblDoUong.getModel();
        showDoUong();
    }

    public main_doUong() {
        initComponents();
        txt_TaiKhoan.setText(staticModify.userName);
        tableModel = (DefaultTableModel) tblDoUong.getModel();
        showDoUong();
    }

    public void autoNullColumn() {
        txtMaDoUong.setText("");
        txtTenDoUong.setText("");
        txtGia.setText("");
    }

    private void addDoUong() {
        if (txtMaDoUong.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Mã đồ uống không được trống");
            txtMaDoUong.requestFocus();
            txtMaDoUong.selectAll();
        } else if (txtTenDoUong.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Tên đồ uống không được trống");
            txtTenDoUong.requestFocus();
            txtTenDoUong.selectAll();
        } else if (txtGia.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Giá đồ uống không được trống");
            txtGia.requestFocus();
            txtGia.selectAll();
        } else if (!txtGia.getText().trim().matches("^[1-9][0-9]*$")) {
            JOptionPane.showMessageDialog(this, "Giá đồ uống phải > 0");
            txtGia.requestFocus();
            txtGia.selectAll();
        } else {
            conn = con.getConnection();
            try {
                String sql = "{call addDoUong (?, ?, ?)}";
                CallableStatement statement = conn.prepareCall(sql);

                statement.setString(1, txtMaDoUong.getText());
                statement.setString(2, txtTenDoUong.getText());
                statement.setInt(3, Integer.valueOf(txtGia.getText().trim()));

                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("ketqua") == -1) {
                        System.out.println("Mã đồ uống đã tồn tại");
                        JOptionPane.showMessageDialog(this, "Mã đồ uống đã tồn tại");
                    } else if (rs.getInt("ketqua") == 0) {
                        System.out.println("Giá đồ uống phải dương");
                        JOptionPane.showMessageDialog(this, "Giá đồ uống phải dương");
                    } else if (rs.getInt("ketqua") == 1) {
                        System.out.println("Thêm đồ uống thành công");
                        JOptionPane.showMessageDialog(this, "Thêm đồ uống thành công");
                    }
                }
            } catch (Exception e) {
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                        System.out.println("Đóng kết nối thành công");
                    } catch (SQLException ex) {
                        Logger.getLogger(main_doUong.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        showDoUong();
    }

    private void saveUpdate() {
        if (txtMaDoUong.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Mã đồ uống không được trống");
            txtMaDoUong.requestFocus();
            txtMaDoUong.selectAll();
        } else if (txtTenDoUong.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Tên đồ uống không được trống");
            txtTenDoUong.requestFocus();
            txtTenDoUong.selectAll();
        } else if (txtGia.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Giá đồ uống không được trống");
            txtGia.requestFocus();
            txtGia.selectAll();
        } else if (!txtGia.getText().trim().matches("^[1-9][0-9]*$")) {
            JOptionPane.showMessageDialog(this, "Giá đồ uống phải > 0");
            txtGia.requestFocus();
            txtGia.selectAll();
        } else {
            conn = con.getConnection();
            try {
                String sql = "{call updateDoUong (?, ?, ?)}";
                CallableStatement statement = conn.prepareCall(sql);

                statement.setString(1, txtMaDoUong.getText());
                statement.setString(2, txtTenDoUong.getText());
                statement.setInt(3, Integer.valueOf(txtGia.getText().trim()));

                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("ketqua") == -1) {
                        System.out.println("Mã đồ uống không tồn tại");
                        JOptionPane.showMessageDialog(this, "Mã đồ uống không tồn tại");
                    } else if (rs.getInt("ketqua") == 0) {
                        System.out.println("Giá đồ uống phải dương");
                        JOptionPane.showMessageDialog(this, "Giá đồ uống phải dương");
                    } else if (rs.getInt("ketqua") == 1) {
                        System.out.println("Cập nhật dữ liệu thành công");
                        JOptionPane.showMessageDialog(this, "Cập nhật dữ liệu thành công");
                    }
                }
            } catch (Exception e) {
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                        System.out.println("Đóng kết nối thành công");
                    } catch (SQLException ex) {
                        Logger.getLogger(main_doUong.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        showDoUong();
    }

    public void showDoUong() {

        doUong test = new doUong();
        list = test.findAll(txtTimKiem.getText().trim());

        tableModel.setRowCount(0); // Đặt lại số hàng là 0 (xóa tất cả dữ liệu)

        list.forEach((doUong) -> {
            tableModel.addRow(new Object[]{/*tableModel.getRowCount() + 1*/
                doUong.getMaDoUong(), doUong.getTenDoUong(), doUong.getGia()});
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        btnTaiKhoan = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        btnPhong = new javax.swing.JButton();
        btnNhanVien = new javax.swing.JButton();
        btnKhachHang = new javax.swing.JButton();
        btnQLPhong = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnDatPhong = new javax.swing.JButton();
        btnDKDV = new javax.swing.JButton();
        btnTraPhong = new javax.swing.JButton();
        btnBaoCao = new javax.swing.JButton();
        btnDangXuat = new javax.swing.JButton();
        btnThoat = new javax.swing.JButton();
        txt_TaiKhoan = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btnTimKiem = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        lbTimKiem = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        lbMaDoAn = new javax.swing.JLabel();
        txtMaDoUong = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtTenDoUong = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDoUong = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        txtGia = new javax.swing.JTextField();
        btnDoUong = new javax.swing.JButton();
        btnDoAn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TaiKhoan");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Tài khoản:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 76, 80, 20));

        btnTaiKhoan.setText("Tài Khoản");
        btnTaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaiKhoanActionPerformed(evt);
            }
        });
        getContentPane().add(btnTaiKhoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 110, -1));

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        getContentPane().add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 30, 130, -1));

        btnPhong.setText("Phòng");
        btnPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhongActionPerformed(evt);
            }
        });
        getContentPane().add(btnPhong, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 30, 110, -1));

        btnNhanVien.setText("Nhân Viên");
        btnNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNhanVienActionPerformed(evt);
            }
        });
        getContentPane().add(btnNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 30, 110, -1));

        btnKhachHang.setText("Khách Hàng");
        btnKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhachHangActionPerformed(evt);
            }
        });
        getContentPane().add(btnKhachHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, 110, -1));

        btnQLPhong.setText("Quản lý tình trạng phòng");
        btnQLPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQLPhongActionPerformed(evt);
            }
        });
        getContentPane().add(btnQLPhong, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 220, -1));

        jLabel4.setBackground(new java.awt.Color(20, 56, 20));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/rsz_xd.png"))); // NOI18N
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel4.setOpaque(true);
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 220, 200));

        btnDatPhong.setText("Đặt phòng");
        btnDatPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatPhongActionPerformed(evt);
            }
        });
        getContentPane().add(btnDatPhong, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 390, 220, -1));

        btnDKDV.setText("Đăng kí dịch vụ");
        btnDKDV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDKDVActionPerformed(evt);
            }
        });
        getContentPane().add(btnDKDV, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 430, 220, -1));

        btnTraPhong.setText("Trả phòng");
        btnTraPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTraPhongActionPerformed(evt);
            }
        });
        getContentPane().add(btnTraPhong, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 470, 220, -1));

        btnBaoCao.setText("Báo cáo - Thống kê");
        btnBaoCao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaoCaoActionPerformed(evt);
            }
        });
        getContentPane().add(btnBaoCao, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 510, 220, -1));

        btnDangXuat.setText("Đăng xuất");
        btnDangXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangXuatActionPerformed(evt);
            }
        });
        getContentPane().add(btnDangXuat, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 560, 220, -1));

        btnThoat.setText("Thoát");
        btnThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoatActionPerformed(evt);
            }
        });
        getContentPane().add(btnThoat, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 600, 220, -1));

        txt_TaiKhoan.setAlignmentX(1.0F);
        txt_TaiKhoan.setAutoscrolls(false);
        txt_TaiKhoan.setPreferredSize(new java.awt.Dimension(120, 22));
        txt_TaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TaiKhoanActionPerformed(evt);
            }
        });
        getContentPane().add(txt_TaiKhoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 150, 30));

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        btnLuu.setText("Lưu");
        btnLuu.setEnabled(false);
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        btnSua.setText("Sửa");
        btnSua.setEnabled(false);
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        lbTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbTimKiem.setText("Tìm kiếm theo mã đồ uống:");

        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });

        lbMaDoAn.setText("Mã đồ uống:");

        jLabel6.setText("Tên đồ uống:");

        tblDoUong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã đồ uống", "Tên đồ uống", "Giá"
            }
        ));
        tblDoUong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDoUongMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDoUong);

        jLabel11.setText("Giá:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(68, 68, 68))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lbTimKiem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbMaDoAn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTenDoUong, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                            .addComponent(txtMaDoUong)
                            .addComponent(txtGia, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnThem, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                            .addComponent(btnSua, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                            .addComponent(btnLuu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(563, 563, 563))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTimKiem)
                    .addComponent(lbTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbMaDoAn)
                    .addComponent(txtMaDoUong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtTenDoUong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSua))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLuu))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(78, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, 830, 500));

        btnDoUong.setText("Đồ uống");
        btnDoUong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoUongActionPerformed(evt);
            }
        });
        getContentPane().add(btnDoUong, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 30, 110, -1));

        btnDoAn.setText("Đồ ăn");
        btnDoAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoAnActionPerformed(evt);
            }
        });
        getContentPane().add(btnDoAn, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 30, 110, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/green.png"))); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(1280, 640));
        jLabel1.setMinimumSize(new java.awt.Dimension(1280, 640));
        jLabel1.setPreferredSize(new java.awt.Dimension(1280, 640));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 640));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        // TODO add your handling code here:
        btnLuu.setEnabled(false);
        addDoUong();
        autoNullColumn();
    }//GEN-LAST:event_btnLuuActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        autoNullColumn();
        txtMaDoUong.requestFocus();
        txtMaDoUong.selectAll();
        txtMaDoUong.setEnabled(true);
        txtMaDoUong.setEditable(true);
        btnLuu.setEnabled(true);
    }//GEN-LAST:event_btnThemActionPerformed

    private void txt_TaiKhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TaiKhoanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_TaiKhoanActionPerformed

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
        new main_doUong().setVisible(true);
        this.setVisible(false);

    }//GEN-LAST:event_btnNhanVienActionPerformed

    private void btnKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhachHangActionPerformed
        // TODO add your handling code here:
        new main_khachHang().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnKhachHangActionPerformed

    private void btnQLPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQLPhongActionPerformed
        // TODO add your handling code here:
        new main_trangThaiPhong(txt_TaiKhoan.getText()).setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnQLPhongActionPerformed

    private void btnDatPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatPhongActionPerformed
        // TODO add your handling code here:
        new main_datPhong(txt_TaiKhoan.getText()).setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnDatPhongActionPerformed

    private void btnDKDVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDKDVActionPerformed
        // TODO add your handling code here:
        new main_DkDichVu(txt_TaiKhoan.getText()).setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnDKDVActionPerformed

    private void btnTraPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTraPhongActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnTraPhongActionPerformed

    private void btnBaoCaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaoCaoActionPerformed
        // TODO add your handling code here:
        new main_thongKe(txt_TaiKhoan.getText()).setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnBaoCaoActionPerformed

    private void btnDangXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangXuatActionPerformed
        // TODO add your handling code here:
        new LoginForm().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnDangXuatActionPerformed

    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_btnThoatActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        staticModify.refresh();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
        showDoUong();
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void tblDoUongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDoUongMouseClicked
        // TODO add your handling code here:
        btnSua.setEnabled(true);
        btnSua.setEnabled(true);
        btnLuu.setEnabled(false);

        txtMaDoUong.setEnabled(false);
        txtMaDoUong.setEditable(false);
        int selectedIndex = tblDoUong.getSelectedRow();
        if (selectedIndex >= 0) {
            doUong std = list.get(selectedIndex);
            txtMaDoUong.setText(std.getMaDoUong());
            txtTenDoUong.setText(std.getTenDoUong());
            txtGia.setText(String.valueOf(std.getGia()));
        }
    }//GEN-LAST:event_tblDoUongMouseClicked

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        btnSua.setEnabled(false);
        saveUpdate();
        autoNullColumn();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnDoUongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoUongActionPerformed
        // TODO add your handling code here:
        new main_doUong().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnDoUongActionPerformed

    private void btnDoAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoAnActionPerformed
        // TODO add your handling code here:
        new main_doUong().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnDoAnActionPerformed

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
            java.util.logging.Logger.getLogger(main_doUong.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main_doUong.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main_doUong.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main_doUong.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main_doUong().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBaoCao;
    private javax.swing.JButton btnDKDV;
    private javax.swing.JButton btnDangXuat;
    private javax.swing.JButton btnDatPhong;
    private javax.swing.JButton btnDoAn;
    private javax.swing.JButton btnDoUong;
    private javax.swing.JButton btnKhachHang;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnNhanVien;
    private javax.swing.JButton btnPhong;
    private javax.swing.JButton btnQLPhong;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnTaiKhoan;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThoat;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnTraPhong;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbMaDoAn;
    private javax.swing.JLabel lbTimKiem;
    private javax.swing.JTable tblDoUong;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtMaDoUong;
    private javax.swing.JTextField txtTenDoUong;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txt_TaiKhoan;
    // End of variables declaration//GEN-END:variables
}
