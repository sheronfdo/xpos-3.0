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
public class Return extends javax.swing.JPanel {

    DefaultTableModel tablemodel;
    DateTimeFormatter defaultDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter defaultTimeFormat = DateTimeFormatter.ofPattern("hh:mm:ss");
    boolean invoiceSelectPanel = false;

    //return data
    private double totalRefundAmount = 0.0;

    //invoice Data
    private int invoiceId = 0;
    private String date = null;
    private String time = null;
    private int customerId = 0;
    private String customerName = null;
    private double totalRetailPrice = 0.0;
    private double discountByItems = 0.0;
    private double discountByTotal = 0.0;
    private double totalDisPercent = 0.0;
    private double cost = 0.0;
    private double finalTotal = 0.0;
    private double profit = 0.0;
    private double pay = 0.0;
    private int userProfileId = 0;

    //soldItem Data
    private int productId = 0;
    private String productName = "";
    private double productRetailPrice = 0.0;
    private int ProductQuantity = 0;
    private double productRetailTotal = 0.0;
    private double productDiscount = 0.0;
    private double productDiscountForItem = 0.0;
    private double productFinalTotal = 0.0;
    private int productBatchId = 0;
    private double productTotalDiscountForItem = 0.0;

    //return item data
    private int curReturnProductId = 0;
    private String curReturnProductName = "";
    private String curReturnType = "";
    private String curReturnCondition = "";
    private int curReturnQuantity = 0;
    private double curReturnRefundAmount = 0.0;

    /**
     * Creates new form Purchase
     */
    public Return() {
        initComponents();
        returnInvoiceId.setText("");
        returnInvoiceDate.setText("");
        returnInvoiceTime.setText("");
        returnInvoiceCusId.setText("");
        returnInvoiceCusName.setText("");
        returnInvoiceTotal.setText("");
        returnInvoiceItDis.setText("");
        returnInvoiceTotDis.setText("");
        returnInvoiceFinalTotal.setText("");
        returnInvoiceCusPay.setText("");
    }

    private void getInvoiceDetails() {
        try {
            String query = "select sale.Id, sale.Date, sale.Time, sale.Customer_Id,"
                    + " customer.Name, sale.TotalRetailPrice, sale.DiscountByItems,"
                    + " sale.DiscountByTotal, sale.Cost, sale.FinalTotal, sale.Profit,"
                    + " sale.Pay, sale.UserProfile_Id"
                    + " FROM sale JOIN customer on sale.Customer_Id=customer.Id"
                    + " WHERE sale.Id=" + invoiceId;
            ResultSet rs = DbConnect.getFromDB(query);
            if (rs.next()) {
                invoiceId = rs.getInt("Id");
                date = rs.getDate("Date").toString();
                time = rs.getTime("Time").toString();
                customerId = rs.getInt("customer_Id");
                customerName = rs.getString("Name");
                totalRetailPrice = rs.getDouble("totalRetailPrice");
                discountByItems = rs.getDouble("discountByItems");
                discountByTotal = rs.getDouble("discountByTotal");
                cost = rs.getDouble("cost");
                finalTotal = rs.getDouble("FinalTotal");
                profit = rs.getDouble("Profit");
                pay = rs.getDouble("Pay");
                userProfileId = rs.getInt("userProfile_Id");
                totalDisPercent = (discountByTotal / (totalRetailPrice - discountByItems)) * 100;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Return.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Return.class.getName()).log(Level.SEVERE, null, ex);
        }
        setInvoiceDetails();
    }

    private void setInvoiceDetails() {
        returnInvoiceId.setText(Integer.toString(invoiceId));
        returnInvoiceDate.setText(date);
        returnInvoiceTime.setText(time);
        returnInvoiceCusId.setText(Integer.toString(customerId));
        returnInvoiceCusName.setText(customerName);
        returnInvoiceTotal.setText(Double.toString(totalRetailPrice));
        returnInvoiceItDis.setText(Double.toString(discountByItems));
        returnInvoiceTotDis.setText(Double.toString(discountByTotal));
        returnInvoiceFinalTotal.setText(Double.toString(finalTotal));
        returnInvoiceCusPay.setText(Double.toString(pay));
        fillSoldItemTable(null);
    }

