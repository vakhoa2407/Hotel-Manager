/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package viewsTop;

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

/**
 *
 * @author DELL
 */
public class main_nhanVien extends javax.swing.JFrame {

    /**
     * Creates new form main_taiKhoan
     */
    ConnectDB con = new ConnectDB();
    Connection conn;
    DefaultTableModel tableModel;
    int selectedIndex = 0;
    List<nhanVien> list = new ArrayList<>();

    public main_nhanVien(String username) {
        initComponents();
        txt_TaiKhoan.setText(username);

        tableModel = (DefaultTableModel) tblNhanVien.getModel();
        showNhanVien();
    }

    public main_nhanVien() {
        initComponents();
        txt_TaiKhoan.setText(staticModify.userName);
        tableModel = (DefaultTableModel) tblNhanVien.getModel();
        showNhanVien();
        if (staticModify.chucVu != 1) {
            btnThem.setEnabled(false);
            btnSua.setEnabled(false);
            btnLuu.setEnabled(false);
            tblNhanVien.setEnabled(false);
        }
    }

    public void autoNullColumn() {
        txtMaNV.setText("");
        txtTenNV.setText("");
        txtCCCD.setText("");
        txtSDT.setText("");
    }

    private void addNhanVien() {
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
            txtMaNV.requestFocus(true);
            txtMaNV.selectAll();
        } else if (txtTenNV.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Tên nhân viên không được trống");
            txtTenNV.requestFocus(true);
            txtTenNV.selectAll();
        } else if (txtCCCD.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "CMT/CCCD không được trống");
            txtCCCD.requestFocus(true);
            txtCCCD.selectAll();
        } else if (!txtCCCD.getText().trim().matches("^\\d{12}$")) {
            JOptionPane.showMessageDialog(this, "CMT/CCCD không đúng định dạng");
            txtCCCD.requestFocus(true);
            txtCCCD.selectAll();
        } else if (txtSDT.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được trống");
            txtSDT.requestFocus(true);
            txtSDT.selectAll();
        } //"^0\\d{9}"
        else if (!txtSDT.getText().trim().matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không đúng định dạng");
            txtSDT.requestFocus(true);
            txtSDT.selectAll();
        } else {
            conn = con.getConnection();
            System.out.println(ngaySinh.toString());
            System.out.println(txtGioiTinh);

            try {
                String sql = "{call addNhanVien (?, ?, ?, ?, ?, ?, ?)}";
                CallableStatement statement = conn.prepareCall(sql);

                statement.setString(1, txtMaNV.getText());
                statement.setString(2, txtTenNV.getText());
                statement.setString(3, ngaySinh.toString());
                statement.setString(4, txtGioiTinh);
                statement.setInt(5, chucVu);
                statement.setString(6, txtCCCD.getText());
                statement.setString(7, txtSDT.getText());

                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("ketqua") == -3) {
                        System.out.println("Mã nhân viên đã tồn tại");
                        JOptionPane.showMessageDialog(this, "Mã nhân viên đã tồn tại");
                        txtMaNV.requestFocus(true);
                        txtMaNV.selectAll();
                    } else if (rs.getInt("ketqua") == -2) {
                        System.out.println("Ngày sinh không hợp lệ\n(Nhân viên chưa đủ 18 tuổi)");
                        JOptionPane.showMessageDialog(this, "Ngày sinh không hợp lệ\n(Nhân viên chưa đủ 18 tuổi)");
                        dateNgaySinh.requestFocus(true);
                    } else if (rs.getInt("ketqua") == -1) {
                        System.out.println("CMT/CCCD đã tồn tại");
                        JOptionPane.showMessageDialog(this, "CMT/CCCD đã tồn tại");
                        txtCCCD.requestFocus(true);
                        txtCCCD.selectAll();
                    } else if (rs.getInt("ketqua") == 0) {
                        System.out.println("Số điện thoại đã tồn tại\nhoặc nhập không đúng định dạng");
                        JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại\nhoặc nhập không đúng định dạng");
                        txtSDT.requestFocus(true);
                        txtSDT.selectAll();
                    } else if (rs.getInt("ketqua") == 1) {
                        System.out.println("Thêm nhân viên thành công");
                        JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công");
                        btnLuu.setEnabled(false);
                        autoNullColumn();
                    }
                }
            } catch (Exception e) {
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                        System.out.println("Đóng kết nối thành công");
                    } catch (SQLException ex) {
                        Logger.getLogger(main_nhanVien.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        showNhanVien();
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
            txtMaNV.requestFocus(true);
            txtMaNV.selectAll();
        } else if (txtTenNV.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Tên nhân viên không được trống");
            txtTenNV.requestFocus(true);
            txtTenNV.selectAll();
        } else if (txtCCCD.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "CMT/CCCD không được trống");
            txtCCCD.requestFocus(true);
            txtCCCD.selectAll();
        } else if (!txtCCCD.getText().trim().matches("^\\d{12}$")) {
            JOptionPane.showMessageDialog(this, "CMT/CCCD không đúng định dạng");
            txtCCCD.requestFocus(true);
            txtCCCD.selectAll();
        } else if (txtSDT.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được trống");
            txtSDT.requestFocus(true);
            txtSDT.selectAll();
        } //"^0\\d{9}"
        else if (!txtSDT.getText().trim().matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không đúng định dạng");
            txtSDT.requestFocus(true);
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
                statement.setString(6, txtCCCD.getText());
                statement.setString(7, txtSDT.getText());

                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("ketqua") == -3) {
                        System.out.println("Mã nhân viên không tồn tại");
                        JOptionPane.showMessageDialog(this, "Mã nhân viên không tồn tại");
                    } else if (rs.getInt("ketqua") == -2) {
                        System.out.println("Ngày sinh không hợp lệ\n(Nhân viên chưa đủ 18 tuổi)");
                        JOptionPane.showMessageDialog(this, "Ngày sinh không hợp lệ\n(Nhân viên chưa đủ 18 tuổi)");
                        dateNgaySinh.requestFocus(true);
                    } else if (rs.getInt("ketqua") == -1) {
                        System.out.println("CMT/CCCD đã tồn tại");
                        JOptionPane.showMessageDialog(this, "CMT/CCCD đã tồn tại");
                    } else if (rs.getInt("ketqua") == 0) {
                        System.out.println("Số điện thoại đã tồn tại\nhoặc nhập không đúng định dạng");
                        JOptionPane.showMessageDialog(this, "Số điện thoại đã tồn tại\nhoặc nhập không đúng định dạng");
                    } else if (rs.getInt("ketqua") == 1) {
                        System.out.println("Cập nhật dữ liệu thành công");
                        JOptionPane.showMessageDialog(this, "Cập nhật dữ liệu thành công");
                        btnSua.setEnabled(false);
                        autoNullColumn();
                    }
                }
            } catch (Exception e) {
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                        System.out.println("Đóng kết nối thành công");
                    } catch (SQLException ex) {
                        Logger.getLogger(main_nhanVien.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        showNhanVien();
    }

    public void showNhanVien() {

        nhanVien test = new nhanVien();
        list = test.findAll(txtTimKiem.getText().trim());

        tableModel.setRowCount(0); // Đặt lại số hàng là 0 (xóa tất cả dữ liệu)

        list.forEach((nhanVien) -> {
            tableModel.addRow(new Object[]{/*tableModel.getRowCount() + 1*/
                nhanVien.getMaNV(), nhanVien.getTenNV(), nhanVien.getNgaySinh(),
                nhanVien.getGioiTinh(), nhanVien.getChucVu(), nhanVien.getCMT(), nhanVien.getSDT()
            });
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
        btnTrangThaiPhong = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnDatPhong = new javax.swing.JButton();
        btnDKDV = new javax.swing.JButton();
        btnThongKe = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        txt_TaiKhoan = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btnTimKiem = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        lbTimKiem = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtTenNV = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        txtCCCD = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtSDT = new javax.swing.JTextField();
        cbGioiTinh = new javax.swing.JComboBox<>();
        dateNgaySinh = new com.toedter.calendar.JDateChooser();
        cbChucVu = new javax.swing.JComboBox<>();
        btnAnUong2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("QUẢN LÝ NHÂN VIÊN");
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
        getContentPane().add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 30, 130, -1));

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
        lbTimKiem.setText("Tìm kiếm theo mã nhân viên:");

        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });

        jLabel5.setText("Mã nhân viên:");

        jLabel6.setText("Tên nhân viên:");

        jLabel7.setText("Ngày sinh:");

        jLabel8.setText("Chức vụ:");

        jLabel9.setText("Giới tính:");

        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã nhân viên", "Tên nhân viên", "Ngày sinh", "Giới tính", "Chức vụ", "CMT/CCCD", "Số điện thoại"
            }
        ));
        tblNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNhanVienMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblNhanVien);

        jLabel10.setText("CMT/CCCD:");

        jLabel11.setText("Số điện thoại:");

        cbGioiTinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ" }));

        dateNgaySinh.setDateFormatString("yyyy-MM-dd");

        cbChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1" }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTenNV, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                            .addComponent(txtMaNV)
                            .addComponent(txtSDT, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dateNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(36, 36, 36)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(33, 33, 33))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lbTimKiem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCCCD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                    .addComponent(cbGioiTinh, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbChucVu, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(482, 482, 482))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(254, 254, 254)
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(cbGioiTinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(cbChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtCCCD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(dateNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSDT)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLuu)
                    .addComponent(btnSua)
                    .addComponent(btnThem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, 830, 500));

        btnAnUong2.setText("Đồ ăn / Đồ uống / Loại phòng");
        btnAnUong2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnUong2ActionPerformed(evt);
            }
        });
        getContentPane().add(btnAnUong2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 200, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/green.png"))); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(1280, 640));
        jLabel1.setMinimumSize(new java.awt.Dimension(1280, 640));
        jLabel1.setPreferredSize(new java.awt.Dimension(1280, 640));
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 640));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        // TODO add your handling code here:
        addNhanVien();
    }//GEN-LAST:event_btnLuuActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        autoNullColumn();
        txtMaNV.requestFocus(true);
        txtMaNV.selectAll();
        txtMaNV.setEnabled(true);
        txtMaNV.setEditable(true);
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

    private void btnDatPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatPhongActionPerformed
        // TODO add your handling code here:
        new main_datPhong().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnDatPhongActionPerformed

    private void btnDKDVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDKDVActionPerformed
        // TODO add your handling code here:
        new main_DkDichVu().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnDKDVActionPerformed

    private void btnThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongKeActionPerformed
        // TODO add your handling code here:
        new main_thongKe(txt_TaiKhoan.getText()).setVisible(true);
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

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
        showNhanVien();
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void tblNhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhanVienMouseClicked
        // TODO add your handling code here:
        if (staticModify.chucVu == 1) {
            btnSua.setEnabled(true);
        }
        btnLuu.setEnabled(false);

        txtMaNV.setEnabled(false);
        txtMaNV.setEditable(false);
        selectedIndex = tblNhanVien.getSelectedRow();
        if (selectedIndex >= 0) {
            nhanVien std = list.get(selectedIndex);
            txtMaNV.setText(std.getMaNV());
            txtTenNV.setText(std.getTenNV());
            SimpleDateFormat only_year = new SimpleDateFormat("yyyy");

            try {
                java.util.Date dateTest = new SimpleDateFormat("yyyy-MM-dd").parse(std.getNgaySinh());
                dateNgaySinh.setDate(dateTest);

                String year_prev = only_year.format(dateTest.getTime());
                System.out.println("Year previous: " + year_prev);

            } catch (ParseException ex) {
                Logger.getLogger(main_nhanVien.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (std.getGioiTinh().trim().equals("Nam")) {
                cbGioiTinh.setSelectedIndex(0);
            } else {
                cbGioiTinh.setSelectedIndex(1);
            }
            if (std.getChucVu() == 0) {
                cbChucVu.setSelectedIndex(0);
            } else {
                cbChucVu.setSelectedIndex(1);
            }
            txtCCCD.setText(std.getCMT());
            txtSDT.setText(std.getSDT());
        }
    }//GEN-LAST:event_tblNhanVienMouseClicked

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        saveUpdate();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnAnUong2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnUong2ActionPerformed
        // TODO add your handling code here:
        new main_AUL().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnAnUong2ActionPerformed

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
            java.util.logging.Logger.getLogger(main_nhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main_nhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main_nhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main_nhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main_nhanVien().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnUong2;
    private javax.swing.JButton btnDKDV;
    private javax.swing.JButton btnDatPhong;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnKhachHang;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnNhanVien;
    private javax.swing.JButton btnPhong;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnTaiKhoan;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThongKe;
    private javax.swing.JButton btnTimKiem;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbTimKiem;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JTextField txtCCCD;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTenNV;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txt_TaiKhoan;
    // End of variables declaration//GEN-END:variables
}
