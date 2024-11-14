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
public class main_AUL extends javax.swing.JFrame {

    /**
     * Creates new form main_taiKhoan
     */
    ConnectDB con = new ConnectDB();
    Connection conn;
    DefaultTableModel tableModel;

    List<doUong> listDoUong = new ArrayList<>();
    List<doAn> listDoAn = new ArrayList<>();
    List<loaiPhong> listLoaiPhong = new ArrayList<>();

    String txtNameAUL = "đồ ăn";
    String[] txtAUL = {"đồ ăn", "đồ uống", "loại phòng"};
    String procAdd = "addDoAn";
    String procUpdate = "updateDoAn";
    String procLP[] = {"addLoaiPhong", "updateLoaiPhong"};
    String procDA[] = {"addDoAn", "updateDoAn"};
    String procDU[] = {"addDoUong", "updateDoUong"};

    public main_AUL(String username) {
        initComponents();
        txt_TaiKhoan.setText(username);

        tableModel = (DefaultTableModel) tblAUL.getModel();
        showAUL();
    }

    public main_AUL() {
        initComponents();
        txt_TaiKhoan.setText(staticModify.userName);
        tableModel = (DefaultTableModel) tblAUL.getModel();
        showAUL();

    }

    public void autoNullColumn() {
        txtTimKiem.setText("");
        txtMaDichVu.setText("");
        txtTenDichVu.setText("");
        txtGia.setText("");
    }

