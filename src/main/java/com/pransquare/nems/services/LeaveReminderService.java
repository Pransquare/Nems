package com.pransquare.nems.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.entities.EmployeeLeaveDetailsConfigEntity;
import com.pransquare.nems.entities.EmployeeLeaveEntity;
import com.pransquare.nems.entities.UserApproverConfigurationsEntity;
import com.pransquare.nems.models.LeaveTypeEntity;
import com.pransquare.nems.repositories.EmployeeLeaveDetailsConfigRepository;
import com.pransquare.nems.repositories.EmployeeRepository;

import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;

@Service
public class LeaveReminderService {
	
	private static final Logger logger = LogManager.getLogger(LeaveReminderService.class);

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private EmployeeLeaveDetailsConfigRepository leaveDetailsConfigRepository;

	@Value("${timesheetEmailId}")
	private String timesheetEmailId;
	
	@Value("${loginurl}")
	private String loginurl;
	
	private  WebClient webClient;
	
	@Value("${master-config-service.url}")
	private String masterconfigurl;
	
	 @Autowired
	    private WebClient.Builder webClientBuilder;
	 
	  @PostConstruct
	    public void init() {
	        this.webClient = webClientBuilder.baseUrl(masterconfigurl).build();
	    }

	  public LeaveReminderService(WebClient.Builder webClientBuilder) {
			
			this.webClient = 	webClientBuilder.baseUrl(masterconfigurl).build();
		}

	// Runs daily at 9 AM
	@Scheduled(cron = "0 42 16 * * *")
	@Transactional
	public void processLeaveReminders() {
		logger.info("Starting leave reminder process at {}", LocalDateTime.now());
		LocalDate today = LocalDate.now();

		try {
			// Process reminders for leaves submitted 2 days ago
			processReminders(today);

			// Process auto-approvals for leaves submitted 3 days ago
			processAutoApprovals(today);
			logger.info("Completed leave reminder process successfully at {}", LocalDateTime.now());
		} catch (Exception e) {
			logger.error("Error in processLeaveReminders: {}", e.fillInStackTrace());
		}
	}

	private void processReminders(LocalDate today) {
		logger.info("Processing reminders for leaves submitted on {}", today.minusDays(2));
		LocalDate submissionDate = today.minusDays(2);
		List<EmployeeLeaveEntity> pendingLeaves = findPendingLeavesForReminder(submissionDate);

		logger.info("Found {} pending leaves for reminder", pendingLeaves.size());
		for (EmployeeLeaveEntity leave : pendingLeaves) {
			try {
				sendManagerReminder(leave);
			} catch (Exception e) {
				logger.error("Failed to process reminder for leave ID {}: {}", leave.getEmpLeaveId(), e.fillInStackTrace());
			}
		}
	}

	private void processAutoApprovals(LocalDate today) {
		logger.info("Processing auto-approvals for leaves submitted on {}", today.minusDays(3));
		LocalDate submissionDate = today.minusDays(3);
		List<EmployeeLeaveEntity> pendingLeaves = findPendingLeavesForAutoApproval(submissionDate);

		logger.info("Found {} pending leaves for auto-approval", pendingLeaves.size());
		for (EmployeeLeaveEntity leave : pendingLeaves) {
			try {
				autoApproveLeave(leave);
			} catch (Exception e) {
				logger.error("Failed to process auto-approval for leave ID {}: {}", leave.getEmpLeaveId(), e.fillInStackTrace());
			}
		}
	}

	private List<EmployeeLeaveEntity> findPendingLeavesForReminder(LocalDate submissionDate) {
		try {
			return entityManager.createQuery(
				"SELECT l FROM EmployeeLeaveEntity l " +
				"WHERE DATE(l.createdDate) = :submissionDate " +
				"AND l.status = '101' " + // Pending status
				"AND (l.isReminderSent IS NULL OR l.isReminderSent = false)", 
				EmployeeLeaveEntity.class)
				.setParameter("submissionDate", submissionDate)
				.getResultList();
		} catch (Exception e) {
			logger.error("Error fetching pending leaves for reminder on {}: {}", submissionDate, e.fillInStackTrace());
			return List.of(); // Return empty list to prevent further processing
		}
	}

