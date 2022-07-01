/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpos;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jamith
 */
public class SystemUserTest {
    
    public SystemUserTest() {
        SystemUser.user(2, "jamith");
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetUserId() {
        System.out.println("getUserId");
        int expResult = 2;
        int result = SystemUser.getUserId();
        assertEquals(expResult, result);
    }

    

    @Test
    public void testGetUserName() {
        System.out.println("getUserName");
        String expResult = "jamith";
        String result = SystemUser.getUserName();
        assertEquals(expResult, result);
    }

    

    @Test
    public void testIsSale() {
        System.out.println("isSale");
        boolean expResult = true;
        boolean result = SystemUser.isSale();
        assertEquals(expResult, result);
    }

    

    @Test
    public void testIsPurchase() {
        System.out.println("isPurchase");
        boolean expResult = true;
        boolean result = SystemUser.isPurchase();
        assertEquals(expResult, result);
    }

    

    @Test
    public void testIsReturns() {
        System.out.println("isReturns");
        boolean expResult = true;
        boolean result = SystemUser.isReturns();
        assertEquals(expResult, result);
    }

    

    @Test
    public void testIsCustomer() {
        System.out.println("isCustomer");
        boolean expResult = true;
        boolean result = SystemUser.isCustomer();
        assertEquals(expResult, result);
    }

    

    @Test
    public void testIsSupplier() {
        System.out.println("isSupplier");
        boolean expResult = true;
        boolean result = SystemUser.isSupplier();
        assertEquals(expResult, result);
    }

    

    @Test
    public void testIsEmployee() {
        System.out.println("isEmployee");
        boolean expResult = true;
        boolean result = SystemUser.isEmployee();
        assertEquals(expResult, result);
    }

    

    @Test
    public void testIsUserPosition() {
        System.out.println("isUserPosition");
        boolean expResult = true;
        boolean result = SystemUser.isUserPosition();
        assertEquals(expResult, result);
    }

    

    @Test
    public void testIsUserProfile() {
        System.out.println("isUserProfile");
        boolean expResult = true;
        boolean result = SystemUser.isUserProfile();
        assertEquals(expResult, result);
    }

    

    @Test
    public void testIsProducts() {
        System.out.println("isProducts");
        boolean expResult = true;
        boolean result = SystemUser.isProducts();
        assertEquals(expResult, result);
    }

    

    @Test
    public void testIsExIncome() {
        System.out.println("isExIncome");
        boolean expResult = true;
        boolean result = SystemUser.isExIncome();
        assertEquals(expResult, result);
    }

    

    @Test
    public void testIsExCost() {
        System.out.println("isExCost");
        boolean expResult = true;
        boolean result = SystemUser.isExCost();
        assertEquals(expResult, result);
    }

   

    @Test
    public void testIsInvoice() {
        System.out.println("isInvoice");
        boolean expResult = true;
        boolean result = SystemUser.isInvoice();
        assertEquals(expResult, result);
    }

    

    @Test
    public void testIsReport() {
        System.out.println("isReport");
        boolean expResult = true;
        boolean result = SystemUser.isReport();
        assertEquals(expResult, result);
    }

    

    @Test
    public void testUser() {
        System.out.println("user");
        int userId = 2;
        String userName = "jamith";
        SystemUser.user(userId, userName);
    }
    
}
