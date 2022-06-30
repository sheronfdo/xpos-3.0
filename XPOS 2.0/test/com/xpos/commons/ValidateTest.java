/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpos.commons;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Jamith
 */
public class ValidateTest {
    
    /**
     * Test of isName method, of class Validate.
     */
    @Test
    public void testIsName() {
        System.out.println("isName");
        String in = "";
        boolean expResult = false;
        boolean result = Validate.isName(in);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isNumber method, of class Validate.
     */
    @Test
    public void testIsNumber() {
        System.out.println("isNumber");
        String in = "";
        boolean expResult = false;
        boolean result = Validate.isNumber(in);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isDoubleNumber method, of class Validate.
     */
    @Test
    public void testIsDoubleNumber() {
        System.out.println("isDoubleNumber");
        String in = "";
        boolean expResult = false;
        boolean result = Validate.isDoubleNumber(in);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isNIC method, of class Validate.
     */
    @Test
    public void testIsNIC() {
        System.out.println("isNIC");
        String in = "";
        boolean expResult = false;
        boolean result = Validate.isNIC(in);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isText method, of class Validate.
     */
    @Test
    public void testIsText() {
        System.out.println("isText");
        String in = "";
        boolean expResult = false;
        boolean result = Validate.isText(in);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isEmail method, of class Validate.
     */
    @Test
    public void testIsEmail() {
        System.out.println("isEmail");
        String email = "";
        boolean expResult = false;
        boolean result = Validate.isEmail(email);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isTelephone method, of class Validate.
     */
    @Test
    public void testIsTelephone() {
        System.out.println("isTelephone");
        String in = "";
        boolean expResult = false;
        boolean result = Validate.isTelephone(in);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isDate method, of class Validate.
     */
    @Test
    public void testIsDate() {
        System.out.println("isDate");
        String date = "";
        boolean expResult = false;
        boolean result = Validate.isDate(date);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
