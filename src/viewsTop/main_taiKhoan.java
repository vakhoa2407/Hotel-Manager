/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package viewsTop;

import connectDatabase.ConnectDB;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import viewsLeft.*;
import staticModify.*;

/**
 *
 * @author DELL
 */
public class main_taiKhoan extends javax.swing.JFrame {

    /**
     * Creates new form main_taiKhoan
     */
    ConnectDB con = new ConnectDB();
    Connection conn;

    public main_taiKhoan(String username) {
        initComponents();
        txt_TaiKhoan.setText(username);
        showInformation(username);
    }

    public main_taiKhoan() {
        initComponents();
        txt_TaiKhoan.setText(staticModify.userName);
        showInformation(staticModify.userName);
        staticModify.maNV = txtMaNV.getText();
        System.out.println("Mã nhân viên đăng nhập: " + staticModify.maNV);
        staticModify.chucVu = cbChucVu.getSelectedIndex();
        System.out.println("Chức vụ của nhân viên: " + staticModify.chucVu);
    }

    private void autoEnableEditTrue() {
        cbChucVu.setEnabled(true);
        cbChucVu.setEditable(true);

        txtTenNV.setEnabled(true);
        txtTenNV.setEditable(true);

        cbGioiTinh.setEnabled(true);
        cbGioiTinh.setEditable(true);

        txtCMT.setEnabled(true);
        txtCMT.setEditable(true);

        txtSDT.setEnabled(true);
        txtSDT.setEditable(true);
    }

    private void autoEnableEditFalse() {
        cbChucVu.setEnabled(false);
        cbChucVu.setEditable(false);

        txtMaNV.setEnabled(false);
        txtMaNV.setEditable(false);

        txtTenNV.setEnabled(false);
        txtTenNV.setEditable(false);

        cbGioiTinh.setEnabled(false);
        cbGioiTinh.setEditable(false);

        txtCMT.setEnabled(false);
        txtCMT.setEditable(false);

        txtSDT.setEnabled(false);
        txtSDT.setEditable(false);
    }

    private void danhSachNhanVien() {
        new main_nhanVien().setVisible(true);
        this.setVisible(false);
    }

