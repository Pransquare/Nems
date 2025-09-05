/*---------------------------------------------------------------------------------------------------
 * ---------------------------------------------------------------------------------------------------
 * --------------------------------------------------------------------------------------------------
 * Working solution for writing into Multiple sheets
 */
//package com.example.emailprocessing.service;
//
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class EmailTableToExcel {
//
//    public void renderTableToExcel(String htmlContent, String excelFilePath, String tableClassName) throws IOException {
//        // Step 1: Parse the HTML content
//        Document document = Jsoup.parse(htmlContent);
//
//        // Step 2: Find all tables with the specified class name
//        Elements tables = document.select("table." + tableClassName);
//        
//        if (tables.isEmpty()) {
//            logger.info("No tables with class '" + tableClassName + "' found.");
//            return;
//        }
//
//        // Step 3: Create a new Excel workbook
//        try (Workbook workbook = new XSSFWorkbook()) {
//
//            // Step 4: Iterate over each table with the specified class
//            int tableIndex = 1;
//            Sheet sheet = workbook.createSheet("Table " + tableIndex++);
//            for (Element table : tables) {
//                // Create a new sheet for each table
//               
//
//                // Extract rows and cells from the HTML table
//                Elements rows = table.select("tr");
//                int rowIndex = 0;
//
//                for (Element row : rows) {
//                    Row excelRow = sheet.createRow(rowIndex++);
//                    Elements cells = row.select("td, th");
//                    int cellIndex = 0;
//
//                    for (Element cell : cells) {
//                        Cell excelCell = excelRow.createCell(cellIndex++);
//                        excelCell.setCellValue(cell.text());
//                    }
//                }
//            }
//
//
//
//            // Step 5: Write the workbook to a file
//            try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
//                workbook.write(fileOut);
//                logger.info("All table data has been written to Excel file: " + excelFilePath);
//            }
//        }
//    }
//}
/*---------------------------------------------------------------------------------------------------
 * ---------------------------------------------------------------------------------------------------
 * --------------------------------------------------------------------------------------------------
 * Working solution for writing into single sheet
 */
//package com.example.emailprocessing.service;
//
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class EmailTableToExcel {
//
//    public void renderTableToExcel(String htmlContent, String excelFilePath, String tableClassName) throws IOException {
//        // Step 1: Parse the HTML content
//        Document document = Jsoup.parse(htmlContent);
//
//        // Step 2: Find all tables with the specified class name
//        Elements tables = document.select("table." + tableClassName);
//
//        if (tables.isEmpty()) {
//            logger.info("No tables with class '" + tableClassName + "' found.");
//            return;
//        }
//
//        // Step 3: Create a new Excel workbook
//        try (Workbook workbook = new XSSFWorkbook()) {
//            // Create a single sheet for all tables
//            Sheet sheet = workbook.createSheet("Merged Table Data");
//
//            // Initialize row index for Excel sheet
//            int rowIndex = 0;
//
//            // Step 4: Iterate over each table with the specified class
//            for (Element table : tables) {
//                Elements rows = table.select("tr");
//
//                // Extract rows and cells from the HTML table
//                for (Element row : rows) {
//                    // Check if the row index exceeds Excel's limit (65,536 for older versions, 1,048,576 for newer versions)
//                    if (rowIndex > 1_048_575) {
//                        logger.info("Reached maximum Excel row limit. Stopping data write.");
//                        return;
//                    }
//
//                    Row excelRow = sheet.createRow(rowIndex++);
//                    Elements cells = row.select("td, th");
//                    int cellIndex = 0;
//
//                    // Populate Excel row with HTML table cell data
//                    for (Element cell : cells) {
//                        Cell excelCell = excelRow.createCell(cellIndex++);
//                        excelCell.setCellValue(cell.text());
//                    }
//                }
//
//                // Add an empty row between tables for better separation
//                rowIndex++;
//            }
//
//            // Step 5: Auto-size columns for better readability
//            int columnCount = sheet.getRow(0) != null ? sheet.getRow(0).getLastCellNum() : 0;
//            for (int i = 0; i < columnCount; i++) {
//                sheet.autoSizeColumn(i);
//            }
//
//            // Step 6: Write the workbook to a file
//            try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
//                workbook.write(fileOut);
//                logger.info("All table data has been written to a single Excel sheet: " + excelFilePath);
//            }
//        }
//    }
//}
/*----------------------------------------------------------------
 * -------------------------
 * Working solution for writing into excel only tables having matching keywords
 */
//package com.example.emailprocessing.service;
//
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class EmailTableToExcel {
//
//    public void renderTableToExcel(String htmlContent, String excelFilePath, String tableClassName) throws IOException {
//        // Step 1: Parse the HTML content
//        Document document = Jsoup.parse(htmlContent);
//
//        // Step 2: Find all tables with the specified class name
//        Elements tables = document.select("table." + tableClassName);
//
//        if (tables.isEmpty()) {
//            logger.info("No tables with class '" + tableClassName + "' found.");
//            return;
//        }
//
//        // Step 3: Create a new Excel workbook
//        try (Workbook workbook = new XSSFWorkbook()) {
//            // Create a single sheet for all tables
//            Sheet sheet = workbook.createSheet("Filtered Remittance Data");
//
//            // Initialize row index for Excel sheet
//            int rowIndex = 0;
//
//            // Step 4: Iterate over each table with the specified class
//            for (Element table : tables) {
//                // Check if the table or any cell contains "Remittance Detail"
//                if( (!table.text().contains("Remittance Detail")) &&
//                	 (!table.text().contains("Payment Date")) &&
//                		(!table.text().contains("Payment Date")) &&
//                			(!table.text().contains("Payment Amount"))) {
//                		
//                	
//                
//
//
//
//
//                    continue; // Skip this table if it doesn't contain "Remittance Detail"
//                }
//
//                Elements rows = table.select("tr");
//
//                // Extract rows and cells from the HTML table
//                for (Element row : rows) {
//                    // Check if the row index exceeds Excel's limit (1,048,576 for newer versions)
//                    if (rowIndex > 1_048_575) {
//                        logger.info("Reached maximum Excel row limit. Stopping data write.");
//                        return;
//                    }
//
//                    Row excelRow = sheet.createRow(rowIndex++);
//                    Elements cells = row.select("td, th");
//                    int cellIndex = 0;
//
//                    // Populate Excel row with HTML table cell data
//                    for (Element cell : cells) {
//                        Cell excelCell = excelRow.createCell(cellIndex++);
//                        excelCell.setCellValue(cell.text());
//                    }
//                }
//
//                // Add an empty row between tables for better separation
//                rowIndex++;
//            }
//
//            // Step 5: Auto-size columns for better readability
//            int columnCount = sheet.getRow(0) != null ? sheet.getRow(0).getLastCellNum() : 0;
//            for (int i = 0; i < columnCount; i++) {
//                sheet.autoSizeColumn(i);
//            }
//
//            // Step 6: Write the workbook to a file
//            try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
//                workbook.write(fileOut);
//                logger.info("Filtered table data with 'Remittance Detail' has been written to the Excel sheet: " + excelFilePath);
//            }
//        }
//    }
//}
//package com.example.emailprocessing.service;
//
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class EmailTableToExcel {
//
//    public void renderTableToExcel(String htmlContent, String excelFilePath, String tableClassName) throws IOException {
//        // Step 1: Parse the HTML content
//        Document document = Jsoup.parse(htmlContent);
//
//        // Step 2: Find all tables with the specified class name
//        Elements tables = document.select("table." + tableClassName);
//
//        if (tables.isEmpty()) {
//            logger.info("No tables with class '" + tableClassName + "' found.");
//            return;
//        }
//
//        // Step 3: Create a new Excel workbook
//        try (Workbook workbook = new XSSFWorkbook()) {
//            // Create a single sheet for all tables
//            Sheet sheet = workbook.createSheet("Filtered Remittance Data");
//
//            // Initialize row index for Excel sheet
//            int rowIndex = 0;
//
//            // Step 4: Iterate over each table with the specified class
//            for (Element table : tables) {
//                // Check if the table or any cell contains "Remittance Detail"
//                if( (!table.text().contains("Remittance Detail")) 
////                		&&(!table.text().contains("Payment Date")) &&
////                		(!table.text().contains("Payment Date")) &&
////                			(!table.text().contains("Payment Amount"))
//                			) {
//                		
//                	
//                
//
//
//
//
//                    continue; // Skip this table if it doesn't contain "Remittance Detail"
//                }
//
//                Elements rows = table.select("tr");
//
//                // Extract rows and cells from the HTML table
//                for (Element row : rows) {
//                    // Check if the row index exceeds Excel's limit (1,048,576 for newer versions)
//                    if (rowIndex > 1_048_575) {
//                        logger.info("Reached maximum Excel row limit. Stopping data write.");
//                        return;
//                    }
//
//                    Row excelRow = sheet.createRow(rowIndex++);
//                    Elements cells = row.select("td, th");
//                    int cellIndex = 0;
//
//                    // Populate Excel row with HTML table cell data
//                    for (Element cell : cells) {
//                        Cell excelCell = excelRow.createCell(cellIndex++);
//                        excelCell.setCellValue(cell.text());
//                    }
//                }
//
//                // Add an empty row between tables for better separation
//                rowIndex++;
//            }
//
//            // Step 5: Auto-size columns for better readability
//            int columnCount = sheet.getRow(0) != null ? sheet.getRow(0).getLastCellNum() : 0;
//            for (int i = 0; i < columnCount; i++) {
//                sheet.autoSizeColumn(i);
//            }
//
//            // Step 6: Write the workbook to a file
//            try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
//                workbook.write(fileOut);
//                logger.info("Filtered table data with 'Remittance Detail' has been written to the Excel sheet: " + excelFilePath);
//            }
//        }
//    }
//}
/*
 * Working solution for writing table data 
 */
//package com.example.emailprocessing.service;
//
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//public class EmailTableToExcel {
//
//    public void renderTableToExcel(String htmlContent, String excelFilePath, String tableClassName,String payer, String payee) throws IOException {
//        // Step 1: Parse the HTML content
//        Document document = Jsoup.parse(htmlContent);
//      
//        // Step 2: Find all tables with the specified class name
//        Elements tables = document.select("table." + tableClassName);
//
//        if (tables.isEmpty()) {
//            logger.info("No tables with class '" + tableClassName + "' found.");
//            return;
//        }
//
//        // Step 3: Define fixed headers
//        String[] fixedHeaders = {
//            "SL.NO", "From Payer/Client", "To PRANSQUARE Inc",
//            "PRANSQUARE Invoice Number", "Document Reference Number", "Document Date",
//            "Document Amount", "Document Currency", "Amount Withheld", "Discount Taken", "Amount Paid"
//        };
//
//        // Step 4: Create a new Excel workbook
//        try (Workbook workbook = new XSSFWorkbook()) {
//            // Create a single sheet for the data
//            Sheet sheet = workbook.createSheet("Remittance Details");
//
//            // Create the header row
//            Row headerRow = sheet.createRow(0);
//            for (int i = 0; i < fixedHeaders.length; i++) {
//                Cell cell = headerRow.createCell(i);
//                cell.setCellValue(fixedHeaders[i]);
//                // Optional: Make header bold
//                CellStyle style = workbook.createCellStyle();
//                Font font = workbook.createFont();
//                font.setBold(true);
//                style.setFont(font);
//                cell.setCellStyle(style);
//            }
//
//            // Initialize row index for Excel sheet (data rows start from 1)
//            int rowIndex = 1;
//            boolean dataFound = false; // Flag to check if any data rows are added
//            int slNo = 1;
//
//            // Step 5: Iterate over each table with the specified class
//            for (Element table : tables) {
//                // Check if the table contains "Remittance Detail"
//                if ((!table.text().contains("Remittance Detail"))) {
//                    continue; // Skip this table if it doesn't contain "Remittance Detail"
//                }
//
//                Elements rows = table.select("tr");
//
//                // Extract rows and cells from the HTML table
//                for (Element row : rows) {
//                    Elements cells = row.select("td");
//
//                    // Skip header row or irrelevant rows in HTML table
//                    if (cells.isEmpty() || cells.text().contains("Remittance Detail") || cells.text().contains("Document Reference Number")) {
//                        continue;
//                    }
//
//                    // Create a new row in Excel
//                    Row excelRow = sheet.createRow(rowIndex++);
//                    dataFound = true; // Set flag to true when a data row is added
//
//                    // Assign SL.NO
//                    excelRow.createCell(0).setCellValue(slNo++);
//                    excelRow.createCell(1).setCellValue(payer); // Leaving column 2 empty
//                    excelRow.createCell(2).setCellValue(payee); // Leaving column 3 empty
//                    excelRow.createCell(3).setCellValue(""); // Leaving column 4 empty
//
//                    // Loop through fixed headers, match, and populate values starting from column 5
//                    for (int i = 4; i < fixedHeaders.length; i++) {
//                        String cellValue = "";
//                        if (i - 4 < cells.size()) {
//                            cellValue = cells.get(i - 4).text();
//                        }
//
//                        // Skip specific headers as per the previous condition
//                        if (!cellValue.equals("Document Reference Number") &&
//                            !cellValue.equals("Document Date") &&
//                            !cellValue.equals("Document Amount") &&
//                            !cellValue.equals("Document Currency") &&
//                            !cellValue.equals("Amount Withheld") &&
//                            !cellValue.equals("Discount Taken") &&
//                            !cellValue.equals("Amount Paid") &&
//                            !cellValue.equals("Remittance Detail")) {
//                            excelRow.createCell(i).setCellValue(cellValue);
//                        }
//                    }
//                }
//
//                // Add an empty row between tables for better separation
//                rowIndex++;
//            }
//
//            // Step 6: Write to Excel file only if data is found
//            if (dataFound) {
//                // Auto-size columns for better readability
//                for (int i = 0; i < fixedHeaders.length; i++) {
//                    sheet.autoSizeColumn(i);
//                }
//
//                // Write the workbook to a file
//                try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
//                    workbook.write(fileOut);
//                    logger.info("Filtered table data with fixed headers written to Excel: " + excelFilePath);
//                }
//            } else {
//                logger.info("No relevant data found, Excel file not created.");
//            }
//        }
//    }
//}
//package com.example.emailprocessing.service;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellStyle;
//import org.apache.poi.ss.usermodel.Font;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//public class EmailTableToExcel {
//
//	public void renderTableToExcel(String htmlContent, String excelFilePath, String tableClassName, String payer,
//			String payee) throws IOException {
//		// Step 1: Parse the HTML content
//		Document document = Jsoup.parse(htmlContent);
//
//		// Step 2: Find all tables with the specified class name
//		Elements tables = document.select("table." + tableClassName);
//
//		if (tables.isEmpty()) {
//			logger.info("No tables with class '" + tableClassName + "' found.");
//			return;
//		}
//
//		// Step 3: Define fixed headers
//		String[] fixedHeaders = { "SL.NO", "From Payer/Client", "To PRANSQUARE Inc", "PRANSQUARE Invoice Number",
//				"Document Reference Number", "Document Date", "Document Amount", "Document Currency", "Amount Withheld",
//				"Discount Taken", "Amount Paid" };
//
//		// Step 4: Check if the Excel file already exists
//		File file = new File(excelFilePath);
//		Workbook workbook;
//		Sheet sheet;
//		int rowIndex;
//
//		if (file.exists()) {
//			// Open the existing Excel file
//			try (FileInputStream fis = new FileInputStream(file)) {
//				workbook = new XSSFWorkbook(fis);
//				sheet = workbook.getSheetAt(0);
//				rowIndex = sheet.getLastRowNum() + 1; // Start at the next empty row
//			}
//		} else {
//			// Create a new Excel workbook and sheet
//			workbook = new XSSFWorkbook();
//			sheet = workbook.createSheet("Remittance Details");
//
//			// Create the header row
//			Row headerRow = sheet.createRow(0);
//			for (int i = 0; i < fixedHeaders.length; i++) {
//				Cell cell = headerRow.createCell(i);
//				cell.setCellValue(fixedHeaders[i]);
//				// Optional: Make header bold
//				CellStyle style = workbook.createCellStyle();
//				Font font = workbook.createFont();
//				font.setBold(true);
//				style.setFont(font);
//				cell.setCellStyle(style);
//			}
//
//			// Initialize row index for Excel sheet (data rows start from 1)
//			rowIndex = 1;
//		}
//
//		boolean dataFound = false; // Flag to check if any data rows are added
//		int slNo = rowIndex; // Continue SL.NO from the last row number
//
//		// Step 5: Iterate over each table with the specified class
//		for (Element table : tables) {
//			// Check if the table contains "Remittance Detail"
//			if ((!table.text().contains("Remittance Detail"))) {
//				continue; // Skip this table if it doesn't contain "Remittance Detail"
//			}
//
//			Elements rows = table.select("tr");
//
//			// Extract rows and cells from the HTML table
//			for (Element row : rows) {
//				Elements cells = row.select("td");
//
//				// Skip header row or irrelevant rows in HTML table
//				if (cells.isEmpty() || cells.text().contains("Remittance Detail")
//						|| cells.text().contains("Document Reference Number") || cells.text().contains("Total")) {
//					continue;
//				}
//
//				// Create a new row in Excel
//				Row excelRow = sheet.createRow(rowIndex++);
//				dataFound = true; // Set flag to true when a data row is added
//
//				// Assign SL.NO
//				excelRow.createCell(0).setCellValue(slNo++);
//				excelRow.createCell(1).setCellValue(payer); // Fill with payer
//				excelRow.createCell(2).setCellValue(payee); // Fill with payee
//				excelRow.createCell(3).setCellValue(""); // Leaving column 4 empty
//
//				// Loop through fixed headers, match, and populate values starting from column 5
//				for (int i = 4; i < fixedHeaders.length; i++) {
//					String cellValue = "";
//					if (i - 4 < cells.size()) {
//						cellValue = cells.get(i - 4).text();
//					}
//
//					// Skip specific headers as per the previous condition
//					if (!cellValue.equals("Document Reference Number") && !cellValue.equals("Document Date")
//							&& !cellValue.equals("Document Amount") && !cellValue.equals("Document Currency")
//							&& !cellValue.equals("Amount Withheld") && !cellValue.equals("Discount Taken")
//							&& !cellValue.equals("Amount Paid") && !cellValue.equals("Remittance Detail")) {
//						excelRow.createCell(i).setCellValue(cellValue);
//					}
//				}
//			}
//
//			// Add an empty row between tables for better separation
//			rowIndex++;
//		}
//
//		// Step 6: Write to Excel file only if data is found
//		if (dataFound) {
//			// Auto-size columns for better readability
//			for (int i = 0; i < fixedHeaders.length; i++) {
//				sheet.autoSizeColumn(i);
//			}
//
//			// Write the workbook to a file (append mode)
//			try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
//				workbook.write(fileOut);
//				logger.info("Filtered table data appended to Excel: " + excelFilePath);
//			}
//		} else {
//			logger.info("No relevant data found, Excel file not modified.");
//		}
//
//		// Close the workbook
//		workbook.close();
//	}
//}boolean containsCisco = false;
//    	if(htmlContent.contains("CISCO")) {
//    	  containsCisco = true;
//    	}
package com.pransquare.nems.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.pransquare.nems.entities.PaymentRefReturnModel;

import net.minidev.json.parser.ParseException;



@Component
public class EmailTableToExcelNew {
	private static final Logger logger = LogManager.getLogger(EmailTableToExcelNew.class);

	public void renderTableToExcel(String htmlContent, String excelFilePath, String tableClassName, String payer,
			String payee) throws IOException {
		boolean containsCisco = false;
		boolean isNetApp = false;
		PaymentRefReturnModel paymentRefReturnModel=	renderTableToExcelFilter(htmlContent, excelFilePath, tableClassName, payer, payee);
		String paymentRefrence=paymentRefReturnModel.getPaymentReference();
		String paymentDate=paymentRefReturnModel.getPaymentDate();
	
		logger.info("htmlContent651:" + htmlContent);
		if (htmlContent.contains("CISCO US LEGAL ENTITY")) {
			containsCisco = true;
		}
		if (htmlContent.contains("Invoice Date")) {
			isNetApp = true;
			logger.info("isNetApp:" + isNetApp);
		}

		// Step 1: Parse the HTML content
		Document document = Jsoup.parse(htmlContent);

		// Step 2: Find all tables with the specified class name
//		Elements tables = document.select("table." + tableClassName);
//		Elements tables;
//		if (tableClassName != null && !tableClassName.isEmpty()) {
//		    tables = document.select("table." + tableClassName);
//		    if (tables.isEmpty()) {
//		        logger.info("No tables with class '" + tableClassName + "' found. Trying all <table> tags.");
//		        tables = document.select("table"); // fallback to all tables
//		    }
//		} else {
//		    logger.info("No table class provided. Extracting all <table> tags.");
//		    tables = document.select("table");
//		}
		Elements tables = document.select("table");
		logger.info(tables.html());
		logger.info(tables.toString());
		

		if (tables.isEmpty()) {
			logger.info("No tables with class '" + tableClassName + "' found.");
			return;
		}

		// Step 3: Define fixed headers
		String[] fixedHeaders = { "SL.NO", "From Payer/Client", "To PRANSQUARE Inc", "PRANSQUARE Invoice Number",
				"Payment Refrence Number", "Payment Date",
				"Document Reference Number", "Document Date", "Document Currency", "Document Amount", "Amount Withheld",
				"Discount Taken", "Amount Paid" };

		// Step 4: Check if the Excel file already exists
		File file = new File(excelFilePath);
		Workbook workbook;
		Sheet sheet;
		int rowIndex;

		if (file.exists()) {
			try (FileInputStream fis = new FileInputStream(file)) {
				workbook = new XSSFWorkbook(fis);
				sheet = workbook.getSheetAt(0);
				rowIndex = sheet.getLastRowNum() + 1; // Start at the next empty row
			}
		} else {
			workbook = new XSSFWorkbook();
			sheet = workbook.createSheet("Remittance Details");

			// Create the header row
			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < fixedHeaders.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(fixedHeaders[i]);
				CellStyle style = workbook.createCellStyle();
				Font font = workbook.createFont();
				font.setBold(true);
				style.setFont(font);
				cell.setCellStyle(style);
			}
			rowIndex = 1;
		}

		boolean dataFound = false;
		int slNo = rowIndex;

		// Step 5: Iterate over each table with the specified class
		for (Element table : tables) {
			if ((!table.text().contains("Remittance Detail")) && (!table.text().contains("Invoice Date"))
				
					) {

				continue;
			}
			logger.info("table.text()719 ==>" + table.text() );
			Elements rows = table.select("tr");

			for (Element row : rows) {
				Elements cells = row.select("td");

				if (cells.isEmpty() || cells.text().contains("Remittance Detail")
						|| cells.text().contains("Document Reference Number") || cells.text().contains("Total")
						|| cells.text().contains("Invoice Date") 
					
						) {
					continue;
				}
				logger.info("cells.text()733 ==>" + cells.text() );
				// Create a new row in Excel
				Row excelRow = sheet.createRow(rowIndex++);
				dataFound = true;

				// Assign SL.NO
				excelRow.createCell(0).setCellValue(slNo++);
				excelRow.createCell(1).setCellValue(payer);
				excelRow.createCell(2).setCellValue(payee);
				excelRow.createCell(3).setCellValue("");
				excelRow.createCell(4).setCellValue(paymentRefrence);
				
				try {
					excelRow.createCell(5).setCellValue(formatDateString(paymentDate));
				} catch (java.text.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Initialize variables for Document Amount and Document Currency
				String documentAmount = "";
				String documentCurrency = "";

				// Step 6: Check if "Cisco" is present in the cell values

				// Populate regular values, and store values for possible swap
				// Step 6: Check if "Cisco" is present in the cell values
				for (int i = 6; i < fixedHeaders.length; i++) {
					String cellValue = "";
					if (i - 6 < cells.size()) {
						cellValue = cells.get(i - 6).text();
					}
					if (i == 7) { // Document Date
						try {
							cellValue = formatDateString(cellValue);
						} catch (java.text.ParseException | ParseException e) {
							e.printStackTrace();
						}
					}

					// Handle "NetApp" logic
					if (isNetApp) {
						// Skip columns 6 and 7 when isNetApp is true
						if (i == 8 || i == 9) {
							continue;
						}

						// Populate only columns 4, 5, 9, and 10 with specific NetApp data
						if (i == 6 || i == 7 || i == 11 || i == 12) {
							String specificCellValue = "";
							switch (i) {
							case 6: // PRANSQUARE Invoice Number
								specificCellValue = cells.size() > 0 ? cells.get(0).text() : "";
								break;
							case 7: // Document Reference Number
								specificCellValue = cells.size() > 1 ? cells.get(1).text() : "";
								break;
							case 11: // Discount Taken
								specificCellValue = cells.size() > 2 ? cells.get(2).text() : "";
								break;
							case 12: // Amount Paid
								specificCellValue = cells.size() > 3 ? cells.get(3).text() : "";
								break;
							}
							excelRow.createCell(i).setCellValue(specificCellValue);
						}
					} else {
						// Handle regular assignment for non-NetApp logic
						if (!containsCisco || (i != 8 && i != 9)) {
							excelRow.createCell(i).setCellValue(cellValue);
						}
					}
				}

				// If "Cisco" is detected, perform the swap for Document Amount and Currency
				if (containsCisco) {
					excelRow.createCell(8).setCellValue(documentAmount); // Swap "Document Amount" to Column 7
					excelRow.createCell(9).setCellValue(documentCurrency); // Swap "Document Currency" to Column 8
				}

//				for (int i = 4; i < fixedHeaders.length; i++) {
//					String cellValue = "";
//					if (i - 4 < cells.size()) {
//						cellValue = cells.get(i - 4).text();
//					}
//					if (i == 5) {
//						try {
//							cellValue = formatDateString(cellValue);
//						} catch (java.text.ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (ParseException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//
//					// Store "Document Amount" and "Document Currency"
//					if (i == 6) {
//						documentCurrency = cellValue; // Column 7 value
//					} else if (i == 7) {
//						documentAmount = cellValue; // Column 8 value
//					}
//
//					// Handle regular assignment
//					if (!containsCisco ||!isNetApp|| (i != 6 && i != 7)) {
//						excelRow.createCell(i).setCellValue(cellValue);
//					}
//				}
//
//				// If "Cisco" is detected, perform the swap
//				if (containsCisco) {
//					excelRow.createCell(6).setCellValue(documentAmount); // Swap "Document Amount" to Column 7
//					excelRow.createCell(7).setCellValue(documentCurrency); // Swap "Document Currency" to Column 8
//				}
				else if (isNetApp) {
					// Populate only columns 4, 5, 9, and 10 with table content
					String invoiceNumber = cells.size() > 0 ? cells.get(0).text() : "";
					String documentRefNumber = cells.size() > 1 ? cells.get(1).text() : "";
					String discountTaken = cells.size() > 2 ? cells.get(2).text() : "";
					String amountPaid = cells.size() > 3 ? cells.get(3).text() : "";

					excelRow.createCell(6).setCellValue(invoiceNumber); // Column 4
					try {
						documentRefNumber = formatDateString(documentRefNumber);
					} catch (java.text.ParseException | ParseException e) {
						e.printStackTrace();
					}
					excelRow.createCell(7).setCellValue(documentRefNumber); // Column 5
					excelRow.createCell(11).setCellValue(discountTaken); // Column 9
					excelRow.createCell(12).setCellValue(amountPaid); // Column 10
				}
			}

			rowIndex++;
		}

		// Step 7: Write to Excel file only if data is found
		if (dataFound) {
			for (int i = 0; i < fixedHeaders.length; i++) {
				sheet.autoSizeColumn(i);
			}

			try (FileOutputStream fileOut = new FileOutputStream(excelFilePath)) {
				workbook.write(fileOut);
				logger.info("Filtered table data appended to Excel: " + excelFilePath);
			}
		} else {
			logger.info("No relevant data found, Excel file not modified.");
		}

		// Close the workbook
		workbook.close();
	}

	public String formatDateString(String dateStr) throws java.text.ParseException, ParseException {
		// Define possible input date formats except 'MMM dd, yyyy'
		String[] dateFormats = { "MM/dd/yy", // Example: 7/22/24
				"MM/dd/yyyy", // Example: 7/22/2024
				"yyyy-MM-dd", // Example: 2024-07-22
				"dd/MM/yyyy", // Example: 22/07/2024
				"dd-MM-yyyy", // Example: 22-07-2024
				"dd MMM yyyy", // Example: 22 Jul 2024
				"dd-mmm-yyyy", // Example: 30-APR-2024
				"dd-MMM-yyyy", // Example: 30-APR-2024
				"dd-Mon-yyyy", "dd-MON-yyyy" };

		// Check if the date format is already 'MMM dd, yyyy'
//		if (dateStr.matches("^[A-Za-z]{3} \\d{2}, \\d{4}$")) {
//			// If the input date is in the format 'MMM dd, yyyy', return it as is
//			return dateStr;
//		}
		logger.info("dateStr:" + dateStr);
		if (dateStr.matches("^\\d{2}-[A-Za-z]{3}-\\d{4}$") || dateStr.matches("^\\d{2}-[A-Z]{3}-\\d{4}$")) {
			logger.info("dateStr:" + dateStr);
			try {
				// Define the format for '30-APR-2024'
				SimpleDateFormat sourceFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
				SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

				// Parse the date and format it to 'MMM dd, yyyy'
				Date date = sourceFormat.parse(dateStr);
				return targetFormat.format(date);
			} catch (java.text.ParseException e) {
				// Handle parse exception (optional)
				e.printStackTrace();
			}
		}
		if (dateStr.matches("^[A-Za-z]{3} \\d{2}, \\d{4}$")) {
			// If the input date is in the format 'MMM dd, yyyy', return it as is
			return dateStr;
		}
		if (dateStr.matches("^[A-Za-z]{3} \\d{1}, \\d{4}$")) {
			// If the input date is in the format 'MMM dd, yyyy', return it as is
			return dateStr;
		}

		// Check if the date format matches 'dd-MMM-yyyy' or 'dd-MON-yyyy'

		// Define the target format
		SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

		// Set default century to interpret two-digit years as 2000s instead of 0000s
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2000); // Setting year start as 2000 for interpretation
		Date defaultStartDate = calendar.getTime();

		// Try parsing the date with each format
		for (String format : dateFormats) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
				sdf.set2DigitYearStart(defaultStartDate); // Set the century start for 2-digit years
				Date date = sdf.parse(dateStr);
				return targetFormat.format(date); // Convert to 'MMM dd, yyyy'
			} catch (java.text.ParseException e) {
				// Log the error or continue trying with the next format
			}
		}

		// Return the original value if no format matches
		return dateStr;
	}
	public PaymentRefReturnModel renderTableToExcelFilter(String htmlContent, String excelFilePath, String tableClassName, String payer,
			String payee) throws IOException {
		
		boolean containsCisco = false;
		boolean isNetApp = false;
		String paymentRefrence="";
		String paymentDate="";
		logger.info("htmlContent651:" + htmlContent);
	
		// Step 1: Parse the HTML content
		Document document = Jsoup.parse(htmlContent);

		// Step 2: Find all tables with the specified class name
//		Elements tables = document.select("table." + tableClassName);
		
		Elements tables = document.select("table");

		if (tables.isEmpty()) {
			logger.info("No tables with class '" + tableClassName + "' found.");
		
		}





		// Step 5: Iterate over each table with the specified class
		for (Element table : tables) {
			if ((!table.text().contains("Payment Number"))
					&& (!table.text().contains("Payment Reference Number"))
					) {

				continue;
			}
			logger.info("table.text()719 ==>" + table.text() );
			Elements rows = table.select("tr");

			for (Element row : rows) {
				Elements cells = row.select("td");

				if (
					cells.text().contains("Payment Reference Number")
						|| cells.text().contains("Payment Number")
						) {
					paymentRefrence=cells.get(1).text();
				}
					
				
				else if (
						cells.text().contains("Payment Date")
							|| cells.text().contains("Payment Reference Date")
							) {
					paymentDate=cells.get(1).text();
					}
		
	
	}
		}
		PaymentRefReturnModel paymentRefReturnModel=new PaymentRefReturnModel();
		paymentRefReturnModel.setPaymentReference(paymentRefrence);
		paymentRefReturnModel.setPaymentDate(paymentDate);
		logger.info("paymentRefReturnModel1031:" + paymentRefReturnModel);
		return paymentRefReturnModel;
	}
}
