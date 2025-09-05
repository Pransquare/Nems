package com.pransquare.nems.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.pransquare.nems.entities.PayrollEntity;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.models.BankDetails;
import com.pransquare.nems.models.PayrollResponseModel;
import com.pransquare.nems.models.PayrollUploadModel;
import com.pransquare.nems.models.PayslipCreateModel;
import com.pransquare.nems.services.PayrollService;

@RestController
@RequestMapping("/Pransquare/nems/Payroll")
public class PayrollController {

	private static final Logger logger = LogManager.getLogger(PayrollController.class);

	private final PayrollService payrollService;

	public PayrollController(PayrollService payrollService) {
		this.payrollService = payrollService;
	}

	@Value("${samplefilepath}")
	private String samplefilepath;

	@PostMapping(value = "/viewOrDownloadPayslip") //over
	public ResponseEntity<InputStreamResource> viewOrDownloadPayslip(
			@RequestBody PayslipCreateModel payslipCreateModel) {
		try {
			Map<String, String> result = payrollService.payslipCreate(payslipCreateModel);
			String empCode = result.get("empCode");

			ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();

			PdfRendererBuilder builder = new PdfRendererBuilder();
			builder.useFastMode();
			builder.withHtmlContent(result.get("html"), null);
			builder.toStream(pdfStream);
			builder.run();

			InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(pdfStream.toByteArray()));

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + empCode + "Payslip.pdf")
					.contentType(MediaType.APPLICATION_PDF).contentLength(pdfStream.size()).body(resource);
		} catch (Exception e) {
			logger.error("Error occurred while generating payslip: {}", e.getMessage());
			return ResponseEntity.status(500).build();
		}
	}

	@PostMapping(value = "/uploadPayrollFile", consumes = "multipart/form-data")//OVER
	public ResponseEntity<Map<String, Object>> uploadPayrollFile(@RequestParam("file") MultipartFile multipartFile,
			@ModelAttribute PayrollUploadModel payrollUploadModel) {
		Map<String, Object> response = payrollService.uploadFile(multipartFile, payrollUploadModel);
		return ResponseEntity.ok().body(response);
	}

	@PostMapping(value = "/migrateToPayrollMaster")//OVER
	public ResponseEntity<Map<String, Object>> migrateToPayrollMaster(
			@RequestBody PayrollUploadModel payrollUploadModel) {
		Map<String, Object> returnResponse = payrollService.migrateToPayrollMaster(payrollUploadModel);
		return ResponseEntity.ok().body(returnResponse);
	}

	@PostMapping(value = "/validatePayroll")//OVER
	public ResponseEntity<Map<String, Object>> validatePayroll(@RequestBody PayrollUploadModel payrollUploadModel) {
		Map<String, Object> returnResponse = new HashMap<>();
		Map<String, Object> response = payrollService.validatePayroll(payrollUploadModel);
		returnResponse.put("response", response);
		return ResponseEntity.ok().body(returnResponse);
	}

	@GetMapping(value = "/getCurrencyInWords")//OVER
	public ResponseEntity<Map<String, Object>> getCurrencyInWords(@RequestParam Long num) {
		Map<String, Object> returnResponse = new HashMap<>();
		String response = payrollService.getCurrencyInWords(num);
		returnResponse.put("response", response);
		return ResponseEntity.ok().body(returnResponse);
	}

	@GetMapping("/downloadpdf")//OVER
	public ResponseEntity<byte[]> downloadPdf(@RequestParam String filePathInput) throws IOException {
		// Path to your PDF file
		Path filePath = Paths.get(filePathInput);
		String filename = filePath.getFileName().toString();
		// Read the file into a byte array
		byte[] fileContent = Files.readAllBytes(filePath);

		// Set the content type and headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("attachment", filename);

		// Return the file as a ResponseEntity with the byte array
		return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
	}

	@GetMapping("/getIFSC") //over
	public BankDetails getIFSC(@RequestParam String ifscCode) {
		RestTemplate restTemplate = new RestTemplate();
		String baseUrl = "https://bankfinder.in/api/v1/ifsc/";

		String url = baseUrl + ifscCode;
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");

		// Creating an HttpEntity with the headers
		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			// Making the API call using exchange()
			ResponseEntity<BankDetails> response = restTemplate.exchange(url, HttpMethod.GET, entity,
					BankDetails.class);

			// Returning the bank details if found
			return response.getBody();
		} catch (HttpClientErrorException e) {
			// Handling errors (e.g., 404 if the IFSC code is not found)
			logger.error("Error occurred: {}", e.getMessage());
			return null;
		}
	}

	@PostMapping(value = "/createResponseFile")//OVER
	public void createResponseFile(@RequestBody PayrollUploadModel payrollUploadModel) {

		payrollService.createResponseFile(payrollUploadModel);
	}

	@PostMapping("/downloadResponseFile")//over
	public ResponseEntity<Resource> downloadFile(@RequestBody PayrollResponseModel payrollResponseModel) {
		try {
			// Define the path to the file
			File file = Paths.get(payrollResponseModel.getFilePath()).toFile();

			// Check if the file exists
			if (!file.exists()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}

			// Serve the file as a resource
			Resource resource = new FileSystemResource(file);

			// Detect the MIME type of the file
			String mimeType = Files.probeContentType(file.toPath());
			if (mimeType == null) {
				mimeType = "application/octet-stream"; // Default MIME type for binary data
			}

			// Set headers for the download response
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
			headers.add(HttpHeaders.CONTENT_TYPE, mimeType);

			// Return the file resource in the response
			return ResponseEntity.ok().headers(headers).body(resource);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping("/downloadSampleTemplate")  //still pending
	public ResponseEntity<Resource> downloadSampleTemplate() {
	    try {
	        Path path = Paths.get(samplefilepath);
	        File file = path.toFile();

	        if (!file.exists()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	        }

	        Resource resource = new FileSystemResource(file);
	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
	        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

	        return ResponseEntity.ok()
	                .headers(headers)
	                .contentLength(file.length())
	                .body(resource);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	@GetMapping("/getEmpoyeePayrollDetails")//over
	public ResponseEntity<List<PayrollEntity>> getEmployeePayrollDetails(@RequestParam("employeeId") Long employeeId) {
		try {
			return ResponseEntity.ok().body(payrollService.getPayrollByEmployeeId(employeeId));
		} catch (Exception e) {
			logger.error(e.fillInStackTrace());
			throw new ResourceNotFoundException("Something went wrong..");
		}
	}
}
