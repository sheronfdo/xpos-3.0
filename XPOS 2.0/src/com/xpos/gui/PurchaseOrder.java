/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpos.gui;

import com.xpos.SystemUser;
import com.xpos.database.DbConnect;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jamit
 */
public class PurchaseOrder extends javax.swing.JPanel {

    int supplierId;
    DefaultTableModel tablemodel;
    DateTimeFormatter defaultDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter defaultTimeFormat = DateTimeFormatter.ofPattern("hh:mm:ss");

    Double totalWithNoDiscount = 0.0;
    Double discount = 0.0;
    Double finalTotal = 0.0;
    int purchaseOrderId = 0;

    /**
     * Creates new form Purchase
     */
    public PurchaseOrder() {
        initComponents();
        loadPurchaseOrderSupplierCombo();
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
            purchOrderSupplierCombo.setModel(new DefaultComboBoxModel(v));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillPurchaseOrderProdTable(String query) {
        tablemodel = (DefaultTableModel) purchOrderProdTable.getModel();
        tablemodel.setRowCount(0);
        if (query == null) {
            query = "SELECT product.Id as productid,"
                    + " product.Description as productname,"
                    + " product.TotalQuantity as productstock,"
                    + " product.ReOrderLevel as reorderlevel,"
                    + " product.OrderedQuantity as orderedquantity,"
                    + " category.Description as category,"
                    + " brand.BrandName as brand,"
                    + " supplierproduct.ItemCode"
                    + " FROM (((product"
                    + " INNER JOIN category ON category.Id=product.Category_Id)"
                    + " INNER JOIN brand ON brand.Id=product.Brand_Id)"
                    + " INNER JOIN supplierproduct ON supplierproduct.Product_Id=product.Id)"
                    + " WHERE product.Status=1 and"
                    + " supplierproduct.Supplier_Id=" + supplierId;
        }
        try {
            ResultSet rs = DbConnect.getFromDB(query);
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt("productid"));
                v.add(rs.getString("category"));
                v.add(rs.getString("brand"));
                v.add(rs.getString("productname"));
                v.add(rs.getInt("productstock"));
                v.add(rs.getInt("reorderlevel"));
                v.add(rs.getInt("orderedquantity"));
                v.add(rs.getString("ItemCode"));
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

    public void calPurchaseOrderDetails() {
        int rows = purchOrderProduct.getRowCount();
        totalWithNoDiscount = 0.0;
        discount = 0.0;
        finalTotal = 0.0;
        for (int i = 0; i < rows; i++) {
            totalWithNoDiscount = totalWithNoDiscount + Double.parseDouble(purchOrderProduct.getValueAt(i, 6).toString());
        }
        if (purchOrderDisAmount.isSelected()) {
            discount = Double.parseDouble(purchOrderDiscount.getText().toString());
        } else if (purchOrderDisPercent.isSelected()) {
            discount = totalWithNoDiscount
                    * Double.parseDouble(purchOrderDiscount.getText().toString().equals("") ? "0" : purchOrderDiscount.getText().toString()) / 100;
        }
        finalTotal = totalWithNoDiscount - discount;
        purchOrderSubTotal.setText(totalWithNoDiscount.toString());
        purchOrderFinalDiscount.setText(discount.toString());
        purchOrderTotal.setText(finalTotal.toString());
    }

    private void clearPurchOrderPanel() {
        purchOrderSupplierCombo.setSelectedIndex(0);
        ((DefaultTableModel) purchOrderProduct.getModel()).setRowCount(0);
        ((DefaultTableModel) purchOrderProdTable.getModel()).setRowCount(0);

        purchOrderProductId.setText("");
        purchOrderBrand.setText("");
        purchOrderDescription.setText("");
        purchOrderStockQuantity.setText("");
        purchOrderPurchasePrice.setText("");
        purchOrderQuantity.setText("");
        purchOrderItemCode.setText("");
        purchOrderProductTotal.setText("");

        purchOrderSubTotal.setText("0.0");
        purchOrderDiscount.setText("0");
        purchOrderDisAmount.setSelected(true);
        purchOrderTotal.setText("0.00");
        fillPurchaseOrderProdTable(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel13 = new javax.swing.JLabel();
        purchOrderSupplierCombo = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        purchOrderProductId = new app.bolivia.swing.JCTextField();
        purchOrderDescription = new javax.swing.JLabel();
        purchOrderStockQuantity = new javax.swing.JLabel();
        purchOrderQuantity = new app.bolivia.swing.JCTextField();
        purchOrderAddToTable = new javax.swing.JButton();
        purchOrderBrand = new javax.swing.JLabel();
        purchOrderItemCode = new javax.swing.JLabel();
        purchOrderProductTotal = new javax.swing.JLabel();
        purchOrderPurchasePrice = new app.bolivia.swing.JCTextField();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        purchOrderProduct = new rojeru_san.complementos.RSTableMetro();
        jPanel5 = new javax.swing.JPanel();
        purchOrderComplete = new javax.swing.JButton();
        purchOrderDeleteItem = new javax.swing.JButton();
        purchOrderCancel = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        purchOrderSubTotal = new javax.swing.JLabel();
        purchOrderDiscount = new app.bolivia.swing.JCTextField();
        purchOrderDisAmount = new javax.swing.JRadioButton();
        purchOrderDisPercent = new javax.swing.JRadioButton();
        jLabel40 = new javax.swing.JLabel();
        purchOrderTotal = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        purchOrderFinalDiscount = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        purchOrderProdTable = new rojeru_san.complementos.RSTableMetro();
        purchOrderProdDescription = new app.bolivia.swing.JCTextField();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setBackground(new java.awt.Color(26, 140, 255));
        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("PURCHASE ORDER");
        jLabel13.setOpaque(true);

        purchOrderSupplierCombo.setForeground(new java.awt.Color(26, 140, 255));
        purchOrderSupplierCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Supplier" }));
        purchOrderSupplierCombo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Supplier", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        purchOrderSupplierCombo.setOpaque(false);
        purchOrderSupplierCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchOrderSupplierComboActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        purchOrderProductId.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Product Id", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        purchOrderProductId.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchOrderProductId.setPhColor(new java.awt.Color(0, 51, 255));
        purchOrderProductId.setPlaceholder("Search by Product Id");
        purchOrderProductId.setPreferredSize(new java.awt.Dimension(200, 30));
        purchOrderProductId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                purchOrderProductIdKeyReleased(evt);
            }
        });

        purchOrderDescription.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchOrderDescription.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Description", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        purchOrderStockQuantity.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchOrderStockQuantity.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "In-Stock Quantity", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        purchOrderQuantity.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Quantity", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        purchOrderQuantity.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchOrderQuantity.setPhColor(new java.awt.Color(0, 51, 255));
        purchOrderQuantity.setPlaceholder("Enter Quantity");
        purchOrderQuantity.setPreferredSize(new java.awt.Dimension(200, 30));
        purchOrderQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                purchOrderQuantityKeyReleased(evt);
            }
        });

        purchOrderAddToTable.setBackground(new java.awt.Color(0, 60, 128));
        purchOrderAddToTable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchOrderAddToTable.setForeground(new java.awt.Color(255, 255, 255));
        purchOrderAddToTable.setText("ADD TO TABLE");
        purchOrderAddToTable.setBorder(null);
        purchOrderAddToTable.setFocusPainted(false);
        purchOrderAddToTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchOrderAddToTableActionPerformed(evt);
            }
        });

        purchOrderBrand.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchOrderBrand.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Brand", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        purchOrderItemCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchOrderItemCode.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Item Code", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        purchOrderProductTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchOrderProductTotal.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Total", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        purchOrderPurchasePrice.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Purchase Price", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        purchOrderPurchasePrice.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchOrderPurchasePrice.setPhColor(new java.awt.Color(0, 51, 255));
        purchOrderPurchasePrice.setPlaceholder("Enter purchase Price");
        purchOrderPurchasePrice.setPreferredSize(new java.awt.Dimension(200, 30));
        purchOrderPurchasePrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                purchOrderPurchasePriceKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(purchOrderBrand, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(purchOrderProductId, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(purchOrderDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(purchOrderPurchasePrice, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(purchOrderQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(purchOrderStockQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(purchOrderItemCode, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addComponent(purchOrderProductTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(purchOrderAddToTable, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(purchOrderAddToTable, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(purchOrderDescription, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(purchOrderProductId, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                        .addComponent(purchOrderItemCode, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(purchOrderStockQuantity, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(purchOrderQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                            .addComponent(purchOrderPurchasePrice, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                        .addComponent(purchOrderBrand, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(purchOrderProductTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Purchased Products", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(26, 140, 255))); // NOI18N

        purchOrderProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Brand", "Description", "Item Code", "Purchase Price", "Quantity", "Total"
            }
        ));
        purchOrderProduct.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        purchOrderProduct.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchOrderProduct.setRowHeight(25);
        purchOrderProduct.setRowMargin(0);
        purchOrderProduct.setSelectionBackground(new java.awt.Color(0, 60, 128));
        purchOrderProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                purchOrderProductMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(purchOrderProduct);

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

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Purchase Actions", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(26, 140, 255))); // NOI18N

        purchOrderComplete.setBackground(new java.awt.Color(0, 60, 128));
        purchOrderComplete.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        purchOrderComplete.setForeground(new java.awt.Color(255, 255, 255));
        purchOrderComplete.setText("COMPLETE PURCHASE");
        purchOrderComplete.setBorder(null);
        purchOrderComplete.setFocusPainted(false);
        purchOrderComplete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchOrderCompleteActionPerformed(evt);
            }
        });

        purchOrderDeleteItem.setBackground(new java.awt.Color(0, 60, 128));
        purchOrderDeleteItem.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        purchOrderDeleteItem.setForeground(new java.awt.Color(255, 255, 255));
        purchOrderDeleteItem.setText("DELETE ITEM");
        purchOrderDeleteItem.setBorder(null);
        purchOrderDeleteItem.setFocusPainted(false);
        purchOrderDeleteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchOrderDeleteItemActionPerformed(evt);
            }
        });

        purchOrderCancel.setBackground(new java.awt.Color(0, 60, 128));
        purchOrderCancel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        purchOrderCancel.setForeground(new java.awt.Color(255, 255, 255));
        purchOrderCancel.setText("CANCEL PURCHASE");
        purchOrderCancel.setBorder(null);
        purchOrderCancel.setFocusPainted(false);
        purchOrderCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchOrderCancelActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("Total Price : ");

        purchOrderSubTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchOrderSubTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        purchOrderSubTotal.setText("0.0");

        purchOrderDiscount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)));
        purchOrderDiscount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        purchOrderDiscount.setText("0");
        purchOrderDiscount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchOrderDiscount.setPhColor(new java.awt.Color(0, 51, 255));
        purchOrderDiscount.setPlaceholder("Discount");
        purchOrderDiscount.setPreferredSize(new java.awt.Dimension(200, 30));
        purchOrderDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                purchOrderDiscountKeyReleased(evt);
            }
        });

        purchOrderDisAmount.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(purchOrderDisAmount);
        purchOrderDisAmount.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        purchOrderDisAmount.setSelected(true);
        purchOrderDisAmount.setText("Amount");
        purchOrderDisAmount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                purchOrderDisAmountMouseClicked(evt);
            }
        });

        purchOrderDisPercent.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(purchOrderDisPercent);
        purchOrderDisPercent.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        purchOrderDisPercent.setText("Percent (%)");
        purchOrderDisPercent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                purchOrderDisPercentMouseClicked(evt);
            }
        });

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel40.setText("Discount Amount :");

        purchOrderTotal.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        purchOrderTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        purchOrderTotal.setText("0.00");

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel42.setText("Total :");

        purchOrderFinalDiscount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchOrderFinalDiscount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        purchOrderFinalDiscount.setText("0.0");

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel41.setText("Discount :");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(purchOrderCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(purchOrderComplete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(purchOrderSubTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(purchOrderDeleteItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(purchOrderTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel41)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(purchOrderDisAmount)))
                        .addGap(18, 18, 18)
                        .addComponent(purchOrderDisPercent))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addGap(0, 16, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(purchOrderFinalDiscount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(purchOrderDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(purchOrderSubTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(purchOrderDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(purchOrderDisPercent, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(purchOrderDisAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(purchOrderFinalDiscount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(purchOrderTotal)
                        .addGap(18, 18, 18)
                        .addComponent(purchOrderDeleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(purchOrderCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(purchOrderComplete, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel42)))
        );

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Products", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(26, 140, 255))); // NOI18N

        purchOrderProdTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Category", "Brand", "Description", "Quantity (In Stock)", "Re-Order Level", "Ordered Quantoty", "Item Code"
            }
        ));
        purchOrderProdTable.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        purchOrderProdTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchOrderProdTable.setRowHeight(25);
        purchOrderProdTable.setRowMargin(0);
        purchOrderProdTable.setSelectionBackground(new java.awt.Color(0, 60, 128));
        purchOrderProdTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                purchOrderProdTableMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(purchOrderProdTable);

        purchOrderProdDescription.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Description", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        purchOrderProdDescription.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        purchOrderProdDescription.setPhColor(new java.awt.Color(0, 51, 255));
        purchOrderProdDescription.setPlaceholder("Search by Description");
        purchOrderProdDescription.setPreferredSize(new java.awt.Dimension(200, 30));
        purchOrderProdDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                purchOrderProdDescriptionKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addComponent(purchOrderProdDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(purchOrderProdDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(purchOrderSupplierCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 426, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(purchOrderSupplierCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void purchOrderSupplierComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchOrderSupplierComboActionPerformed
        if (purchOrderSupplierCombo.getSelectedIndex() > 0) {
            supplierId = Integer.parseInt(purchOrderSupplierCombo.getSelectedItem().toString().split(" - ")[0].trim());
            String query = "SELECT product.Id as productid,"
                    + " product.Description as productname,"
                    + " product.TotalQuantity as productstock,"
                    + " product.ReOrderLevel as reorderlevel,"
                    + " product.OrderedQuantity as orderedquantity,"
                    + " category.Description as category,"
                    + " brand.BrandName as brand,"
                    + " supplierproduct.ItemCode"
                    + " FROM (((product"
                    + " INNER JOIN category ON category.Id=product.Category_Id)"
                    + " INNER JOIN brand ON brand.Id=product.Brand_Id)"
                    + " INNER JOIN supplierproduct ON supplierproduct.Product_Id=product.Id)"
                    + " WHERE product.Status=1 and"
                    + " supplierproduct.Supplier_Id=" + supplierId;
            fillPurchaseOrderProdTable(query);
        }
    }//GEN-LAST:event_purchOrderSupplierComboActionPerformed

    private void purchOrderProductIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_purchOrderProductIdKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String query = "SELECT `Id`, `Description` FROM `product` WHERE status=1 and Id="
                    + purchOrderProductId.getText();
            try {
                ResultSet rs = DbConnect.getFromDB(query);
                if (rs.next()) {
                    purchOrderDescription.setText(rs.getString("Description").toString());
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Purchase.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Purchase.class.getName()).log(Level.SEVERE, null, ex);
            }
            //purchBarcode.setText("");
            purchOrderStockQuantity.setText("");
            //purchOrderOrderedQuantity.setText("");
            //purchPurchasePrice.setText("");
            purchOrderQuantity.setText("");
            purchOrderBrand.setText("");

            String query1 = "SELECT product.Id as Id, product.Brand_Id as brandId, brand.BrandName as brandName,"
                    + " product.Category_Id as categoryId, category.Description as categoryName,"
                    + " product.Description as description, product.TotalQuantity as totalQuantity, product.orderedQuantity as orderedQuantity"
                    + " FROM ((product JOIN brand ON product.Brand_Id=brand.Id) JOIN category ON product.Category_Id=category.Id)"
                    + " WHERE product.Status = 1 and product.Id=" + purchOrderProductId.getText();
            fillPurchaseOrderProdTable(query1);
            String query2 = "SELECT batchesofproduct.Id as batchid,"
                    + " batchesofproduct.Barcode as Barcode,"
                    + " batchesofproduct.BatchNumber as BatchNumber,"
                    + " batchesofproduct.QuantityByBatch as quantity,"
                    + " batchesofproduct.PurchasePrice as purchPrice,"
                    + " batchesofproduct.RetailPrice AS RetailPrice"
                    + " FROM batchesofproduct "
                    + " WHERE batchesofproduct.Status = 1 and batchesofproduct.Product_Id=" + purchOrderProductId.getText();
            //fillPurchaseBatchTable(query2);
        }
    }//GEN-LAST:event_purchOrderProductIdKeyReleased

    private void purchOrderQuantityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_purchOrderQuantityKeyReleased
        double purchPrice = Double.parseDouble(purchOrderPurchasePrice.getText().toString());
        int quantity = Integer.parseInt(purchOrderQuantity.getText().toString());
        double total = purchPrice * quantity;
        purchOrderProductTotal.setText(Double.toString(total));
    }//GEN-LAST:event_purchOrderQuantityKeyReleased

    private void purchOrderAddToTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchOrderAddToTableActionPerformed
        Vector v = new Vector();
        v.add(purchOrderProductId.getText());
        v.add(purchOrderBrand.getText());
        v.add(purchOrderDescription.getText());
        v.add(purchOrderItemCode.getText());
        v.add(purchOrderPurchasePrice.getText());
        v.add(purchOrderQuantity.getText());
        v.add(purchOrderProductTotal.getText());

        tablemodel = (DefaultTableModel) purchOrderProduct.getModel();
        tablemodel.addRow(v);

        purchOrderProductId.setText("");
        purchOrderBrand.setText("");
        purchOrderDescription.setText("");
        purchOrderStockQuantity.setText("");
        purchOrderPurchasePrice.setText("");
        purchOrderQuantity.setText("");
        purchOrderItemCode.setText("");
        purchOrderProductTotal.setText("");

        calPurchaseOrderDetails();
    }//GEN-LAST:event_purchOrderAddToTableActionPerformed

    private void purchOrderProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purchOrderProductMouseClicked

    }//GEN-LAST:event_purchOrderProductMouseClicked

    private void purchOrderCompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchOrderCompleteActionPerformed
        if ((purchOrderProduct.getRowCount() > 0) && (purchOrderSupplierCombo.getSelectedIndex() > 0)) {
            String date = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(defaultDateFormat);

            calPurchaseOrderDetails();

            int userProId = SystemUser.userId;
            int currentPurchaseOrderId = 0;

            String purchQuery = "INSERT INTO `purchaseorder`(`Supplier_Id`, `Date`, `SubTotal`,"
                    + " `Discount`, `FinalTotal`, `UserProfile_Id`) VALUES (?,?,?,?,?,?)";
            try {
                PreparedStatement pst = DbConnect.getDBConnection().prepareStatement(purchQuery, Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, supplierId);
                pst.setString(2, date);
                pst.setDouble(3, totalWithNoDiscount);
                pst.setDouble(4, discount);
                pst.setDouble(5, finalTotal);
                pst.setInt(6, SystemUser.userId);
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

            for (int i = 0; i < purchOrderProduct.getRowCount(); i++) {
                String proId = purchOrderProduct.getValueAt(i, 0).toString();
                //String batchId = purchOrderProduct.getValueAt(i, 2).toString();
                String purchPrice = purchOrderProduct.getValueAt(i, 4).toString();
                String quantity = purchOrderProduct.getValueAt(i, 5).toString();
                String itemTotal = purchOrderProduct.getValueAt(i, 6).toString();

                try {
                    String itemQuery = "INSERT INTO `purchaseorderitem`(`PurchaseOrder_Id`,"
                            + " `Product_Id`, `ItemPrice`, `Quantity`, `TotalPrice`)"
                            + " VALUES ("+currentPurchaseOrderId+","+proId+","+purchPrice+","+quantity+","+itemTotal+")";

                    String updateProduct = "UPDATE product SET TotalQuantity=TotalQuantity+"
                            + quantity + ", OrderedQuantity=OrderedQuantity+" + quantity + " WHERE Id=" + proId;

                    DbConnect.pushToDB(itemQuery);
                    DbConnect.pushToDB(updateProduct);
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            clearPurchOrderPanel();
            calPurchaseOrderDetails();
        } else {
            if (purchOrderProduct.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No Items In the Purchased Product Table");
            }
            if (purchOrderSupplierCombo.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Please select Supplier");
            }
        }
    }//GEN-LAST:event_purchOrderCompleteActionPerformed

    private void purchOrderDeleteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchOrderDeleteItemActionPerformed
        try {
            tablemodel = (DefaultTableModel) purchOrderProduct.getModel();
            tablemodel.removeRow(purchOrderProduct.getSelectedRow());
            calPurchaseOrderDetails();
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }//GEN-LAST:event_purchOrderDeleteItemActionPerformed

    private void purchOrderCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchOrderCancelActionPerformed
        clearPurchOrderPanel();
    }//GEN-LAST:event_purchOrderCancelActionPerformed

    private void purchOrderDiscountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_purchOrderDiscountKeyReleased
        if (purchOrderDiscount.getText().toString().equals("")) {
            purchOrderDiscount.setText("0");
        }
        calPurchaseOrderDetails();
    }//GEN-LAST:event_purchOrderDiscountKeyReleased

    private void purchOrderDisAmountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purchOrderDisAmountMouseClicked
        calPurchaseOrderDetails();
    }//GEN-LAST:event_purchOrderDisAmountMouseClicked

    private void purchOrderDisPercentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purchOrderDisPercentMouseClicked
        calPurchaseOrderDetails();
    }//GEN-LAST:event_purchOrderDisPercentMouseClicked

    private void purchOrderProdTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purchOrderProdTableMouseClicked
        int selectedRow = purchOrderProdTable.getSelectedRow();
        purchOrderProductId.setText(purchOrderProdTable.getValueAt(selectedRow, 0).toString());
        purchOrderDescription.setText(purchOrderProdTable.getValueAt(selectedRow, 3).toString());
        purchOrderItemCode.setText(purchOrderProdTable.getValueAt(selectedRow, 7).toString());
        purchOrderStockQuantity.setText(purchOrderProdTable.getValueAt(selectedRow, 4).toString());
        purchOrderPurchasePrice.setText("");
        purchOrderProductTotal.setText("");
        purchOrderQuantity.setText("");
        purchOrderBrand.setText(purchOrderProdTable.getValueAt(selectedRow, 2).toString());
        purchOrderPurchasePrice.requestFocus();
    }//GEN-LAST:event_purchOrderProdTableMouseClicked

    private void purchOrderProdDescriptionKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_purchOrderProdDescriptionKeyReleased
        String query = "SELECT product.Id as productid,"
                + " product.Description as productname,"
                + " product.TotalQuantity as productstock,"
                + " product.ReOrderLevel as reorderlevel,"
                + " product.OrderedQuantity as orderedquantity,"
                + " category.Description as category,"
                + " brand.BrandName as brand,"
                + " supplierproduct.ItemCode"
                + " FROM (((product"
                + " INNER JOIN category ON category.Id=product.Category_Id)"
                + " INNER JOIN brand ON brand.Id=product.Brand_Id)"
                + " INNER JOIN supplierproduct ON supplierproduct.Product_Id=product.Id)"
                + " WHERE product.Status=1 and"
                + " supplierproduct.Supplier_Id=" + supplierId
                + " and product.Description like '%" + purchOrderProdDescription.getText().toString() + "%'";
        fillPurchaseOrderProdTable(query);
    }//GEN-LAST:event_purchOrderProdDescriptionKeyReleased

    private void purchOrderPurchasePriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_purchOrderPurchasePriceKeyReleased
        if ((evt.getKeyCode() == KeyEvent.VK_ENTER) && (purchOrderPurchasePrice.getText().length() > 0)) {
            purchOrderQuantity.requestFocus();
        }
    }//GEN-LAST:event_purchOrderPurchasePriceKeyReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JButton purchOrderAddToTable;
    private javax.swing.JLabel purchOrderBrand;
    private javax.swing.JButton purchOrderCancel;
    private javax.swing.JButton purchOrderComplete;
    private javax.swing.JButton purchOrderDeleteItem;
    private javax.swing.JLabel purchOrderDescription;
    private javax.swing.JRadioButton purchOrderDisAmount;
    private javax.swing.JRadioButton purchOrderDisPercent;
    private app.bolivia.swing.JCTextField purchOrderDiscount;
    private javax.swing.JLabel purchOrderFinalDiscount;
    private javax.swing.JLabel purchOrderItemCode;
    private app.bolivia.swing.JCTextField purchOrderProdDescription;
    private rojeru_san.complementos.RSTableMetro purchOrderProdTable;
    private rojeru_san.complementos.RSTableMetro purchOrderProduct;
    private app.bolivia.swing.JCTextField purchOrderProductId;
    private javax.swing.JLabel purchOrderProductTotal;
    private app.bolivia.swing.JCTextField purchOrderPurchasePrice;
    private app.bolivia.swing.JCTextField purchOrderQuantity;
    private javax.swing.JLabel purchOrderStockQuantity;
    private javax.swing.JLabel purchOrderSubTotal;
    private javax.swing.JComboBox<String> purchOrderSupplierCombo;
    private javax.swing.JLabel purchOrderTotal;
    // End of variables declaration//GEN-END:variables
}
