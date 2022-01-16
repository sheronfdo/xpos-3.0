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
import com.xpos.validation.Validate;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Jamit
 */
public class SaleInvoice extends javax.swing.JPanel {

    /**
     * Creates new form Brand
     */
    public SaleInvoice() {
        initComponents();
        fillInvoiceTable(null);
    }

    private void fillInvoiceTable(String query) {
        DefaultTableModel tableModel = (DefaultTableModel) soldItemTable.getModel();
        tableModel.setRowCount(0);
        tableModel = (DefaultTableModel) saleTable.getModel();
        tableModel.setRowCount(0);
        try {
            if (query == null) {
                query = "SELECT sale.`Id` as saleId, sale.`Date` as saleDate, sale.`Time` as saleTime,"
                        + " sale.`Customer_Id` as customerId, customer.`Name` as customerName, sale.`TotalRetailPrice` as saleTotal,"
                        + " sale.`DiscountByItems` as discountByItem, sale.`DiscountByTotal` as discountByTotal,"
                        + " sale.`FinalTotal` as finalTotal, sale.`Pay` as customerPay,"
                        + " userprofile.Username as username"
                        + " FROM `sale` JOIN customer on customer.Id=sale.Customer_Id"
                        + " JOIN userprofile ON sale.UserProfile_Id=userprofile.Id"
                        + " WHERE sale.Status=1";
            }
            ResultSet rs = DbConnect.getFromDB(query);
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt("saleId"));
                v.add(rs.getDate("saleDate") + " - " + rs.getTime("saleTime"));
                v.add(rs.getInt("customerId") + " - " + rs.getString("customerName"));
                v.add(rs.getFloat("saleTotal"));
                v.add(rs.getFloat("discountByItem"));
                v.add(rs.getFloat("discountByTotal"));
                v.add(rs.getFloat("finalTotal"));
                v.add(rs.getFloat("customerPay"));
                v.add(rs.getString("username"));
                tableModel.addRow(v);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SaleInvoice.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SaleInvoice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fillSoldItemTable(int saleId) {
        if (saleId == 0) {
            saleId = Integer.parseInt(saleTable.getValueAt(saleTable.getSelectedRow(), 0).toString());
        }
        DefaultTableModel tableModel = (DefaultTableModel) soldItemTable.getModel();
        tableModel.setRowCount(0);
        try {

            String query = "SELECT  solditem.`Product_Id` as productId,"
                    + " product.Description as description,"
                    + " solditem.`RetailPrice` as retailPrice,"
                    + " solditem.`Quantity` as quantity,"
                    + " solditem.`Total` as subtotal,"
                    + " solditem.`DiscountForItem` as discount,"
                    + " solditem.`Balance` as finalTotal,"
                    + " solditem.`BatchesOfProduct_Id` as batchId,"
                    + " batchesofproduct.BatchNumber as batchNumber,"
                    + " solditem.`Status`"
                    + " FROM `solditem`"
                    + " JOIN product ON solditem.Product_Id=product.Id"
                    + " JOIN batchesofproduct ON batchesofproduct.Id=solditem.BatchesOfProduct_Id"
                    + " WHERE solditem.Sale_Id=" + saleId;

            ResultSet rs = DbConnect.getFromDB(query);
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt("productId") + " - " + rs.getString("description"));
                v.add(rs.getInt("batchId") + " - " + rs.getInt("batchNumber"));
                v.add(rs.getFloat("retailPrice"));
                v.add(rs.getInt("quantity"));
                v.add(rs.getFloat("subtotal"));
                v.add(rs.getFloat("discount"));
                v.add(rs.getFloat("finalTotal"));
                tableModel.addRow(v);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SaleInvoice.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SaleInvoice.class.getName()).log(Level.SEVERE, null, ex);
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
        saleTable = new rojeru_san.complementos.RSTableMetro();
        saleSearchByInvoice = new app.bolivia.swing.JCTextField();
        saleSearchByCustomer = new app.bolivia.swing.JCTextField();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        soldItemTable = new rojeru_san.complementos.RSTableMetro();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setBackground(new java.awt.Color(26, 140, 255));
        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("SALES INVOICES");
        jLabel6.setOpaque(true);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        saleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Date - Time", "Customer", "Total Retail Price", "Discount For Items", "Discount For Total", "GrandTotal", "Pay", "System User"
            }
        ));
        saleTable.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        saleTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleTable.setRowHeight(25);
        saleTable.setRowMargin(0);
        saleTable.setSelectionBackground(new java.awt.Color(0, 60, 128));
        saleTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saleTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(saleTable);

        saleSearchByInvoice.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Invoice Number", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        saleSearchByInvoice.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleSearchByInvoice.setPhColor(new java.awt.Color(0, 51, 255));
        saleSearchByInvoice.setPlaceholder("Search Invoice Number");
        saleSearchByInvoice.setPreferredSize(new java.awt.Dimension(200, 30));
        saleSearchByInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saleSearchByInvoiceActionPerformed(evt);
            }
        });
        saleSearchByInvoice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                saleSearchByInvoiceKeyReleased(evt);
            }
        });

        saleSearchByCustomer.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Search Customer", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        saleSearchByCustomer.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleSearchByCustomer.setPhColor(new java.awt.Color(0, 51, 255));
        saleSearchByCustomer.setPlaceholder("Search Customer");
        saleSearchByCustomer.setPreferredSize(new java.awt.Dimension(200, 30));
        saleSearchByCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saleSearchByCustomerActionPerformed(evt);
            }
        });
        saleSearchByCustomer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                saleSearchByCustomerKeyReleased(evt);
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
                        .addComponent(saleSearchByInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saleSearchByCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saleSearchByInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saleSearchByCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        soldItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID - Product", "ID - Batch Number", "Retail Price", "Quantity", "Sub Total", "Discount", "Total"
            }
        ));
        soldItemTable.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        soldItemTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        soldItemTable.setRowHeight(25);
        soldItemTable.setRowMargin(0);
        soldItemTable.setSelectionBackground(new java.awt.Color(0, 60, 128));
        soldItemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                soldItemTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(soldItemTable);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jScrollPane3)
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

    private void saleTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saleTableMouseClicked
        fillSoldItemTable(0);
    }//GEN-LAST:event_saleTableMouseClicked

    private void saleSearchByInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saleSearchByInvoiceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saleSearchByInvoiceActionPerformed

    private void saleSearchByInvoiceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saleSearchByInvoiceKeyReleased
        String query = null;
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int saleId = Integer.parseInt(saleSearchByInvoice.getText().toString().equals("") ? "0" : saleSearchByInvoice.getText().toString());
            if (saleId != 0) {
                query = "SELECT sale.`Id` as saleId, sale.`Date` as saleDate, sale.`Time` as saleTime,"
                        + " sale.`Customer_Id` as customerId, customer.`Name` as customerName, sale.`TotalRetailPrice` as saleTotal,"
                        + " sale.`DiscountByItems` as discountByItem, sale.`DiscountByTotal` as discountByTotal,"
                        + " sale.`FinalTotal` as finalTotal, sale.`Pay` as customerPay,"
                        + " userprofile.Username as username"
                        + " FROM `sale` JOIN customer on customer.Id=sale.Customer_Id"
                        + " JOIN userprofile ON sale.UserProfile_Id=userprofile.Id"
                        + " WHERE sale.Status=1 and sale.id=" + saleId;
            }
        }
        fillInvoiceTable(query);
    }//GEN-LAST:event_saleSearchByInvoiceKeyReleased

    private void soldItemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_soldItemTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_soldItemTableMouseClicked

    private void saleSearchByCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saleSearchByCustomerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saleSearchByCustomerActionPerformed

    private void saleSearchByCustomerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saleSearchByCustomerKeyReleased
        String query = "SELECT sale.`Id` as saleId, sale.`Date` as saleDate, sale.`Time` as saleTime,"
                + " sale.`Customer_Id` as customerId, customer.`Name` as customerName, sale.`TotalRetailPrice` as saleTotal,"
                + " sale.`DiscountByItems` as discountByItem, sale.`DiscountByTotal` as discountByTotal,"
                + " sale.`FinalTotal` as finalTotal, sale.`Pay` as customerPay,"
                + " userprofile.Username as username"
                + " FROM `sale` JOIN customer on customer.Id=sale.Customer_Id"
                + " JOIN userprofile ON sale.UserProfile_Id=userprofile.Id"
                + " WHERE sale.Status=1 and customer.`Name` like '%" + saleSearchByCustomer.getText() + "%'";
        fillInvoiceTable(query);
    }//GEN-LAST:event_saleSearchByCustomerKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private app.bolivia.swing.JCTextField saleSearchByCustomer;
    private app.bolivia.swing.JCTextField saleSearchByInvoice;
    private rojeru_san.complementos.RSTableMetro saleTable;
    private rojeru_san.complementos.RSTableMetro soldItemTable;
    // End of variables declaration//GEN-END:variables
}