    private void calTotalRefundAmount() {
        totalRefundAmount = 0.0;
        for (int i = 0; i < returnTable.getRowCount(); i++) {
            totalRefundAmount += Double.parseDouble(returnTable.getValueAt(i, 5).toString());
        }
        totalRefundAmountLabel.setText(Double.toString(totalRefundAmount));
    }

    private void calRefundAmount() {
        curReturnProductId = productId;
        curReturnProductName = productName;
        curReturnType = returnTypeReplacement.isSelected() ? "Replacement" : "Refund";
        curReturnCondition = returnProductCondDamage.isSelected() ? "Damage" : "Good";
        try {
            curReturnQuantity = Integer.parseInt(returnQuantity.getText() == null ? "0" : returnQuantity.getText()) > ProductQuantity
                    ? ProductQuantity : Integer.parseInt(returnQuantity.getText());
            returnQuantity.setText(Integer.toString(curReturnQuantity));
        } catch (NumberFormatException e) {
            returnQuantity.setText("0");
            curReturnQuantity = 0;
        }
        curReturnRefundAmount = 0.0;

        if (returnTypeReplacement.isSelected()) {
            curReturnRefundAmount = 0.0;
        } else if (returnTypeRefund.isSelected()) {
            curReturnRefundAmount = (productRetailPrice - productTotalDiscountForItem) * curReturnQuantity;
        }

        returnProductRefundAmount.setText(Double.toString(curReturnRefundAmount));
    }

    private void clearReturnItemPanel() {
        returnProductId.setText("");
        returnProductDescription.setText("");
        returnSoldQuantity.setText("");
        returnProductRetailPrice.setText("");
        returnProductTotalDiscount.setText("");
        returnQuantity.setText("");
        returnProductRefundAmount.setText("");
        returnTypeReplacement.setSelected(true);
        returnProductCondDamage.setSelected(true);
    }

    private void clearReturnPanel() {
        //invoice Data
        invoiceId = 0;
        date = null;
        time = null;
        customerId = 0;
        customerName = null;
        totalRetailPrice = 0.0;
        discountByItems = 0.0;
        discountByTotal = 0.0;
        totalDisPercent = 0.0;
        cost = 0.0;
        finalTotal = 0.0;
        profit = 0.0;
        pay = 0.0;
        userProfileId = 0;

        //soldItem Data
        productId = 0;
        productName = "";
        productRetailPrice = 0.0;
        ProductQuantity = 0;
        productRetailTotal = 0.0;
        productDiscount = 0.0;
        productDiscountForItem = 0.0;
        productFinalTotal = 0.0;
        productBatchId = 0;
        productTotalDiscountForItem = 0.0;

        //return item data
        curReturnProductId = 0;
        curReturnProductName = "";
        curReturnType = "";
        curReturnCondition = "";
        curReturnQuantity = 0;
        curReturnRefundAmount = 0.0;

        clearReturnItemPanel();

        returnInvoiceId.setText("");
        returnInvoiceDate.setText("");
        returnInvoiceTime.setText("");
        returnInvoiceCusId.setText("");
        returnInvoiceCusName.setText("");
        returnInvoiceTotal.setText("");
        returnInvoiceItDis.setText("");
        returnInvoiceTotDis.setText("");
        returnInvoiceFinalTotal.setText("");
        returnInvoiceCusPay.setText("");
        returnSoldItemSearch.setText("");
        saleInvoiceLabel.setText("");
        totalRefundAmountLabel.setText("0.0");

        tablemodel = (DefaultTableModel) returnSoldItemTable.getModel();
        tablemodel.setRowCount(0);
        tablemodel = (DefaultTableModel) returnTable.getModel();
        tablemodel.setRowCount(0);
    }

