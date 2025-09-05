package com.pransquare.nems.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.pransquare.nems.batch.ExcelHelper;
import com.pransquare.nems.entities.PayrollEntity;
import com.pransquare.nems.entities.PayrollStagingEntity;
import com.pransquare.nems.entities.PayrollUploadSummaryEntity;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.models.PayrollUploadModel;
import com.pransquare.nems.models.PayslipCreateModel;
import com.pransquare.nems.repositories.PayrollRepository;
import com.pransquare.nems.repositories.PayrollStagingRepository;
import com.pransquare.nems.repositories.PayrollUploadSummaryRepository;
import com.pransquare.nems.utils.IntegerUtils;
import com.pransquare.nems.utils.MonthUtil;
import com.pransquare.nems.utils.NumberToWord;
import com.pransquare.nems.utils.StringUtil;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import reactor.core.publisher.Mono;



@Service
public class PayrollService {

	private static final Logger logger = LogManager.getLogger(PayrollService.class);
	public static final String FILE_ID_PARAMETER = "p_file_id";
	public static final String ERROR_STATUS_PARAMETER = "p_error_status";
	public static final String ERROR_DESCRIPTION_PARAMETER = "p_error_description";

	PayrollRepository payrollRepository;

	ExcelHelper excelHelper;

	PayrollStagingRepository payrollStagingRepository;

	PayrollUploadSummaryRepository payloadUploadSummaryRepository;

	@Value("${payrollfilepath}")
	private String uploadDirectory;

	EntityManager em;
	private  WebClient webClient;
	
	@Value("${master-config-service.url}")
	private String masterconfigurl;
	
	 @Autowired
	    private WebClient.Builder webClientBuilder;
	 
	  @PostConstruct
	    public void init() {
	        this.webClient = webClientBuilder.baseUrl(masterconfigurl).build();
	    }
	    
	


		

	  @Autowired
	public PayrollService(PayrollRepository payrollRepository, ExcelHelper excelHelper,
			PayrollStagingRepository payrollStagingRepository,
			PayrollUploadSummaryRepository payloadUploadSummaryRepository, EntityManager em,WebClient.Builder webClientBuilder) {
		this.payrollRepository = payrollRepository;
		this.excelHelper = excelHelper;
		this.payrollStagingRepository = payrollStagingRepository;
		this.payloadUploadSummaryRepository = payloadUploadSummaryRepository;
		this.em = em;
		this.webClient = 	webClientBuilder.baseUrl(masterconfigurl).build();
	}

	public void printPdf(String htmlContent) throws Exception {

		String outputPdf =uploadDirectory + LocalDateTime.now().getSecond()
				+ ".pdf";

		try {
			convertHtmlToPdf(htmlContent, outputPdf);
			logger.info("PDF created successfully.");
		} catch (IOException e) {
			logger.error("Error creating PDF: {}", e.getMessage());
		}
	}

	public void convertHtmlToPdf(String htmlContent, String outputPdf) throws Exception {
		try (FileOutputStream os = new FileOutputStream(outputPdf)) {
			PdfRendererBuilder builder = new PdfRendererBuilder();
			builder.useFastMode();
			builder.withHtmlContent(htmlContent, null);
			builder.toStream(os);
			logger.info("Convert Html to Pdf");
			builder.run();
		}
	}
//	public void convertHtmlToPdf(String htmlContent, String outputPdf) throws Exception {
//	    try (FileOutputStream os = new FileOutputStream(outputPdf)) {
//	        PdfRendererBuilder builder = new PdfRendererBuilder();
//	        builder.useFastMode();
//	        builder.withHtmlContent(htmlContent, null); // base URI is null unless using external resources
//
//	        // Optional: use a specific font
//	        // builder.useFont(new File("path/to/arial.ttf"), "Arial");
//
//	        // Set output stream
//	        builder.toStream(os);
//	        
//	        logger.info("Converting HTML to PDF...");
//	        builder.run();
//	    }
//	}