    private void addAnOrUong() {
        if (txtMaDichVu.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Mã " + txtNameAUL + " không được trống");
            txtMaDichVu.requestFocus(true);
            txtMaDichVu.selectAll();
        } else if (txtTenDichVu.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Tên " + txtNameAUL + " không được trống");
            txtTenDichVu.requestFocus(true);
            txtTenDichVu.selectAll();
        } else if (txtGia.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Giá " + txtNameAUL + " không được trống");
            txtGia.requestFocus(true);
            txtGia.selectAll();
        } else if (!txtGia.getText().trim().matches("^[1-9][0-9]*$")) {
            JOptionPane.showMessageDialog(this, "Giá " + txtNameAUL + " phải > 0");
            txtGia.requestFocus(true);
            txtGia.selectAll();
        } else {
            conn = con.getConnection();
            try {
                String sql = "{call " + procAdd + " (?, ?, ?)}";
                CallableStatement statement = conn.prepareCall(sql);

                statement.setString(1, txtMaDichVu.getText());
                statement.setString(2, txtTenDichVu.getText());
                statement.setInt(3, Integer.valueOf(txtGia.getText().trim()));

                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("ketqua") == -1) {
                        System.out.println("Mã " + txtNameAUL + " đã tồn tại");
                        JOptionPane.showMessageDialog(this, "Mã " + txtNameAUL + " đã tồn tại");
                    } else if (rs.getInt("ketqua") == 0) {
                        System.out.println("Giá " + txtNameAUL + " phải dương");
                        JOptionPane.showMessageDialog(this, "Giá " + txtNameAUL + " phải dương");
                    } else if (rs.getInt("ketqua") == 1) {
                        System.out.println("Thêm đồ uống thành công");
                        JOptionPane.showMessageDialog(this, "Thêm " + txtNameAUL + " thành công");
                    }
                }
            } catch (Exception e) {
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                        System.out.println("Đóng kết nối thành công");
                    } catch (SQLException ex) {
                        Logger.getLogger(main_AUL.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        showAUL();
    }

    private void saveUpdate() {
        if (txtMaDichVu.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Mã " + txtNameAUL + " không được trống");
            txtMaDichVu.requestFocus(true);
            txtMaDichVu.selectAll();
        } else if (txtTenDichVu.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Tên " + txtNameAUL + " không được trống");
            txtTenDichVu.requestFocus(true);
            txtTenDichVu.selectAll();
        } else if (txtGia.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Giá " + txtNameAUL + " không được trống");
            txtGia.requestFocus(true);
            txtGia.selectAll();
        } else if (!txtGia.getText().trim().matches("^[1-9][0-9]*$")) {
            JOptionPane.showMessageDialog(this, "Giá " + txtNameAUL + " phải > 0");
            txtGia.requestFocus(true);
            txtGia.selectAll();
        } else {
            conn = con.getConnection();
            try {
                String sql = "{call " + procUpdate + " (?, ?, ?)}";
                CallableStatement statement = conn.prepareCall(sql);

                statement.setString(1, txtMaDichVu.getText());
                statement.setString(2, txtTenDichVu.getText());
                statement.setInt(3, Integer.valueOf(txtGia.getText().trim()));

                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("ketqua") == -1) {
                        System.out.println("Mã " + txtNameAUL + " không tồn tại");
                        JOptionPane.showMessageDialog(this, "Mã " + txtNameAUL + " không tồn tại");
                    } else if (rs.getInt("ketqua") == 0) {
                        System.out.println("Giá " + txtNameAUL + " phải dương");
                        JOptionPane.showMessageDialog(this, "Giá " + txtNameAUL + " phải dương");
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
                        Logger.getLogger(main_AUL.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        showAUL();
    }

    public void showAUL() {
        tableModel.setRowCount(0); // Đặt lại số hàng là 0 (xóa tất cả dữ liệu)
        if (cbDichVu.getSelectedIndex() == 0) {
            doAn tDoAn = new doAn();
            listDoAn = tDoAn.findAll(txtTimKiem.getText().trim());
            listDoAn.forEach((doAn) -> {
                tableModel.addRow(new Object[]{/*tableModel.getRowCount() + 1*/
                    doAn.getMaDoAn(), doAn.getTenDoAn(), doAn.getGia()});
            });
        } else if (cbDichVu.getSelectedIndex() == 1) {
            doUong tDoUong = new doUong();
            listDoUong = tDoUong.findAll(txtTimKiem.getText().trim());
            listDoUong.forEach((doUong) -> {
                tableModel.addRow(new Object[]{/*tableModel.getRowCount() + 1*/
                    doUong.getMaDoUong(), doUong.getTenDoUong(), doUong.getGia()});
            });
        } else if (cbDichVu.getSelectedIndex() == 2) {
            loaiPhong tLoaiPhong = new loaiPhong();
            listLoaiPhong = tLoaiPhong.findAll(txtTimKiem.getText().trim());
            listLoaiPhong.forEach((loaiPhong) -> {
                tableModel.addRow(new Object[]{/*tableModel.getRowCount() + 1*/
                    loaiPhong.getMaLoaiPhong(), loaiPhong.getMoTa(), loaiPhong.getGia()});
            });
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
        lbMaDichVu = new javax.swing.JLabel();
        txtMaDichVu = new javax.swing.JTextField();
        lbTenDichVu = new javax.swing.JLabel();
        txtTenDichVu = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAUL = new javax.swing.JTable();
        lbGia = new javax.swing.JLabel();
        txtGia = new javax.swing.JTextField();
        cbDichVu = new javax.swing.JComboBox<>();
        lbCbDichVu = new javax.swing.JLabel();
        btnAnUong = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DỊCH VỤ VÀ LOẠI PHÒNG");
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
        getContentPane().add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 30, 130, -1));

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
        getContentPane().add(btnNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 30, 110, -1));

        btnKhachHang.setText("Khách Hàng");
        btnKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhachHangActionPerformed(evt);
            }
        });
        getContentPane().add(btnKhachHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 30, 110, -1));

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
        lbTimKiem.setText("Tìm kiếm theo mã đồ đồ ăn:");

        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });

        lbMaDichVu.setText("Mã đồ ăn:");

        lbTenDichVu.setText("Tên đồ ăn:");

        tblAUL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã đồ ăn", "Tên đồ ăn", "Giá"
            }
        ));
        tblAUL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAULMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblAUL);

        lbGia.setText("Giá:");

        cbDichVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đồ ăn", "Đồ uống", "Loại Phòng" }));
        cbDichVu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbDichVuItemStateChanged(evt);
            }
        });

        lbCbDichVu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lbCbDichVu.setText("Lựa chọn:");

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
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lbTenDichVu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbMaDichVu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbGia, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTenDichVu, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                            .addComponent(txtMaDichVu)
                            .addComponent(txtGia, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnThem, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                                .addComponent(btnLuu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbTimKiem)
                            .addComponent(lbCbDichVu))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(cbDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(563, 563, 563))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbCbDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTimKiem)
                    .addComponent(lbTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbMaDichVu)
                    .addComponent(txtMaDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTenDichVu)
                    .addComponent(txtTenDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLuu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbGia, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSua))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(78, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 130, 830, 500));

        btnAnUong.setText("Đồ ăn / Đồ uống / Loại phòng");
        btnAnUong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnUongActionPerformed(evt);
            }
        });
        getContentPane().add(btnAnUong, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 200, -1));

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
        addAnOrUong();
        autoNullColumn();
    }//GEN-LAST:event_btnLuuActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        autoNullColumn();
        txtMaDichVu.requestFocus(true);
        txtMaDichVu.selectAll();
        txtMaDichVu.setEnabled(true);
        txtMaDichVu.setEditable(true);
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
        new main_AUL().setVisible(true);
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

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
        showAUL();
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void tblAULMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAULMouseClicked
        // TODO add your handling code here:
        btnSua.setEnabled(true);
        btnSua.setEnabled(true);
        btnLuu.setEnabled(false);

        txtMaDichVu.setEnabled(false);
        txtMaDichVu.setEditable(false);
        int selectedIndex = tblAUL.getSelectedRow();
        if (selectedIndex >= 0) {
            if (cbDichVu.getSelectedIndex() == 0) {
                doAn std = listDoAn.get(selectedIndex);
                txtMaDichVu.setText(std.getMaDoAn());
                txtTenDichVu.setText(std.getTenDoAn());
                txtGia.setText(String.valueOf(std.getGia()));
            } else if (cbDichVu.getSelectedIndex() == 1) {
                doUong std = listDoUong.get(selectedIndex);
                txtMaDichVu.setText(std.getMaDoUong());
                txtTenDichVu.setText(std.getTenDoUong());
                txtGia.setText(String.valueOf(std.getGia()));
            } else if (cbDichVu.getSelectedIndex() == 2) {
                loaiPhong std = listLoaiPhong.get(selectedIndex);
                txtMaDichVu.setText(std.getMaLoaiPhong());
                txtTenDichVu.setText(std.getMoTa());
                txtGia.setText(String.valueOf(std.getGia()));
            }
        }
    }//GEN-LAST:event_tblAULMouseClicked

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        btnSua.setEnabled(false);
        saveUpdate();
        autoNullColumn();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnAnUongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnUongActionPerformed
        // TODO add your handling code here:
        new main_AUL().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnAnUongActionPerformed

    private void cbDichVuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbDichVuItemStateChanged
        // TODO add your handling code here:
        autoNullColumn();
        if (cbDichVu.getSelectedIndex() == 0) {
            System.out.println("index 0");
            lbTimKiem.setText("Tìm kiếm theo mã đồ ăn:");
            lbMaDichVu.setText("Mã đồ ăn");
            lbTenDichVu.setText("Tên đồ ăn");

            txtNameAUL = txtAUL[0];
            procAdd = procDA[0];
            procUpdate = procDA[1];

            tblAUL.setModel(new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"Mã đồ ăn", "Tên đồ ăn", "Giá"}
            ) {
                boolean[] canEdit = new boolean[]{
                    false, false, false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            });
        } else if (cbDichVu.getSelectedIndex() == 1) {
            System.out.println("index 1");
            lbTimKiem.setText("Tìm kiếm theo mã đồ uống:");
            lbMaDichVu.setText("Mã đồ uống");
            lbTenDichVu.setText("Tên đồ uống");

            txtNameAUL = txtAUL[1];
            procAdd = procDU[0];
            procUpdate = procDU[1];

            tblAUL.setModel(new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"Mã đồ uống", "Tên đồ uống", "Giá"}
            ) {
                boolean[] canEdit = new boolean[]{
                    false, false, false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            });
        } else if (cbDichVu.getSelectedIndex() == 2) {
            System.out.println("index 2");
            lbTimKiem.setText("Tìm kiếm theo mã loại phòng:");
            lbMaDichVu.setText("Mã loại phòng");
            lbTenDichVu.setText("Mô tả");

            txtNameAUL = txtAUL[2];
            procAdd = procLP[0];
            procUpdate = procLP[1];

            tblAUL.setModel(new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"Mã loại phòng", "Mô tả", "Giá"}
            ) {
                boolean[] canEdit = new boolean[]{
                    false, false, false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            });
        }
        tableModel = (DefaultTableModel) tblAUL.getModel();
        showAUL();
    }//GEN-LAST:event_cbDichVuItemStateChanged

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
            java.util.logging.Logger.getLogger(main_AUL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main_AUL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main_AUL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main_AUL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new main_AUL().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnUong;
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
    private javax.swing.JComboBox<String> cbDichVu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbCbDichVu;
    private javax.swing.JLabel lbGia;
    private javax.swing.JLabel lbMaDichVu;
    private javax.swing.JLabel lbTenDichVu;
    private javax.swing.JLabel lbTimKiem;
    private javax.swing.JTable tblAUL;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtMaDichVu;
    private javax.swing.JTextField txtTenDichVu;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txt_TaiKhoan;
    // End of variables declaration//GEN-END:variables
}