    private void fillSoldItemTable(String query) {
        tablemodel = (DefaultTableModel) returnSoldItemTable.getModel();
        tablemodel.setRowCount(0);
        try {
            if (query == null) {
                query = "SELECT solditem.Product_Id,"
                        + " product.Description,"
                        + " solditem.BatchesOfProduct_Id,"
                        + " solditem.RetailPrice,"
                        + " solditem.Quantity,"
                        + " solditem.Total,"
                        + " solditem.DiscountForItem,"
                        + " solditem.Balance"
                        + " FROM `solditem` JOIN product ON solditem.Product_Id=product.Id"
                        + " WHERE solditem.Sale_Id=" + invoiceId;
            }
            ResultSet rs = DbConnect.getFromDB(query);
            while (rs.next()) {
                Vector v = new Vector();
                v.add(rs.getInt("Product_Id"));
                v.add(rs.getString("Description"));
                v.add(rs.getInt("BatchesOfProduct_Id"));
                v.add(rs.getDouble("RetailPrice"));
                v.add(rs.getInt("quantity"));
                v.add(rs.getDouble("Total"));
                v.add(rs.getDouble("DiscountForItem"));
                v.add(rs.getDouble("Balance"));
                tablemodel.addRow(v);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Return.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Return.class
                    .getName()).log(Level.SEVERE, null, ex);
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

        invoicePopup = new javax.swing.JPopupMenu();
        selectInvoicePopup = new com.xpos.gui.SelectInvoicePopup();
        returnType = new javax.swing.ButtonGroup();
        returnCondition = new javax.swing.ButtonGroup();
        jLabel13 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        returnSoldItemTable = new rojeru_san.complementos.RSTableMetro();
        returnSoldItemSearch = new app.bolivia.swing.JCTextField();
        jPanel3 = new javax.swing.JPanel();
        returnProductDescription = new javax.swing.JLabel();
        returnSoldQuantity = new javax.swing.JLabel();
        returnProductRetailPrice = new javax.swing.JLabel();
        returnQuantity = new app.bolivia.swing.JCTextField();
        returnAddToTable = new javax.swing.JButton();
        returnProductTotalDiscount = new javax.swing.JLabel();
        returnProductId = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        returnTypeRefund = new javax.swing.JRadioButton();
        returnTypeReplacement = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        returnProductCondGood = new javax.swing.JRadioButton();
        returnProductCondDamage = new javax.swing.JRadioButton();
        returnProductRefundAmount = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        returnComplete = new javax.swing.JButton();
        returnDeleteItem = new javax.swing.JButton();
        returnCancel = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        totalRefundAmountLabel = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        returnTable = new rojeru_san.complementos.RSTableMetro();
        saleInvoiceLabel = new javax.swing.JLabel();
        selectInvoice = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        returnInvoiceId = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        returnInvoiceTotal = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        returnInvoiceDate = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        returnInvoiceItDis = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        returnInvoiceTime = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        returnInvoiceTotDis = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        returnInvoiceCusId = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        returnInvoiceFinalTotal = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        returnInvoiceCusName = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        returnInvoiceCusPay = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setBackground(new java.awt.Color(26, 140, 255));
        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("RETURN");
        jLabel13.setOpaque(true);

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Sold Items", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(26, 140, 255))); // NOI18N

        returnSoldItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Description", "Batch ID", "Retail Price", "Quantity", "Sum", "Discount", "Total"
            }
        ));
        returnSoldItemTable.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        returnSoldItemTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnSoldItemTable.setRowHeight(25);
        returnSoldItemTable.setRowMargin(0);
        returnSoldItemTable.setSelectionBackground(new java.awt.Color(0, 60, 128));
        returnSoldItemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                returnSoldItemTableMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(returnSoldItemTable);

        returnSoldItemSearch.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Search", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        returnSoldItemSearch.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnSoldItemSearch.setPhColor(new java.awt.Color(0, 51, 255));
        returnSoldItemSearch.setPlaceholder("Search by Description");
        returnSoldItemSearch.setPreferredSize(new java.awt.Dimension(200, 30));
        returnSoldItemSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                returnSoldItemSearchKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane9)
            .addComponent(returnSoldItemSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addComponent(returnSoldItemSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        returnProductDescription.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnProductDescription.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Description", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        returnSoldQuantity.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnSoldQuantity.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Sold Quantity", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        returnProductRetailPrice.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnProductRetailPrice.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Retail Price", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        returnQuantity.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Quantity", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N
        returnQuantity.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnQuantity.setPhColor(new java.awt.Color(0, 51, 255));
        returnQuantity.setPlaceholder("Enter Quantity");
        returnQuantity.setPreferredSize(new java.awt.Dimension(200, 30));
        returnQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                returnQuantityKeyReleased(evt);
            }
        });

        returnAddToTable.setBackground(new java.awt.Color(0, 60, 128));
        returnAddToTable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnAddToTable.setForeground(new java.awt.Color(255, 255, 255));
        returnAddToTable.setText("ADD TO TABLE");
        returnAddToTable.setBorder(null);
        returnAddToTable.setFocusPainted(false);
        returnAddToTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnAddToTableActionPerformed(evt);
            }
        });

        returnProductTotalDiscount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnProductTotalDiscount.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Total Discount for one Item", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        returnProductId.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnProductId.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Product ID", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Return Type", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        returnTypeRefund.setBackground(new java.awt.Color(255, 255, 255));
        returnType.add(returnTypeRefund);
        returnTypeRefund.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        returnTypeRefund.setText("Cash Refund");
        returnTypeRefund.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                returnTypeRefundMouseClicked(evt);
            }
        });

        returnTypeReplacement.setBackground(new java.awt.Color(255, 255, 255));
        returnType.add(returnTypeReplacement);
        returnTypeReplacement.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        returnTypeReplacement.setSelected(true);
        returnTypeReplacement.setText("Replacement");
        returnTypeReplacement.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                returnTypeReplacementMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(returnTypeRefund)
                    .addComponent(returnTypeReplacement))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(returnTypeRefund)
                .addGap(18, 18, 18)
                .addComponent(returnTypeReplacement)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Product Condtion", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        returnProductCondGood.setBackground(new java.awt.Color(255, 255, 255));
        returnCondition.add(returnProductCondGood);
        returnProductCondGood.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        returnProductCondGood.setText("Good");
        returnProductCondGood.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                returnProductCondGoodMouseClicked(evt);
            }
        });

        returnProductCondDamage.setBackground(new java.awt.Color(255, 255, 255));
        returnCondition.add(returnProductCondDamage);
        returnProductCondDamage.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        returnProductCondDamage.setSelected(true);
        returnProductCondDamage.setText("Damage");
        returnProductCondDamage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                returnProductCondDamageMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(returnProductCondGood)
                    .addComponent(returnProductCondDamage))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(returnProductCondGood)
                .addGap(18, 18, 18)
                .addComponent(returnProductCondDamage)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        returnProductRefundAmount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnProductRefundAmount.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Total Refund Amount", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(returnProductId, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addComponent(returnProductDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(returnSoldQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addComponent(returnProductRetailPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(returnQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addComponent(returnProductTotalDiscount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(returnProductRefundAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(returnAddToTable, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(returnAddToTable, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(returnProductTotalDiscount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(returnSoldQuantity, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(returnProductId, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE))
                            .addComponent(returnProductRefundAmount, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(returnProductRetailPrice, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(returnQuantity, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(returnProductDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Return Actions", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(26, 140, 255))); // NOI18N

        returnComplete.setBackground(new java.awt.Color(0, 60, 128));
        returnComplete.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        returnComplete.setForeground(new java.awt.Color(255, 255, 255));
        returnComplete.setText("COMPLETE RETURN");
        returnComplete.setBorder(null);
        returnComplete.setFocusPainted(false);
        returnComplete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnCompleteActionPerformed(evt);
            }
        });

        returnDeleteItem.setBackground(new java.awt.Color(0, 60, 128));
        returnDeleteItem.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        returnDeleteItem.setForeground(new java.awt.Color(255, 255, 255));
        returnDeleteItem.setText("DELETE ITEM");
        returnDeleteItem.setBorder(null);
        returnDeleteItem.setFocusPainted(false);
        returnDeleteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnDeleteItemActionPerformed(evt);
            }
        });

        returnCancel.setBackground(new java.awt.Color(0, 60, 128));
        returnCancel.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        returnCancel.setForeground(new java.awt.Color(255, 255, 255));
        returnCancel.setText("CANCEL RETURN");
        returnCancel.setBorder(null);
        returnCancel.setFocusPainted(false);
        returnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnCancelActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        jLabel29.setText("Total Refund:");

        totalRefundAmountLabel.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        totalRefundAmountLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalRefundAmountLabel.setText("0.0");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(returnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(returnComplete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(returnDeleteItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalRefundAmountLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(totalRefundAmountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(returnDeleteItem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(returnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(returnComplete, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Return Products", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(26, 140, 255))); // NOI18N

        returnTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Description", "Return Type", "Item Condition", "Quantity", "Refund Amount"
            }
        ));
        returnTable.setColorBackgoundHead(new java.awt.Color(26, 140, 255));
        returnTable.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnTable.setRowHeight(25);
        returnTable.setRowMargin(0);
        returnTable.setSelectionBackground(new java.awt.Color(0, 60, 128));
        returnTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                returnTableMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(returnTable);

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

        saleInvoiceLabel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Invoice ID", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 10), new java.awt.Color(26, 140, 255))); // NOI18N

        selectInvoice.setBackground(new java.awt.Color(0, 60, 128));
        selectInvoice.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        selectInvoice.setForeground(new java.awt.Color(255, 255, 255));
        selectInvoice.setText("SELECT INVOICE");
        selectInvoice.setBorder(null);
        selectInvoice.setFocusPainted(false);
        selectInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectInvoiceActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(26, 140, 255)), "Invoice Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11), new java.awt.Color(26, 140, 255))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Invoice ID : ");

        returnInvoiceId.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnInvoiceId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        returnInvoiceId.setText("id");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Total : ");

        returnInvoiceTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnInvoiceTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        returnInvoiceTotal.setText("total");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Date : ");

        returnInvoiceDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnInvoiceDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        returnInvoiceDate.setText("date");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Discount for Items : ");

        returnInvoiceItDis.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnInvoiceItDis.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        returnInvoiceItDis.setText("itdis");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setText("Time : ");

        returnInvoiceTime.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnInvoiceTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        returnInvoiceTime.setText("time");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel6.setText("Discount for Total : ");

        returnInvoiceTotDis.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnInvoiceTotDis.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        returnInvoiceTotDis.setText("totdis");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Customer ID : ");

        returnInvoiceCusId.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnInvoiceCusId.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        returnInvoiceCusId.setText("cusid");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("Grand Total : ");

        returnInvoiceFinalTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnInvoiceFinalTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        returnInvoiceFinalTotal.setText("grTot");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("Customer Name : ");

        returnInvoiceCusName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnInvoiceCusName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        returnInvoiceCusName.setText("customer");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setText("Payment : ");

        returnInvoiceCusPay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        returnInvoiceCusPay.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        returnInvoiceCusPay.setText("pay");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(returnInvoiceTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(returnInvoiceId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(82, 82, 82)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(returnInvoiceDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(returnInvoiceItDis, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(82, 82, 82)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(returnInvoiceTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(returnInvoiceTotDis, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(82, 82, 82)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(12, 12, 12)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(returnInvoiceFinalTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(returnInvoiceCusId, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(82, 82, 82)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(returnInvoiceCusPay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(returnInvoiceCusName, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(returnInvoiceCusName))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(returnInvoiceCusPay)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(returnInvoiceTime)
                            .addComponent(jLabel7)
                            .addComponent(returnInvoiceCusId))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(returnInvoiceTotDis)
                            .addComponent(jLabel8)
                            .addComponent(returnInvoiceFinalTotal)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(returnInvoiceId)
                            .addComponent(jLabel3)
                            .addComponent(returnInvoiceDate))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(returnInvoiceTotal)
                            .addComponent(jLabel4)
                            .addComponent(returnInvoiceItDis))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(saleInvoiceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(selectInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(saleInvoiceLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                    .addComponent(selectInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void returnSoldItemTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_returnSoldItemTableMouseClicked
        productId = Integer.parseInt(returnSoldItemTable.getValueAt(returnSoldItemTable.getSelectedRow(), 0).toString());
        productName = returnSoldItemTable.getValueAt(returnSoldItemTable.getSelectedRow(), 1).toString();
        productBatchId = Integer.parseInt(returnSoldItemTable.getValueAt(returnSoldItemTable.getSelectedRow(), 2).toString());
        productRetailPrice = Double.parseDouble(returnSoldItemTable.getValueAt(returnSoldItemTable.getSelectedRow(), 3).toString());
        ProductQuantity = Integer.parseInt(returnSoldItemTable.getValueAt(returnSoldItemTable.getSelectedRow(), 4).toString());
        productRetailTotal = Double.parseDouble(returnSoldItemTable.getValueAt(returnSoldItemTable.getSelectedRow(), 5).toString());
        productDiscount = Double.parseDouble(returnSoldItemTable.getValueAt(returnSoldItemTable.getSelectedRow(), 6).toString());
        productFinalTotal = Double.parseDouble(returnSoldItemTable.getValueAt(returnSoldItemTable.getSelectedRow(), 7).toString());

        productDiscountForItem = productDiscount / ProductQuantity;
        productTotalDiscountForItem = productDiscountForItem + ((productRetailPrice - productDiscountForItem) * totalDisPercent / 100);

        returnProductId.setText(Integer.toString(productId));
        returnProductDescription.setText(productName);
        returnSoldQuantity.setText(Integer.toString(ProductQuantity));
        returnProductRetailPrice.setText(Double.toString(productRetailPrice));
        returnProductTotalDiscount.setText(Double.toString(productTotalDiscountForItem));
    }//GEN-LAST:event_returnSoldItemTableMouseClicked

    private void returnSoldItemSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_returnSoldItemSearchKeyReleased
        String query = "SELECT solditem.Product_Id,"
                + " product.Description,"
                + " solditem.BatchesOfProduct_Id,"
                + " solditem.RetailPrice,"
                + " solditem.Quantity,"
                + " solditem.Total,"
                + " solditem.DiscountForItem,"
                + " solditem.Balance"
                + " FROM `solditem` JOIN product ON solditem.Product_Id=product.Id"
                + " WHERE solditem.Sale_Id=" + invoiceId + " and product.Description like '%" + returnSoldItemSearch.getText().toString() + "%'";
        fillSoldItemTable(query);
    }//GEN-LAST:event_returnSoldItemSearchKeyReleased

    private void returnQuantityKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_returnQuantityKeyReleased
        calRefundAmount();
    }//GEN-LAST:event_returnQuantityKeyReleased

    private void returnAddToTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnAddToTableActionPerformed
        calRefundAmount();
        Vector v = new Vector();
        v.add(Integer.toString(curReturnProductId));
        v.add(curReturnProductName);
        v.add(curReturnType);
        v.add(curReturnCondition);
        v.add(Integer.toString(curReturnQuantity));
        v.add(Double.toString(curReturnRefundAmount));

        tablemodel = (DefaultTableModel) returnTable.getModel();
        tablemodel.addRow(v);
        clearReturnItemPanel();
        calTotalRefundAmount();
    }//GEN-LAST:event_returnAddToTableActionPerformed

    private void returnCompleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnCompleteActionPerformed
        try {
            calTotalRefundAmount();
            int returnInvoice = 0;
            String query1 = "INSERT INTO `return`( `Sale_Id`, `Date`, `RefundAmount`) VALUES (?,?,?)";
            PreparedStatement sql = DbConnect.getDBConnection().prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            sql.setInt(1, invoiceId);
            sql.setString(2, LocalDate.now().format(defaultDateFormat));
            sql.setDouble(3, totalRefundAmount);
            sql.executeUpdate();
            ResultSet rs = sql.getGeneratedKeys();
            if (rs.next()) {
                returnInvoice = rs.getInt(1);
                for (int i = 0; i < returnTable.getRowCount(); i++) {
                    curReturnProductId = Integer.parseInt(returnTable.getValueAt(i, 0).toString());
                    curReturnProductName = returnTable.getValueAt(i, 1).toString();
                    curReturnType = returnTable.getValueAt(i, 2).toString().equals("Replacement") ? "R" : "C";
                    curReturnCondition = returnTable.getValueAt(i, 3).toString().equals("Damage") ? "D" : "G";
                    curReturnQuantity = Integer.parseInt(returnTable.getValueAt(i, 4).toString());
                    curReturnRefundAmount = Double.parseDouble(returnTable.getValueAt(i, 5).toString());

                    query1 = "INSERT INTO `returnitem`( `Return_Id`, `Product_Id`, `Type`, `Cond`, `Quantity`, `ReturnPrice`) VALUES (?,?,?,?,?,?)";
                    sql = DbConnect.getDBConnection().prepareStatement(query1);
                    sql.setInt(1, returnInvoice);
                    sql.setInt(2, curReturnProductId);
                    sql.setString(3, curReturnType);
                    sql.setString(4, curReturnCondition);
                    sql.setInt(5, curReturnQuantity);
                    sql.setDouble(6, curReturnRefundAmount);
                    sql.executeUpdate();

                    if (curReturnType.equals("R")) {
                        query1 = "UPDATE `product` SET `TotalQuantity`=`TotalQuantity`-? WHERE `Id`=?";
                        sql = DbConnect.getDBConnection().prepareStatement(query1);
                        sql.setInt(1, curReturnQuantity);
                        sql.setInt(2, curReturnProductId);
                        sql.executeUpdate();
                    }

                    if (curReturnCondition.equals("G")) {
                        query1 = "UPDATE `product` SET `TotalQuantity`=`TotalQuantity`+? WHERE `Id`=?";
                        sql = DbConnect.getDBConnection().prepareStatement(query1);
                        sql.setInt(1, curReturnQuantity);
                        sql.setInt(2, curReturnProductId);
                        sql.executeUpdate();
                    }
                    if (curReturnCondition.equals("D")) {
                        query1 = "select * from removedProduct where Product_id=?";
                        sql = DbConnect.getDBConnection().prepareStatement(query1);
                        sql.setInt(1, curReturnProductId);
                        ResultSet rs1 = sql.executeQuery();
                        if (rs1.next()) {
                            query1 = "UPDATE `removedproduct` SET `Quantity`=`Quantity`+? WHERE `Product_Id`=?";
                            sql = DbConnect.getDBConnection().prepareStatement(query1);
                            sql.setInt(1, curReturnQuantity);
                            sql.setInt(2, curReturnProductId);
                            sql.executeUpdate();
                        }else{
                            query1 = "INSERT INTO `removedproduct`(`Product_Id`, `Quantity`) VALUES (?,?)";
                            sql = DbConnect.getDBConnection().prepareStatement(query1);
                            sql.setInt(1, curReturnProductId);
                            sql.setInt(2, curReturnQuantity);
                            sql.executeUpdate();
                        }
                    }
                }
            }
            clearReturnPanel();
        } catch (Exception ex) {
            Logger.getLogger(Return.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_returnCompleteActionPerformed

    private void returnDeleteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnDeleteItemActionPerformed
        try {
            tablemodel = (DefaultTableModel) returnTable.getModel();
            tablemodel.removeRow(returnTable.getSelectedRow());
            calTotalRefundAmount();
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }//GEN-LAST:event_returnDeleteItemActionPerformed

    private void returnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnCancelActionPerformed
        clearReturnPanel();
    }//GEN-LAST:event_returnCancelActionPerformed

    private void returnTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_returnTableMouseClicked

    }//GEN-LAST:event_returnTableMouseClicked

    private void selectInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectInvoiceActionPerformed
        if (!invoiceSelectPanel) {
            selectInvoice.setText("CONFIRM INVOICE");
            invoicePopup.add(selectInvoicePopup);
            invoicePopup.show(selectInvoice, selectInvoice.getWidth() - 506, selectInvoice.getHeight());
            invoiceSelectPanel = true;
        } else {
            invoiceId = Integer.parseInt(selectInvoicePopup.getInvoiceId().toString());
            saleInvoiceLabel.setText(Integer.toString(invoiceId));
            invoiceSelectPanel = false;
            selectInvoice.setText("SELECT INVOICE");
            getInvoiceDetails();
        }
    }//GEN-LAST:event_selectInvoiceActionPerformed

    private void returnTypeRefundMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_returnTypeRefundMouseClicked
        calRefundAmount();
    }//GEN-LAST:event_returnTypeRefundMouseClicked

    private void returnTypeReplacementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_returnTypeReplacementMouseClicked
        calRefundAmount();
    }//GEN-LAST:event_returnTypeReplacementMouseClicked

    private void returnProductCondGoodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_returnProductCondGoodMouseClicked
        calRefundAmount();
    }//GEN-LAST:event_returnProductCondGoodMouseClicked

    private void returnProductCondDamageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_returnProductCondDamageMouseClicked
        calRefundAmount();
    }//GEN-LAST:event_returnProductCondDamageMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu invoicePopup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JButton returnAddToTable;
    private javax.swing.JButton returnCancel;
    private javax.swing.JButton returnComplete;
    private javax.swing.ButtonGroup returnCondition;
    private javax.swing.JButton returnDeleteItem;
    private javax.swing.JLabel returnInvoiceCusId;
    private javax.swing.JLabel returnInvoiceCusName;
    private javax.swing.JLabel returnInvoiceCusPay;
    private javax.swing.JLabel returnInvoiceDate;
    private javax.swing.JLabel returnInvoiceFinalTotal;
    private javax.swing.JLabel returnInvoiceId;
    private javax.swing.JLabel returnInvoiceItDis;
    private javax.swing.JLabel returnInvoiceTime;
    private javax.swing.JLabel returnInvoiceTotDis;
    private javax.swing.JLabel returnInvoiceTotal;
    private javax.swing.JRadioButton returnProductCondDamage;
    private javax.swing.JRadioButton returnProductCondGood;
    private javax.swing.JLabel returnProductDescription;
    private javax.swing.JLabel returnProductId;
    private javax.swing.JLabel returnProductRefundAmount;
    private javax.swing.JLabel returnProductRetailPrice;
    private javax.swing.JLabel returnProductTotalDiscount;
    private app.bolivia.swing.JCTextField returnQuantity;
    private app.bolivia.swing.JCTextField returnSoldItemSearch;
    private rojeru_san.complementos.RSTableMetro returnSoldItemTable;
    private javax.swing.JLabel returnSoldQuantity;
    private rojeru_san.complementos.RSTableMetro returnTable;
    private javax.swing.ButtonGroup returnType;
    private javax.swing.JRadioButton returnTypeRefund;
    private javax.swing.JRadioButton returnTypeReplacement;
    private javax.swing.JLabel saleInvoiceLabel;
    private javax.swing.JButton selectInvoice;
    private com.xpos.gui.SelectInvoicePopup selectInvoicePopup;
    private javax.swing.JLabel totalRefundAmountLabel;
    // End of variables declaration//GEN-END:variables
}
