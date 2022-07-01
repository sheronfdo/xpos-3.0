/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpos.gui;

import com.xpos.commons.SystemLogger;
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
public class POInvoice extends javax.swing.JPanel {

    private static org.apache.log4j.Logger log;

    /**
     * Creates new form Brand
     */
    public POInvoice() {
        initComponents();
        SystemLogger.initLogger();
        log = org.apache.log4j.Logger.getLogger(ExIncome.class);
        log.info("exincome panel running start...");
        fillPOInvoiceTable(null);
    }

    public void fillPOInvoiceTable(String query) {
        log.info("fill PO invoice table, query : " + query);
        DefaultTableModel tableModel = (DefaultTableModel) POItemTable.getModel();
        tableModel.setRowCount(0);
        tableModel = (DefaultTableModel) POTable.getModel();
        tableModel.setRowCount(0);
        try {
            if (query == null) {
                log.info("assign query because null");
                query = "SELECT purchaseorder.`Id` as POId,"
                        + " purchaseorder.`Supplier_Id` as supplierId,"
                        + " purchaseorder.`Date` as PODate,"
                        + " purchaseorder.`subTotal` as subtotal,"
                        + " purchaseorder.`Discount` as discount,"
                        + " purchaseorder.`finalTotal` as FinalTotal,"
                        + " isSupplied as isSupplied,"
                        + " supplier.Name as supplierName"
                        + " FROM `purchaseorder`"
                        + " JOIN supplier on purchaseorder.Supplier_Id=supplier.Id"
                        + " WHERE purchaseorder.`Status` = 1";
            }
            ResultSet rs = DbConnect.getFromDB(query);
            log.info("execute query");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt("POId"));
                v.add(rs.getInt("supplierId") + " - " + rs.getString("supplierName"));
                v.add(rs.getDate("PODate"));
                v.add(rs.getDouble("subtotal"));
                v.add(rs.getDouble("discount"));
                v.add(rs.getDouble("finaltotal"));
                v.add(rs.getString("isSupplied"));
                tableModel.addRow(v);
            }
            log.info("table filled");
        } catch (ClassNotFoundException ex) {
            log.error("table filled failed", ex);
        } catch (SQLException ex) {
            log.error("table filled failed", ex);
        }
    }

    private void fillPOItemTable(int POId) {
        log.info("fillPOItemTable, poid : " + POId);
        if (POId == 0) {
            POId = Integer.parseInt(POTable.getValueAt(POTable.getSelectedRow(), 0).toString());
            log.info("assign poid");
        }
        DefaultTableModel tableModel = (DefaultTableModel) POItemTable.getModel();
        tableModel.setRowCount(0);
        try {
            log.info("assign query");
            String query = "SELECT PurchaseOrderItem.`Product_Id` as productId,"
                    + " PurchaseOrderItem.`Quantity` as quantity,"
                    + " PurchaseOrderItem.`ItemPrice` as itemprice,"
                    + " PurchaseOrderItem.`TotalPrice` as totalprice,"
                    + " product.Description as description,"
                    + " (SELECT supplierproduct.ItemCode"
                    + " FROM supplierproduct"
                    + " WHERE supplierproduct.Supplier_Id=purchaseorder.Supplier_Id"
                    + " AND supplierproduct.Product_Id=PurchaseOrderItem.Product_Id) as itemcode"
                    + " FROM ((`PurchaseOrderItem`"
                    + " JOIN product ON PurchaseOrderItem.Product_Id=product.Id)"
                    + " JOIN purchaseorder ON purchaseorder.Id=PurchaseOrderItem.PurchaseOrder_Id)"
                    + " WHERE PurchaseOrderItem.`PurchaseOrder_Id`=" + POId;
            ResultSet rs = DbConnect.getFromDB(query);
            log.info("execute query");
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt("productId"));
                v.add(rs.getString("description"));
                v.add(rs.getString("itemcode"));
                v.add(rs.getString("itemPrice"));
                v.add(rs.getInt("quantity"));
                v.add(rs.getString("totalprice"));
                tableModel.addRow(v);
            }
            log.info("table filled");
        } catch (ClassNotFoundException ex) {
            log.error("table filled failed", ex);
        } catch (SQLException ex) {
            log.error("table filled failed", ex);
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
        POTable = new rojeru_san.complementos.RSTableMetro();
        POSearchByInvoice = new app.bolivia.swing.JCTextField();
        POSearchBySupplier = new app.bolivia.swing.JCTextField();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        POItemTable = new rojeru_san.complementos.RSTableMetro();
        butInvoicePrint = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setBackground(new java.awt.Color(26, 140, 255));
        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("PURCHASE ORDERS");
        jLabel6.setOpaque(true);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        POTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Supplier", "Date", "Sub Total", "Discount", "Final Total", "Is Supplied"
            }
        ));
        POTable.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        POTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        POTable.setRowHeight(25);
        POTable.setRowMargin(0);
        POTable.setSelectionBackground(new java.awt.Color(0, 60, 128));
        POTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                POTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(POTable);

        POSearchByInvoice.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Invoice Number", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        POSearchByInvoice.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        POSearchByInvoice.setPhColor(new java.awt.Color(0, 51, 255));
        POSearchByInvoice.setPlaceholder("Search PO Number");
        POSearchByInvoice.setPreferredSize(new java.awt.Dimension(200, 30));
        POSearchByInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                POSearchByInvoiceActionPerformed(evt);
            }
        });
        POSearchByInvoice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                POSearchByInvoiceKeyReleased(evt);
            }
        });

        POSearchBySupplier.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Search Supplier", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        POSearchBySupplier.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        POSearchBySupplier.setPhColor(new java.awt.Color(0, 51, 255));
        POSearchBySupplier.setPlaceholder("Search Supplier");
        POSearchBySupplier.setPreferredSize(new java.awt.Dimension(200, 30));
        POSearchBySupplier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                POSearchBySupplierActionPerformed(evt);
            }
        });
        POSearchBySupplier.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                POSearchBySupplierKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(POSearchByInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(POSearchBySupplier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(POSearchByInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(POSearchBySupplier, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        POItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID ", "Product", "Item Code", "Item Price", "Quantity", "Total Price"
            }
        ));
        POItemTable.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        POItemTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        POItemTable.setRowHeight(25);
        POItemTable.setRowMargin(0);
        POItemTable.setSelectionBackground(new java.awt.Color(0, 60, 128));
        POItemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                POItemTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(POItemTable);

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
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)
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

    private void POTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_POTableMouseClicked
        fillPOItemTable(Integer.parseInt(POTable.getValueAt(POTable.getSelectedRow(), 0).toString()));
    }//GEN-LAST:event_POTableMouseClicked

    private void POSearchByInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_POSearchByInvoiceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_POSearchByInvoiceActionPerformed

    private void POSearchByInvoiceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_POSearchByInvoiceKeyReleased
        log.info("user searched");
        String query = null;
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            log.info("user presses enter");
            if (Validate.isNumber(POSearchByInvoice.getText().toString()) || POSearchByInvoice.getText().toString().equals("")) {
                log.info("po validation passed");
                int POId = Integer.parseInt(POSearchByInvoice.getText().toString().equals("") ? "0" : POSearchByInvoice.getText().toString());
                log.info("assign poid");
                if (POId != 0) {
                    log.info("assign query");
                    query = "SELECT purchaseorder.`Id` as POId,"
                            + " purchaseorder.`Supplier_Id` as supplierId,"
                            + " purchaseorder.`Date` as PODate,"
                            + " purchaseorder.`subTotal` as subtotal,"
                            + " purchaseorder.`Discount` as discount,"
                            + " purchaseorder.`finalTotal` as FinalTotal,"
                            + " isSupplied as isSupplied,"
                            + " supplier.Name as supplierName"
                            + " FROM `purchaseorder`"
                            + " JOIN supplier on purchaseorder.Supplier_Id=supplier.Id"
                            + " WHERE purchaseorder.id=" + POId;
                }
            } else {
                log.info("po validation failed");
                JOptionPane.showMessageDialog(null, "Not Text", "Validation Failed", 1);
            }
            fillPOInvoiceTable(query);
        }
    }//GEN-LAST:event_POSearchByInvoiceKeyReleased

    private void POItemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_POItemTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_POItemTableMouseClicked

    private void POSearchBySupplierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_POSearchBySupplierActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_POSearchBySupplierActionPerformed

    private void POSearchBySupplierKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_POSearchBySupplierKeyReleased
        log.info("user searches by by supplier");
        String query = "SELECT purchaseorder.`Id` as POId,"
                + " purchaseorder.`Supplier_Id` as supplierId,"
                + " purchaseorder.`Date` as PODate,"
                + " purchaseorder.`IsSupplied` as isSupplied,"
                + " supplier.Name as supplierName"
                + " FROM `purchaseorder`"
                + " JOIN supplier on purchaseorder.Supplier_Id=supplier.Id"
                + " WHERE purchaseorder.`Status` = 1 and supplier.Name like '%" + POSearchBySupplier.getText() + "%'";
        fillPOInvoiceTable(query);
    }//GEN-LAST:event_POSearchBySupplierKeyReleased

    private void butInvoicePrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_butInvoicePrintActionPerformed
        try {
            int invoiceId = Integer.parseInt(POTable.getValueAt(POTable.getSelectedRow(), 0).toString());
            HashMap<String, Object> para = new HashMap<String, Object>();
            para.put("POID", invoiceId);
            JasperPrint jprint = JasperFillManager.fillReport(Report.POReport, para, DbConnect.getDBConnection());
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
    private rojeru_san.complementos.RSTableMetro POItemTable;
    private app.bolivia.swing.JCTextField POSearchByInvoice;
    private app.bolivia.swing.JCTextField POSearchBySupplier;
    private rojeru_san.complementos.RSTableMetro POTable;
    private javax.swing.JButton butInvoicePrint;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
