/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpos.commons;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Jamith
 */
public class SystemLogger {

    /**
     * @param args the command line arguments
     */
    
     public static void initLogger(){
        PropertyConfigurator.configure(new SystemLogger().getClass().getResource("log4j.properties"));
    }
}
