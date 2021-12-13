/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpos.gui;

import com.xpos.database.DbConnect;
import java.awt.Event;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.AncestorEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jamit
 */
public class ProductBatch extends javax.swing.JPanel {

    DefaultTableModel tablemodel;
    DateTimeFormatter defaultDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    int batchId;

    /**
     * Creates new form ProductBatch
     */
    public ProductBatch() {
        initComponents();
        batchManufacDate.setEnabled(false);
        batchExpireDate.setEnabled(false);
        batchDateEnable.setSelected(false);
        clearBatchPanel();
    }

    public void clearBatchPanel() {
        batchDescription.setText("");
        batchBarcode.setText("");
        batchProductId.setText("");
        batchNumber.setText("");
        batchQuantity.setText("");
        batchManufacDate.setDate(null);
        batchExpireDate.setDate(null);
        batchSearch.setText("");
        batchPurchPrice.setText("");
        batchRetailPrice.setText("");
        fillBatchTable(null);
        fillProductTable(null);
        batchDateEnable.setSelected(false);
        batchManufacDate.setEnabled(false);
        batchExpireDate.setEnabled(false);
    }

    public void fillBatchTable(String query) {
        tablemodel = (DefaultTableModel) batchTable.getModel();
        tablemodel.setRowCount(0);
        if (query == null) {
            query = "SELECT batchesofproduct.Id as batchid,"
                    + " batchesofproduct.Barcode as Barcode,"
                    + " product.Id as Product_Id,"
                    + " product.Description as productDescription,"
                    + " batchesofproduct.BatchNumber as BatchNumber,"
                    + " batchesofproduct.QuantityByBatch as quantity,"
                    + " batchesofproduct.PurchasePrice as purchPrice,"
                    + " batchesofproduct.RetailPrice AS RetailPrice"
                    + " FROM batchesofproduct inner join product"
                    + " on batchesofproduct.Product_Id=product.Id"
                    + " WHERE batchesofproduct.Status = 1";
        }
        try {
            ResultSet rs = DbConnect.getFromDB(query);
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt("batchid"));
                v.add(rs.getInt("Barcode"));
                v.add(rs.getInt("Product_Id") + " - " + rs.getString("productDescription"));
                v.add(rs.getInt("BatchNumber"));
                v.add(rs.getInt("quantity"));
                v.add(rs.getDouble("purchPrice"));
                v.add(rs.getDouble("RetailPrice"));

                String query2 = "select ManufactureDate as manufacDate,"
                        + "ExpireDate as expireDate "
                        + "from datesOfBatch "
                        + "where Status=1 and BatchesOfProduct_Id="
                        + rs.getInt("batchId");
                ResultSet rs2 = DbConnect.getFromDB(query2);
                if (rs2.next()) {
                    v.add(rs2.getDate("manufacDate"));
                    v.add(rs2.getDate("expireDate"));
                }
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

    public void fillProductTable(String query) {
        tablemodel = (DefaultTableModel) productTable.getModel();
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

    private void batchTableAncestorAdded(AncestorEvent evt) {
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel43 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        productTable = new rojeru_san.complementos.RSTableMetro();
        prodSearch = new app.bolivia.swing.JCTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        batchTable = new rojeru_san.complementos.RSTableMetro();
        batchSearch = new app.bolivia.swing.JCTextField();
        jPanel34 = new javax.swing.JPanel();
        batchNumber = new app.bolivia.swing.JCTextField();
        batchQuantity = new app.bolivia.swing.JCTextField();
        batchButInsert = new javax.swing.JButton();
        batchButEdit = new javax.swing.JButton();
        batchButDelete = new javax.swing.JButton();
        batchButCancel = new javax.swing.JButton();
        batchBarcode = new app.bolivia.swing.JCTextField();
        batchProductId = new app.bolivia.swing.JCTextField();
        batchDescription = new javax.swing.JLabel();
        batchManufacDate = new com.toedter.calendar.JDateChooser();
        batchExpireDate = new com.toedter.calendar.JDateChooser();
        batchPurchPrice = new app.bolivia.swing.JCTextField();
        batchRetailPrice = new app.bolivia.swing.JCTextField();
        batchDateEnable = new javax.swing.JCheckBox();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel43.setBackground(new java.awt.Color(26, 140, 255));
        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("PRODUCT BATCHES");
        jLabel43.setOpaque(true);

        jPanel33.setBackground(new java.awt.Color(255, 255, 255));

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Product Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Description", "Brand", "Category", "Re-Order Level", "Ordered Quantity", "Quantity"
            }
        ));
        productTable.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        productTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        productTable.setRowHeight(25);
        productTable.setRowMargin(0);
        productTable.setSelectionBackground(new java.awt.Color(0, 60, 128));
        productTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(productTable);

        prodSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Search", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        prodSearch.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        prodSearch.setPhColor(new java.awt.Color(0, 51, 255));
        prodSearch.setPlaceholder("Search");
        prodSearch.setPreferredSize(new java.awt.Dimension(200, 30));
        prodSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prodSearchActionPerformed(evt);
            }
        });
        prodSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                prodSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(prodSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1010, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(prodSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Batch Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        batchTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Barcode", "Product Description", "Batch Number", "Quantity", "Purchase Price", "Retail Price", "Manufacture Date", "Expire Date"
            }
        ));
        batchTable.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        batchTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        batchTable.setRowHeight(25);
        batchTable.setRowMargin(0);
        batchTable.setSelectionBackground(new java.awt.Color(0, 60, 128));
        batchTable.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                batchTableAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        batchTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                batchTableMouseClicked(evt);
            }
        });
        jScrollPane15.setViewportView(batchTable);

        batchSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Search", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        batchSearch.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        batchSearch.setPhColor(new java.awt.Color(0, 51, 255));
        batchSearch.setPlaceholder("Search");
        batchSearch.setPreferredSize(new java.awt.Dimension(200, 30));
        batchSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batchSearchActionPerformed(evt);
            }
        });
        batchSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                batchSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(batchSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane15)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(batchSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));

        batchNumber.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Batch Number", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        batchNumber.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        batchNumber.setPhColor(new java.awt.Color(0, 51, 255));
        batchNumber.setPlaceholder("Batch Number");
        batchNumber.setPreferredSize(new java.awt.Dimension(200, 30));

        batchQuantity.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Quantity", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        batchQuantity.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        batchQuantity.setPhColor(new java.awt.Color(0, 51, 255));
        batchQuantity.setPlaceholder("Quantity");
        batchQuantity.setPreferredSize(new java.awt.Dimension(200, 30));

        batchButInsert.setBackground(new java.awt.Color(0, 60, 128));
        batchButInsert.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        batchButInsert.setForeground(new java.awt.Color(255, 255, 255));
        batchButInsert.setText("INSERT");
        batchButInsert.setBorder(null);
        batchButInsert.setFocusPainted(false);
        batchButInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batchButInsertActionPerformed(evt);
            }
        });

        batchButEdit.setBackground(new java.awt.Color(0, 60, 128));
        batchButEdit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        batchButEdit.setForeground(new java.awt.Color(255, 255, 255));
        batchButEdit.setText("EDIT");
        batchButEdit.setBorder(null);
        batchButEdit.setFocusPainted(false);
        batchButEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batchButEditActionPerformed(evt);
            }
        });

        batchButDelete.setBackground(new java.awt.Color(0, 60, 128));
        batchButDelete.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        batchButDelete.setForeground(new java.awt.Color(255, 255, 255));
        batchButDelete.setText("DELETE");
        batchButDelete.setBorder(null);
        batchButDelete.setFocusPainted(false);
        batchButDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batchButDeleteActionPerformed(evt);
            }
        });

        batchButCancel.setBackground(new java.awt.Color(0, 60, 128));
        batchButCancel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        batchButCancel.setForeground(new java.awt.Color(255, 255, 255));
        batchButCancel.setText("CANCEL");
        batchButCancel.setBorder(null);
        batchButCancel.setFocusPainted(false);
        batchButCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batchButCancelActionPerformed(evt);
            }
        });

        batchBarcode.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Barcode", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        batchBarcode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        batchBarcode.setPhColor(new java.awt.Color(0, 51, 255));
        batchBarcode.setPlaceholder("Barcode");
        batchBarcode.setPreferredSize(new java.awt.Dimension(200, 30));
        batchBarcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                batchBarcodeKeyReleased(evt);
            }
        });

        batchProductId.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Product Id", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        batchProductId.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        batchProductId.setPhColor(new java.awt.Color(0, 51, 255));
        batchProductId.setPlaceholder("Product Id");
        batchProductId.setPreferredSize(new java.awt.Dimension(200, 30));
        batchProductId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                batchProductIdKeyReleased(evt);
            }
        });

        batchDescription.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        batchDescription.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Description", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        batchManufacDate.setBackground(new java.awt.Color(255, 255, 255));
        batchManufacDate.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Manufacture Date", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        batchManufacDate.setDateFormatString("yyyy-MM-dd");
        batchManufacDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        batchExpireDate.setBackground(new java.awt.Color(255, 255, 255));
        batchExpireDate.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Expire Date", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        batchExpireDate.setDateFormatString("yyyy-MM-dd");
        batchExpireDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        batchPurchPrice.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Purchase Price", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        batchPurchPrice.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        batchPurchPrice.setPhColor(new java.awt.Color(0, 51, 255));
        batchPurchPrice.setPlaceholder("Purchase Price");
        batchPurchPrice.setPreferredSize(new java.awt.Dimension(200, 30));

        batchRetailPrice.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Retail Price", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        batchRetailPrice.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        batchRetailPrice.setPhColor(new java.awt.Color(0, 51, 255));
        batchRetailPrice.setPlaceholder("Retail Price");
        batchRetailPrice.setPreferredSize(new java.awt.Dimension(200, 30));

        batchDateEnable.setBackground(new java.awt.Color(255, 255, 255));
        batchDateEnable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        batchDateEnable.setText("Include Below Dates");
        batchDateEnable.setFocusPainted(false);
        batchDateEnable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                batchDateEnableMouseReleased(evt);
            }
        });
        batchDateEnable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batchDateEnableActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(batchRetailPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(batchPurchPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(batchDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(batchProductId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(batchDateEnable)
                            .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(batchBarcode, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                                .addComponent(batchNumber, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(batchQuantity, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel34Layout.createSequentialGroup()
                                .addComponent(batchButInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(batchButEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(batchButDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(batchButCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(batchExpireDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(batchManufacDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel34Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(batchProductId, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(batchDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(batchBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(batchNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(batchQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(batchPurchPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(batchRetailPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(batchDateEnable)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(batchManufacDate, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(batchExpireDate, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(batchButInsert, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(batchButEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(batchButDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(batchButCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(206, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void batchTableMouseClicked(java.awt.event.MouseEvent evt) {
        int selectedRow = batchTable.getSelectedRow();
        batchId = Integer.parseInt(batchTable.getValueAt(selectedRow, 0).toString());
        batchBarcode.setText(batchTable.getValueAt(selectedRow, 1).toString());
        batchProductId.setText(batchTable.getValueAt(selectedRow, 2).toString().split(" - ")[0].trim());
        batchDescription.setText(batchTable.getValueAt(selectedRow, 2).toString().split(" - ")[1].trim());
        batchNumber.setText(batchTable.getValueAt(selectedRow, 3).toString());
        batchQuantity.setText(batchTable.getValueAt(selectedRow, 4).toString());
        batchPurchPrice.setText(batchTable.getValueAt(selectedRow, 5).toString());
        batchRetailPrice.setText(batchTable.getValueAt(selectedRow, 6).toString());
        try {
            if ((batchTable.getValueAt(selectedRow, 7) == null)
                    && (batchTable.getValueAt(selectedRow, 8) == null)) {
                batchDateEnable.setSelected(false);
                batchManufacDate.setEnabled(false);
                batchExpireDate.setEnabled(false);
                batchManufacDate.setDate(null);
                batchExpireDate.setDate(null);

            } else if ((new String(batchTable.getValueAt(selectedRow, 7).toString()).length() > 0)
                    && (new String(batchTable.getValueAt(selectedRow, 8).toString()).length() > 0)) {
                batchDateEnable.setSelected(true);
                batchManufacDate.setEnabled(true);
                batchExpireDate.setEnabled(true);
                batchManufacDate.setDate(
                        new SimpleDateFormat("yyyy-MM-dd").parse(batchTable.getValueAt(selectedRow, 7).toString()));
                batchExpireDate.setDate(
                        new SimpleDateFormat("yyyy-MM-dd").parse(batchTable.getValueAt(selectedRow, 8).toString()));
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    private void batchSearchActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void batchSearchKeyReleased(java.awt.event.KeyEvent evt) {
        String query = "SELECT batchesofproduct.Id as batchid,"
                + " batchesofproduct.Barcode as Barcode,"
                + " product.Id as Product_Id,"
                + " product.Description as productDescription,"
                + " batchesofproduct.BatchNumber as BatchNumber,"
                + " batchesofproduct.QuantityByBatch as quantity,"
                + " batchesofproduct.PurchasePrice as purchPrice,"
                + " batchesofproduct.RetailPrice AS RetailPrice"
                + " FROM batchesofproduct inner join product"
                + " on batchesofproduct.Product_Id=product.Id"
                + " WHERE product.Description LIKE '%" + batchSearch.getText() + "%' AND batchesofproduct.Status = 1";
        fillBatchTable(query);
    }

    private void batchButInsertActionPerformed(java.awt.event.ActionEvent evt) {
        int productId = Integer.parseInt(batchProductId.getText());
        int barcode = Integer.parseInt(batchBarcode.getText());
        int batchNum = Integer.parseInt(batchNumber.getText());
        int quantity = Integer.parseInt(batchQuantity.getText());
        double purchPrice = Double.valueOf(batchPurchPrice.getText());
        double retailPrice = Double.valueOf(batchRetailPrice.getText());

        String query = "INSERT INTO `batchesofproduct`(`Product_Id`, `BatchNumber`,"
                + " `Barcode`, `RetailPrice`, `PurchasePrice`, `QuantityByBatch`) VALUES"
                + " (" + productId + "," + batchNum + "," + barcode + ","
                + retailPrice + "," + purchPrice + "," + quantity + ")";
        try {

            PreparedStatement stmt = DbConnect.getDBConnection().prepareStatement(query,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            int batchId = rs.getInt(1);
            if (batchDateEnable.isSelected()) {
                LocalDate manuDate = batchManufacDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate expDate = batchExpireDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                query = "INSERT INTO `datesofbatch`(`BatchesOfProduct_Id`, `ManufactureDate`, `ExpireDate`) VALUES ('"
                        + batchId + "','"
                        + defaultDateFormat.format(manuDate) + "','" + defaultDateFormat.format(expDate) + "')";
                DbConnect.pushToDB(query);
            }
            clearBatchPanel();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void batchButEditActionPerformed(java.awt.event.ActionEvent evt) {
        int productId = Integer.parseInt(batchProductId.getText());
        int barcode = Integer.parseInt(batchBarcode.getText());
        int batchNum = Integer.parseInt(batchNumber.getText());
        int quantity = Integer.parseInt(batchQuantity.getText());
        double purchPrice = Double.valueOf(batchPurchPrice.getText());
        double retailPrice = Double.valueOf(batchRetailPrice.getText());

        String query = "UPDATE `batchesofproduct` SET "
                + "`Product_Id`='" + productId + "',"
                + "`BatchNumber`='" + batchNum + "',"
                + "`Barcode`='" + barcode + "',"
                + "`RetailPrice`='" + retailPrice + "',"
                + "`PurchasePrice`='" + purchPrice + "',"
                + "`QuantityByBatch`='" + quantity + "'"
                + "WHERE Id=" + batchId;
        try {
            DbConnect.pushToDB(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean dateCheckBox = batchDateEnable.isSelected();
        boolean hasManufacDate = !(batchTable.getValueAt(batchTable.getSelectedRow(), 7) == null);
        boolean hasExpireDate = !(batchTable.getValueAt(batchTable.getSelectedRow(), 8) == null);

        System.out.println(dateCheckBox);
        System.out.println(hasManufacDate);
        System.out.println(hasExpireDate);

        if ((dateCheckBox) && (hasManufacDate && hasExpireDate)) {
            String manuDate = defaultDateFormat.format(batchManufacDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            String expDate = defaultDateFormat.format(batchExpireDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            String query2 = "UPDATE `datesofbatch` SET `ManufactureDate`='" + manuDate + "',"
                    + "`ExpireDate`='" + expDate + "' WHERE `BatchesOfProduct_Id`=" + batchId;
            try {
                DbConnect.pushToDB(query2);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("(dateCheckBox) && (hasManufacDate && hasExpireDate)");
        } else if ((dateCheckBox == false) && (hasManufacDate && hasExpireDate)) {
            String query2 = "DELETE from `datesofbatch` WHERE `BatchesOfProduct_Id`=" + batchId;
            try {
                DbConnect.pushToDB(query2);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("(dateCheckBox == false) && (hasManufacDate && hasExpireDate)");
        } else if ((dateCheckBox) && ((hasManufacDate && hasExpireDate) == false)) {
            LocalDate manuDate = batchManufacDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate expDate = batchExpireDate.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            query = "INSERT INTO `datesofbatch`(`BatchesOfProduct_Id`, `ManufactureDate`, `ExpireDate`) VALUES ('"
                    + batchId + "','"
                    + defaultDateFormat.format(manuDate) + "','"
                    + defaultDateFormat.format(expDate) + "')";
            try {
                DbConnect.pushToDB(query);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("(dateCheckBox) && ((hasManufacDate && hasExpireDate) == false)");
        }
        clearBatchPanel();
    }

    private void batchButDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        String query = "UPDATE `batchesofproduct` SET `Status`=0 WHERE Id=" + batchId;
        try {
            DbConnect.pushToDB(query);
            clearBatchPanel();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void batchButCancelActionPerformed(java.awt.event.ActionEvent evt) {
        clearBatchPanel();
    }

    private void batchBarcodeKeyReleased(java.awt.event.KeyEvent evt) {

    }
//654645

    private void batchProductIdKeyReleased(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String prodId = batchProductId.getText();
            String query = "select Description from product where Id='" + prodId + "' and status=1";
            try {
                ResultSet rs = DbConnect.getFromDB(query);
                while (rs.next()) {
                    batchDescription.setText(rs.getString("Description"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String query2 = "SELECT batchesofproduct.Id as batchid,"
                    + " batchesofproduct.Barcode as Barcode,"
                    + " product.Id as Product_Id,"
                    + " product.Description as productDescription,"
                    + " batchesofproduct.BatchNumber as BatchNumber,"
                    + " batchesofproduct.QuantityByBatch as quantity,"
                    + " batchesofproduct.PurchasePrice as purchPrice,"
                    + " batchesofproduct.RetailPrice AS RetailPrice"
                    + " FROM batchesofproduct inner join product"
                    + " on batchesofproduct.Product_Id=product.Id"
                    + " WHERE product.Id=" + prodId + " AND batchesofproduct.Status = 1";
            fillBatchTable(query2);
            String query3 = "SELECT product.Id as Id, product.Brand_Id as brandId, brand.BrandName as brandName,"
                    + " product.Category_Id as categoryId, category.Description as categoryName,"
                    + " product.Description as description, product.TotalQuantity as totalQuantity,"
                    + " product.OrderedQuantity as orderedQuantity, product.ReOrderLevel as reOrderLevel"
                    + " FROM ((product JOIN brand ON product.Brand_Id=brand.Id) JOIN category ON product.Category_Id=category.Id)"
                    + " WHERE product.Status = 1 and product.id=" + prodId;
            fillProductTable(query3);
        }
    }

    private void batchDateEnableActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void batchDateEnableMouseReleased(java.awt.event.MouseEvent evt) {
        if (batchDateEnable.isSelected()) {
            batchManufacDate.setEnabled(true);
            batchExpireDate.setEnabled(true);
        } else {
            batchManufacDate.setEnabled(false);
            batchExpireDate.setEnabled(false);
        }
    }

    private void productTableMouseClicked(java.awt.event.MouseEvent evt) {
        int selectedRow = productTable.getSelectedRow();
        int prodId = Integer.parseInt(productTable.getValueAt(selectedRow, 0).toString());
        String query = "select Description from product where Id='" + prodId + "' and status=1";
        String query2 = "SELECT batchesofproduct.Id as batchid,"
                + " batchesofproduct.Barcode as Barcode,"
                + " product.Id as Product_Id,"
                + " product.Description as productDescription,"
                + " batchesofproduct.BatchNumber as BatchNumber,"
                + " batchesofproduct.QuantityByBatch as quantity,"
                + " batchesofproduct.PurchasePrice as purchPrice,"
                + " batchesofproduct.RetailPrice AS RetailPrice"
                + " FROM batchesofproduct inner join product on"
                + " batchesofproduct.Product_Id=product.Id"
                + " WHERE batchesofproduct.Status = 1 and product.Id=" + prodId;
        fillBatchTable(query2);
        try {
            ResultSet rs = DbConnect.getFromDB(query);
            while (rs.next()) {
                batchDescription.setText(rs.getString("Description"));
                batchProductId.setText(Integer.toString(prodId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prodSearchActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void prodSearchKeyReleased(java.awt.event.KeyEvent evt) {
        String query = "SELECT product.Id as Id, product.Brand_Id as brandId, brand.BrandName as brandName,"
                + " product.Category_Id as categoryId, category.Description as categoryName,"
                + " product.Description as description, product.TotalQuantity as totalQuantity,"
                + " product.OrderedQuantity as orderedQuantity, product.ReOrderLevel as reOrderLevel"
                + " FROM ((product JOIN brand ON product.Brand_Id=brand.Id) JOIN category ON product.Category_Id=category.Id)"
                + " where product.Description like '%" + prodSearch.getText() + "%' and product.Status=1";
        fillProductTable(query);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private app.bolivia.swing.JCTextField batchBarcode;
    private javax.swing.JButton batchButCancel;
    private javax.swing.JButton batchButDelete;
    private javax.swing.JButton batchButEdit;
    private javax.swing.JButton batchButInsert;
    private javax.swing.JCheckBox batchDateEnable;
    private javax.swing.JLabel batchDescription;
    private com.toedter.calendar.JDateChooser batchExpireDate;
    private com.toedter.calendar.JDateChooser batchManufacDate;
    private app.bolivia.swing.JCTextField batchNumber;
    private app.bolivia.swing.JCTextField batchProductId;
    private app.bolivia.swing.JCTextField batchPurchPrice;
    private app.bolivia.swing.JCTextField batchQuantity;
    private app.bolivia.swing.JCTextField batchRetailPrice;
    private app.bolivia.swing.JCTextField batchSearch;
    private rojeru_san.complementos.RSTableMetro batchTable;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane2;
    private app.bolivia.swing.JCTextField prodSearch;
    private rojeru_san.complementos.RSTableMetro productTable;
    // End of variables declaration//GEN-END:variables
}
