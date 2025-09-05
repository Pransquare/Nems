package com.pransquare.nems.utils;

public class ClientUtil {
	
	public static String getClient(String htmlContent) {
        // Implementation of getting response from the URL using HttpClient or similar libraries
        // Return the response as a String
		String response="NETAPP";
      if(htmlContent.contains("CISCO US LEGAL ENTITY")) {
    	  response= "CISCO";
      }
      else if(htmlContent.contains("Western Digital Technologies, Inc.")) {
    	  response= "Western Digital";
      }
      else if(htmlContent.contains("Cetera Financial")) {
    	  response= "Cetera";
      }
      return response;
    }	
	public static  String getPayee(String htmlContent) {
        // Implementation of getting response from the URL using HttpClient or similar libraries
        // Return the response as a String
		String response="Pransquare";
      if(htmlContent.contains("SSIT")) {
    	  response= "Pransquare";
      }
      else if(htmlContent.contains("SSIT Inc")) {
    	  response= "Pransquare";
      }
     
      return response;
    }	
}
