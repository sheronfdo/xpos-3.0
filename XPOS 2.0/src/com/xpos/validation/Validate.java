/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xpos.validation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Jamit
 */
public class Validate {
    public static boolean isName(String in){
        return Pattern.matches("[a-zA-Z]+", in);
    }
    
    public static boolean  isEmail(String email){
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(email);
        return matcher.find();
    }
    
    public static boolean isTelephone(String in){
        return in.length()==10 && in.matches("[0-9]+") && in.charAt(0)=='0';
    }
    
    public static boolean isDate(String date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = null;
        df.setLenient(false);
        try{
            date2 = df.parse(date);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
