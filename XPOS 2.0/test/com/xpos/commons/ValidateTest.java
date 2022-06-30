/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpos.commons;

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
public class ValidateTest {

    @Test
    public void testIsName() {
        assertEquals(true, Validate.isName("Jamith Sheron"));
    }

    @Test
    public void testIsNumber() {
        assertEquals(true, Validate.isNumber("321"));
    }

    @Test
    public void testIsDoubleNumber() {
        String in = "0.00";
        boolean expResult = true;
        boolean result = Validate.isDoubleNumber(in);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsNIC() {
        String in = "200022502886";
        boolean expResult = true;
        boolean result = Validate.isNIC(in);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsText() {
        String in = "werh tbdrhbterty7rbty rt7 8r7t";
        boolean expResult = true;
        boolean result = Validate.isText(in);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsEmail() {
        String email = "jamith@mail.com";
        boolean expResult = true;
        boolean result = Validate.isEmail(email);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsTelephone() {
        String in = "0770470323";
        boolean expResult = true;
        boolean result = Validate.isTelephone(in);
        assertEquals(expResult, result);
    }

    @Test
    public void testIsDate() {
        String date = "2022-04-15";
        boolean expResult = true;
        boolean result = Validate.isDate(date);
        assertEquals(expResult, result);
    }

}
