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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Jamit
 */
public class Sale extends javax.swing.JPanel {

    DefaultTableModel tablemodel;
    boolean cusSelectPanel = false;

    DateTimeFormatter defaultDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter defaultTimeFormat = DateTimeFormatter.ofPattern("hh:mm:ss");

    //needed details
    Double total = 0.0d; // total retail price
    Double itemDis = 0.0d; // discount by items
    Double finalTotalDiscount = 0.0d; // discount final
    Double finalTotal = 0.0d; // final total  (with all discounts)
    Double totalPurchaseCost = 0.0d; // purchasing cost
    Double profit = 0.0d; // profit
    Double payment = 0.0d; // customer pay
    Double totDisValue = 0.0d;// discount by total

    int customerID;
    int user = SystemUser.userId;

    /**
     * Creates new form Sale
     */
    public Sale() {
        initComponents();
        clearSalePanel();
    }

    private void identifyCustomerId() {
        customerID = Integer.parseInt(saleCustomerLabel.getText().trim().split(" - ")[0]);
    }

    private void completeSale() {
        LocalDateTime dateTime = LocalDateTime.now();
        String date = dateTime.format(defaultDateFormat);
        String time = dateTime.format(defaultTimeFormat);

        identifyCustomerId();
        fillSaleDetails();

        int saleId;

        String query = "INSERT INTO `sale`(`Date`, `Time`, `Customer_Id`, `TotalRetailPrice`, `DiscountByItems`, "
                + "`DiscountByTotal`, `Cost`, `FinalTotal`, `Profit`, `Pay`, `UserProfile_Id`) "
                + "VALUES ('" + date + "','" + time + "'," + customerID + ",'" + total.toString() + "','" + itemDis.toString()
                + "','" + totDisValue.toString() + "','" + totalPurchaseCost.toString() + "','" + finalTotal.toString() + "','" + profit.toString()
                + "','" + payment.toString() + "'," + user + ")";

        try {
            PreparedStatement pst = DbConnect.getDBConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                saleId = rs.getInt(1);
                for (int i = 0; i < saleTable.getRowCount(); i++) {
                    int prodId = Integer.parseInt(saleTable.getValueAt(i, 0).toString());
                    double retailPrice = Double.parseDouble(saleTable.getValueAt(i, 3).toString());
                    int quantity = Integer.parseInt(saleTable.getValueAt(i, 4).toString());
                    double total = Double.parseDouble(saleTable.getValueAt(i, 5).toString());
                    double discount = Double.parseDouble(saleTable.getValueAt(i, 6).toString());
                    double balance = Double.parseDouble(saleTable.getValueAt(i, 7).toString());
                    int batchId = Integer.parseInt(saleTable.getValueAt(i, 2).toString());

                    String query2 = "INSERT INTO `solditem`(`Sale_Id`, `Product_Id`, `RetailPrice`, `Quantity`,"
                            + " `Total`, `DiscountForItem`, `Balance`, `BatchesOfProduct_Id`) VALUES"
                            + " (" + saleId + "," + prodId + "," + retailPrice + "," + quantity
                            + "," + total + "," + discount + "," + balance + "," + batchId + ")";
                    DbConnect.pushToDB(query2);

                    String query3 = "UPDATE product SET TotalQuantity=TotalQuantity-" + quantity + " WHERE product.Id=" + prodId;
                    DbConnect.pushToDB(query3);
                    String query4 = "UPDATE batchesofproduct SET QuantityByBatch=QuantityByBatch-" + quantity + " WHERE batchesofproduct.Id=" + batchId;
                    DbConnect.pushToDB(query4);

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void clearSalePanel() {
        clearSaleItem();
        fillSaleProdTable(null);
        tablemodel = (DefaultTableModel) saleBatchTable.getModel();
        tablemodel.setRowCount(0);

        tablemodel = (DefaultTableModel) saleTable.getModel();
        tablemodel.setRowCount(0);
        saleTotDisAmount.setSelected(true);
        fillSaleDetails();
        saleTotDiscount.setText("0.0");
        saleCustPay.setText("0.0");
        saleCustomerLabel.setText("1 - Temporary");
    }

    private void fillSaleDetails() {
        total = 0.0d; // total retail price
        itemDis = 0.0d; // discount by items
        finalTotalDiscount = 0.0d; // discount final
        finalTotal = 0.0d; // final total  (with all discounts)
        totalPurchaseCost = 0.0d; // purchasing cost
        profit = 0.0d; // profit
        payment = 0.0d; // customer pay
        totDisValue = 0.0d;

        tablemodel = (DefaultTableModel) saleTable.getModel();

        payment = Double.parseDouble(saleCustPay.getText().toString().equals("") ? "0"
                : saleCustPay.getText().toString());

        Double balance = 0.0d;
        Double totDis = Double.parseDouble(saleTotDiscount.getText().toString().equals("") ? "0"
                : saleTotDiscount.getText().toString());
        boolean totDisType = saleTotDisAmount.isSelected();

        if (saleTable.getRowCount() > 0) {
            for (int i = 0; i < saleTable.getRowCount(); i++) {
                total += Double.parseDouble(saleTable.getValueAt(i, 5).toString());
                itemDis += Double.parseDouble(saleTable.getValueAt(i, 6).toString());
            }

            if (totDisType) {
                totDisValue = totDis;
            } else {
                totDisValue = totDis / 100 * (total - itemDis);
            }
            finalTotalDiscount = itemDis + totDisValue;
            finalTotal = total - finalTotalDiscount;
            balance = payment - finalTotal;
        }
        saleSaveBalance.setText(balance.toString());
        saleFinalTotal.setText(finalTotal.toString());
        saleFinalTotalDiscount.setText(finalTotalDiscount.toString());
        saleTotalDiscount.setText(totDisValue.toString());
        saleSubTotal.setText(total.toString());
        saleIBIDiscount.setText(itemDis.toString());
        totalPurchaseCost = calTotalPurchaseCost();
        salePurchCost.setText(totalPurchaseCost.toString());
        profit = finalTotal - totalPurchaseCost;
        saleProfit.setText(profit.toString());
    }

    private Double calTotalPurchaseCost() {
        Double purchaseCost = 0.0d;

        for (int i = 0; i < saleTable.getRowCount(); i++) {
            Double itemPurchaseCost = 0.0d;
            double itemTotal = 0.0d;
            int soldItemQuantity = Integer.parseInt(saleTable.getValueAt(i, 4).toString());
            int batchId = Integer.parseInt(saleTable.getValueAt(i, 2).toString());
            String query = "select PurchasePrice from batchesofproduct where id=" + batchId;
            try {
                ResultSet rs = DbConnect.getFromDB(query);
                if (rs.next()) {
                    itemPurchaseCost = rs.getDouble("PurchasePrice");
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
            }
            itemTotal = itemPurchaseCost * soldItemQuantity;
            purchaseCost += itemTotal;
        }
        return purchaseCost;
    }

    private void fillSaleProdTable(String query) {
        tablemodel = (DefaultTableModel) saleProdTable.getModel();
        tablemodel.setRowCount(0);
        if (query == null) {
            query = "SELECT product.Id as Id, product.Brand_Id as brandId, brand.BrandName as brandName,"
                    + " product.Category_Id as categoryId, category.Description as categoryName,"
                    + " product.Description as description, product.TotalQuantity as totalQuantity"
                    + " FROM ((product JOIN brand ON product.Brand_Id=brand.Id) JOIN category ON product.Category_Id=category.Id)"
                    + " WHERE product.Status = 1";
        }
        try {
            ResultSet rs = DbConnect.getFromDB(query);
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt("Id"));
                v.add(rs.getString("categoryId") + " - " + rs.getString("categoryName"));
                v.add(rs.getString("brandId") + " - " + rs.getString("brandName"));
                v.add(rs.getString("Description"));
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

    private void fillSaleBatchTable(String query) {
        int prodId = 0;
        tablemodel = (DefaultTableModel) saleBatchTable.getModel();
        tablemodel.setRowCount(0);
        if (query == null) {
            prodId = Integer.parseInt(saleProdTable.getValueAt(saleProdTable.getSelectedRow(), 0).toString().trim());
            query = "SELECT batchesofproduct.Id as batchid,"
                    + " batchesofproduct.Barcode as Barcode,"
                    + " batchesofproduct.BatchNumber as BatchNumber,"
                    + " batchesofproduct.QuantityByBatch as quantity,"
                    + " batchesofproduct.PurchasePrice as purchPrice,"
                    + " batchesofproduct.RetailPrice AS RetailPrice"
                    + " FROM batchesofproduct "
                    + " WHERE batchesofproduct.Status = 1 and batchesofproduct.Product_Id=" + prodId;
        }
        try {
            ResultSet rs = DbConnect.getFromDB(query);
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt("batchid"));
                v.add(rs.getInt("BatchNumber"));
                v.add(rs.getInt("Barcode"));
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

    private void calSaleItemTotal() {
        int quantity;
        if (saleQuantity.getText().toString().equals("")) {
            quantity = 0;
        } else if (2147483647 < Long.parseLong(saleQuantity.getText().toString())) {
            quantity = 0;
        } else {
            quantity = Integer.parseInt(saleQuantity.getText());
        }
        double retailPrice = Double.parseDouble(saleRetailPrice.getText());
        double discount = 0.0;
        if (saleDiscount.getText().equals("")) {
            discount = 0.0;
        } else {
            discount = Double.parseDouble(saleDiscount.getText());
        }
        if (saleItemDisAmount.isSelected()) {
            retailPrice = retailPrice - discount;
        }
        if (saleItemDisPercent.isSelected()) {
            retailPrice = (retailPrice * (100 - discount)) / 100;
        }
        Double saleItemSubTotal = retailPrice * quantity;
        saleItemTotal.setText(saleItemSubTotal.toString());
    }

    private void clearSaleItem() {
        saleProdId.setText("");
        saleBarcode.setText("");
        saleDesc.setText("");
        saleRetailPrice.setText("");
        salePurchasePrice.setText("");
        saleBatchId.setText("");
        saleManufacDate.setText("");
        saleExpireDate.setText("");
        saleQuantity.setText("");
        saleDiscount.setText("");
        saleItemTotal.setText("");
        saleItemDisAmount.setSelected(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        saleItemDiscount = new javax.swing.ButtonGroup();
        finalItemDiscount = new javax.swing.ButtonGroup();
        customerPopup = new javax.swing.JPopupMenu();
        selectCustPopup = new com.xpos.gui.SelectCustPopup();
        jLabel19 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        saleProdTable = new rojeru_san.complementos.RSTableMetro();
        saleProdBarcode = new app.bolivia.swing.JCTextField();
        saleProdDesc = new app.bolivia.swing.JCTextField();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        saleBatchTable = new rojeru_san.complementos.RSTableMetro();
        saleProdBatchNumber = new app.bolivia.swing.JCTextField();
        jPanel30 = new javax.swing.JPanel();
        saleProdId = new app.bolivia.swing.JCTextField();
        saleBarcode = new app.bolivia.swing.JCTextField();
        saleDesc = new javax.swing.JLabel();
        saleBatchId = new app.bolivia.swing.JCTextField();
        saleManufacDate = new javax.swing.JLabel();
        saleExpireDate = new javax.swing.JLabel();
        saleQuantity = new app.bolivia.swing.JCTextField();
        saleAddToTable = new javax.swing.JButton();
        salePurchasePrice = new javax.swing.JLabel();
        saleDiscount = new app.bolivia.swing.JCTextField();
        saleItemDisAmount = new javax.swing.JRadioButton();
        saleItemDisPercent = new javax.swing.JRadioButton();
        saleRetailPrice = new javax.swing.JLabel();
        saleItemTotal = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        saleComplete = new javax.swing.JButton();
        saleDeleteItem = new javax.swing.JButton();
        saleCancel = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        saleSubTotal = new javax.swing.JLabel();
        saleIBIDiscount = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        saleTotDiscount = new app.bolivia.swing.JCTextField();
        jLabel28 = new javax.swing.JLabel();
        saleTotDisAmount = new javax.swing.JRadioButton();
        saleTotDisPercent = new javax.swing.JRadioButton();
        jLabel29 = new javax.swing.JLabel();
        saleFinalTotal = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        saleCustPay = new app.bolivia.swing.JCTextField();
        jLabel32 = new javax.swing.JLabel();
        saleSaveBalance = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        saleProfit = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        salePurchCost = new javax.swing.JLabel();
        saleTotalDiscount = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        saleFinalTotalDiscount = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel32 = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        saleTable = new rojeru_san.complementos.RSTableMetro();
        selectCustomer = new javax.swing.JButton();
        saleCustomer = new javax.swing.JPanel();
        saleCustomerLabel = new javax.swing.JLabel();

        customerPopup.setBackground(new java.awt.Color(255, 255, 255));

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel19.setBackground(new java.awt.Color(26, 140, 255));
        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("SALE");
        jLabel19.setOpaque(true);

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));
        jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Products", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(26, 140, 255))); // NOI18N

        saleProdTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Category", "Brand", "Description", "Total Quantity"
            }
        ));
        saleProdTable.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        saleProdTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleProdTable.setRowHeight(25);
        saleProdTable.setRowMargin(0);
        saleProdTable.setSelectionBackground(new java.awt.Color(0, 60, 128));
        saleProdTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saleProdTableMouseClicked(evt);
            }
        });
        saleProdTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                saleProdTableKeyReleased(evt);
            }
        });
        jScrollPane12.setViewportView(saleProdTable);

        saleProdBarcode.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Barcode", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        saleProdBarcode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleProdBarcode.setPhColor(new java.awt.Color(0, 51, 255));
        saleProdBarcode.setPlaceholder("Search Barcode");
        saleProdBarcode.setPreferredSize(new java.awt.Dimension(200, 30));
        saleProdBarcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                saleProdBarcodeKeyReleased(evt);
            }
        });

        saleProdDesc.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Description", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        saleProdDesc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleProdDesc.setPhColor(new java.awt.Color(0, 51, 255));
        saleProdDesc.setPlaceholder("Search by Description");
        saleProdDesc.setPreferredSize(new java.awt.Dimension(200, 30));
        saleProdDesc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                saleProdDescKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(saleProdBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saleProdDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane12)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saleProdBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saleProdDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE))
        );

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));
        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Batch details of Products", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(26, 140, 255))); // NOI18N

        saleBatchTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Batch ID", "Batch Number", "Barcode", "Quantities By Batch", "Purchase Price", "Retail Price", "Manufac. Date", "Expire Date"
            }
        ));
        saleBatchTable.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        saleBatchTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleBatchTable.setRowHeight(25);
        saleBatchTable.setRowMargin(0);
        saleBatchTable.setSelectionBackground(new java.awt.Color(0, 60, 128));
        saleBatchTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saleBatchTableMouseClicked(evt);
            }
        });
        jScrollPane13.setViewportView(saleBatchTable);

        saleProdBatchNumber.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Batch Number", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        saleProdBatchNumber.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleProdBatchNumber.setPhColor(new java.awt.Color(0, 51, 255));
        saleProdBatchNumber.setPlaceholder("Search By Batch Number");
        saleProdBatchNumber.setPreferredSize(new java.awt.Dimension(200, 30));
        saleProdBatchNumber.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                saleProdBatchNumberKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addComponent(saleProdBatchNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane13)
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addComponent(saleProdBatchNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));

        saleProdId.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Product Id", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        saleProdId.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleProdId.setPhColor(new java.awt.Color(0, 51, 255));
        saleProdId.setPlaceholder("Search by Product Id");
        saleProdId.setPreferredSize(new java.awt.Dimension(200, 30));
        saleProdId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                saleProdIdKeyReleased(evt);
            }
        });

        saleBarcode.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Barcode", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        saleBarcode.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleBarcode.setPhColor(new java.awt.Color(0, 51, 255));
        saleBarcode.setPlaceholder("Search Barcode");
        saleBarcode.setPreferredSize(new java.awt.Dimension(200, 30));
        saleBarcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                saleBarcodeKeyReleased(evt);
            }
        });

        saleDesc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleDesc.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Description", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        saleBatchId.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Batch ID", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        saleBatchId.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleBatchId.setPhColor(new java.awt.Color(0, 51, 255));
        saleBatchId.setPlaceholder("Search Batch ID");
        saleBatchId.setPreferredSize(new java.awt.Dimension(200, 30));
        saleBatchId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                saleBatchIdKeyReleased(evt);
            }
        });

        saleManufacDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleManufacDate.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Manufacture Date", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        saleExpireDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleExpireDate.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Expire Date", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        saleQuantity.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Quantity", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        saleQuantity.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleQuantity.setPhColor(new java.awt.Color(0, 51, 255));
        saleQuantity.setPlaceholder("Enter Quantity");
        saleQuantity.setPreferredSize(new java.awt.Dimension(200, 30));
        saleQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                saleQuantityKeyReleased(evt);
            }
        });

        saleAddToTable.setBackground(new java.awt.Color(0, 60, 128));
        saleAddToTable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleAddToTable.setForeground(new java.awt.Color(255, 255, 255));
        saleAddToTable.setText("ADD TO TABLE");
        saleAddToTable.setBorder(null);
        saleAddToTable.setFocusPainted(false);
        saleAddToTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saleAddToTableActionPerformed(evt);
            }
        });

        salePurchasePrice.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        salePurchasePrice.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Purchase Price", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        saleDiscount.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Discount", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        saleDiscount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleDiscount.setPhColor(new java.awt.Color(0, 51, 255));
        saleDiscount.setPlaceholder("Discount for One Unit");
        saleDiscount.setPreferredSize(new java.awt.Dimension(200, 30));
        saleDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                saleDiscountKeyReleased(evt);
            }
        });

        saleItemDisAmount.setBackground(new java.awt.Color(255, 255, 255));
        saleItemDiscount.add(saleItemDisAmount);
        saleItemDisAmount.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        saleItemDisAmount.setSelected(true);
        saleItemDisAmount.setText("Amount");
        saleItemDisAmount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saleItemDisAmountMouseClicked(evt);
            }
        });

        saleItemDisPercent.setBackground(new java.awt.Color(255, 255, 255));
        saleItemDiscount.add(saleItemDisPercent);
        saleItemDisPercent.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        saleItemDisPercent.setText("Percent (%)");
        saleItemDisPercent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saleItemDisPercentMouseClicked(evt);
            }
        });

        saleRetailPrice.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleRetailPrice.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Retail Price", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        saleItemTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleItemTotal.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Total with Discount", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saleProdId, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saleBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(saleDesc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saleBatchId, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(saleExpireDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saleManufacDate, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(salePurchasePrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saleQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(saleDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(saleRetailPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addComponent(saleItemTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(saleItemDisAmount)
                            .addComponent(saleItemDisPercent))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 190, Short.MAX_VALUE)
                        .addComponent(saleAddToTable, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addComponent(saleProdId, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(saleBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(saleDesc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saleManufacDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(salePurchasePrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saleRetailPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saleItemTotal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(saleDiscount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(saleQuantity, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(saleExpireDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(saleBatchId, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addComponent(saleItemDisPercent)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saleItemDisAmount))
                    .addComponent(saleAddToTable, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));
        jPanel31.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Purchase Actions", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(26, 140, 255))); // NOI18N

        saleComplete.setBackground(new java.awt.Color(0, 60, 128));
        saleComplete.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        saleComplete.setForeground(new java.awt.Color(255, 255, 255));
        saleComplete.setText("COMPLETE SALE");
        saleComplete.setBorder(null);
        saleComplete.setFocusPainted(false);
        saleComplete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saleCompleteActionPerformed(evt);
            }
        });

        saleDeleteItem.setBackground(new java.awt.Color(0, 60, 128));
        saleDeleteItem.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        saleDeleteItem.setForeground(new java.awt.Color(255, 255, 255));
        saleDeleteItem.setText("DELETE ITEM");
        saleDeleteItem.setBorder(null);
        saleDeleteItem.setFocusPainted(false);
        saleDeleteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saleDeleteItemActionPerformed(evt);
            }
        });

        saleCancel.setBackground(new java.awt.Color(0, 60, 128));
        saleCancel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        saleCancel.setForeground(new java.awt.Color(255, 255, 255));
        saleCancel.setText("CANCEL SALE");
        saleCancel.setBorder(null);
        saleCancel.setFocusPainted(false);
        saleCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saleCancelActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        jLabel23.setText("Total (without discount): ");

        saleSubTotal.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        saleSubTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        saleSubTotal.setText("0.00");

        saleIBIDiscount.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        saleIBIDiscount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        saleIBIDiscount.setText("0.00");

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        jLabel27.setText("Discount (item by item):");

        saleTotDiscount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)));
        saleTotDiscount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleTotDiscount.setPhColor(new java.awt.Color(0, 51, 255));
        saleTotDiscount.setPlaceholder("Discount for Total");
        saleTotDiscount.setPreferredSize(new java.awt.Dimension(200, 30));
        saleTotDiscount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                saleTotDiscountKeyReleased(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        jLabel28.setText("Discount (for Total):");

        saleTotDisAmount.setBackground(new java.awt.Color(255, 255, 255));
        finalItemDiscount.add(saleTotDisAmount);
        saleTotDisAmount.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        saleTotDisAmount.setSelected(true);
        saleTotDisAmount.setText("Amount");
        saleTotDisAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saleTotDisAmountActionPerformed(evt);
            }
        });

        saleTotDisPercent.setBackground(new java.awt.Color(255, 255, 255));
        finalItemDiscount.add(saleTotDisPercent);
        saleTotDisPercent.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        saleTotDisPercent.setText("Percent (%)");
        saleTotDisPercent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saleTotDisPercentActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        jLabel29.setText("Total:");

        saleFinalTotal.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        saleFinalTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        saleFinalTotal.setText("0.00");

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        jLabel31.setText("Payment:");

        saleCustPay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)));
        saleCustPay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        saleCustPay.setPhColor(new java.awt.Color(0, 51, 255));
        saleCustPay.setPlaceholder("Customer Payment");
        saleCustPay.setPreferredSize(new java.awt.Dimension(200, 30));
        saleCustPay.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                saleCustPayKeyReleased(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        jLabel32.setText("Balance");

        saleSaveBalance.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        saleSaveBalance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        saleSaveBalance.setText("0.00");

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel34.setText("Profit:");

        saleProfit.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        saleProfit.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        saleProfit.setText("0.00");

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel36.setText("Purchase cost");

        salePurchCost.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        salePurchCost.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        salePurchCost.setText("0.00");

        saleTotalDiscount.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        saleTotalDiscount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        saleTotalDiscount.setText("0.00");

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        jLabel24.setText("Discount (for Total):");

        saleFinalTotalDiscount.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        saleFinalTotalDiscount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        saleFinalTotalDiscount.setText("0.00");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        jLabel25.setText("Final Total Discount:");

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saleComplete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saleSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(saleTotDisPercent)
                            .addComponent(saleIBIDiscount, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addComponent(saleTotDiscount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addGap(18, 18, 18)
                        .addComponent(saleFinalTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(saleCustPay, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saleSaveBalance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel31Layout.createSequentialGroup()
                                .addComponent(jLabel34)
                                .addGap(18, 18, 18)
                                .addComponent(saleProfit, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(saleDeleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(saleTotDisAmount, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(saleCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                                .addComponent(jLabel36)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(salePurchCost, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10)
                        .addComponent(saleFinalTotalDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(saleTotalDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(saleSubTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(saleIBIDiscount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saleTotDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saleTotDisPercent)
                    .addComponent(saleTotDisAmount))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saleTotalDiscount, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saleFinalTotalDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saleFinalTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saleCustPay, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(saleSaveBalance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(salePurchCost)
                        .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(saleProfit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saleCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saleDeleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saleComplete, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel32.setBackground(new java.awt.Color(255, 255, 255));
        jPanel32.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Sold Products", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(26, 140, 255))); // NOI18N

        saleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Description", "Batch ID", "Retail Price", "Quantity", "Total", "Discount", "Total with Discount"
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
        jScrollPane14.setViewportView(saleTable);

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane14, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane14, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE))
        );

        selectCustomer.setBackground(new java.awt.Color(0, 60, 128));
        selectCustomer.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        selectCustomer.setForeground(new java.awt.Color(255, 255, 255));
        selectCustomer.setText("SELECT CUSTOMER");
        selectCustomer.setBorder(null);
        selectCustomer.setFocusPainted(false);
        selectCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectCustomerActionPerformed(evt);
            }
        });

        saleCustomer.setBackground(new java.awt.Color(255, 255, 255));

        saleCustomerLabel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Customer", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        javax.swing.GroupLayout saleCustomerLayout = new javax.swing.GroupLayout(saleCustomer);
        saleCustomer.setLayout(saleCustomerLayout);
        saleCustomerLayout.setHorizontalGroup(
            saleCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
            .addGroup(saleCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(saleCustomerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
        );
        saleCustomerLayout.setVerticalGroup(
            saleCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(saleCustomerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(saleCustomerLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(saleCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(selectCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(saleCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    public void selectProduct() {
        fillSaleBatchTable(null);
        clearSaleItem();
        int selectedProdId = saleProdTable.getSelectedRow();
        saleProdId.setText(saleProdTable.getValueAt(selectedProdId, 0).toString());
        saleDesc.setText(saleProdTable.getValueAt(selectedProdId, 3).toString());
    }

    private void saleProdTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saleProdTableMouseClicked
        selectProduct();
    }//GEN-LAST:event_saleProdTableMouseClicked

    private void saleProdBarcodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saleProdBarcodeKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String query = "SELECT `Id`, `Barcode`, `Description`, `PurchasePrice`, "
                    + "`RetailPrice`, `TotalQuantity`, `Status` FROM `product` WHERE status=1 and Barcode="
                    + saleProdBarcode.getText();
            fillSaleProdTable(query);
        }
    }//GEN-LAST:event_saleProdBarcodeKeyReleased

    private void saleProdDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saleProdDescKeyReleased
        String query = "SELECT product.Id as Id, product.Brand_Id as brandId, brand.BrandName as brandName,"
                    + " product.Category_Id as categoryId, category.Description as categoryName,"
                    + " product.Description as description, product.TotalQuantity as totalQuantity"
                    + " FROM ((product JOIN brand ON product.Brand_Id=brand.Id) JOIN category ON product.Category_Id=category.Id)"
                    + " WHERE product.Status = 1 and product.Description like '%"+saleProdDesc.getText().toString()+"%'";
        fillSaleProdTable(query);
    }//GEN-LAST:event_saleProdDescKeyReleased

    private void saleBatchTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saleBatchTableMouseClicked
        int selectedRow = saleBatchTable.getSelectedRow();
        saleBatchId.setText(saleBatchTable.getValueAt(selectedRow, 0).toString());
        saleBarcode.setText(saleBatchTable.getValueAt(selectedRow, 2).toString());
        saleManufacDate.setText(saleBatchTable.getValueAt(selectedRow, 6) == null ? "" : saleBatchTable.getValueAt(selectedRow, 6).toString());
        saleExpireDate.setText(saleBatchTable.getValueAt(selectedRow, 7) == null ? "" : saleBatchTable.getValueAt(selectedRow, 7).toString());
        salePurchasePrice.setText(saleBatchTable.getValueAt(selectedRow, 4).toString());
        saleRetailPrice.setText(saleBatchTable.getValueAt(selectedRow, 5).toString());
    }//GEN-LAST:event_saleBatchTableMouseClicked

    private void saleProdBatchNumberKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saleProdBatchNumberKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String query = "SELECT "
                    + "batchesofproduct.BatchNumber as BatchNumber, "
                    + "batchesofproduct.QuantityByBatch as Quantity, "
                    + "batchesofproduct.ManufactureDate as ManufactureDate, "
                    + "batchesofproduct.ExpireDate as ExpireDate "
                    + "from batchesofproduct WHERE batchesofproduct.BatchNumber="
                    + saleProdBatchNumber.getText() + " and batchesofproduct.Product_Id=" + saleProdId.getText();
            fillSaleBatchTable(query);
        }
    }//GEN-LAST:event_saleProdBatchNumberKeyReleased

    private void saleProdIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saleProdIdKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String prodId = saleProdId.getText().trim();
            clearSaleItem();
            saleProdId.setText(prodId);
            String query = "SELECT product.Id as Id, product.Brand_Id as brandId, brand.BrandName as brandName,"
                    + " product.Category_Id as categoryId, category.Description as categoryName,"
                    + " product.Description as description, product.TotalQuantity as totalQuantity"
                    + " FROM ((product JOIN brand ON product.Brand_Id=brand.Id) JOIN category ON product.Category_Id=category.Id)"
                    + " WHERE product.Status = 1 and product.Id='" + prodId + "'";

            fillSaleProdTable(query);
            String query2 = "SELECT batchesofproduct.Id as batchid,"
                    + " batchesofproduct.Barcode as Barcode,"
                    + " batchesofproduct.BatchNumber as BatchNumber,"
                    + " batchesofproduct.QuantityByBatch as quantity,"
                    + " batchesofproduct.PurchasePrice as purchPrice,"
                    + " batchesofproduct.RetailPrice AS RetailPrice"
                    + " FROM batchesofproduct "
                    + " WHERE batchesofproduct.Status = 1 and batchesofproduct.Product_Id='" + prodId + "'";
            fillSaleBatchTable(query2);
            String query3 = "select Description from product where id='" + prodId + "'";
            try {
                ResultSet rs = DbConnect.getFromDB(query3);
                rs.next();
                saleDesc.setText(rs.getString("Description"));
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_saleProdIdKeyReleased

    private void saleBarcodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saleBarcodeKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String query = "SELECT batchesofproduct.Id as batchid,"
                    + " batchesofproduct.Barcode as Barcode,"
                    + " batchesofproduct.BatchNumber as BatchNumber,"
                    + " batchesofproduct.QuantityByBatch as quantity,"
                    + " batchesofproduct.PurchasePrice as purchPrice,"
                    + " batchesofproduct.RetailPrice AS RetailPrice"
                    + " FROM batchesofproduct "
                    + " WHERE batchesofproduct.Status = 1 and batchesofproduct.Barcode='" + saleBarcode.getText().trim() + "'";
            fillSaleBatchTable(query);
            String query2 = "SELECT product.id as id, product.Description as description"
                    + " FROM product JOIN batchesofproduct ON product.Id = batchesofproduct.Product_Id"
                    + " WHERE batchesofproduct.Barcode='" + saleBarcode.getText().trim() + "'";
            ResultSet rs;
            try {
                rs = DbConnect.getFromDB(query2);
                rs.next();
                saleProdId.setText(rs.getString("id"));
                saleDesc.setText(rs.getString("description"));
                String query3 = "SELECT product.Id as Id, product.Brand_Id as brandId, brand.BrandName as brandName,"
                        + " product.Category_Id as categoryId, category.Description as categoryName,"
                        + " product.Description as description, product.TotalQuantity as totalQuantity"
                        + " FROM ((product JOIN brand ON product.Brand_Id=brand.Id) JOIN category ON product.Category_Id=category.Id)"
                        + " WHERE product.Status = 1 and product.Id='" + rs.getString("id").toString().trim() + "'";

                fillSaleProdTable(query3);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(Sale.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_saleBarcodeKeyReleased

    private void saleBatchIdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saleBatchIdKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int typedBatchNumber = Integer.parseInt(saleBatchId.getText());
            int productId = Integer.parseInt(saleProdId.getText());
            String query = "SELECT batchesofproduct.Id as batchid,"
                    + " batchesofproduct.Barcode as Barcode,"
                    + " batchesofproduct.BatchNumber as BatchNumber,"
                    + " batchesofproduct.QuantityByBatch as quantity,"
                    + " batchesofproduct.PurchasePrice as purchPrice,"
                    + " batchesofproduct.RetailPrice AS RetailPrice,"
                    + " datesofbatch.manufacturedate as ManufactureDate,"
                    + " datesofbatch.expiredate AS ExpireDate"
                    + " FROM batchesofproduct join datesofbatch on batchesofproduct.id=datesofbatch.batchesofproduct_id"
                    + " WHERE batchesofproduct.Status = 1 and batchesofproduct.Product_Id="
                    + productId + " and batchesofproduct.id=" + typedBatchNumber;
            try {
                ResultSet rs = DbConnect.getFromDB(query);
                if (rs.next()) {
                    saleManufacDate.setText(rs.getDate("ManufactureDate").toString());
                    saleExpireDate.setText(rs.getDate("ExpireDate").toString());
                    saleBarcode.setText(rs.getString("Barcode").toString());
                    salePurchasePrice.setText(rs.getString("purchPrice").toString());
                    saleRetailPrice.setText(rs.getString("RetailPrice").toString());
                } else {
                    saleManufacDate.setText("");
                    saleExpireDate.setText("");
                    saleBarcode.setText("");
                    salePurchasePrice.setText("");
                    saleRetailPrice.setText("");
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_saleBatchIdKeyReleased

    private void saleQuantityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saleQuantityKeyReleased
        calSaleItemTotal();
    }//GEN-LAST:event_saleQuantityKeyReleased

    private void saleAddToTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saleAddToTableActionPerformed
        String prodId = saleProdId.getText();
        String description = saleDesc.getText();
        String batch = saleBatchId.getText();
        String retailPrice = saleRetailPrice.getText();
        String quantity = saleQuantity.getText();
        Double total = Double.parseDouble(retailPrice) * Integer.parseInt(quantity);
        String totWithDisc = saleItemTotal.getText();
        Double discount = total - Double.parseDouble(totWithDisc);

        Vector v = new Vector();
        v.add(prodId);
        v.add(description);
        v.add(batch);
        v.add(retailPrice);
        v.add(quantity);
        v.add(total);
        v.add(discount);
        v.add(totWithDisc);

        tablemodel = (DefaultTableModel) saleTable.getModel();
        tablemodel.addRow(v);
        fillSaleDetails();
        clearSaleItem();
    }//GEN-LAST:event_saleAddToTableActionPerformed

    private void saleDiscountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saleDiscountKeyReleased
        if (!saleQuantity.getText().equals("")) {
            calSaleItemTotal();
        }
    }//GEN-LAST:event_saleDiscountKeyReleased

    private void saleItemDisAmountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saleItemDisAmountMouseClicked
        if (!saleQuantity.getText().equals("")) {
            calSaleItemTotal();
        }
    }//GEN-LAST:event_saleItemDisAmountMouseClicked

    private void saleItemDisPercentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saleItemDisPercentMouseClicked
        if (!saleQuantity.getText().equals("")) {
            calSaleItemTotal();
        }
    }//GEN-LAST:event_saleItemDisPercentMouseClicked

    private void saleCompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saleCompleteActionPerformed
        completeSale();
        clearSalePanel();
    }//GEN-LAST:event_saleCompleteActionPerformed

    private void saleDeleteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saleDeleteItemActionPerformed
        tablemodel = (DefaultTableModel) saleTable.getModel();
        tablemodel.removeRow(saleTable.getSelectedRow());
        fillSaleDetails();
    }//GEN-LAST:event_saleDeleteItemActionPerformed

    private void saleCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saleCancelActionPerformed
        clearSalePanel();
    }//GEN-LAST:event_saleCancelActionPerformed

    private void saleTotDiscountKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saleTotDiscountKeyReleased
        fillSaleDetails();
    }//GEN-LAST:event_saleTotDiscountKeyReleased

    private void saleCustPayKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saleCustPayKeyReleased
        fillSaleDetails();
    }//GEN-LAST:event_saleCustPayKeyReleased

    private void saleTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saleTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_saleTableMouseClicked

    private void selectCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectCustomerActionPerformed
        if (!cusSelectPanel) {
            selectCustomer.setText("CONFIRM CUSTOMER");
            customerPopup.add(selectCustPopup);
            customerPopup.show(selectCustomer, selectCustomer.getWidth() - 506, selectCustomer.getHeight());
            cusSelectPanel = true;
        } else {
            String customer = selectCustPopup.getCusIdName();
            saleCustomerLabel.setText(customer);
            cusSelectPanel = false;
            selectCustomer.setText("SELECT CUSTOMER");
            identifyCustomerId();
        }
    }//GEN-LAST:event_selectCustomerActionPerformed

    private void saleProdTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saleProdTableKeyReleased
        selectProduct();
    }//GEN-LAST:event_saleProdTableKeyReleased

    private void saleTotDisPercentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saleTotDisPercentActionPerformed
        fillSaleDetails();
    }//GEN-LAST:event_saleTotDisPercentActionPerformed

    private void saleTotDisAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saleTotDisAmountActionPerformed
        fillSaleDetails();
    }//GEN-LAST:event_saleTotDisAmountActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu customerPopup;
    private javax.swing.ButtonGroup finalItemDiscount;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JButton saleAddToTable;
    private app.bolivia.swing.JCTextField saleBarcode;
    private app.bolivia.swing.JCTextField saleBatchId;
    private rojeru_san.complementos.RSTableMetro saleBatchTable;
    private javax.swing.JButton saleCancel;
    private javax.swing.JButton saleComplete;
    private app.bolivia.swing.JCTextField saleCustPay;
    private javax.swing.JPanel saleCustomer;
    private javax.swing.JLabel saleCustomerLabel;
    private javax.swing.JButton saleDeleteItem;
    private javax.swing.JLabel saleDesc;
    private app.bolivia.swing.JCTextField saleDiscount;
    private javax.swing.JLabel saleExpireDate;
    private javax.swing.JLabel saleFinalTotal;
    private javax.swing.JLabel saleFinalTotalDiscount;
    private javax.swing.JLabel saleIBIDiscount;
    private javax.swing.JRadioButton saleItemDisAmount;
    private javax.swing.JRadioButton saleItemDisPercent;
    private javax.swing.ButtonGroup saleItemDiscount;
    private javax.swing.JLabel saleItemTotal;
    private javax.swing.JLabel saleManufacDate;
    private app.bolivia.swing.JCTextField saleProdBarcode;
    private app.bolivia.swing.JCTextField saleProdBatchNumber;
    private app.bolivia.swing.JCTextField saleProdDesc;
    private app.bolivia.swing.JCTextField saleProdId;
    private rojeru_san.complementos.RSTableMetro saleProdTable;
    private javax.swing.JLabel saleProfit;
    private javax.swing.JLabel salePurchCost;
    private javax.swing.JLabel salePurchasePrice;
    private app.bolivia.swing.JCTextField saleQuantity;
    private javax.swing.JLabel saleRetailPrice;
    private javax.swing.JLabel saleSaveBalance;
    private javax.swing.JLabel saleSubTotal;
    private rojeru_san.complementos.RSTableMetro saleTable;
    private javax.swing.JRadioButton saleTotDisAmount;
    private javax.swing.JRadioButton saleTotDisPercent;
    private app.bolivia.swing.JCTextField saleTotDiscount;
    private javax.swing.JLabel saleTotalDiscount;
    private com.xpos.gui.SelectCustPopup selectCustPopup;
    private javax.swing.JButton selectCustomer;
    // End of variables declaration//GEN-END:variables
}