	public Map<String, String> payslipCreate(PayslipCreateModel payslipCreateModel) throws Exception {
		Map<String, String> result = new HashMap<>();
		logger.info(payslipCreateModel);
		String month = MonthUtil.getMonth(payslipCreateModel.getMonth());
		logger.info(webClient.get());
		InputStreamResource inputResource = null;
		String html = "";
		try {
			
			PayrollEntity payrollEntity = payrollRepository.findByEmpBasicDetailIdAndPayPeriodMonthAndPayPeriodYear(
					payslipCreateModel.getEmpBasicDetailId(), month, payslipCreateModel.getYear());
			logger.info("payrollEntity"+payrollEntity);
			String design = webClient.get()
				    .uri("/pransquare/MasterConfiguration/designations/getDesignation/{designationCode}",
				    		//here
				         payrollEntity.getEmployeeEntity().getDesignation())
				    .retrieve()
				    .bodyToMono(String.class)
				    .onErrorResume(e -> {
				        logger.error("Error calling designation service", e);
				        return Mono.just("UNKNOWN");
				    })
				    .block();
			

				if (design == null || design.isBlank()) {
					logger.error("Designation returned null or empty for code: {}",payrollEntity.getEmployeeEntity().getDesignation() );
				    throw new IllegalStateException("Designation resolution failed");
				}
			if (payrollEntity == null) {
				throw new ResourceNotFoundException("Payroll not found for given employee and month and year.");
			}

			String employeeCode = payrollEntity.getEmployeeEntity().getEmployeeCode();

			Double noOfWorkingDays = payrollEntity.getNoOfDays() - payrollEntity.getLop();
			int num = (int) Math.round(payrollEntity.getNetPay());
			logger.info(num);

			html = "<html lang=\"en\">\r\n" + "<head>\r\n" + "    <meta charset=\"utf-8\"></meta>\r\n"
					+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></meta>\r\n"
					+ "    <title>Payslip</title>\r\n" + "<style>\r\n"
							+ "        @page {\r\n"
							+ "            size: A4 landscape;\r\n"
							+ "            margin: 10mm;\r\n"
							+ "        }</style></head>\r\n"
					+ "<body style=\"font-family: sans-serif; margin: 0; padding: 5px; line-height: 1;\">\r\n"
					+ "    <div style=\"width: 90%; margin: auto; padding: 15px; box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);\">\r\n"
					+ "        <div style=\"border: 2px solid #000; padding: 15px;\">\r\n"
					+ "            <div style=\"display: flex; align-items: center; padding: 5px; border-bottom: 1px solid #000;\">\r\n"
					+ "                <img src=\"file:///C:/Users/Admin/Pictures/image.png\" alt=\"Company Logo\" style=\"max-width: 180px; margin-right: 20px;\"></img>\r\n"
					+ "                <div style=\"text-align: center; flex-grow: 1;\">\r\n"
					+ "                    <h2 style=\"font-size: 1em; font-weight: bold;\">Pransquare Evalutions Pvt. Ltd.</h2>\r\n"
					+ "                    <p style=\"font-size: 0.7em;\">Ground Floor, DLF Cybercity, Block 2, Gachibowli, Hyderabad, Telangana-500032</p>\r\n"
					+ "                    <h3 style=\"font-size: 0.7em;\">Payslip for the Month of "
					+ payrollEntity.getPayPeriodMonth() +"-"+ payrollEntity.getPayPeriodYear() + " </h3>\r\n"
					+ "                </div>\r\n" + "            </div>\r\n" + " \r\n"
					+ "            <div style=\"padding: 5px; border-bottom: 1px solid #000;\">\r\n"
					+ "                <table style=\"width: 100%;\">\r\n" + "                    <tr>\r\n"
					+ "                        <td style=\"padding: 5px 5px; font-size: 0.68em;\">Employee Code:</td>\r\n"
					+ "                        <td style=\"padding: 5px 5px; font-size: 0.68em;\">"
					+ payrollEntity.getEmployeeEntity().getEmployeeCode() + "</td>\r\n"
					+ "                        <td style=\"padding: 5px 5px; font-size: 0.68em;\">Full Name:</td>\r\n"
					+ "                        <td style=\"padding: 5px 5px; font-size: 0.68em;\">"
					+ payrollEntity.getEmployeeEntity().getFullName() + "</td>\r\n" + "                    </tr>\r\n"
					+ "                    <tr>\r\n"
					+ "                        <td style=\"padding: 5px 5px; font-size: 0.68em;\">Designation:</td>\r\n"
					+ "                        <td style=\"padding: 5px 5px; font-size: 0.68em;\">"
					+ design+ "</td>\r\n"
					+ "                        <td style=\"padding: 5px 5px; font-size: 0.68em;\">Date of Joining:</td>\r\n"
					+ "                        <td style=\"padding: 5px 5px; font-size: 0.68em;\">"
					+ payrollEntity.getEmployeeEntity().getDateOfJoining() + " </td>\r\n"
					+ "                    </tr>\r\n" + "                    <tr>\r\n"
					+ "                        <td style=\"padding: 5px 5px; font-size: 0.68em;\">UAN Number:</td>\r\n"
					+ "                        <td style=\"padding: 5px 5px; font-size: 0.68em;\">"
					+ payrollEntity.getEmployeeEntity().getUanNo() + "</td>\r\n"
					+ "                        <td style=\"padding: 5px 5px; font-size: 0.68em;\">Month Days:</td>\r\n"
					+ "                        <td style=\"padding: 5px 5px; font-size: 0.68em;\">"
					+ String.format("%.2f",payrollEntity.getNoOfDays() )+ "</td>\r\n" + "                    </tr>\r\n"
					+ "                    <tr>\r\n"
					+ "                        <td style=\"padding: 5px 5px; font-size: 0.68em;\">Paid Days:</td>\r\n"
					+ "                        <td style=\"padding: 5px 5px; font-size: 0.68em;\">" + String.format("%.2f",noOfWorkingDays)
					+ "</td>\r\n" + "                        <td></td>\r\n" + "                        <td></td>\r\n"
					+ "                    </tr>\r\n" + "                </table>\r\n" + "            </div>\r\n"
					+ " \r\n" + "            <div style=\"padding: 5px 0; border-bottom: 1px solid #000;\">\r\n"
					+ "                <table style=\"width: 100%; border-collapse: collapse;\">\r\n"
					+ "                    <thead>\r\n"
					+ "                        <tr style=\"font-weight: bold; font-size: 0.8em;\">\r\n"
					+ "                            <th style=\"text-align: left; padding: 5px; border-bottom: 1px solid #000;\">EARNINGS</th>\r\n"
					+ "                            <th style=\"text-align: left; padding: 5px; border-bottom: 1px solid #000;\">AMOUNT</th>\r\n"
					+ "                            <th style=\"text-align: left; padding: 5px; border-bottom: 1px solid #000;\">DEDUCTIONS</th>\r\n"
					+ "                            <th style=\"text-align: center; padding: 5px; border-bottom: 1px solid #000;\">AMOUNT</th>\r\n"
					+ "                        </tr>\r\n" + "                    </thead>\r\n"
					+ "                    <tbody>\r\n" + "                        <tr>\r\n"
					+ "                            <td style=\"padding: 5px; font-size: 0.68em;\">Earned Basic</td>\r\n"
					+ "                            <td style=\"text-align: left; padding: 5px; font-size: 0.68em;\">"
					+ String.format("%.2f",payrollEntity.getBasicSalary()) + "</td>\r\n"
					+ "                            <td style=\"padding: 5px; font-size: 0.68em;\">PF Amount</td>\r\n"
					+ "                            <td style=\"text-align: center; padding: 5px; font-size: 0.68em;\">"
					+ String.format("%.2f",payrollEntity.getPf()) + "</td>\r\n" + "                        </tr>\r\n"
					+ "                        <tr>\r\n"
					+ "                            <td style=\"padding: 5px; font-size: 0.68em;\">Earned Conveyance</td>\r\n"
					+ "                            <td style=\"text-align: left; padding: 5px; font-size: 0.68em;\">"
					+ String.format("%.2f",payrollEntity.getConveyanceAllowance()) + "</td>\r\n"
					+ "                            <td style=\"padding: 5px; font-size: 0.68em;\">Professional Tax</td>\r\n"
					+ "                            <td style=\"text-align: center; padding: 5px; font-size: 0.68em;\">"
					+ String.format("%.2f",payrollEntity.getProfessionalTax()) + "</td>\r\n" + "                        </tr>\r\n"
					
								+ "                        <tr>\r\n"
					+ "                            <td style=\"padding: 5px; font-size: 0.68em;\">Earned HRA</td>\r\n"
					+ "                            <td style=\"text-align: left; padding: 5px; font-size: 0.68em;\">"
					+ String.format("%.2f",payrollEntity.getHra() )+ "</td>\r\n" 
					+" <td style=\"padding: 5px; font-size: 0.68em;\">Income Tax</td>\r\n"
					+ "                            <td style=\"text-align: center; padding: 5px; font-size: 0.68em;\">"
					+ String.format("%.2f",payrollEntity.getIncomeTax()) + "</td>\r\n"  
					+ "                            <td></td>\r\n" + "                        </tr>\r\n"
					+ "                        <tr>\r\n"
					+ "                            <td style=\"padding: 5px; font-size: 0.68em;\">Earned Medical Allowance</td>\r\n"
					+ "                            <td style=\"text-align: left; padding: 5px; font-size: 0.68em;\">"
					+ String.format("%.2f",payrollEntity.getMedicalAllowance()) + "</td>\r\n" + "                            <td></td>\r\n"
					+ "                            <td></td>\r\n" + "                        </tr>\r\n"
					+ "                        <tr>\r\n"
					+ "                            <td style=\"padding: 5px; font-size: 0.68em;\">Earned Special Allowance</td>\r\n"
					+ "                            <td style=\"text-align: left; padding: 5px; font-size: 0.68em;\">"
					+ String.format("%.2f",payrollEntity.getSpecialAllowance()) + "</td>\r\n" + "                            <td></td>\r\n"
					+  "<td></td>\r\n" + "                        </tr>\r\n"
						+ "                        <tr>\r\n"
						+ "                            <td style=\"padding: 5px; font-size: 0.68em;\">Variable Pay</td>\r\n"
						+ "                            <td style=\"text-align: left; padding: 5px; font-size: 0.68em;\">"
						+ String.format("%.2f",payrollEntity.getVariablePay()) + "</td>\r\n" + "                            <td></td>\r\n"
					+ "                            <td></td>\r\n" + "                        </tr>\r\n"
					+ "                    </tbody>\r\n" + "                </table>\r\n" + "            </div>\r\n"
					+ " \r\n" + "            <div style=\"padding: 5px 0; border-bottom: 1px solid #000;\">\r\n"
					+ "                <table style=\"width: 100%; border-collapse: collapse;\">\r\n"
					+ "                    <tr>\r\n"
					+ "                        <td style=\"font-weight: bold; font-size: 0.8em; padding: 5px;\">TOTAL GROSS PAY:</td>\r\n"
					+ "                        <td style=\"text-align: left; font-size: 0.8em; padding: 5px;display: grid; grid-template-columns: 0.7fr 1fr;\">"
					+ String.format("%.2f",payrollEntity.getTotalEarnings()) + "</td>\r\n"
					+ "                        <td style=\"font-weight: bold; font-size: 0.8em; padding: 5px;\">DEDUCTION TOTAL:</td>\r\n"
					+ "                        <td style=\"text-align: left; font-size: 0.8em; padding: 5px;display: grid; grid-template-columns: 0.7fr 1fr;\">"
					+String.format("%.2f", payrollEntity.getTotalDeductions()) + "</td>\r\n" + "                    </tr>\r\n"
					+ "                </table>\r\n" + "            </div>\r\n" + " \r\n"
					+ "            <div style=\"display: grid; grid-template-columns: 0.6fr 1fr; gap: 5px; font-weight: bold; font-size: 0.9em;\">\r\n"
					 +"<td style=\"font-weight: bold; font-size: 0.9em; padding: 5px;\">NET PAY:</td>\r\n"
								+ "                        <td style=\"text-align: left; font-size: 0.9em; padding: 5px;\">"
								+ String.format("%.2f",payrollEntity.getNetPay()) + "</td>\r\n"  
					//					+ "                <div style=\"padding: 5px 0;\">NET PAY:</div>\r\n"
//					+ "                <div style=\"padding: 5px 0;\">" + payrollEntity.getNetPay() + "</div>\r\n"
					+ "            </div>\r\n"
					+ "            <div style=\"border-top: 1px solid #000; margin: 5px 0;\"></div>\r\n" + " \r\n"
					+ "            <div style=\"text-align: center; font-weight: bold; font-size: 0.9em;\">\r\n"
					+ "                <p style=\"margin: 5px 0;\">(Rupees "+NumberToWord.convert(( int)
							  Math.round(payrollEntity.getNetPay()))+" Only)</p>\r\n"
					+ "            </div>\r\n" + "        </div>\r\n" + "    </div>\r\n" + "</body>\r\n" + "</html>";
			

			result.put("html", html);
			result.put("empCode", employeeCode);

			printPdf(html);
		} catch (Exception e) {
			logger.error("Error generating payslip for employee code {}: {}", payslipCreateModel.getEmpBasicDetailId(),
					e.getMessage());
		}
		return result;
	}

