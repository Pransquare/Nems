package com.pransquare.nems.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.entities.EmployeeTdsDetailsView;
import com.pransquare.nems.entities.GroupEmailEntity;
import com.pransquare.nems.entities.TdsDetails;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.repositories.EmployeeRepository;
import com.pransquare.nems.repositories.EmployeeTdsDetailsViewRepository;
import com.pransquare.nems.repositories.GroupEmailRepository;
import com.pransquare.nems.repositories.TdsDetailsRepository;
import com.pransquare.nems.utils.IntegerUtils;
import com.pransquare.nems.utils.StringUtil;

import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

@Service
public class TdsDetailsService {

	@Value("${timesheetEmailId}")
	private String timesheetEmailId;

	private TdsDetailsRepository tdsDetailsRepository;

	private EmployeeRepository employeeRepository;

	private JavaMailSender mailSender;

	private GroupEmailRepository groupEmailRepository;

	public TdsDetailsService(TdsDetailsRepository tdsDetailsRepository,
			EmployeeRepository employeeRepository,
			JavaMailSender mailSender, GroupEmailRepository groupEmailRepository) {
		this.tdsDetailsRepository = tdsDetailsRepository;
		this.employeeRepository = employeeRepository;
		this.mailSender = mailSender;
		this.groupEmailRepository = groupEmailRepository;
	}

