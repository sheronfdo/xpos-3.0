/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpos.gui;

import com.xpos.database.DbConnect;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import com.xpos.commons.Validate;
import com.xpos.report.Report;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Jamit
 */
public class PurchaseInvoice extends javax.swing.JPanel {

    /**
     * Creates new form Brand
     */
    public PurchaseInvoice() {
        initComponents();
        fillPurchaseInvoiceTable(null);
    }

    public void fillPurchaseInvoiceTable(String query) {
        DefaultTableModel tableModel = (DefaultTableModel) purchaseItemTable.getModel();
        tableModel.setRowCount(0);
        tableModel = (DefaultTableModel) purchaseTable.getModel();
        tableModel.setRowCount(0);
        try {
            if (query == null) {
                query = "SELECT purchase.`Id` as purchaseId,"
                        + " purchase.`PurchaseOrder_Id` as POId,"
                        + " purchase.`Supplier_Id` as supplierId,"
                        + " supplier.Name as supplierName,"
                        + " purchase.`PurchaseDate` PDate,"
                        + " purchase.`Total` as total,"
                        + " purchase.`Discount` as discount,"
                        + " purchase.`Balance` as balance,"
                        + " userprofile.Username as username"
                        + " FROM `purchase`"
                        + " JOIN supplier on purchase.Supplier_Id=supplier.Id"
                        + " JOIN userprofile on purchase.UserProfile_Id=userprofile.Id"
                        + " WHERE purchase.Status=1";
            }
            ResultSet rs = DbConnect.getFromDB(query);
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt("purchaseId"));
                v.add(rs.getInt("POId"));
                v.add(rs.getInt("supplierId") + " - " + rs.getString("supplierName"));
                v.add(rs.getDate("PDate"));
                v.add(rs.getFloat("total"));
                v.add(rs.getFloat("discount"));
                v.add(rs.getFloat("balance"));
                v.add(rs.getString("username"));
                tableModel.addRow(v);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fillPurchaseItemTable(int purchaseId) {
        if (purchaseId == 0) {
            purchaseId = Integer.parseInt(purchaseTable.getValueAt(purchaseTable.getSelectedRow(), 0).toString());
        }
        DefaultTableModel tableModel = (DefaultTableModel) purchaseItemTable.getModel();
        tableModel.setRowCount(0);
        try {

            String query = "SELECT purchaseitem.`Product_Id` as productId,"
                    + " product.Description as description,"
                    + " purchaseitem.`PurchasePrice` as purchasePrice,"
                    + " purchaseitem.`Quantity` as quantity,"
                    + " purchaseitem.`TotalPrice` as total,"
                    + " purchaseitem.`BatchesOfProduct_Id` as batchId,"
                    + " batchesofproduct.BatchNumber as batchNumber"
                    + " FROM ((`purchaseitem`"
                    + " JOIN product on purchaseitem.Product_Id=product.Id)"
                    + " JOIN batchesofproduct on purchaseitem.BatchesOfProduct_Id=batchesofproduct.Id)"
                    + " WHERE purchaseitem.Purchase_Id=" + purchaseId;
            ResultSet rs = DbConnect.getFromDB(query);
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt("productId") + " - " + rs.getString("description"));
                v.add(rs.getInt("batchId") + " - " + rs.getInt("batchNumber"));
                v.add(rs.getFloat("purchasePrice"));
                v.add(rs.getInt("quantity"));
                v.add(rs.getFloat("total"));
                tableModel.addRow(v);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseInvoice.class.getName()).log(Level.SEVERE, null, ex);
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

        jLabel6 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        purchaseTable = new rojeru_san.complementos.RSTableMetro();
        purchaseSearchByInvoice = new app.bolivia.swing.JCTextField();
        purchaseSearchBySupplier = new app.bolivia.swing.JCTextField();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        purchaseItemTable = new rojeru_san.complementos.RSTableMetro();
        butInvoicePrint = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setBackground(new java.awt.Color(26, 140, 255));
        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("PURCHASE INVOICES");
        jLabel6.setOpaque(true);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        purchaseTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Purchase Order ID", "Supplier", "Date", "Total", "Discount", "GrandTotal", "System User"
            }
        ));
        purchaseTable.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        purchaseTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchaseTable.setRowHeight(25);
        purchaseTable.setRowMargin(0);
        purchaseTable.setSelectionBackground(new java.awt.Color(0, 60, 128));
        purchaseTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                purchaseTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(purchaseTable);

        purchaseSearchByInvoice.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Invoice Number", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        purchaseSearchByInvoice.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchaseSearchByInvoice.setPhColor(new java.awt.Color(0, 51, 255));
        purchaseSearchByInvoice.setPlaceholder("Search Invoice Number");
        purchaseSearchByInvoice.setPreferredSize(new java.awt.Dimension(200, 30));
        purchaseSearchByInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchaseSearchByInvoiceActionPerformed(evt);
            }
        });
        purchaseSearchByInvoice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                purchaseSearchByInvoiceKeyReleased(evt);
            }
        });

        purchaseSearchBySupplier.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Search Supplier", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        purchaseSearchBySupplier.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchaseSearchBySupplier.setPhColor(new java.awt.Color(0, 51, 255));
        purchaseSearchBySupplier.setPlaceholder("Search Supplier");
        purchaseSearchBySupplier.setPreferredSize(new java.awt.Dimension(200, 30));
        purchaseSearchBySupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchaseSearchBySupplierActionPerformed(evt);
            }
        });
        purchaseSearchBySupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                purchaseSearchBySupplierKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 802, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(purchaseSearchByInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(purchaseSearchBySupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(purchaseSearchByInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(purchaseSearchBySupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        purchaseItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID - Product", "ID - Batch Number", "Purchase Price", "Quantity", "Total"
            }
        ));
        purchaseItemTable.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        purchaseItemTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchaseItemTable.setRowHeight(25);
        purchaseItemTable.setRowMargin(0);
        purchaseItemTable.setSelectionBackground(new java.awt.Color(0, 60, 128));
        purchaseItemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                purchaseItemTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(purchaseItemTable);

        butInvoicePrint.setBackground(new java.awt.Color(0, 60, 128));
        butInvoicePrint.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        butInvoicePrint.setForeground(new java.awt.Color(255, 255, 255));
        butInvoicePrint.setText("PRINT INVOICE");
        butInvoicePrint.setBorder(null);
        butInvoicePrint.setFocusPainted(false);
        butInvoicePrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butInvoicePrintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(butInvoicePrint, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(butInvoicePrint, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void purchaseTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purchaseTableMouseClicked
        fillPurchaseItemTable(Integer.parseInt(purchaseTable.getValueAt(purchaseTable.getSelectedRow(), 0).toString()));
    }//GEN-LAST:event_purchaseTableMouseClicked

    private void purchaseSearchByInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseSearchByInvoiceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_purchaseSearchByInvoiceActionPerformed

    private void purchaseSearchByInvoiceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_purchaseSearchByInvoiceKeyReleased
        String query = null;
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (Validate.isNumber(purchaseSearchByInvoice.getText().toString().equals("") ? "0" : purchaseSearchByInvoice.getText().toString())) {
                int saleId = Integer.parseInt(purchaseSearchByInvoice.getText().toString().equals("") ? "0" : purchaseSearchByInvoice.getText().toString());
                if (saleId != 0) {
                    query = "SELECT purchase.`Id` as purchaseId,"
                            + " purchase.`PurchaseOrder_Id` as POId,"
                            + " purchase.`Supplier_Id` as supplierId,"
                            + " supplier.Name as supplierName,"
                            + " purchase.`PurchaseDate` PDate,"
                            + " purchase.`Total` as total,"
                            + " purchase.`Discount` as discount,"
                            + " purchase.`Balance` as balance,"
                            + " userprofile.Username as username"
                            + " FROM `purchase`"
                            + " JOIN supplier on purchase.Supplier_Id=supplier.Id"
                            + " JOIN userprofile on purchase.UserProfile_Id=userprofile.Id"
                            + " WHERE purchase.Status=1 and purchase.id=" + saleId;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Input Validation Failed", "Validation Failed", 1);
            }
        }
        fillPurchaseInvoiceTable(query);
    }//GEN-LAST:event_purchaseSearchByInvoiceKeyReleased

    private void purchaseItemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purchaseItemTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_purchaseItemTableMouseClicked

    private void purchaseSearchBySupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseSearchBySupplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_purchaseSearchBySupplierActionPerformed

    private void purchaseSearchBySupplierKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_purchaseSearchBySupplierKeyReleased
        String query = "SELECT purchase.`Id` as purchaseId,"
                + " purchase.`PurchaseOrder_Id` as POId,"
                + " purchase.`Supplier_Id` as supplierId,"
                + " supplier.Name as supplierName,"
                + " purchase.`PurchaseDate` PDate,"
                + " purchase.`Total` as total,"
                + " purchase.`Discount` as discount,"
                + " purchase.`Balance` as balance,"
                + " userprofile.Username as username"
                + " FROM `purchase`"
                + " JOIN supplier on purchase.Supplier_Id=supplier.Id"
                + " JOIN userprofile on purchase.UserProfile_Id=userprofile.Id"
                + " WHERE purchase.Status=1 and supplier.`Name` like '%" + purchaseSearchBySupplier.getText() + "%'";
        fillPurchaseInvoiceTable(query);
    }//GEN-LAST:event_purchaseSearchBySupplierKeyReleased

    private void butInvoicePrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butInvoicePrintActionPerformed
        try {int invoiceId = Integer.parseInt(purchaseTable.getValueAt(purchaseTable.getSelectedRow(), 0).toString());
            HashMap<String, Object> para = new HashMap<String, Object>();
            para.put("purchaseId", invoiceId);
            JasperPrint jprint = JasperFillManager.fillReport(Report.purchaseReport, para, DbConnect.getDBConnection());
            JasperViewer.viewReport(jprint, false);
        } catch (JRException ex) {
            Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ArrayIndexOutOfBoundsException ex) {
            JOptionPane.showMessageDialog(null, "No Selected Invoice", "Selection Failed", 1);
            Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_butInvoicePrintActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton butInvoicePrint;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private rojeru_san.complementos.RSTableMetro purchaseItemTable;
    private app.bolivia.swing.JCTextField purchaseSearchByInvoice;
    private app.bolivia.swing.JCTextField purchaseSearchBySupplier;
    private rojeru_san.complementos.RSTableMetro purchaseTable;
    // End of variables declaration//GEN-END:variables
}
