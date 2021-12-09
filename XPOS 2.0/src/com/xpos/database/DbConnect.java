/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpos.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Jamith
 */
public class DbConnect {
    static Connection con;
    
    private static String dbHost = "localhost";
    private static String port = "3306";
    private static String dbName = "xpos_db_2";
    private static String userName = "root";
    private static String password = "Jamith@5000";
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        connect();
    }
    
    public static void connect() throws ClassNotFoundException,SQLException{
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://"+dbHost+":"+port+"/"+dbName,userName,password); // database name = xpos_db_2
    }
    
    public static void pushToDB(String sql)throws ClassNotFoundException,SQLException{
        if(con == null){
            connect();
        }
        con.createStatement().executeUpdate(sql);
    }
    
    public static ResultSet getFromDB(String sql)throws ClassNotFoundException,SQLException{
        if(con == null){
            connect();
        }
        return con.createStatement().executeQuery(sql);
    }
    
    public static Connection getDBConnection()throws Exception{
        if (con == null) {
            connect();
        }
        return con;
    }
}