	public Page<TdsDetails> getAllEmployeeTdsDetails(Pageable pageable) {
		Pageable sortedByCreatedDateDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(Sort.Direction.DESC, "createdDate"));
		return tdsDetailsRepository.findAll(sortedByCreatedDateDesc);
	}

	public List<TdsDetails> saveTdsDetails(List<TdsDetails> tdsDetailsList) {
		List<TdsDetails> tdsDetails = tdsDetailsRepository.saveAll(tdsDetailsList);
		String empCode = tdsDetailsList.get(0).getEmployeeCode();
		System.out.println("Searching employeeCode: '" + empCode + "'");
		EmployeeEntity e = employeeRepository.findByEmployeeCode(tdsDetailsList.get(0).getEmployeeCode());

		e.setIsTaxdeclarationEnabled(0);
		
		employeeRepository.save(e);
		// Email to the Hr
		tdsEmails(tdsDetailsList.get(0).getEmployeeCode(), "125");



		return tdsDetails;
	}

	// Update multiple TDS details
	public List<TdsDetails> updateTdsDetails(List<TdsDetails> tdsDetailsList) {
		// First, we need to check if the IDs exist in the database
		for (TdsDetails tdsDetails : tdsDetailsList) {
			Optional<TdsDetails> existingTds = tdsDetailsRepository.findById(tdsDetails.getId());
			if (existingTds.isEmpty()) {
				throw new IllegalArgumentException("TDS Details with ID " + tdsDetails.getId() + " not found");
			}
		}

		// Email notification to the Employee
		TdsDetails tdsDetails = tdsDetailsList.get(0);
		tdsEmails(tdsDetails.getEmployeeCode(), tdsDetails.getStatus());

		// Save the updated TDS details
		return tdsDetailsRepository.saveAll(tdsDetailsList);
	}

	// Get TDS details by ID
	public List<TdsDetails> getTdsDetailsByEmployeeCode(String employeeCode) {
		return tdsDetailsRepository.findByEmployeeCode(employeeCode);
	}

	public static Specification<EmployeeTdsDetailsView> hasEmployeeCode(String employeeCode) {
		return (root, query, cb) -> {
			if (employeeCode == null || employeeCode.isEmpty()) {
				return cb.conjunction(); // Matches all if employeeCode is null
			}
			return cb.equal(root.get("employeeCode"), employeeCode);
		};
	}

	public static Specification<EmployeeTdsDetailsView> hasFinancialYearCode(String financialYearCode) {
		return (root, query, cb) -> {
			if (financialYearCode == null || financialYearCode.isEmpty()) {
				return cb.conjunction(); // Matches all if financialYearCode is null
			}
			return cb.equal(root.get("financialYearCode"), financialYearCode);
		};
	}

	@Autowired
	private EmployeeTdsDetailsViewRepository repository;

	public Page<EmployeeTdsDetailsView> search(String employeeCode, String financialYearCode, Pageable pageable) {
		Specification<EmployeeTdsDetailsView> spec = Specification.where(hasEmployeeCode(employeeCode))
				.and(hasFinancialYearCode(financialYearCode));

		return repository.findAll(spec, pageable);
	}

	@Transactional
	public ResponseEntity<String> enableProofDeclarationForAllActiveEmployees() {
		if (employeeRepository.enableProofDeclarationForAllActiveEmployees() > 0) {
			for (EmployeeEntity employee : employeeRepository.findByStatus("108")) {
				tdsDetailsRepository.updateTdsStatus(employee.getEmployeeCode(), "126");
			}
			tdsEmails(null, "common");
			return ResponseEntity.ok().body("Proof Declaration enabled successfully");
		}
		return ResponseEntity.badRequest().body("Error in enabling Proof Declaration");
	}

	@Transactional
	public ResponseEntity<String> enableProofDeclarationForEmployee(String employeeCode) {
		if (employeeRepository.enableProofDeclarationforEmployee(employeeCode) > 0) {
			tdsDetailsRepository.updateTdsStatus(employeeCode, "126");
			// Email notification to the employee
			tdsEmails(employeeCode, "126");
			return ResponseEntity.ok().body("Proof Declaration enabled successfully");
		}
		return ResponseEntity.badRequest().body("Error in enabling Proof Declaration");
	}

	@Transactional
	public ResponseEntity<String> updateTdsStatus(String employeeCode, String status) {
		if (tdsDetailsRepository.updateTdsStatus(employeeCode, status) > 0) {
			// Email notification to the employee
			EmployeeEntity employeeEntity = employeeRepository.findByEmployeeCode(employeeCode);
			employeeEntity.setIsProofdeclarationEnabled(0);
			employeeRepository.save(employeeEntity);
			tdsEmails(employeeCode, status);
			return ResponseEntity.ok().body("TDS Status updated successfully");
		}
		return ResponseEntity.badRequest().body("Error in updating TDS Status");
	}

	public String tdsEmails(String employeeCode, String status) {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
			String emailBody = "";
			String subject = "";
			String recipientEmail = "";
			EmployeeEntity employee = new EmployeeEntity();

			// Fetch employee and HR details based on employeeCode
			if (Boolean.TRUE.equals(StringUtil.isNotNull(employeeCode))) {
				employee = employeeRepository.findByEmployeeCode(employeeCode);
				if (employee == null) {
					throw new ResourceNotFoundException("Employee not found with code: " + employeeCode);
				}

			}

			String employeeEmail = employee.getEmailId();
			String hrEmail = groupEmailRepository.findByGroupName("hr").getEmailId();

			switch (status) {
				case "127":
					// Proof submission Email notification to the HR from Employee
					subject = "TDS Proof Submission by Employee";
					emailBody = "Dear HR,\n\nEmployee with code " + employeeCode +
							" has submitted the required TDS proof documents. Please review the submitted documents at your earliest convenience.\n\nRegards,\nSystem";
					recipientEmail = hrEmail;
					break;

				case "102":
					// Proof approval Email notification to the Employee
					subject = "TDS Proof Approved";
					emailBody = "Dear " + employee.getFullName()
							+ ",\n\nYour TDS proof submission has been approved. Thank you for your prompt response.\n\nRegards,\nHR Team";
					recipientEmail = employeeEmail;
					break;

				case "125":
					// Employee TDS Declaration Email notification to the HR
					subject = "TDS Declaration Submitted by Employee";
					emailBody = "Dear HR,\n\nEmployee with code " + employeeCode +
							" has completed their TDS declaration for the financial year. Please review the details.\n\nRegards,\nSystem";
					recipientEmail = hrEmail;
					break;

				case "126":
					// Employee TDS Submission enabled Email notification to the Employee
					subject = "TDS Submission Enabled";
					emailBody = "Dear " + employee.getFullName()
							+ ",\n\nThe portal is now enabled for your TDS submission. Please log in and complete the process at your earliest convenience.\n\nRegards,\nHR Team";
					recipientEmail = employeeEmail;
					break;
				case "common":
					// Common Email to All Active employees for TDS Declaration
					subject = "TDS Submission Enabled";
					emailBody = "Dear Employee,\n\nThe portal is now enabled for your TDS submission. Please log in and complete the process at your earliest convenience.\n\nRegards,\nHR Team";
					recipientEmail = groupEmailRepository.findByGroupName("common").getEmailId();
					break;

				default:
					return "Invalid status code";
			}

			// Send the email
			helper.setFrom(timesheetEmailId);
			helper.setTo(recipientEmail);
			helper.setSubject(subject);
			helper.setText(emailBody);
			mailSender.send(mimeMessage);
			return "Email sent successfully to " + recipientEmail;

		} catch (Exception e) {
			throw new ResourceNotFoundException("Failed to send email: " + e.getMessage());
		}
	}
	@Transactional
	public ResponseEntity<String> enableTaxDeclarationForAllActiveEmployees() {
		if (employeeRepository.enableTaxDeclarationForAllActiveEmployees() > 0) {
			for (EmployeeEntity employee : employeeRepository.findByStatus("108")) {
				tdsDetailsRepository.updateTdsStatus(employee.getEmployeeCode(), "126");
			}
			tdsEmails(null, "common");
			return ResponseEntity.ok().body("Tax Declaration enabled successfully");
		}
		return ResponseEntity.badRequest().body("Error in enabling Tax Declaration");
	}
	@Transactional
	public ResponseEntity<String> enableTaxDeclarationForEmployee(String employeeCode) {
		if (employeeRepository.enableTaxDeclarationforEmployee(employeeCode) > 0) {
			tdsDetailsRepository.updateTdsStatus(employeeCode, "126");
			// Email notification to the employee
			tdsEmails(employeeCode, "126");
			return ResponseEntity.ok().body("Tax Declaration enabled successfully");
		}
		return ResponseEntity.badRequest().body("Error in enabling Tax Declaration");
	}
}