	private List<EmployeeLeaveEntity> findPendingLeavesForAutoApproval(LocalDate submissionDate) {
		try {
			return entityManager.createQuery(
				"SELECT l FROM EmployeeLeaveEntity l " +
				"WHERE DATE(l.createdDate) = :submissionDate " +
				"AND l.status = '101' " + // Pending status
				"AND l.isReminderSent = true " +
				"AND DATE(l.reminderSentDate) = :yesterday", 
				EmployeeLeaveEntity.class)
				.setParameter("submissionDate", submissionDate)
				.setParameter("yesterday", LocalDate.now().minusDays(1))
				.getResultList();
		} catch (Exception e) {
			logger.error("Error fetching pending leaves for auto-approval on {}: {}", submissionDate, e.fillInStackTrace());
			return List.of(); // Return empty list to prevent further processing
		}
	}

	private UserApproverConfigurationsEntity getLeaveApproverConfig(Long employeeId) {
		try {
			logger.debug("Fetching leave approver config for employee ID: {}", employeeId);
			UserApproverConfigurationsEntity config = entityManager.createQuery(
				"SELECT u FROM user_approver_configurations u " +
				"WHERE u.empBasicDetailId = :employeeId " +
				"AND u.moduleName = 'leave'",
				UserApproverConfigurationsEntity.class)
				.setParameter("employeeId", employeeId)
				.getSingleResult();
			logger.debug("Found leave approver config for employee ID: {}", employeeId);
			return config;
		} catch (Exception e) {
			logger.warn("No leave approver config found for employee ID {}: {}", employeeId, e.fillInStackTrace());
			return null;
		}
	}

	private void sendManagerReminder(EmployeeLeaveEntity leave) {
		logger.info("Sending reminder for leave ID: {}", leave.getEmpLeaveId());
		UserApproverConfigurationsEntity approverConfig = getLeaveApproverConfig(leave.getEmployeeId());
		if (approverConfig == null || approverConfig.getApproverId() == null) {
			logger.warn("No approver configured for employee ID: {} for leave ID: {}", 
				leave.getEmployeeId(), leave.getEmpLeaveId());
			return;
		}

		EmployeeEntity manager = employeeRepository.findByEmployeeBasicDetailId(approverConfig.getApproverId());
		EmployeeEntity employee = employeeRepository.findByEmployeeBasicDetailId(leave.getEmployeeId());

		if (manager == null || employee == null) {
			logger.warn("Manager or employee not found for leave ID: {}. Manager ID: {}, Employee ID: {}", 
				leave.getEmpLeaveId(), approverConfig.getApproverId(), leave.getEmployeeId());
			return;
		}

		String subject = "Leave Approval Reminder - Action Required";
		String body = buildReminderEmailBody(employee, manager, leave);

		sendEmail(manager.getEmailId(), subject, body, null);

		// Update leave entity with reminder details
		leave.setReminderSentDate(LocalDateTime.now());
		leave.setIsReminderSent(true);
		leave.setModifiedDate(LocalDateTime.now());
		leave.setRemarks((leave.getRemarks() != null ? leave.getRemarks() : "") + 
			"\nReminder sent to manager on " + LocalDateTime.now());
		entityManager.merge(leave);
		logger.info("Reminder sent and leave updated for leave ID: {}", leave.getEmpLeaveId());
	}

	private void autoApproveLeave(EmployeeLeaveEntity leave) {
	    logger.info("Auto-approving leave ID: {}", leave.getEmpLeaveId());
	    try {
	        // Auto-approve the leave
	        leave.setStatus("102"); // Approved status
	        leave.setWorkflowStatus("APPROVED");
	        leave.setModifiedDate(LocalDateTime.now());
	        leave.setRemarks((leave.getRemarks() != null ? leave.getRemarks() : "") + 
	            "\nAuto-approved on " + LocalDateTime.now());
	        entityManager.merge(leave);
	        logger.info("Leave ID: {} auto-approved successfully", leave.getEmpLeaveId());

	        // UPDATE CONFIG LOGIC
	        Optional<EmployeeLeaveDetailsConfigEntity> optionalConfig =
	            leaveDetailsConfigRepository.findByEmployeeBasicDetailsIdAndLeaveCode(
	                leave.getEmployeeId(), leave.getLeaveType());

	        if (optionalConfig.isPresent()) {
	            EmployeeLeaveDetailsConfigEntity config = optionalConfig.get();
	            Float leaveDays = leave.getNoOfDays() != null ? leave.getNoOfDays() : 0.0f;

	            // Adjust the balances
	            config.setPending(safeSubtract(config.getPending(), leaveDays));
	            config.setUsed(safeAdd(config.getUsed(), leaveDays));
	            config.setModifiedDate(LocalDate.now());
	            config.setModifiedBy("AutoApproval");

	            leaveDetailsConfigRepository.save(config);
	            logger.info("Leave details config updated for employeeId: {} and leaveCode: {}",
	                leave.getEmployeeId(), leave.getLeaveType());
	        } else {
	            logger.warn("No leave config found for employeeId={} and leaveCode={}",
	                leave.getEmployeeId(), leave.getLeaveType());
	        }

	        // Notify employee with managers in CC
	        sendAutoApprovalNotification(leave);
	    } catch (Exception e) {
	        logger.error("Failed to auto-approve leave ID {}: {}", leave.getEmpLeaveId(), e.fillInStackTrace());
	    }
	}

