
package com.pransquare.nems.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pransquare.nems.dto.VendorApprovalDto;
import com.pransquare.nems.dto.VendorInputDto;
import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.entities.UserApproverConfigurationsEntity;
import com.pransquare.nems.entities.Vendor;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.repositories.ApproverConfigRepository;
import com.pransquare.nems.repositories.EmployeeRepository;
import com.pransquare.nems.repositories.VendorRepository;

@Service
public class VendorService {

	private static final Logger logger = LogManager.getLogger(VendorService.class);

	@Autowired
	private VendorRepository vendorRepository;

	@Autowired
	private ApproverConfigRepository userApprovalConfigRepository;

	@Autowired
	private EmployeeRepository employeeBasicDetailsRepository;

	@Autowired
	private EmailService emailService;

	@Transactional
	public Vendor createVendor(VendorInputDto vendorInputDto) {
		Vendor vendor = new Vendor();

		// Set other vendor details from DTO
		vendor.setVendorName(vendorInputDto.getVendorName());
		vendor.setClient(vendorInputDto.getClient());
		vendor.setResource(vendorInputDto.getResource());
		vendor.setContractType(vendorInputDto.getContractType());
		vendor.setClientRate(vendorInputDto.getClientRatePerHour());
		vendor.setSsitRate(vendorInputDto.getSsItRatePerHour());
		vendor.setRateMargin(vendorInputDto.getRateMarginPerHour());
		vendor.setVendorStatus(vendorInputDto.getVendorStatus());
		vendor.setEmpId(vendorInputDto.getEmpId());
		vendor.setWorkflowStatus("101");
		vendor.setStartDate(vendorInputDto.getStartDate());
		vendor.setEndDate(vendorInputDto.getEndDate());

		Vendor v = vendorRepository.save(vendor);
		Long empBasicDetailsId = vendorInputDto.getEmpId();
		String moduleName = "vendorProcess"; // Define module name

		// Fetch Manager ID from UserApprovalConfigRepository using empBasicDetailsId
		// and moduleName
		UserApproverConfigurationsEntity manager = userApprovalConfigRepository
				.findByEmpBasicDetailIdAndModuleName(empBasicDetailsId, moduleName);
		if (manager != null) {
			Long managerId = manager.getApproverId();
			vendor.setManagerId(managerId);

			// Fetch manager details (like email) from EmployeeBasicDetailsRepository
			Optional<EmployeeEntity> managerDetailsOpt = employeeBasicDetailsRepository.findById(managerId);
			if (managerDetailsOpt.isPresent()) {
				EmployeeEntity managerDetails = managerDetailsOpt.get();
				String managerEmail = managerDetails.getEmailId();
				String subject = "Vendor Approval Request";
				String emailBody = "Dear " + managerDetails.getFirstName() + ",\n\n"
						+ "A new vendor has been created and requires your approval.\n" + "Vendor Name: "
						+ vendor.getVendorName() + "\n" + "Client: " + vendor.getClient() + "\n"
						+ "Please review the vendor details and approve.\n\n" + "Best regards,\nYour Team";

				emailService.sendEmail(managerEmail, subject, emailBody);

				// You can use the managerEmail for sending notifications or storing in the
				// vendor entity if required
				logger.info("Manager Email: " + managerEmail);
			} else {
				logger.info("Manager details not found for ID: " + managerId);
			}
		} else {
			logger.info("No Manager found for empBasicDetailsId: " + empBasicDetailsId);
		}

		return v;
	}

