package com.pransquare.nems.controllers;

import java.io.File;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.services.InvoiceExtract;


@RestController
@RequestMapping("/Pransquare/nems/Emailextraction")
public class WordDataExtraction {
	
	
	
	
	@Autowired
	InvoiceExtract invoiceExtract;
	
	 @Value("${remittancefile.path}")
		private String remittanceFilepath;
	
//	@GetMapping("/getWordDataExtract")
//	public void getWordDataExtract() {
//		try {
////			String filePath = null;
////			wordDataExtractionService.getWordDataExtract(filePath);
//			wordDataExtractionService.readEmails1();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
//	@GetMapping("/getpdfTestExtract")
//	public void getpdfTestExtract() {
//		try {
////			String filePath = null;
////			wordDataExtractionService.getWordDataExtract(filePath);
//			wordDataExtractionService.mainTable();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
//	@GetMapping("/invoiceExtractionAR")
	@GetMapping("/callAsyncInvoice")
	public void invoiceExtractionAR() {
		try {
//			String filePath = null;
//			wordDataExtractionService.getWordDataExtract(filePath);
			invoiceExtract.getAccessToken();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
//	@GetMapping("/fileArchive")
	   @GetMapping("/invoiceFileArchive")
	public String fileArchive() {
		String result="";
		try {
//			String filePath = null;
//			wordDataExtractionService.getWordDataExtract(filePath);
			result= invoiceExtract.fileRename();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
    }
	
//	@GetMapping("/downloadRemittanceInvoice")
	@GetMapping("/downloadInvoiceExcel")
	public ResponseEntity<Resource> downloadSampleTemplate() {
		try {
			// Define the path to the file
			File file = Paths.get(remittanceFilepath).toFile();

			// Check if the file exists
			if (!file.exists()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			// Serve the file as a resource
			Resource resource = new FileSystemResource(file);

			// Set headers for the download response
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
			headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

			// Return the file resource in the response
			return ResponseEntity.ok().headers(headers).body(resource);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}
}