	public Map<String, Object> uploadFile(MultipartFile file, PayrollUploadModel payrollUploadModel) {
		Map<String, Object> returnResponse = new HashMap<String, Object>();
		if (file.isEmpty()) {
			throw new ResourceNotFoundException("File is Empty");
		}
		PayrollUploadSummaryEntity payloadSummary = payloadUploadSummaryRepository
				.findByFileName(file.getOriginalFilename());
		if (payloadSummary != null) {
			throw new ResourceNotFoundException("File Already Exists");
		}
		try {
			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			String filePath = uploadDirectory + File.separator + file.getOriginalFilename();
			Path path = Paths.get(filePath);
			Files.write(path, bytes);

			// After saving the file, read it as an InputStream
			InputStream inputStream = Files.newInputStream(path);
			PayrollUploadSummaryEntity payrollUploadSummaryEntity = new PayrollUploadSummaryEntity();
			payrollUploadSummaryEntity.setFileName(file.getOriginalFilename());
			payrollUploadSummaryEntity.setCreatedBy(payrollUploadModel.getCreatedBy());
			payrollUploadSummaryEntity.setCreatedDate(LocalDateTime.now());
			payrollUploadSummaryEntity.setStatus("100");
			payrollUploadSummaryEntity.setFilePath(filePath);
			// Create stored procedure query
			StoredProcedureQuery query = em.createStoredProcedureQuery("PayrollFileIdSeq")
					.registerStoredProcedureParameter("p_sequence", String.class, ParameterMode.OUT);
			// Execute stored procedure
			query.execute();
			String fileId = (String) query.getOutputParameterValue("p_sequence");

			payrollUploadSummaryEntity.setFileId(fileId);
			payrollUploadSummaryEntity = payloadUploadSummaryRepository.save(payrollUploadSummaryEntity);
			List<PayrollStagingEntity> payrollList = excelHelper.readPayrollFromExcel(inputStream, fileId);
			logger.info("payrollList: {}", payrollList);
			payrollStagingRepository.saveAll(payrollList);
			returnResponse.put("response", "File uploaded and processed successfully: " + file.getOriginalFilename());
			returnResponse.put("payrollUploadSummaryEntity", payrollUploadSummaryEntity);
			return returnResponse;
		} catch (IOException e) {

			throw new RuntimeException("Exception occured");
		}
	}