	// Helper methods for null-safe arithmetic
	private Float safeAdd(Float a, Float b) {
	    return (a == null ? 0 : a) + (b == null ? 0 : b);
	}

	private Float safeSubtract(Float a, Float b) {
	    return (a == null ? 0 : a) - (b == null ? 0 : b);
	}


	private UserApproverConfigurationsEntity getJobManagerConfig(Long managerId) {
		try {
			logger.debug("Fetching jobManager config for manager ID: {}", managerId);
			UserApproverConfigurationsEntity config = entityManager.createQuery(
				"SELECT u FROM user_approver_configurations u " +
				"WHERE u.empBasicDetailId = :managerId " +
				"AND u.moduleName = 'jobManager'",
				UserApproverConfigurationsEntity.class)
				.setParameter("managerId", managerId)
				.getSingleResult();
			logger.debug("Found jobManager config for manager ID: {}", managerId);
			return config;
		} catch (Exception e) {
			logger.warn("No jobManager config found for manager ID {}: {}", managerId, e.fillInStackTrace());
			return null;
		}
	}

	private void sendAutoApprovalNotification(EmployeeLeaveEntity leave) {
		logger.info("Sending auto-approval notification for leave ID: {}", leave.getEmpLeaveId());
		UserApproverConfigurationsEntity approverConfig = getLeaveApproverConfig(leave.getEmployeeId());
		if (approverConfig == null || approverConfig.getApproverId() == null) {
			logger.warn("No approver configured for employee ID: {} for leave ID: {}", 
				leave.getEmployeeId(), leave.getEmpLeaveId());
			return;
		}

		EmployeeEntity employee = employeeRepository.findByEmployeeBasicDetailId(leave.getEmployeeId());
		EmployeeEntity manager = employeeRepository.findByEmployeeBasicDetailId(approverConfig.getApproverId());

		if (employee == null || manager == null) {
			logger.warn("Employee or manager not found for leave ID: {}. Employee ID: {}, Manager ID: {}", 
				leave.getEmpLeaveId(), leave.getEmployeeId(), approverConfig.getApproverId());
			return;
		}

		// Get skip-level manager (manager's manager) from jobManager module
		EmployeeEntity skipLevelManager = null;
		UserApproverConfigurationsEntity managerConfig = getJobManagerConfig(manager.getEmployeeBasicDetailId());
		if (managerConfig != null && managerConfig.getApproverId() != null) {
			skipLevelManager = employeeRepository.findByEmployeeBasicDetailId(managerConfig.getApproverId());
			if (skipLevelManager == null) {
				logger.warn("Skip-level manager not found for ID: {} for leave ID: {}", 
					managerConfig.getApproverId(), leave.getEmpLeaveId());
			}
		}

		String subject = "Leave Request Auto-Approved Notification";
		String body = buildAutoApprovalEmailBody(leave, employee, manager, skipLevelManager);

		String[] ccList;
		if (skipLevelManager != null && skipLevelManager.getEmailId() != null) {
			ccList = new String[]{manager.getEmailId(), skipLevelManager.getEmailId()};
		} else {
			ccList = new String[]{manager.getEmailId()};
		}

		sendEmail(employee.getEmailId(), subject, body, ccList);
	}

	private String buildReminderEmailBody(EmployeeEntity employee, EmployeeEntity manager, EmployeeLeaveEntity leave) {
		ResponseEntity<String> leaveType=	getLeaveTypeDescription(leave.getLeaveType());
		String leaveTypeDescription = leaveType.getBody();
		return "<html>" + 
			"<body>" + 
			"<p>Dear " + manager.getFullName() + ",</p>" +
			"<p>This is a reminder to approve a pending leave request from " + employee.getFullName() + " (" +
			employee.getEmployeeCode() + "):</p>" + 
			"<p>Leave Type: " + leaveTypeDescription + "</p>" +
			"<p>From: " + leave.getLeaveFrom() + " To: " + leave.getLeaveTo() + "</p>" + 
			"<p>Reason: " + leave.getReason() + "</p>" + 
			"<p>Submitted on: " + leave.getCreatedDate().toLocalDate() + "</p>" +
			"<p>Please take action within 24 hours, or it will be auto-approved.</p>" +
			"<p><a href=" + loginurl + ">Login to approve</a></p>" + 
			"</body></html>";
	}

