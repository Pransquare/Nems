package com.pransquare.nems.utils;

public class MonthUtil {
	
	// Private constructor to prevent instantiation
    private MonthUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

	public static String getMonth(String month) {
	String resMonth="";
	   switch (month) {
       case "0":
    	   resMonth= "January";
           break;
       case "1":
           resMonth= "February";
            break;
       case "2":
           resMonth= "March";
            break;
       case "3":
           resMonth= "April";
            break;
       case "4":
           resMonth= "May";
            break;
       case "5":
           resMonth= "June";
            break;
        case "6":
            resMonth= "July";
             break;
        case "7":
            resMonth= "August";
             break;
        case "8":
            resMonth= "September";
             break;
        case "9":
            resMonth= "October";
             break;
        case "10":
            resMonth= "November";
             break;
        case "11":
            resMonth= "Decemebr";
             break;
        default:
        	  
            break;
	   }
 
	return resMonth;

}
	}
