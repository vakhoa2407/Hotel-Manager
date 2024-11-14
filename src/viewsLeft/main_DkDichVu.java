/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package viewsLeft;

import connectDatabase.ConnectDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import viewsTop.*;
import model.*;
import staticModify.*;

/**
 *
 * @author DELL
 */
public class main_DkDichVu extends javax.swing.JFrame {

    /**
     * Creates new form main_taiKhoan
     */
    ConnectDB con = new ConnectDB();
    Connection conn;
    DefaultTableModel tableModelDV;
    List<doUong> listDoUong = new ArrayList<>();
    List<doAn> listDoAn = new ArrayList<>();

    DefaultTableModel tableModelDKDV;
    List<datDoAn> listDatDoAn = new ArrayList<>();
    List<datDoUong> listDatDoUong = new ArrayList<>();

    String txtNameDV = "đồ ăn";
    String[] txtDV = {"đồ ăn", "đồ uống"};
    String procAdd = "datDoAn";
    String procDA[] = {"datDoAn"};
    String procDU[] = {"datDoUong"};

    public main_DkDichVu(String username) {
        initComponents();
        txt_TaiKhoan.setText(username);
    }

    public main_DkDichVu() {
        initComponents();
        txt_TaiKhoan.setText(staticModify.userName);
        tableModelDV = (DefaultTableModel) tblDichVu.getModel();
        tableModelDKDV = (DefaultTableModel) tblDangKyDV.getModel();
        showInformation();
    }

    public void autoNullColumn() {
        txtMaPhong.setText("");
        txtMaDichVu.setText("");
        txtSoLuong.setText("");
    }

    private void addDKDV() {
        if (txtMaPhong.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Mã phòng không được trống");
            txtMaPhong.requestFocus(true);
            txtMaPhong.selectAll();
        } else if (txtMaDichVu.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Tên " + txtNameDV + " không được trống");
            txtMaDichVu.requestFocus(true);
            txtMaDichVu.selectAll();
        } else if (!txtSoLuong.getText().trim().matches("^[1-9][0-9]*$")) {
            JOptionPane.showMessageDialog(this, "Số lượng phải > 0");
            txtSoLuong.requestFocus(true);
            txtSoLuong.selectAll();
        } else {
            conn = con.getConnection();
            try {
                String sql = "{call " + procAdd + " (?, ?, ?)}";
                CallableStatement statement = conn.prepareCall(sql);

                statement.setString(1, txtMaPhong.getText().trim());
                statement.setString(2, txtMaDichVu.getText());
                statement.setInt(3, Integer.valueOf(txtSoLuong.getText().trim()));

                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("ketqua") == -2) {
                        System.out.println("Mã " + txtNameDV + " không tồn tại");
                        JOptionPane.showMessageDialog(this, "Mã " + txtNameDV + " không tồn tại");
                        txtMaDichVu.requestFocus(true);
                        txtMaDichVu.selectAll();
                    } else if (rs.getInt("ketqua") == -1) {
                        System.out.println("Số lượng phải dương");
                        JOptionPane.showMessageDialog(this, "Số lượng phải dương");
                        txtSoLuong.requestFocus(true);
                        txtSoLuong.selectAll();
                    } else if (rs.getInt("ketqua") == 0) {
                        System.out.println("Mã phòng không hợp lệ\n(Phòng trống/không tồn tại,...)");
                        JOptionPane.showMessageDialog(this, "Mã phòng không hợp lệ\n(Phòng trống/không tồn tại,...)");
                    } else if (rs.getInt("ketqua") == 1) {
                        System.out.println("Đặt " + txtNameDV + " thành công");
                        JOptionPane.showMessageDialog(this, "Đặt " + txtNameDV + " thành công");
                        btnDat.setEnabled(false);
                        showInformation();
                    }
                }
            } catch (Exception e) {
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                        System.out.println("Đóng kết nối thành công");
                    } catch (SQLException ex) {
                        Logger.getLogger(main_DkDichVu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public void showInformation() {
        tableModelDV.setRowCount(0); // Đặt lại số hàng là 0 (xóa tất cả dữ liệu)
        tableModelDKDV.setRowCount(0);
        if (cbDichVu.getSelectedIndex() == 0) {
            doAn tDoAn = new doAn();
            listDoAn = tDoAn.findAll(txtMaDichVu.getText().trim());
            listDoAn.forEach((doAn) -> {
                tableModelDV.addRow(new Object[]{/*tableModel.getRowCount() + 1*/
                    doAn.getMaDoAn(), doAn.getTenDoAn(), doAn.getGia()});
            });

            datDoAn tDatDoAn = new datDoAn();
            listDatDoAn = tDatDoAn.findAll(txtMaDichVu.getText().trim());
            listDatDoAn.forEach((datDoAn) -> {
                tableModelDKDV.addRow(new Object[]{/*tableModel.getRowCount() + 1*/
                    datDoAn.getNgay(), datDoAn.getMaPhong(),
                    datDoAn.getMaDA(), datDoAn.getSoLuong()});
            });
        } else if (cbDichVu.getSelectedIndex() == 1) {
            doUong tDoUong = new doUong();
            listDoUong = tDoUong.findAll(txtMaDichVu.getText().trim());
            listDoUong.forEach((doUong) -> {
                tableModelDV.addRow(new Object[]{/*tableModel.getRowCount() + 1*/
                    doUong.getMaDoUong(), doUong.getTenDoUong(), doUong.getGia()});
            });

            datDoUong tDatDoUong = new datDoUong();
            listDatDoUong = tDatDoUong.findAll(txtMaDichVu.getText().trim());
            listDatDoUong.forEach((datDoUong) -> {
                tableModelDKDV.addRow(new Object[]{/*tableModel.getRowCount() + 1*/
                    datDoUong.getNgay(), datDoUong.getMaPhong(),
                    datDoUong.getMaDU(), datDoUong.getSoLuong()});
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

        btnTaiKhoan = new javax.swing.JButton();
        btnPhong = new javax.swing.JButton();
        btnNhanVien = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        btnTimKiem = new javax.swing.JButton();
        lbDichVu = new javax.swing.JLabel();
        cbDichVu = new javax.swing.JComboBox<>();
        txtMaPhong = new javax.swing.JTextField();
        lbMaPhong = new javax.swing.JLabel();
        lbSoLuong = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        lbMaDV = new javax.swing.JLabel();
        txtMaDichVu = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDangKyDV = new javax.swing.JTable();
        btnDat = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDichVu = new javax.swing.JTable();
        lbInfoDV = new javax.swing.JLabel();
        lbInfoDKDV = new javax.swing.JLabel();
        btnThem = new javax.swing.JButton();
        btnKhachHang = new javax.swing.JButton();
        btnTrangThaiPhong = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnDatPhong = new javax.swing.JButton();
        btnDkdv = new javax.swing.JButton();
        btnThongKe = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        txt_TaiKhoan = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnAUL = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ĐĂNG KÝ DỊCH VỤ");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        getContentPane().add(btnPhong, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, 110, -1));

        btnNhanVien.setText("Nhân Viên");
        btnNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNhanVienActionPerformed(evt);
            }
        });
        getContentPane().add(btnNhanVien, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 30, 110, -1));

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        getContentPane().add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 30, 130, -1));

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setText("ĐĂNG KÍ DỊCH VỤ:");

        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        lbDichVu.setText("Dịch vụ:");

        cbDichVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Đồ ăn", "Đồ uống" }));
        cbDichVu.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbDichVuItemStateChanged(evt);
            }
        });

        lbMaPhong.setText("Mã phòng:");

        lbSoLuong.setText("Số lượng:");

        lbMaDV.setText("Mã đồ ăn:");

        tblDangKyDV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ngày", "Mã phòng", "Mã đồ ăn", "Số lượng"
            }
        ));
        tblDangKyDV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDangKyDVMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDangKyDV);

        btnDat.setText("Đặt");
        btnDat.setEnabled(false);
        btnDat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatActionPerformed(evt);
            }
        });

        tblDichVu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã đồ ăn", "Tên đồ ăn", "Giá"
            }
        ));
        tblDichVu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDichVuMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblDichVu);

        lbInfoDV.setText("Thông tin đồ ăn:");

        lbInfoDKDV.setText("Thông tin đăng ký đồ ăn:");

        btnThem.setText("Đặt mới");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbMaDV, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMaDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtMaPhong)
                                        .addComponent(cbDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnThem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(274, 274, 274))))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbInfoDV, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbInfoDKDV, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbDichVu))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbMaPhong)
                    .addComponent(btnThem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbMaDV)
                    .addComponent(txtMaDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbSoLuong)
                    .addComponent(btnDat))
                .addGap(16, 16, 16)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbInfoDV)
                    .addComponent(lbInfoDKDV))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 67, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 120, 830, 510));

        btnKhachHang.setText("Khách Hàng");
        btnKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhachHangActionPerformed(evt);
            }
        });
        getContentPane().add(btnKhachHang, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 30, 110, -1));

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
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, 220, 200));

        btnDatPhong.setText("Đặt phòng");
        btnDatPhong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDatPhongActionPerformed(evt);
            }
        });
        getContentPane().add(btnDatPhong, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 390, 220, -1));

        btnDkdv.setText("Đặt đồ ăn / đồ uống");
        btnDkdv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDkdvActionPerformed(evt);
            }
        });
        getContentPane().add(btnDkdv, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 430, 220, -1));

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
        getContentPane().add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 570, 220, -1));

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
        getContentPane().add(btnAUL, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 30, 200, -1));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/green.png"))); // NOI18N
        jLabel1.setText("import viewsTop.*;");
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

    private void btnDatPhongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatPhongActionPerformed
        // TODO add your handling code here:
        new main_datPhong().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnDatPhongActionPerformed

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
        // TODO add your handling code here:
        staticModify.refresh();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnAULActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAULActionPerformed
        // TODO add your handling code here:
        new main_AUL().setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnAULActionPerformed

    private void btnDatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDatActionPerformed
        // TODO add your handling code here:
        addDKDV();
    }//GEN-LAST:event_btnDatActionPerformed

    private void cbDichVuItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbDichVuItemStateChanged
        // TODO add your handling code here:
        autoNullColumn();
        if (cbDichVu.getSelectedIndex() == 0) {
            System.out.println("index 0");
            lbMaDV.setText("Mã đồ ăn:");
            lbInfoDV.setText("Thông tin đồ ăn:");
            lbInfoDKDV.setText("Thông tin đăng ký đồ ăn:");
            //
            txtNameDV = txtDV[0];
            procAdd = procDA[0];
            //
            tblDichVu.setModel(new DefaultTableModel(
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
            tblDangKyDV.setModel(new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"Ngày", "Mã phòng", "Mã đồ ăn", "Số lượng"}
            ) {
                boolean[] canEdit = new boolean[]{
                    false, false, false, false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            });
        } else if (cbDichVu.getSelectedIndex() == 1) {
            System.out.println("index 1");
            lbMaDV.setText("Mã đồ uống:");
            lbInfoDV.setText("Thông tin đồ uống:");
            lbInfoDKDV.setText("Thông tin đăng ký đồ uống:");
            //
            txtNameDV = txtDV[1];
            procAdd = procDU[0];
            //
            tblDichVu.setModel(new DefaultTableModel(
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
            tblDangKyDV.setModel(new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"Ngày", "Mã phòng", "Mã đồ uống", "Số lượng"}
            ) {
                boolean[] canEdit = new boolean[]{
                    false, false, false, false
                };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            });
        }
        //
        tableModelDV = (DefaultTableModel) tblDichVu.getModel();
        tableModelDKDV = (DefaultTableModel) tblDangKyDV.getModel();
        showInformation();
    }//GEN-LAST:event_cbDichVuItemStateChanged

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:conn = con.getConnection();
        showInformation();
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        autoNullColumn();
        txtMaPhong.requestFocus(true);
        btnDat.setEnabled(true);
        showInformation();
    }//GEN-LAST:event_btnThemActionPerformed

    private void tblDichVuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDichVuMouseClicked
        // TODO add your handling code here:
        btnDat.setEnabled(true);

        int selectedIndex = tblDichVu.getSelectedRow();
        if (selectedIndex >= 0) {
            if (cbDichVu.getSelectedIndex() == 0) {
                doAn std = listDoAn.get(selectedIndex);
                txtMaDichVu.setText(std.getMaDoAn());
            } else if (cbDichVu.getSelectedIndex() == 1) {
                doUong std = listDoUong.get(selectedIndex);
                txtMaDichVu.setText(std.getMaDoUong());
            }
            txtSoLuong.setText("1");
            txtSoLuong.requestFocus(true);
            txtSoLuong.selectAll();
        }
    }//GEN-LAST:event_tblDichVuMouseClicked

    private void tblDangKyDVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDangKyDVMouseClicked
        // TODO add your handling code here:
        btnDat.setEnabled(true);

        int selectedIndex = tblDangKyDV.getSelectedRow();
        if (selectedIndex >= 0) {
            if (cbDichVu.getSelectedIndex() == 0) {
                datDoAn std = listDatDoAn.get(selectedIndex);
                txtMaPhong.setText(std.getMaPhong());
                txtMaDichVu.setText(std.getMaDA());
            } else if (cbDichVu.getSelectedIndex() == 1) {
                datDoUong std = listDatDoUong.get(selectedIndex);
                txtMaPhong.setText(std.getMaPhong());
                txtMaDichVu.setText(std.getMaDU());
            }
            txtSoLuong.setText("1");
            txtSoLuong.requestFocus(true);
            txtSoLuong.selectAll();
        }
    }//GEN-LAST:event_tblDangKyDVMouseClicked

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
            java.util.logging.Logger.getLogger(main_DkDichVu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main_DkDichVu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main_DkDichVu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main_DkDichVu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main_DkDichVu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAUL;
    private javax.swing.JButton btnDat;
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
    private javax.swing.JButton btnTrangThaiPhong;
    private javax.swing.JComboBox<String> cbDichVu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbDichVu;
    private javax.swing.JLabel lbInfoDKDV;
    private javax.swing.JLabel lbInfoDV;
    private javax.swing.JLabel lbMaDV;
    private javax.swing.JLabel lbMaPhong;
    private javax.swing.JLabel lbSoLuong;
    private javax.swing.JTable tblDangKyDV;
    private javax.swing.JTable tblDichVu;
    private javax.swing.JTextField txtMaDichVu;
    private javax.swing.JTextField txtMaPhong;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txt_TaiKhoan;
    // End of variables declaration//GEN-END:variables
}
