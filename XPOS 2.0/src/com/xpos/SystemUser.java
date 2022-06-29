/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpos;

import com.xpos.database.DbConnect;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jamit
 */
public class SystemUser {

    private static int userId = 1;
    private static String userName = "jamith";

    //privileges
    private static boolean sale;
    private static boolean purchase;
    private static boolean returns;
    private static boolean customer;
    private static boolean supplier;
    private static boolean employee;
    private static boolean userPosition;
    private static boolean userProfile;
    private static boolean products;
    private static boolean exIncome;
    private static boolean exCost;
    private static boolean invoice;
    private static boolean report;

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        SystemUser.userId = userId;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        SystemUser.userName = userName;
    }

    public static boolean isSale() {
        return sale;
    }

    public static void setSale(boolean sale) {
        SystemUser.sale = sale;
    }

    public static boolean isPurchase() {
        return purchase;
    }

    public static void setPurchase(boolean purchase) {
        SystemUser.purchase = purchase;
    }

    public static boolean isReturns() {
        return returns;
    }

    public static void setReturns(boolean returns) {
        SystemUser.returns = returns;
    }

    public static boolean isCustomer() {
        return customer;
    }

    public static void setCustomer(boolean customer) {
        SystemUser.customer = customer;
    }

    public static boolean isSupplier() {
        return supplier;
    }

    public static void setSupplier(boolean supplier) {
        SystemUser.supplier = supplier;
    }

    public static boolean isEmployee() {
        return employee;
    }

    public static void setEmployee(boolean employee) {
        SystemUser.employee = employee;
    }

    public static boolean isUserPosition() {
        return userPosition;
    }

    public static void setUserPosition(boolean userPosition) {
        SystemUser.userPosition = userPosition;
    }

    public static boolean isUserProfile() {
        return userProfile;
    }

    public static void setUserProfile(boolean userProfile) {
        SystemUser.userProfile = userProfile;
    }

    public static boolean isProducts() {
        return products;
    }

    public static void setProducts(boolean products) {
        SystemUser.products = products;
    }

    public static boolean isExIncome() {
        return exIncome;
    }

    public static void setExIncome(boolean exIncome) {
        SystemUser.exIncome = exIncome;
    }

    public static boolean isExCost() {
        return exCost;
    }

    public static void setExCost(boolean exCost) {
        SystemUser.exCost = exCost;
    }

    public static boolean isInvoice() {
        return invoice;
    }

    public static void setInvoice(boolean invoice) {
        SystemUser.invoice = invoice;
    }

    public static boolean isReport() {
        return report;
    }

    public static void setReport(boolean report) {
        SystemUser.report = report;
    }
        
    public static void user(int userId, String userName) {
        SystemUser.setUserId(userId);
        SystemUser.setUserName(userName);

        String query = "SELECT employeeposition.Sale,"
                + " employeeposition.Purchase,"
                + " employeeposition.Return,"
                + " employeeposition.Customer,"
                + " employeeposition.Supplier,"
                + " employeeposition.Employee,"
                + " employeeposition.UserPosition,"
                + " employeeposition.UserProfile,"
                + " employeeposition.Products,"
                + " employeeposition.ExIncome,"
                + " employeeposition.Invoice,"
                + " employeeposition.Report,"
                + " employeeposition.ExCost"
                + " FROM userprofile JOIN employee ON userprofile.Employee_Id=employee.Id"
                + " JOIN employeeposition ON employeeposition.Id=employee.EmployeePosition_id"
                + " WHERE userprofile.Id=" + SystemUser.userId;

        try {
            ResultSet rs = DbConnect.getFromDB(query);
            if (rs.next()) {
                SystemUser.setSale(rs.getBoolean("sale"));
                SystemUser.setPurchase(rs.getBoolean("purchase"));
                SystemUser.setReturns(rs.getBoolean("return"));
                SystemUser.setCustomer(rs.getBoolean("customer"));
                SystemUser.setSupplier(rs.getBoolean("supplier"));
                SystemUser.setEmployee(rs.getBoolean("employee"));
                SystemUser.setUserPosition(rs.getBoolean("userposition"));
                SystemUser.setUserProfile(rs.getBoolean("userprofile"));
                SystemUser.setProducts(rs.getBoolean("products"));
                SystemUser.setExIncome(rs.getBoolean("exincome"));
                SystemUser.setExCost(rs.getBoolean("excost"));
                SystemUser.setInvoice(rs.getBoolean("invoice"));
                SystemUser.setReport(rs.getBoolean("report"));

            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SystemUser.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SystemUser.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
}