	@Transactional
	public String approveOrRejectVendor(VendorApprovalDto approvalDto) {
		logger.info("inside approveOrRejectVendor");
		Long vendorId = approvalDto.getVendorId();
		Long managerId = approvalDto.getManagerId();
		String workflowStatus = approvalDto.getWorkflowStatus();
		try {
		Vendor vendor = vendorRepository.findById(vendorId)
				.orElseThrow(() -> new RuntimeException("Vendor not found with ID: " + vendorId));

		if (!vendor.getManagerId().equals(managerId)) {
			return "Unauthorized: You are not assigned to approve this vendor.";
		}

		vendor.setWorkflowStatus(workflowStatus);
		vendor.setRemarks(approvalDto.getRemarks());
		vendorRepository.save(vendor);

		// Fetch employee details
		Optional<EmployeeEntity> employeeOpt = Optional
				.of(employeeBasicDetailsRepository.findByEmployeeBasicDetailId(vendor.getEmpId()));
		if (employeeOpt.isPresent()) {
			EmployeeEntity employee = employeeOpt.get();
			String employeeEmail = employee.getEmailId();
			String subject = "Vendor Request " + (workflowStatus.equals("102") ? "Approved" : "Rejected");
			String emailBody = "Dear " + employee.getFirstName() + ",\n\n" + "Your vendor request has been "
					+ (workflowStatus.equals("102") ? "Approved" : "Rejected") + " by the manager.\n" + "Vendor Name: "
					+ vendor.getVendorName() + "\n" + "Client: " + vendor.getClient() + "\n\n"
					+ "Best regards,\nYour Team";
			logger.info("Before sending email approveOrRejectVendor");
			emailService.sendEmail(employeeEmail, subject, emailBody);
		}

		return "Vendor request has been " + (workflowStatus.equals("102") ? "Approved" : "Rejected") + " successfully.";
		}
		catch(Exception e) {
			logger.error("error in approveorrejectVendor "+ e.fillInStackTrace());
			throw new ResourceNotFoundException("Error in Approving or Rejecting Vendor");
		}
	}

	public Page<Vendor> searchVendors(
	        String vendorName,
	        String client,
	        List<String> workflowStatuses,
	        Long managerId,
	        int page,
	        int size,
	        LocalDate startDate,
	        LocalDate endDate,
	        String resource,
	        String vendorStatus) {
	    PageRequest pageable = PageRequest.of(page, size);
	    return vendorRepository.searchVendors(
	            vendorName,
	            client,
	            workflowStatuses,
	            managerId,
	            startDate,
	            endDate,
	            resource,
	            vendorStatus,
	            pageable);
	}

	public ByteArrayResource exportVendorsToExcel(String vendorName, String client, List<String> workflowStatuses,
			Long managerId, LocalDate startDate, LocalDate endDate,String vendorStatus) throws IOException {

		List<Vendor> vendors = vendorRepository.searchVendorsWithoutPagination(vendorName, client, workflowStatuses,
				managerId, startDate, endDate,vendorStatus);

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("Vendors");

			// Header
			Row headerRow = sheet.createRow(0);
			String[] headers = { "Vendor Name", "Client", "Resource", "Contract Type", "Client Rate", "SSIT Rate", "Margin Rate",
					"Start Date", "End Date", "Remarks", "Status" };
			for (int i = 0; i < headers.length; i++) {
				headerRow.createCell(i).setCellValue(headers[i]);
			}

			// Optional: Use a consistent date format
			CreationHelper createHelper = workbook.getCreationHelper();
			CellStyle dateCellStyle = workbook.createCellStyle();
			dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));

			// Data Rows
			int rowNum = 1;
			for (Vendor v : vendors) {
				Row row = sheet.createRow(rowNum++);

				row.createCell(0).setCellValue(v.getVendorName() != null ? v.getVendorName() : "");
				row.createCell(1).setCellValue(v.getClient() != null ? v.getClient() : "");
				row.createCell(2).setCellValue(v.getResource() != null ? v.getResource() : "");
				row.createCell(3).setCellValue(v.getContractType() != null ? v.getContractType() : "");

				// Handle null for numeric values
				row.createCell(4).setCellValue(v.getClientRate() != null ? v.getClientRate().toString() : "");
				row.createCell(5).setCellValue(v.getSsitRate() != null ? v.getSsitRate().toString() : "");
				row.createCell(6).setCellValue(v.getRateMargin() != null ? v.getRateMargin().toString() : "");
				

				// Handle dates with formatting
				Cell startDateCell = row.createCell(7);
				if (v.getStartDate() != null) {
					startDateCell.setCellValue(v.getStartDate());
					startDateCell.setCellStyle(dateCellStyle);
				} else {
					startDateCell.setCellValue("");
				}

				Cell endDateCell = row.createCell(8);
				if (v.getEndDate() != null) {
					endDateCell.setCellValue(v.getEndDate());
					endDateCell.setCellStyle(dateCellStyle);
				} else {
					endDateCell.setCellValue("");
				}
				row.createCell(9).setCellValue(v.getRemarks()!= null ? v.getRemarks().toString():"");
				row.createCell(10).setCellValue(v.getVendorStatus()!= null ? v.getVendorStatus().toString():"");
			}

			// Auto-size all columns
			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}

			workbook.write(out);
			return new ByteArrayResource(out.toByteArray());
		}
	}
	
	public boolean deleteVendorById(Long id) {
        if (vendorRepository.existsById(id)) {
            vendorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