	public Map<String, Object> migrateToPayrollMaster(PayrollUploadModel payrollUploadModel) {
		Map<String, Object> returnResponse = new HashMap<>();

		String errorDescription = "";
		try {
			// Begin transaction

			// Create stored procedure query
			StoredProcedureQuery query = em.createStoredProcedureQuery("MoveValidPayrollRecords")
					.registerStoredProcedureParameter(FILE_ID_PARAMETER, String.class, ParameterMode.IN)
					.registerStoredProcedureParameter(ERROR_STATUS_PARAMETER, String.class, ParameterMode.OUT)
					.registerStoredProcedureParameter(ERROR_DESCRIPTION_PARAMETER, String.class, ParameterMode.OUT)
					.setParameter(FILE_ID_PARAMETER, payrollUploadModel.getFileId());

			// Execute stored procedure
			query.execute();

			// Get output parameters
			String errorStatus = (String) query.getOutputParameterValue(ERROR_STATUS_PARAMETER);
			errorDescription = (String) query.getOutputParameterValue(ERROR_DESCRIPTION_PARAMETER);
			PayrollUploadSummaryEntity payrollUploadSummaryEntity = createResponseFile(payrollUploadModel);
			returnResponse.put("response", errorDescription);
			returnResponse.put("errorStatus", errorStatus);
			returnResponse.put("filePath", payrollUploadSummaryEntity.getResponseFilePath());
		} catch (Exception e) {
			// Rollback transaction if there is an exception
			logger.error("Error occurred during payroll migration: {}", e.getMessage());
		} finally {
			// Close EntityManager and EntityManagerFactory
			if (em.isOpen()) {
				em.close(); // Ensure EntityManager is closed
				logger.info("EntityManager closed successfully.");
			}
		}
		return returnResponse;
	}