	private String buildAutoApprovalEmailBody(EmployeeLeaveEntity leave, EmployeeEntity employee,
			EmployeeEntity manager, EmployeeEntity skipLevelManager) {
	ResponseEntity<String> leaveType=	getLeaveTypeDescription(leave.getLeaveType());
	String leaveTypeDescription = leaveType.getBody();
		return "<html>" + 
			"<body>" + 
			"<p>Dear " + employee.getFullName() + ",</p>" +
			"<p>Your leave request has been auto-approved due to no response from your manager " +
			manager.getFullName() + ":</p>" + 
			"<p>Leave Type: " + leaveTypeDescription+ "</p>" + 
			"<p>From: " + leave.getLeaveFrom() + " To: " + leave.getLeaveTo() + "</p>" + 
			"<p>Reason: " + leave.getReason() + "</p>" + 
			"<p>Submitted on: " + leave.getCreatedDate().toLocalDate() + "</p>" + 
			"<p>Auto-approved on: " + LocalDateTime.now() + "</p>" + 
			"<p>Note: Your manager " + manager.getFullName() +
			(skipLevelManager != null ? " and " + skipLevelManager.getFullName() : "") +
			" have been CC'd on this email for their information.</p>" + 
			"</body></html>";
	}

	private void sendEmail(String toEmail, String subject, String body, String[] ccList) {
		try {
			logger.debug("Sending email to: {} with subject: {}", toEmail, subject);
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setFrom(timesheetEmailId); // Using configured email ID
			helper.setTo(toEmail);
			helper.setSubject(subject);
			helper.setText(body, true);
			if (ccList != null && ccList.length > 0) {
				helper.setCc(ccList);
				logger.debug("CC list added: {}", String.join(", ", ccList));
			}
			mailSender.send(mimeMessage);
			logger.info("Email sent successfully to: {}", toEmail);
		} catch (Exception e) {
			logger.error("Failed to send email to {}: {}", toEmail, e.fillInStackTrace());
		}
	}
	 public ResponseEntity<LeaveTypeEntity> dedupeCheckWithLeaveTypeCode(String leaveTypeCode) {
	        if (leaveTypeCode == null || leaveTypeCode.isEmpty()) {
	            return ResponseEntity.badRequest().body(null);
	        }

	        try {
	            LeaveTypeEntity result = webClient.get()
	                .uri("/pransquare/MasterConfiguration/leaveType/dedupeCheck/{leaveTypeCode}", leaveTypeCode)
	                .retrieve()
	                .bodyToMono(LeaveTypeEntity.class)
	                .block(); // Blocking call for simplicity

	            return ResponseEntity.ok(result);
	        } catch (WebClientResponseException e) {
	            System.err.println("WebClient error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
	            return ResponseEntity.status(e.getStatusCode()).body(null);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(500).body(null);
	        }
	    }
	 
	 public ResponseEntity<String> getLeaveTypeDescription(String leaveTypeCode) {
		    if (leaveTypeCode == null || leaveTypeCode.isEmpty()) {
		        return ResponseEntity.badRequest().body("Leave type code is missing");
		    }

		    try {
		        LeaveTypeEntity result = webClient.get()
		            .uri("/pransquare/MasterConfiguration/leaveType/dedupeCheck/{leaveTypeCode}", leaveTypeCode)
		            .retrieve()
		            .bodyToMono(LeaveTypeEntity.class)
		            .block(); // Blocking for simplicity

		        if (result != null && result.getLeaveTypeDescription() != null) {
		            return ResponseEntity.ok(result.getLeaveTypeDescription());
		        } else {
		            return ResponseEntity.status(404).body("Leave type description not found");
		        }
		    } catch (WebClientResponseException e) {
		        System.err.println("WebClient error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
		        return ResponseEntity.status(e.getStatusCode()).body("WebClient error occurred");
		    } catch (Exception e) {
		        e.printStackTrace();
		        return ResponseEntity.status(500).body("Internal server error");
		    }
		}

	
}