    private void showInformation(String username) {
        conn = con.getConnection();

        String sql = " select a.maNV,a.tenNV,a.ngaySinh,a.gioiTinh,a.chucVu,a.cmt,a.sdt from tbl_nhanvien a, tbl_taikhoan b \n"
                + "  where a.maNV = b.maNV and b.taiKhoan='" + username + "' \n"
                + "  group by a.maNV,a.tenNV,a.ngaySinh,a.gioiTinh,a.chucVu,a.cmt,a.sdt;";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String maNV = rs.getString("maNV");
                txtMaNV.setText(maNV);
                String tenNV = rs.getString("tenNV");
                txtTenNV.setText(tenNV);
                String ngaySinh = rs.getString("ngaySinh");
                //
                try {
                    java.util.Date dateTest = new SimpleDateFormat("yyyy-MM-dd").parse(ngaySinh);
                    dateNgaySinh.setDate(dateTest);
                } catch (ParseException ex) {
                    Logger.getLogger(main_taiKhoan.class.getName()).log(Level.SEVERE, null, ex);
                }
                //
                String gioiTinh = rs.getString("gioiTinh");
                if (gioiTinh.equals("Nam")) {
                    cbChucVu.setSelectedIndex(0);
                } else {
                    cbChucVu.setSelectedIndex(1);
                }
                String chucVu = rs.getString("chucVu");
                if (chucVu.equals("0")) {
                    cbChucVu.setSelectedIndex(0);
                } else if (chucVu.equals("1")) {
                    cbChucVu.setSelectedIndex(1);
                }
                String CMT = rs.getString("cmt");
                txtCMT.setText(CMT);
                String SDT = rs.getString("sdt");
                txtSDT.setText(SDT);
            }
        } catch (Exception e) {

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Đóng kết nối thành công");
                } catch (SQLException ex) {
                    Logger.getLogger(main_taiKhoan.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void saveUpdate() {
        java.sql.Date ngaySinh = new java.sql.Date(dateNgaySinh.getDate().getTime());

        int chucVu = 0;
        if (cbChucVu.getSelectedIndex() == 0) {
            chucVu = 0;
        } else if (cbChucVu.getSelectedIndex() == 1) {
            chucVu = 1;
        }

        String txtGioiTinh = "Nam";
        if (cbGioiTinh.getSelectedIndex() == 0) {
            txtGioiTinh = "Nam";
        } else if (cbGioiTinh.getSelectedIndex() == 1) {
            txtGioiTinh = "Nu";
        }
        if (txtMaNV.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên không được trống");
            txtMaNV.requestFocus();
            txtMaNV.selectAll();
        } else if (txtTenNV.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Tên nhân viên không được trống");
            txtTenNV.requestFocus();
            txtTenNV.selectAll();
        } else if (txtCMT.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "CMT/CCCD không được trống");
            txtCMT.requestFocus();
            txtCMT.selectAll();
        } else if (!txtCMT.getText().trim().matches("^\\d{12}$")) {
            JOptionPane.showMessageDialog(this, "CMT/CCCD không đúng định dạng");
            txtCMT.requestFocus();
            txtCMT.selectAll();
        } else if (txtSDT.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được trống");
            txtSDT.requestFocus();
            txtSDT.selectAll();
        } //"^0\\d{9}"
        else if (!txtSDT.getText().trim().matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không đúng định dạng");
            txtSDT.requestFocus();
            txtSDT.selectAll();
        } else {
            conn = con.getConnection();
            System.out.println(ngaySinh.toString());
            System.out.println(txtGioiTinh);

            try {
                String sql = "{call updateNhanVien (?, ?, ?, ?, ?, ?, ?)}";
                CallableStatement statement = conn.prepareCall(sql);

                statement.setString(1, txtMaNV.getText());
                statement.setString(2, txtTenNV.getText());
                statement.setString(3, ngaySinh.toString());
                statement.setString(4, txtGioiTinh);
                statement.setInt(5, chucVu);
                statement.setString(6, txtCMT.getText());
                statement.setString(7, txtSDT.getText());

                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("ketqua") == -3) {
                        System.out.println("Mã nhân viên không tồn tại");
                        JOptionPane.showMessageDialog(this, "Mã nhân viên không tồn tại");
                    } else if (rs.getInt("ketqua") == -2) {
                        System.out.println("Ngày sinh không hợp lệ\n(Nhân viên chưa đủ 18 tuổi)");
                        JOptionPane.showMessageDialog(this, "Ngày sinh không hợp lệ\n(Nhân viên chưa đủ 18 tuổi)");
                        dateNgaySinh.requestFocus();
                    } else if (rs.getInt("ketqua") == -1) {
                        System.out.println("CMT/CCCD đã tồn tại");
                        JOptionPane.showMessageDialog(this, "CMT/CCCD đã tồn tại");
                    } else if (rs.getInt("ketqua") == 0) {
                        System.out.println("Số điện thoại đã tồn tại\nhoặc nhập không đúng định dạng");
                        JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại\nhoặc nhập không đúng định dạng");
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
                        Logger.getLogger(main_taiKhoan.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        showInformation(staticModify.userName);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnTaiKhoan = new javax.swing.JButton();
        btnNhanVien = new javax.swing.JButton();
        btnKhachHang = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txt_TaiKhoan = new javax.swing.JTextField();
        btnRefresh = new javax.swing.JButton();
        btnPhong = new javax.swing.JButton();
        btnDatPhong = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnDKDV = new javax.swing.JButton();
        btnThongKe = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnTrangThaiPhong = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnDanhSach = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTenNV = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtCMT = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        dateNgaySinh = new com.toedter.calendar.JDateChooser();
        cbChucVu = new javax.swing.JComboBox<>();
        lbAdmin = new javax.swing.JLabel();
        cbGioiTinh = new javax.swing.JComboBox<>();
        btnDoAn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("THÔNG TIN TÀI KHOẢN");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnTaiKhoan.setText("Tài Khoản");
        btnTaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaiKhoanActionPerformed(evt);
            }
        });
        getContentPane().add(btnTaiKhoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 110, -1));

        btnNhanVien.setText("Nhân Viên");
        btnNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNhanVienActionPerformed(evt);
            }
        });
        getContentPane().add(btnNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 30, 110, -1));

        btnKhachHang.setText("Khách Hàng");
        btnKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhachHangActionPerformed(evt);
            }
        });
        getContentPane().add(btnKhachHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 30, 110, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Tài khoản:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 76, 80, 20));

        txt_TaiKhoan.setAlignmentX(1.0F);
        txt_TaiKhoan.setAutoscrolls(false);
        txt_TaiKhoan.setPreferredSize(new java.awt.Dimension(120, 22));
        txt_TaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_TaiKhoanActionPerformed(evt);
            }
        });
        getContentPane().add(txt_TaiKhoan, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 150, 30));

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        getContentPane().add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 30, 130, -1));

        btnPhong.setText("Phòng");
        btnPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhongActionPerformed(evt);
            }
        });
        getContentPane().add(btnPhong, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 30, 110, -1));

        btnDatPhong.setText("Đặt phòng");
        btnDatPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatPhongActionPerformed(evt);
            }
        });
        getContentPane().add(btnDatPhong, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 390, 220, -1));

        jLabel4.setBackground(new java.awt.Color(20, 56, 20));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/rsz_xd.png"))); // NOI18N
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel4.setOpaque(true);
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, 220, 200));

        btnDKDV.setText("Đặt đồ ăn / đồ uống");
        btnDKDV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDKDVActionPerformed(evt);
            }
        });
        getContentPane().add(btnDKDV, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 430, 220, -1));

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

        btnTrangThaiPhong.setText("Trạng thái phòng theo ngày");
        btnTrangThaiPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTrangThaiPhongActionPerformed(evt);
            }
        });
        getContentPane().add(btnTrangThaiPhong, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 220, -1));

        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("THÔNG TIN TÀI KHOẢN:");

        btnDanhSach.setText("Danh sách nhân viên");
        btnDanhSach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDanhSachActionPerformed(evt);
            }
        });
        btnDanhSach.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnDanhSachKeyPressed(evt);
            }
        });

        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });
        btnSua.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSuaKeyPressed(evt);
            }
        });

        btnLuu.setText("Lưu");
        btnLuu.setEnabled(false);
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });
        btnLuu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnLuuKeyPressed(evt);
            }
        });

        jLabel6.setText("Mã nhân viên:");

        txtMaNV.setEditable(false);
        txtMaNV.setText("jTextField1");
        txtMaNV.setEnabled(false);

        jLabel7.setText("Tên nhân viên:");

        txtTenNV.setEditable(false);
        txtTenNV.setText("jTextField2");
        txtTenNV.setEnabled(false);

        jLabel8.setText("Ngày sinh:");

        jLabel9.setText("Giới tính:");

        jLabel10.setText("CMT/CCCD:");

        txtCMT.setEditable(false);
        txtCMT.setText("jTextField5");
        txtCMT.setEnabled(false);

        jLabel11.setText("SĐT:");

        txtSDT.setEditable(false);
        txtSDT.setText("jTextField6");
        txtSDT.setEnabled(false);
        txtSDT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSDTActionPerformed(evt);
            }
        });

        dateNgaySinh.setAutoscrolls(true);
        dateNgaySinh.setDateFormatString("yyyy-MM-dd");

        cbChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1" }));
        cbChucVu.setEnabled(false);

        lbAdmin.setText("Admin:");

        cbGioiTinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));
        cbGioiTinh.setEnabled(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(32, 32, 32)
                                        .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtSDT, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtCMT, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(dateNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                                            .addComponent(cbGioiTinh, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(62, 62, 62))))
                            .addComponent(jLabel10)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(lbAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cbChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtTenNV))
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(244, 244, 244)
                        .addComponent(btnDanhSach))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(213, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbAdmin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(dateNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cbGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(txtCMT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(256, 256, 256))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSua)
                            .addComponent(btnLuu))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDanhSach)
                        .addGap(171, 171, 171))))
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 120, 680, 510));

        btnDoAn.setText("Đồ ăn / Đồ uống / Loại Phòng");
        btnDoAn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoAnActionPerformed(evt);
            }
        });
        getContentPane().add(btnDoAn, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 200, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/green.png"))); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(1280, 640));
        jLabel1.setMinimumSize(new java.awt.Dimension(1280, 640));
        jLabel1.setPreferredSize(new java.awt.Dimension(1280, 640));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 640));

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void btnPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhongActionPerformed
        // TODO add your handling code here:
        new main_phong().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnPhongActionPerformed

    private void btnDanhSachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDanhSachActionPerformed
        // TODO add your handling code here:
        danhSachNhanVien();
    }//GEN-LAST:event_btnDanhSachActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        autoEnableEditTrue();
        btnLuu.setEnabled(true);
    }//GEN-LAST:event_btnSuaActionPerformed

    private void txtSDTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSDTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSDTActionPerformed

    private void btnDKDVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDKDVActionPerformed
        // TODO add your handling code here:
        new main_DkDichVu().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnDKDVActionPerformed

    private void btnTaiKhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaiKhoanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTaiKhoanActionPerformed

    private void txt_TaiKhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_TaiKhoanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_TaiKhoanActionPerformed

    private void btnTrangThaiPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTrangThaiPhongActionPerformed
        // TODO add your handling code here:
        new main_trangThaiPhong().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnTrangThaiPhongActionPerformed

    private void btnDatPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatPhongActionPerformed
        // TODO add your handling code here:
        new main_datPhong().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnDatPhongActionPerformed

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

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        staticModify.refresh();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnSuaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSuaKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("Nhấn Phím enter sửa");
            autoEnableEditTrue();
            btnLuu.setEnabled(true);
        }
    }//GEN-LAST:event_btnSuaKeyPressed

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        // TODO add your handling code here:
        autoEnableEditFalse();
        btnLuu.setEnabled(false);
        saveUpdate();

    }//GEN-LAST:event_btnLuuActionPerformed

    private void btnLuuKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnLuuKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("Nhấn Phím enter lưu");
            autoEnableEditFalse();
            btnLuu.setEnabled(false);
            saveUpdate();
        }
    }//GEN-LAST:event_btnLuuKeyPressed

    private void btnDanhSachKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDanhSachKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("Nhấn Phím enter danh sách nhân viên");
            danhSachNhanVien();
        }

    }//GEN-LAST:event_btnDanhSachKeyPressed

    private void btnDoAnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoAnActionPerformed
        // TODO add your handling code here:
        new main_AUL().setVisible(true);
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
            java.util.logging.Logger.getLogger(main_taiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main_taiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main_taiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main_taiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main_taiKhoan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDKDV;
    private javax.swing.JButton btnDanhSach;
    private javax.swing.JButton btnDatPhong;
    private javax.swing.JButton btnDoAn;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnKhachHang;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnNhanVien;
    private javax.swing.JButton btnPhong;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnTaiKhoan;
    private javax.swing.JButton btnThongKe;
    private javax.swing.JButton btnTrangThaiPhong;
    private javax.swing.JComboBox<String> cbChucVu;
    private javax.swing.JComboBox<String> cbGioiTinh;
    private com.toedter.calendar.JDateChooser dateNgaySinh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lbAdmin;
    private javax.swing.JTextField txtCMT;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTenNV;
    private javax.swing.JTextField txt_TaiKhoan;
    // End of variables declaration//GEN-END:variables
}