	public PayrollUploadSummaryEntity createResponseFile(PayrollUploadModel payrollUploadModel) {

		List<PayrollStagingEntity> payrollStagingEntities = payrollStagingRepository
				.findByFileId(payrollUploadModel.getFileId());
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Payroll Data");

		// Creating Header Row
		Row headerRow = sheet.createRow(0);
		String[] headers = { "File ID", "Employee Code", "Full Name", "Month", "Working Days", "LOP", "Basic Salary",
				"HRA", "Medical Allowance", "Special Allowance", "Other Allowance", "Employer PF", "Employee PF", "PT",
				"Insurance", "Variable Pay Reserve", "Variable Pay Release", "Created Date", "Created By",
				"Modified Date", "Modified By", "Status", "Gross Salary", "Net Salary", "Year", "Error Details" };

		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
		}

		// Populating the data rows
		int rowNum = 1;
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		for (PayrollStagingEntity payrollData : payrollStagingEntities) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(payrollData.getFileId());
			row.createCell(1).setCellValue(payrollData.getEmployeeCode());
			row.createCell(2).setCellValue(payrollData.getFullName());
			row.createCell(3).setCellValue(payrollData.getMonth());
			row.createCell(4).setCellValue(payrollData.getNoOfDays());
			if (IntegerUtils.isNotNull(payrollData.getLop())) {
				row.createCell(5).setCellValue((payrollData.getLop()));
			}
			if (IntegerUtils.isNotNull(payrollData.getBasicSalary())) {
				row.createCell(6).setCellValue((payrollData.getBasicSalary()));
			}
			if (IntegerUtils.isNotNull(payrollData.getHra())) {
				row.createCell(7).setCellValue(payrollData.getHra());
			}
			if (IntegerUtils.isNotNull(payrollData.getMedicalAllowance())) {
				row.createCell(8).setCellValue(payrollData.getMedicalAllowance());
			}
			if (IntegerUtils.isNotNull(payrollData.getSpecialAllowance())) {
				row.createCell(9).setCellValue(payrollData.getSpecialAllowance());
			}
			if (IntegerUtils.isNotNull(payrollData.getOtherAllowance())) {
				row.createCell(10).setCellValue((payrollData.getOtherAllowance()));
			}
			if (IntegerUtils.isNotNull(payrollData.getEmployerPf())) {
				row.createCell(11).setCellValue((payrollData.getEmployerPf()));
			}
			if (IntegerUtils.isNotNull(payrollData.getEmployeePf())) {
				row.createCell(12).setCellValue((payrollData.getEmployeePf()));
			}
			if (IntegerUtils.isNotNull(payrollData.getPt())) {
				row.createCell(13).setCellValue((payrollData.getPt()));
			}
			if (IntegerUtils.isNotNull(payrollData.getInsurance())) {
				row.createCell(14).setCellValue(payrollData.getInsurance());
			}
			if (IntegerUtils.isNotNull(payrollData.getVariablePayReserve())) {
				row.createCell(15).setCellValue((payrollData.getVariablePayReserve()));
			}
			if (IntegerUtils.isNotNull(payrollData.getVariablePayRelease())) {
				row.createCell(16).setCellValue((payrollData.getVariablePayRelease()));
			}
			row.createCell(17).setCellValue(
					payrollData.getCreatedDate() != null ? payrollData.getCreatedDate().format(dateTimeFormatter) : "");
			row.createCell(18).setCellValue(payrollData.getCreatedBy());
			row.createCell(19)
					.setCellValue(payrollData.getModifiedDate() != null
							? payrollData.getModifiedDate().format(dateTimeFormatter)
							: "");
			row.createCell(20).setCellValue(payrollData.getModifiedBy());
			row.createCell(21).setCellValue(payrollData.getStatusMasterEntity().getDescription());
			if (IntegerUtils.isNotNull(payrollData.getGrossSalary())) {
				row.createCell(22).setCellValue(payrollData.getGrossSalary());
			}
			if (IntegerUtils.isNotNull(payrollData.getNetSalary())) {
				row.createCell(23).setCellValue(payrollData.getNetSalary());
			}
			if (StringUtil.isNotNull(payrollData.getYear())) {
				row.createCell(24).setCellValue(payrollData.getYear());
			}
			if (StringUtil.isNotNull(payrollData.getErrorDetails())) {
				row.createCell(25).setCellValue(payrollData.getErrorDetails());
			}
		}

		// Auto-size the columns
		for (int i = 0; i < headers.length; i++) {
			sheet.autoSizeColumn(i);
		}
		PayrollUploadSummaryEntity payrollUploadSummaryEntity = payloadUploadSummaryRepository
				.findByFileId(payrollUploadModel.getFileId());
		// Write the output to a file
		String fileName = uploadDirectory + payrollUploadModel.getFileId() + " ResponseFile.xlsx";
		File file = new File(fileName);
		try (FileOutputStream fileOut = new FileOutputStream(file)) {
			workbook.write(fileOut);

			payrollUploadSummaryEntity.setResponseFilePath(fileName);
			payrollUploadSummaryEntity.setModifiedBy(payrollUploadModel.getCreatedBy());
			payrollUploadSummaryEntity.setModifiedDate(LocalDateTime.now());
			payloadUploadSummaryRepository.save(payrollUploadSummaryEntity);
			logger.info("Response file created and saved successfully at {}", fileName);
		} catch (FileNotFoundException e) {
			logger.error("File not found: {}. Error: {}", fileName, e.getMessage());
		} catch (IOException e) {
			logger.error("I/O error while writing the file: {}. Error: {}", fileName, e.getMessage());
		}
		try {
			workbook.close();
		} catch (IOException e) {
			logger.error("I/O error while closing the workbook. Error: {}", e.getMessage());
		}
		return payrollUploadSummaryEntity;
	}

	public Map<String, Object> validatePayroll(PayrollUploadModel payrollUploadModel) {
		Map<String, Object> returnResult = new HashMap<String, Object>();
		try {
			// Begin transaction

			// Create stored procedure query
			StoredProcedureQuery query = em.createStoredProcedureQuery("MarkDuplicatePayrollStagingRecords")
					.registerStoredProcedureParameter(FILE_ID_PARAMETER, String.class, ParameterMode.IN)
					.registerStoredProcedureParameter(ERROR_STATUS_PARAMETER, String.class, ParameterMode.OUT)
					.registerStoredProcedureParameter(ERROR_DESCRIPTION_PARAMETER, String.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_success_count", Integer.class, ParameterMode.OUT)
					.registerStoredProcedureParameter("p_failed_count", Integer.class, ParameterMode.OUT)
					.setParameter(FILE_ID_PARAMETER, payrollUploadModel.getFileId());

			// Execute stored procedure
			query.execute();

			// Get output parameters
			String errorStatus = (String) query.getOutputParameterValue(ERROR_STATUS_PARAMETER);
			String errorDescription = (String) query.getOutputParameterValue(ERROR_DESCRIPTION_PARAMETER);
			Integer successCount = (Integer) query.getOutputParameterValue("p_success_count");
			Integer failedCount = (Integer) query.getOutputParameterValue("p_failed_count");
			returnResult.put("errorStatus", errorStatus);
			returnResult.put("errorDescription", errorDescription);
			returnResult.put("successCount", successCount);
			returnResult.put("failedCount", failedCount);
			if (successCount < 1) {
				throw new ResourceNotFoundException("No success records exist");
			}
			// Commit transaction

			// Print results

		} catch (Exception e) {
			// Rollback transaction if there is an exception

		} finally {
			// Close EntityManager and EntityManagerFactory
			em.close();
		}
		return returnResult;
	}

	public String getCurrencyInWords(Long num) {
		String word = NumberToWord.convert(num);
		return word;
	}

	public List<PayrollEntity> getPayrollByEmployeeId(Long employeeId) {
		return payrollRepository.findByEmpBasicDetailIdOrderByPayPeriod(employeeId);
	}

}
