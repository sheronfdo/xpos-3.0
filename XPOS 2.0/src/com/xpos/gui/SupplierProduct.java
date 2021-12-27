/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpos.gui;

import com.xpos.database.DbConnect;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jamit
 */
public class SupplierProduct extends javax.swing.JPanel {

    DefaultTableModel tablemodel;

    /**
     * Creates new form SupplierProduct
     */
    public SupplierProduct() {
        initComponents();
        clearSupplierProductPanel();
    }

    private void clearSupplierProductPanel() {
        fillSupplierTable(null);
        fillProductTable(null);
        fillSupplierProductTable(null);
        supplierId.setText("");
        supplierName.setText("");
        productId.setText("");
        productName.setText("");
        itemCode.setText("");
    }

    public void fillProductTable(String query) {
        tablemodel = (DefaultTableModel) supProdProductTable.getModel();
        tablemodel.setRowCount(0);
        if (query == null) {
            query = "SELECT product.Id as Id, product.Brand_Id as brandId, brand.BrandName as brandName,"
                    + " product.Category_Id as categoryId, category.Description as categoryName,"
                    + " product.Description as description, product.TotalQuantity as totalQuantity,"
                    + " product.OrderedQuantity as orderedQuantity, product.ReOrderLevel as reOrderLevel"
                    + " FROM ((product JOIN brand ON product.Brand_Id=brand.Id) JOIN category ON product.Category_Id=category.Id)"
                    + " WHERE product.Status = 1";
        }
        try {
            ResultSet rs = DbConnect.getFromDB(query);
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt("Id"));
                v.add(rs.getString("Description"));
                v.add(rs.getString("brandId") + " - " + rs.getString("brandName"));
                v.add(rs.getString("categoryId") + " - " + rs.getString("categoryName"));
                v.add(rs.getInt("reOrderLevel"));
                v.add(rs.getInt("orderedQuantity"));
                v.add(rs.getInt("totalQuantity"));

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

    private void fillSupplierTable(String query) {
        tablemodel = (DefaultTableModel) supProdSupplierTable.getModel();
        tablemodel.setRowCount(0);
        if (query == null) {
            query = "select * from supplier where Status=1";
        }
        try {
            ResultSet rs = DbConnect.getFromDB(query);
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt("Id"));
                v.add(rs.getString("Name"));
                v.add(rs.getString("Address"));
                v.add(rs.getString("TelephoneNo"));
                v.add(rs.getString("RegisterNo"));
                tablemodel.addRow(v);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillSupplierProductTable(String query) {
        tablemodel = (DefaultTableModel) supplierProductTable.getModel();
        tablemodel.setRowCount(0);
        if (query == null) {
            query = "SELECT supplierproduct.`Supplier_Id` as SupplierId, supplierproduct.`Product_Id` as ProductId,"
                    + " supplierproduct. `ItemCode` as ItemCode,supplier.Name as supplierName, product.Description as productName"
                    + " FROM ((`supplierproduct` JOIN supplier on supplierproduct.Supplier_Id=supplier.Id)"
                    + " JOIN product on supplierproduct.Product_Id=product.Id) WHERE supplierproduct.Status=1";
        }
        ResultSet rs;
        try {
            rs = DbConnect.getFromDB(query);
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt("SupplierId"));
                v.add(rs.getString("supplierName"));
                v.add(rs.getInt("ProductId"));
                v.add(rs.getString("productName"));
                v.add(rs.getString("ItemCode"));
                tablemodel.addRow(v);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SupplierProduct.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SupplierProduct.class.getName()).log(Level.SEVERE, null, ex);
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

        jLabel5 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        supplierId = new app.bolivia.swing.JCTextField();
        supProdInsert = new javax.swing.JButton();
        supProdEdit = new javax.swing.JButton();
        supProdDelete = new javax.swing.JButton();
        supProdCancel = new javax.swing.JButton();
        supplierName = new javax.swing.JLabel();
        productId = new app.bolivia.swing.JCTextField();
        productName = new javax.swing.JLabel();
        itemCode = new app.bolivia.swing.JCTextField();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        supplierProductTable = new rojeru_san.complementos.RSTableMetro();
        supProductSearch = new app.bolivia.swing.JCTextField();
        jPanel1 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        supProdProductTable = new rojeru_san.complementos.RSTableMetro();
        supProdProductSearch = new app.bolivia.swing.JCTextField();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        supProdSupplierTable = new rojeru_san.complementos.RSTableMetro();
        supProdSupplierSearch = new app.bolivia.swing.JCTextField();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setBackground(new java.awt.Color(26, 140, 255));
        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("SUPPLIER PRODUCTS");
        jLabel5.setOpaque(true);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        supplierId.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Supplier ID", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        supplierId.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        supplierId.setPhColor(new java.awt.Color(0, 51, 255));
        supplierId.setPlaceholder("Supplier ID");
        supplierId.setPreferredSize(new java.awt.Dimension(200, 30));
        supplierId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                supplierIdKeyReleased(evt);
            }
        });

        supProdInsert.setBackground(new java.awt.Color(0, 60, 128));
        supProdInsert.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        supProdInsert.setForeground(new java.awt.Color(255, 255, 255));
        supProdInsert.setText("INSERT");
        supProdInsert.setBorder(null);
        supProdInsert.setFocusPainted(false);
        supProdInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supProdInsertActionPerformed(evt);
            }
        });

        supProdEdit.setBackground(new java.awt.Color(0, 60, 128));
        supProdEdit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        supProdEdit.setForeground(new java.awt.Color(255, 255, 255));
        supProdEdit.setText("EDIT");
        supProdEdit.setBorder(null);
        supProdEdit.setFocusPainted(false);
        supProdEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supProdEditActionPerformed(evt);
            }
        });

        supProdDelete.setBackground(new java.awt.Color(0, 60, 128));
        supProdDelete.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        supProdDelete.setForeground(new java.awt.Color(255, 255, 255));
        supProdDelete.setText("DELETE");
        supProdDelete.setBorder(null);
        supProdDelete.setFocusPainted(false);
        supProdDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supProdDeleteActionPerformed(evt);
            }
        });

        supProdCancel.setBackground(new java.awt.Color(0, 60, 128));
        supProdCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        supProdCancel.setForeground(new java.awt.Color(255, 255, 255));
        supProdCancel.setText("CANCEL");
        supProdCancel.setBorder(null);
        supProdCancel.setFocusPainted(false);
        supProdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supProdCancelActionPerformed(evt);
            }
        });

        supplierName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        supplierName.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Supplier Name", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        productId.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Product ID", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        productId.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        productId.setPhColor(new java.awt.Color(0, 51, 255));
        productId.setPlaceholder("Product ID");
        productId.setPreferredSize(new java.awt.Dimension(200, 30));
        productId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                productIdKeyReleased(evt);
            }
        });

        productName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        productName.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Product Description", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        itemCode.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Item Code", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        itemCode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        itemCode.setPhColor(new java.awt.Color(0, 51, 255));
        itemCode.setPlaceholder("Item Code");
        itemCode.setPreferredSize(new java.awt.Dimension(200, 30));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(supplierName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(supplierId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                                .addComponent(supProdInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(supProdEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(supProdDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(supProdCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(productId, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(productName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemCode, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(supplierId, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(supplierName, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(productId, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(productName, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(itemCode, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supProdInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supProdEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supProdDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(supProdCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(135, Short.MAX_VALUE))
        );

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Supplier Products", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        supplierProductTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Supplier ID", "Supplier Name", "Product ID", "Product Name", "Item Code"
            }
        ));
        supplierProductTable.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        supplierProductTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        supplierProductTable.setRowHeight(25);
        supplierProductTable.setRowMargin(0);
        supplierProductTable.setSelectionBackground(new java.awt.Color(0, 60, 128));
        supplierProductTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                supplierProductTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(supplierProductTable);

        supProductSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Search", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        supProductSearch.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        supProductSearch.setPhColor(new java.awt.Color(0, 51, 255));
        supProductSearch.setPlaceholder("Search");
        supProductSearch.setPreferredSize(new java.awt.Dimension(200, 30));
        supProductSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                supProductSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(supProductSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(supProductSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Prodcts", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        supProdProductTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Description", "Brand", "Category", "Re-Order Level", "Ordered Quantity", "Quantity"
            }
        ));
        supProdProductTable.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        supProdProductTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        supProdProductTable.setRowHeight(25);
        supProdProductTable.setRowMargin(0);
        supProdProductTable.setSelectionBackground(new java.awt.Color(0, 60, 128));
        supProdProductTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                supProdProductTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(supProdProductTable);

        supProdProductSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Search", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        supProdProductSearch.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        supProdProductSearch.setPhColor(new java.awt.Color(0, 51, 255));
        supProdProductSearch.setPlaceholder("Search");
        supProdProductSearch.setPreferredSize(new java.awt.Dimension(200, 30));
        supProdProductSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supProdProductSearchActionPerformed(evt);
            }
        });
        supProdProductSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                supProdProductSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(supProdProductSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 725, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(supProdProductSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Suppliers", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        supProdSupplierTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Address", "Telephone No", "Register No"
            }
        ));
        supProdSupplierTable.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        supProdSupplierTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        supProdSupplierTable.setRowHeight(25);
        supProdSupplierTable.setRowMargin(0);
        supProdSupplierTable.setSelectionBackground(new java.awt.Color(0, 60, 128));
        supProdSupplierTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                supProdSupplierTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(supProdSupplierTable);

        supProdSupplierSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Search", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        supProdSupplierSearch.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        supProdSupplierSearch.setPhColor(new java.awt.Color(0, 51, 255));
        supProdSupplierSearch.setPlaceholder("Search");
        supProdSupplierSearch.setPreferredSize(new java.awt.Dimension(200, 30));
        supProdSupplierSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                supProdSupplierSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(supProdSupplierSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(supProdSupplierSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void supProdInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supProdInsertActionPerformed
        int supId = Integer.parseInt(supplierId.getText().toString());
        int prodId = Integer.parseInt(productId.getText().toString());
        String itemCodeValue = itemCode.getText();

        String query = "INSERT INTO `supplierproduct`(`Supplier_Id`, `Product_Id`, `ItemCode`) VALUES (" + supId + "," + prodId + ",'" + itemCodeValue + "')";
        try {
            DbConnect.pushToDB(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        clearSupplierProductPanel();
    }//GEN-LAST:event_supProdInsertActionPerformed

    private void supProdEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supProdEditActionPerformed
        int supId = Integer.parseInt(supplierId.getText().toString());
        int prodId = Integer.parseInt(productId.getText().toString());
        String itemCodeValue = itemCode.getText();

        String query = "UPDATE `supplierproduct` SET `ItemCode`='" + itemCodeValue + "' WHERE `Supplier_Id`=" + supId + " and `Product_Id`=" + prodId;
        try {
            DbConnect.pushToDB(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        clearSupplierProductPanel();
    }//GEN-LAST:event_supProdEditActionPerformed

    private void supProdDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supProdDeleteActionPerformed
        int supId = Integer.parseInt(supplierId.getText().toString());
        int prodId = Integer.parseInt(productId.getText().toString());
        String query = "UPDATE `supplierproduct` SET `Status`=" + 0 + " WHERE `Supplier_Id`=" + supId + " and `Product_Id`=" + prodId;
        try {
            DbConnect.pushToDB(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        clearSupplierProductPanel();
    }//GEN-LAST:event_supProdDeleteActionPerformed

    private void supProdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supProdCancelActionPerformed
        clearSupplierProductPanel();
    }//GEN-LAST:event_supProdCancelActionPerformed

    private void supplierProductTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_supplierProductTableMouseClicked
        int selRow = supplierProductTable.getSelectedRow();

        supplierId.setText(supplierProductTable.getValueAt(selRow, 0).toString());
        supplierName.setText(supplierProductTable.getValueAt(selRow, 1).toString());
        productId.setText(supplierProductTable.getValueAt(selRow, 2).toString());
        productName.setText(supplierProductTable.getValueAt(selRow, 3).toString());
        itemCode.setText(supplierProductTable.getValueAt(selRow, 4).toString());
    }//GEN-LAST:event_supplierProductTableMouseClicked

    private void supProductSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_supProductSearchKeyReleased
        String search = supProductSearch.getText().toString();
        String query = "SELECT supplierproduct.`Supplier_Id` as SupplierId, supplierproduct.`Product_Id` as ProductId,"
                + " supplierproduct. `ItemCode` as ItemCode,supplier.Name as supplierName, product.Description as productName"
                + " FROM ((`supplierproduct` JOIN supplier on supplierproduct.Supplier_Id=supplier.Id)"
                + " JOIN product on supplierproduct.Product_Id=product.Id) WHERE supplierproduct.Status=1 and (supplier.Name like '%"
                + search + "%' OR product.Description like '%" + search + "%')";
        fillSupplierProductTable(query);
    }//GEN-LAST:event_supProductSearchKeyReleased

    private void supProdSupplierTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_supProdSupplierTableMouseClicked
        int selRow = supProdSupplierTable.getSelectedRow();
        supplierId.setText(supProdSupplierTable.getValueAt(selRow, 0).toString());
        supplierName.setText(supProdSupplierTable.getValueAt(selRow, 1).toString());
    }//GEN-LAST:event_supProdSupplierTableMouseClicked

    private void supProdSupplierSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_supProdSupplierSearchKeyReleased
        String query = "SELECT * from supplier WHERE Name LIKE '%" + supProdSupplierSearch.getText().toString() + "%' and Status=1";
        fillSupplierTable(query);
    }//GEN-LAST:event_supProdSupplierSearchKeyReleased

    private void supProdProductTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_supProdProductTableMouseClicked
        int selectedRow = supProdProductTable.getSelectedRow();
        productId.setText(supProdProductTable.getValueAt(selectedRow, 0).toString());
        productName.setText(supProdProductTable.getValueAt(selectedRow, 1).toString());
    }//GEN-LAST:event_supProdProductTableMouseClicked

    private void supProdProductSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supProdProductSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supProdProductSearchActionPerformed

    private void supProdProductSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_supProdProductSearchKeyReleased
        String query = "SELECT product.Id as Id, product.Brand_Id as brandId, brand.BrandName as brandName,"
                + " product.Category_Id as categoryId, category.Description as categoryName,"
                + " product.Description as description, product.TotalQuantity as totalQuantity,"
                + " product.OrderedQuantity as orderedQuantity, product.ReOrderLevel as reOrderLevel"
                + " FROM ((product JOIN brand ON product.Brand_Id=brand.Id) JOIN category ON product.Category_Id=category.Id)"
                + " where product.Description like '%" + supProdProductSearch.getText() + "%' and product.Status=1";
        fillProductTable(query);
    }//GEN-LAST:event_supProdProductSearchKeyReleased

    private void supplierIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_supplierIdKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int supId = Integer.parseInt(supplierId.getText().toString());
            String query = "SELECT supplier.Name FROM `supplier` WHERE id="+supId;
            try {
                ResultSet rs =  DbConnect.getFromDB(query);
                if (rs.next()) {
                    supplierName.setText(rs.getString("supplier.Name"));
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SupplierProduct.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(SupplierProduct.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_supplierIdKeyReleased

    private void productIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_productIdKeyReleased
if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int prodId = Integer.parseInt(productId.getText().toString());
            String query = "SELECT product.description FROM `product` WHERE id="+prodId;
            try {
                ResultSet rs =  DbConnect.getFromDB(query);
                if (rs.next()) {
                    productName.setText(rs.getString("product.description"));
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SupplierProduct.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(SupplierProduct.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_productIdKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private app.bolivia.swing.JCTextField itemCode;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private app.bolivia.swing.JCTextField productId;
    private javax.swing.JLabel productName;
    private javax.swing.JButton supProdCancel;
    private javax.swing.JButton supProdDelete;
    private javax.swing.JButton supProdEdit;
    private javax.swing.JButton supProdInsert;
    private app.bolivia.swing.JCTextField supProdProductSearch;
    private rojeru_san.complementos.RSTableMetro supProdProductTable;
    private app.bolivia.swing.JCTextField supProdSupplierSearch;
    private rojeru_san.complementos.RSTableMetro supProdSupplierTable;
    private app.bolivia.swing.JCTextField supProductSearch;
    private app.bolivia.swing.JCTextField supplierId;
    private javax.swing.JLabel supplierName;
    private rojeru_san.complementos.RSTableMetro supplierProductTable;
    // End of variables declaration//GEN-END:variables
}
