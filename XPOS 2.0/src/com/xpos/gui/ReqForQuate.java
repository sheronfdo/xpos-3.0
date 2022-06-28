/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpos.gui;

import com.xpos.SystemUser;
import com.xpos.commons.Validate;
import com.xpos.database.DbConnect;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Jamit
 */
public class ReqForQuate extends javax.swing.JPanel {

    int supplierId = 0;
    DefaultTableModel tablemodel;
    DateTimeFormatter defaultDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter defaultTimeFormat = DateTimeFormatter.ofPattern("hh:mm:ss");

    /**
     * Creates new form Purchase
     */
    public ReqForQuate() {
        initComponents();
        //fillSupplierProductTable(null);
        clearPurchOrderPanel();
    }

    private void clearPurchOrderPanel() {
        supplierId = 0;
        RFQProductId.setText("");
        RFQBrand.setText("");
        RFQDescription.setText("");
        RFQItemCode.setText("");
        RFQQuantity.setText("");
        RFQStock.setText("");
        RFQOrderedQty.setText("");

        tablemodel = (DefaultTableModel) RFQSupplierProduct.getModel();
        tablemodel.setRowCount(0);
        tablemodel = (DefaultTableModel) RFQTable.getModel();
        tablemodel.setRowCount(0);
        loadPurchaseOrderSupplierCombo();
        fillSupplierProductTable(null);
    }

