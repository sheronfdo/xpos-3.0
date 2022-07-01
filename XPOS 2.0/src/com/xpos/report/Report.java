/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpos.report;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author Jamith
 */
public class Report {

    public static JasperDesign design;
    public static JasperReport saleReport;
    public static JasperReport returnReport;
    public static JasperReport RFQReport;
    public static JasperReport POReport;
    public static JasperReport purchaseReport;

    public static void compile() {
        try {
            design = JRXmlLoader.load("src\\com\\xpos\\report\\saleInvoice.jrxml");
            saleReport = JasperCompileManager.compileReport(design);
            design = JRXmlLoader.load("src\\com\\xpos\\report\\returnInvoice.jrxml");
            returnReport = JasperCompileManager.compileReport(design);
            design = JRXmlLoader.load("src\\com\\xpos\\report\\reqForQuate.jrxml");
            RFQReport = JasperCompileManager.compileReport(design);
            design = JRXmlLoader.load("src\\com\\xpos\\report\\purchaseOrder.jrxml");
            POReport = JasperCompileManager.compileReport(design);
            design = JRXmlLoader.load("src\\com\\xpos\\report\\purchaseInvoice.jrxml");
            purchaseReport = JasperCompileManager.compileReport(design);
        } catch (JRException ex) {
            Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