    private void loadPurchaseOrderSupplierCombo() {
        try {
            ResultSet rs = DbConnect.getFromDB("select Id,Name from supplier where status=1");
            Vector v = new Vector();
            v.add("Select Supplier");
            while (rs.next()) {
                String supplier = rs.getString("Id") + " - " + rs.getString("Name");
                v.add(supplier);
            }
            RFQSupplierCombo.setModel(new DefaultComboBoxModel(v));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillQuateReqProductTable(int supId) {
        String query = null;
        tablemodel = (DefaultTableModel) RFQTable.getModel();
        tablemodel.setRowCount(0);
        query = "SELECT product.Id as productId,"
                + " brand.BrandName as brand,"
                + " product.Description as description,"
                + " supplierproduct.ItemCode as itemcode,"
                + " product.ReOrderLevel as reorderlevel"
                + " FROM (((product"
                + " INNER JOIN brand on brand.Id=product.Brand_Id)"
                + " INNER JOIN category ON category.Id=product.Category_Id)"
                + " INNER JOIN supplierproduct ON supplierproduct.Product_Id = product.Id)"
                + " WHERE product.TotalQuantity<product.ReOrderLevel"
                + " and supplierproduct.Supplier_Id=" + supId
                + " and product.Status=1";

        try {
            ResultSet rs = DbConnect.getFromDB(query);
            while (rs.next()) {

                Vector v = new Vector();
                v.add(rs.getInt("productId"));
                v.add(rs.getString("brand"));
                v.add(rs.getString("description"));
                v.add(rs.getString("itemcode"));
                v.add(rs.getInt("reorderlevel"));
                tablemodel.addRow(v);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fillSupplierProductTable(String query) {

        tablemodel = (DefaultTableModel) RFQSupplierProduct.getModel();
        tablemodel.setRowCount(0);
        if (query == null) {
            query = "SELECT product.Id as productId,"
                    + " brand.BrandName as brand,"
                    + " product.Description as description,"
                    + " category.Description as category,"
                    + " product.TotalQuantity as quantity,"
                    + " product.ReOrderLevel as reorderlevel,"
                    + " product.OrderedQuantity as orderedquantity"
                    + " FROM ((product"
                    + " INNER JOIN brand on brand.Id=product.Brand_Id)"
                    + " INNER JOIN category ON category.Id=product.Category_Id)"
                    + " WHERE product.TotalQuantity<product.ReOrderLevel and product.Status=1";
        }
        try {
            ResultSet rs = DbConnect.getFromDB(query);
            while (rs.next()) {

                Vector v = new Vector();
                v.add(rs.getInt("productId"));
                v.add(rs.getString("brand"));
                v.add(rs.getString("description"));
                v.add(rs.getString("category"));
                v.add(rs.getInt("quantity"));
                v.add(rs.getInt("reorderlevel"));
                v.add(rs.getInt("orderedquantity"));
                tablemodel.addRow(v);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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

        jLabel13 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        RFQSupplierProduct = new rojeru_san.complementos.RSTableMetro();
        RFQProdDescriptionSearch = new app.bolivia.swing.JCTextField();
        jPanel3 = new javax.swing.JPanel();
        RFQDescription = new javax.swing.JLabel();
        RFQStock = new javax.swing.JLabel();
        RFQOrderedQty = new javax.swing.JLabel();
        RFQQuantity = new app.bolivia.swing.JCTextField();
        RFQAddToTable = new javax.swing.JButton();
        RFQBrand = new javax.swing.JLabel();
        RFQItemCode = new javax.swing.JLabel();
        RFQProductId = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        RFQComplete = new javax.swing.JButton();
        RFQDeleteItem = new javax.swing.JButton();
        RFQCancel = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        RFQTable = new rojeru_san.complementos.RSTableMetro();
        RFQSupplierCombo = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setBackground(new java.awt.Color(26, 140, 255));
        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("REQUEST FOR QUATE");
        jLabel13.setOpaque(true);

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Purchase Required Products", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(26, 140, 255))); // NOI18N

        RFQSupplierProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Brand", "Description", "Category", "Quantity (In Stock)", "Re-Order Level", "Ordered Quantity"
            }
        ));
        RFQSupplierProduct.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        RFQSupplierProduct.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        RFQSupplierProduct.setRowHeight(25);
        RFQSupplierProduct.setRowMargin(0);
        RFQSupplierProduct.setSelectionBackground(new java.awt.Color(0, 60, 128));
        RFQSupplierProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RFQSupplierProductMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(RFQSupplierProduct);

        RFQProdDescriptionSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Search", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        RFQProdDescriptionSearch.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        RFQProdDescriptionSearch.setPhColor(new java.awt.Color(0, 51, 255));
        RFQProdDescriptionSearch.setPlaceholder("Search by Description");
        RFQProdDescriptionSearch.setPreferredSize(new java.awt.Dimension(200, 30));
        RFQProdDescriptionSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                RFQProdDescriptionSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 1410, Short.MAX_VALUE)
            .addComponent(RFQProdDescriptionSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RFQProdDescriptionSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        RFQDescription.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        RFQDescription.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Description", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        RFQStock.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        RFQStock.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "In-Stock Quantity", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        RFQOrderedQty.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        RFQOrderedQty.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Ordered Quantity", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        RFQQuantity.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Quantity", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        RFQQuantity.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        RFQQuantity.setPhColor(new java.awt.Color(0, 51, 255));
        RFQQuantity.setPlaceholder("Enter Quantity");
        RFQQuantity.setPreferredSize(new java.awt.Dimension(200, 30));
        RFQQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                RFQQuantityKeyReleased(evt);
            }
        });

        RFQAddToTable.setBackground(new java.awt.Color(0, 60, 128));
        RFQAddToTable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        RFQAddToTable.setForeground(new java.awt.Color(255, 255, 255));
        RFQAddToTable.setText("ADD TO TABLE");
        RFQAddToTable.setBorder(null);
        RFQAddToTable.setFocusPainted(false);
        RFQAddToTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RFQAddToTableActionPerformed(evt);
            }
        });

        RFQBrand.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        RFQBrand.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Brand", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        RFQItemCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        RFQItemCode.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Item Code", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        RFQProductId.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        RFQProductId.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Product ID", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RFQProductId, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(RFQBrand, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addComponent(RFQDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(RFQOrderedQty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(RFQStock, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(RFQQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RFQItemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(RFQAddToTable, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RFQAddToTable, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(RFQItemCode, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(RFQStock, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(RFQBrand, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(RFQProductId, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(RFQOrderedQty, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(RFQQuantity, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(RFQDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Purchase Actions", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(26, 140, 255))); // NOI18N

        RFQComplete.setBackground(new java.awt.Color(0, 60, 128));
        RFQComplete.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        RFQComplete.setForeground(new java.awt.Color(255, 255, 255));
        RFQComplete.setText("COMPLETE PURCHASE ORDER");
        RFQComplete.setBorder(null);
        RFQComplete.setFocusPainted(false);
        RFQComplete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RFQCompleteActionPerformed(evt);
            }
        });

        RFQDeleteItem.setBackground(new java.awt.Color(0, 60, 128));
        RFQDeleteItem.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        RFQDeleteItem.setForeground(new java.awt.Color(255, 255, 255));
        RFQDeleteItem.setText("DELETE ITEM");
        RFQDeleteItem.setBorder(null);
        RFQDeleteItem.setFocusPainted(false);
        RFQDeleteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RFQDeleteItemActionPerformed(evt);
            }
        });

        RFQCancel.setBackground(new java.awt.Color(0, 60, 128));
        RFQCancel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        RFQCancel.setForeground(new java.awt.Color(255, 255, 255));
        RFQCancel.setText("CANCEL ORDER");
        RFQCancel.setBorder(null);
        RFQCancel.setFocusPainted(false);
        RFQCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RFQCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RFQCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(RFQComplete, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                    .addComponent(RFQDeleteItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(214, Short.MAX_VALUE)
                .addComponent(RFQDeleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RFQCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RFQComplete, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Quate Requesting Products", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(26, 140, 255))); // NOI18N

        RFQTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Brand", "Description", "Item Code", "Quantity"
            }
        ));
        RFQTable.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        RFQTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        RFQTable.setRowHeight(25);
        RFQTable.setRowMargin(0);
        RFQTable.setSelectionBackground(new java.awt.Color(0, 60, 128));
        RFQTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RFQTableMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(RFQTable);

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane11)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        RFQSupplierCombo.setForeground(new java.awt.Color(26, 140, 255));
        RFQSupplierCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Supplier" }));
        RFQSupplierCombo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Supplier", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        RFQSupplierCombo.setOpaque(false);
        RFQSupplierCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RFQSupplierComboActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(RFQSupplierCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 436, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RFQSupplierCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void RFQSupplierProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RFQSupplierProductMouseClicked
        RFQProductId.setText("");
        RFQBrand.setText("");
        RFQDescription.setText("");
        RFQItemCode.setText("");
        RFQQuantity.setText("");
        RFQStock.setText("");
        RFQOrderedQty.setText("");
        try {
            int selectedRow = RFQSupplierProduct.getSelectedRow();
            String query = "SELECT supplierproduct.ItemCode as itemcode"
                    + " FROM supplierproduct"
                    + " WHERE supplierproduct.Supplier_Id=" + supplierId
                    + " AND supplierproduct.Product_Id=" + RFQSupplierProduct.getValueAt(selectedRow, 0).toString();
            ResultSet rs = DbConnect.getFromDB(query);

            RFQProductId.setText(RFQSupplierProduct.getValueAt(selectedRow, 0).toString());
            RFQBrand.setText(RFQSupplierProduct.getValueAt(selectedRow, 1).toString());
            RFQDescription.setText(RFQSupplierProduct.getValueAt(selectedRow, 2).toString());
            RFQStock.setText(RFQSupplierProduct.getValueAt(selectedRow, 4).toString());
            RFQOrderedQty.setText(RFQSupplierProduct.getValueAt(selectedRow, 6).toString());
            if (rs.next()) {
                RFQItemCode.setText(rs.getString("itemcode"));
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReqForQuate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ReqForQuate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_RFQSupplierProductMouseClicked

    private void RFQProdDescriptionSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RFQProdDescriptionSearchKeyReleased
        String query = "SELECT product.Id as productId,"
                + " brand.BrandName as brand,"
                + " product.Description as description,"
                + " category.Description as category,"
                + " product.TotalQuantity as quantity,"
                + " product.ReOrderLevel as reorderlevel,"
                + " product.OrderedQuantity as orderedquantity"
                + " FROM ((product"
                + " INNER JOIN brand on brand.Id=product.Brand_Id)"
                + " INNER JOIN category ON category.Id=product.Category_Id)"
                + " WHERE product.TotalQuantity<product.ReOrderLevel and product.Description like '%"
                + RFQProdDescriptionSearch.getText().toString() + "%' and product.Status=1";
        fillSupplierProductTable(query);
    }//GEN-LAST:event_RFQProdDescriptionSearchKeyReleased

    private void RFQQuantityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_RFQQuantityKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && Validate.isNumber(RFQQuantity.getText())) {
            RFQAddToTable.setFocusable(true);
        } else {
            JOptionPane.showMessageDialog(this, "Form Validation FAiled", "Invalid Number Format", 1);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_RFQQuantityKeyReleased

    private void RFQAddToTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RFQAddToTableActionPerformed
        Vector v = new Vector();
        v.add(RFQProductId.getText());
        v.add(RFQBrand.getText());
        v.add(RFQDescription.getText());
        v.add(RFQItemCode.getText());
        v.add(RFQQuantity.getText());

        tablemodel = (DefaultTableModel) RFQTable.getModel();
        tablemodel.addRow(v);

        RFQProductId.setText("");
        RFQBrand.setText("");
        RFQDescription.setText("");
        RFQItemCode.setText("");
        RFQQuantity.setText("");
        RFQStock.setText("");
        RFQOrderedQty.setText("");
    }//GEN-LAST:event_RFQAddToTableActionPerformed

    private void RFQCompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RFQCompleteActionPerformed
        if ((RFQTable.getRowCount() > 0) && (RFQSupplierCombo.getSelectedIndex() > 0)) {
            String date = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(defaultDateFormat);

            String supplier = Integer.toString(supplierId);

            int currentPurchaseOrderId = 0;
            String purchOrderQuery = "INSERT INTO `ReqForQuate`(`Supplier_Id`, `Date`, `IsResponses`, `UserProfile_Id`)"
                    + " VALUES (" + supplier + ",'" + date + "', false," + SystemUser.userId + ")";
            try {
                PreparedStatement pst = DbConnect.getDBConnection().prepareStatement(purchOrderQuery, Statement.RETURN_GENERATED_KEYS);
                pst.executeUpdate();
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    currentPurchaseOrderId = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < RFQTable.getRowCount(); i++) {
                int productId = Integer.parseInt(RFQTable.getValueAt(i, 0).toString());
                int quantity = Integer.parseInt(RFQTable.getValueAt(i, 4).toString());

                String query = "INSERT INTO `ReqForQuateItem`"
                        + "( `ReqForQuate_Id`, `Product_Id`, `Quantity`)"
                        + " VALUES (" + currentPurchaseOrderId + "," + productId + "," + quantity + ")";
                //String query2 = "UPDATE `product` SET `OrderedQuantity`=product.OrderedQuantity+" + quantity + " WHERE product.Id=" + productId;
                try {
                    DbConnect.pushToDB(query);
                    //DbConnect.pushToDB(query2);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ReqForQuate.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ReqForQuate.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (currentPurchaseOrderId > 0) {
                printInvoice(currentPurchaseOrderId);
            }
            clearPurchOrderPanel();
        } else {
            if (RFQTable.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No Items In the Purchased Product Table");
            }
            if (RFQSupplierCombo.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Please select Supplier");
            }
        }
    }//GEN-LAST:event_RFQCompleteActionPerformed

    private void printInvoice(int invoiceId) {
        try {
            HashMap<String, Object> para = new HashMap<String, Object>();
            para.put("reqforquateid", invoiceId);
            JasperDesign design = JRXmlLoader.load("src\\com\\xpos\\report\\reqForQuate.jrxml");
            JasperReport report = JasperCompileManager.compileReport(design);
            JasperPrint jprint = JasperFillManager.fillReport(report, para, DbConnect.getDBConnection());
            JasperViewer.viewReport(jprint, false);
        } catch (JRException ex) {
            Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void RFQDeleteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RFQDeleteItemActionPerformed
        try {
            tablemodel = (DefaultTableModel) RFQTable.getModel();
            tablemodel.removeRow(RFQTable.getSelectedRow());
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }//GEN-LAST:event_RFQDeleteItemActionPerformed

    private void RFQCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RFQCancelActionPerformed
        clearPurchOrderPanel();
    }//GEN-LAST:event_RFQCancelActionPerformed

    private void RFQTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RFQTableMouseClicked

    }//GEN-LAST:event_RFQTableMouseClicked

    private void RFQSupplierComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RFQSupplierComboActionPerformed
        if (RFQSupplierCombo.getSelectedIndex() == 0) {
            supplierId = 0;
        } else {
            supplierId = Integer.parseInt(RFQSupplierCombo.getSelectedItem().toString().trim().split(" - ")[0]);
        }
        if (RFQTable.getRowCount() == 0) {
            fillQuateReqProductTable(supplierId);
        } else {
            for (int i = 0; i < RFQTable.getRowCount(); i++) {
                try {
                    int productId = Integer.parseInt(RFQTable.getValueAt(i, 0).toString());
                    String query = "SELECT supplierproduct.ItemCode as itemcode"
                            + " FROM supplierproduct"
                            + " WHERE supplierproduct.Supplier_Id=" + supplierId
                            + " AND supplierproduct.Product_Id=" + productId;
                    ResultSet rs = DbConnect.getFromDB(query);
                    if (rs.next()) {
                        RFQTable.setValueAt(rs.getString("itemcode"), i, 3);
                    } else {
                        RFQTable.setValueAt("", i, 3);
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ReqForQuate.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ReqForQuate.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_RFQSupplierComboActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton RFQAddToTable;
    private javax.swing.JLabel RFQBrand;
    private javax.swing.JButton RFQCancel;
    private javax.swing.JButton RFQComplete;
    private javax.swing.JButton RFQDeleteItem;
    private javax.swing.JLabel RFQDescription;
    private javax.swing.JLabel RFQItemCode;
    private javax.swing.JLabel RFQOrderedQty;
    private app.bolivia.swing.JCTextField RFQProdDescriptionSearch;
    private javax.swing.JLabel RFQProductId;
    private app.bolivia.swing.JCTextField RFQQuantity;
    private javax.swing.JLabel RFQStock;
    private javax.swing.JComboBox<String> RFQSupplierCombo;
    private rojeru_san.complementos.RSTableMetro RFQSupplierProduct;
    private rojeru_san.complementos.RSTableMetro RFQTable;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane9;
    // End of variables declaration//GEN-END:variables
}
