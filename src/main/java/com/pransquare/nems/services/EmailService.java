
package com.pransquare.nems.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.entities.EmployeeProjectConfigEntity;
import com.pransquare.nems.entities.UserApproverConfigurationsEntity;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.models.EmailModel;
import com.pransquare.nems.models.EmployeeLeaveModel;
import com.pransquare.nems.repositories.ApproverConfigRepository;
import com.pransquare.nems.repositories.EmployeeRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Value("${timesheetEmailId}")
	private String mailId;

	@Value("${loginurl}")
	private String loginurl;

	public static final String UTF_8 = "UTF-8";
	public static final String MAIL_SENT_SUCCESS = "Mail sent successfully to {}";
	public static final String FAILED_TO_SEND_EMAIL = "Failed to send email to {}: {}";
	public static final String FAILED_TO_SEND_EMAIL_MESSAGE = "Failed to send email";
	public static final String UNEXPECTED_ERROR_OCCURRED = "An unexpected error occurred: {}";
	public static final String UNEXPECTED_ERROR_SENDING_EMAIL = "Unexpected error occurred while sending email";

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	private JavaMailSender mailSender;

	private ApproverConfigRepository userApproverConfigurationRepository;

	private EmployeeRepository employeeRepository;

	public EmailService(JavaMailSender mailSender, ApproverConfigRepository userApproverConfigurationRepository,
			EmployeeRepository employeeRepository) {
		this.mailSender = mailSender;
		this.userApproverConfigurationRepository = userApproverConfigurationRepository;
		this.employeeRepository = employeeRepository;
	}


	public void sendEmail(String to, String subject, String body) {
		// SimpleMailMessage message = new SimpleMailMessage();
		// message.setFrom(mailId);
		// message.setTo(to);
		// message.setSubject(subject);
		// message.setText(body);
		// mailSender.send(message);

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(mimeMessage, true, UTF_8);

			helper.setFrom(mailId);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(body, true); // true indicates that the body is HTML
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendSimpleEmail(EmailModel emailModel) {

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("hr@pransquare.in"); // Replace with your email
			message.setTo(emailModel.getToEmail());
			message.setSubject(emailModel.getSubject());
			message.setText(emailModel.getBody());
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, UTF_8);
			helper.setFrom("hr@pransquare.in");
			helper.setTo(emailModel.getToEmail());
			helper.setSubject(emailModel.getSubject());
			helper.setText(emailModel.getBody(), true); // true indicates that the body is HTML
			mailSender.send(mimeMessage);

			// mailSender.send(message);

			logger.info(MAIL_SENT_SUCCESS, emailModel.getToEmail());
		} catch (MailException e) {
			logger.error(FAILED_TO_SEND_EMAIL, emailModel.getToEmail(), e.fillInStackTrace());

		} catch (Exception e) {
			logger.error(UNEXPECTED_ERROR_OCCURRED, e.getMessage(),e.fillInStackTrace());

		}
	}

	public void sendTimesheetMailRequest(EmailModel emailModel) {
		String body = "<html lang=\"en\">\r\n" + "<head>\r\n" + "    <meta charset=\"UTF-8\"></meta>\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></meta>\r\n"
				+ "    <title>Include URL Example</title>\r\n" + "<body>\r\n" + "    <p>Dear "
				+ emailModel.getAddressTo() + "</p>\r\n" + "    <p>You have a Timesheet Approval request from "
				+ emailModel.getRequestor() + "(" + emailModel.getEmployeeCode() + ")" + "</p>\r\n"
				+ "    <p>Click here to view the Timesheet request</p>\r\n"
				+ "    <a href=" + loginurl + " target=\"_blank\">PranNexus login</a>\r\n"
				+ "    <p>Thank you!</p>\r\n" + "</body>\r\n" + "</html>";
		emailModel.setBody(body);
		logger.info("emailModel {}", emailModel);
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("hr@pransquare.in"); // Replace with your email
			message.setTo(emailModel.getToEmail());
			message.setSubject(emailModel.getSubject());
			message.setText(emailModel.getBody());
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, UTF_8);
			helper.setFrom("hr@pransquare.in");
			helper.setTo(emailModel.getToEmail());
			helper.setSubject(emailModel.getSubject());
			helper.setText(emailModel.getBody(), true); // true indicates that the body is HTML
			mailSender.send(mimeMessage);

			logger.info(MAIL_SENT_SUCCESS, emailModel.getToEmail());
		} catch (MailException e) {
			logger.error(FAILED_TO_SEND_EMAIL, emailModel.getToEmail(), e.getMessage());
		} catch (Exception e) {
			logger.error(UNEXPECTED_ERROR_OCCURRED, e.getMessage());
		}
	}

	// public void sendLeaveRequestMail(EmployeeLeaveModel employeeLeaveModel) {
	// Long employeeId = employeeLeaveModel.getEmployeeId();
	// EmailModel emailModel = new EmailModel();
	// UserApproverConfigurationsEntity approverConfigurationEntity =
	// userApproverConfigurationRepository
	// .findByEmpBasicDetailIdAndModuleName(employeeId, "leave");
	//
	// if (approverConfigurationEntity != null) {
	// Long approver = approverConfigurationEntity.getApproverId();
	// Optional<EmployeeEntity> approverEntity =
	// employeeRepository.findById(approver);
	//
	// // Check if the approver entity is present
	// if (approverEntity.isPresent()) {
	// emailModel.setAddressTo(approverEntity.get().getFullName());
	// emailModel.setToEmail(approverEntity.get().getEmailId());
	// emailModel.setSubject("Leave Approval Request");
	//
	// Optional<EmployeeEntity> employeeEntity =
	// employeeRepository.findById(employeeId);
	//
	// // Check if the employee entity is present before accessing its fields
	// if (employeeEntity.isPresent()) {
	// emailModel.setRequestor(employeeEntity.get().getFullName());
	// emailModel.setEmployeeCode(employeeEntity.get().getEmployeeCode());
	//
	// // Compose the email body with text and an HTML link
	// String body = "<html lang=\"en\">\r\n" + "<head>\r\n"
	// + " <meta charset=\"UTF-8\"></meta>\r\n"
	// + " <meta name=\"viewport\" content=\"width=device-width,
	// initial-scale=1.0\"></meta>\r\n"
	// + " <title>Include URL Example</title>\r\n" + "<body>\r\n"
	// + " <p>Dear " + emailModel.getAddressTo() + "</p>\r\n"
	// + " <p>You have a leave request from " + emailModel.getRequestor() + "("
	// + emailModel.getEmployeeCode() + ") for the reason " +
	// employeeLeaveModel.getReason()
	// + " from " + employeeLeaveModel.getLeaveFrom() + " till "
	// + employeeLeaveModel.getLeaveTo() + "</p>\r\n"
	// + " <p>Thank you!</p>\r\n" + "</body>\r\n" + "</html>";
	//
	// emailModel.setBody(body);
	//
	// try {
	// SimpleMailMessage message = new SimpleMailMessage();
	// message.setFrom("timesheets@sigmasoft.ai"); // Replace with your email
	// message.setTo(emailModel.getToEmail());
	// message.setSubject(emailModel.getSubject());
	// message.setText(emailModel.getBody());
	// MimeMessage mimeMessage = mailSender.createMimeMessage();
	// MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, UTF_8);
	// helper.setFrom("timesheets@sigmasoft.ai");
	// helper.setTo(emailModel.getToEmail());
	// helper.setSubject(emailModel.getSubject());
	// helper.setText(emailModel.getBody(), true); // true indicates that the body
	// is HTML
	// mailSender.send(mimeMessage);
	//
	// logger.info(MAIL_SENT_SUCCESS, emailModel.getToEmail());
	// } catch (MailException e) {
	// logger.error(FAILED_TO_SEND_EMAIL, emailModel.getToEmail(), e.getMessage());
	// throw new RuntimeException(FAILED_TO_SEND_EMAIL_MESSAGE, e);
	// } catch (Exception e) {
	// logger.error(UNEXPECTED_ERROR_OCCURRED, e.getMessage());
	// throw new RuntimeException(UNEXPECTED_ERROR_SENDING_EMAIL, e);
	// }
	// } else {
	// throw new ResourceNotFoundException("Employee not found with ID: " +
	// employeeId);
	// }
	// } else {
	// throw new ResourceNotFoundException("Approver not found with ID: " +
	// approver);
	// }
	// }
	// }

	public void sendLeaveRequestMail(EmployeeLeaveModel employeeLeaveModel) {
		Long employeeId = employeeLeaveModel.getEmployeeId();
		EmailModel emailModel = new EmailModel();
		UserApproverConfigurationsEntity approverConfigurationEntity = userApproverConfigurationRepository
				.findByEmpBasicDetailIdAndModuleName(employeeId, "leave");

		if (approverConfigurationEntity == null) {
			throw new ResourceNotFoundException("Approver not found for Employee ID: " + employeeId);
		}

		Long approverId = approverConfigurationEntity.getApproverId();
		EmployeeEntity approverEntity = employeeRepository.findById(approverId)
				.orElseThrow(() -> new ResourceNotFoundException("Approver not found with ID: " + approverId));

		emailModel.setAddressTo(approverEntity.getFullName());
		emailModel.setToEmail(approverEntity.getEmailId());
		emailModel.setSubject("Leave Approval Request");

		EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

		emailModel.setRequestor(employeeEntity.getFullName());
		emailModel.setEmployeeCode(employeeEntity.getEmployeeCode());

		String body = "<html lang=\"en\">\r\n" + "<head>\r\n" + "    <meta charset=\"UTF-8\"></meta>\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></meta>\r\n"
				+ "    <title>Leave Request</title>\r\n" + "<body>\r\n" + "    <p>Dear " + emailModel.getAddressTo()
				+ "</p>\r\n" + "    <p>You have a leave request from " + emailModel.getRequestor() + " ("
				+ emailModel.getEmployeeCode() + ") for the reason " + employeeLeaveModel.getReason() + " from "
				+ employeeLeaveModel.getLeaveFrom() + " till " + employeeLeaveModel.getLeaveTo() + ".</p>\r\n"
				+ "    <p>Click here to view the leave request</p>\r\n"
				+ "    <a href=" + loginurl + " target=\"_blank\">PransNexus login</a>\r\n"
				+ "    <p>Thank you!</p>\r\n" + "</body>\r\n" + "</html>";

		emailModel.setBody(body);

		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, UTF_8);
			helper.setFrom("hr@pransquare.in");
			helper.setTo(emailModel.getToEmail());
			helper.setSubject(emailModel.getSubject());
			helper.setText(emailModel.getBody(), true); // true indicates that the body is HTML
			mailSender.send(mimeMessage);

			logger.info(MAIL_SENT_SUCCESS, emailModel.getToEmail());
		} catch (MailException e) {
			logger.error(FAILED_TO_SEND_EMAIL, emailModel.getToEmail(), e.getMessage());
		} catch (Exception e) {
			logger.error(UNEXPECTED_ERROR_OCCURRED, e.getMessage());
		}
	}

	public void sendLeaveApprovalMail(EmailModel emailModel) {
		// Compose the email body with text and an HTML link
		String body = "<html lang=\"en\">\r\n" + "<head>\r\n" + "    <meta charset=\"UTF-8\"></meta>\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></meta>\r\n"
				+ "    <title>Include URL Example</title>\r\n" + "<body>\r\n" + "    <p>Dear "
				+ emailModel.getRequestor() + "</p>\r\n" + "    <p>Your leave request has been approved" +
				"</p>\r\n"

				// + " <p>Click here to approve the leave request</p>\r\n"
				// + " <a href=\"https://www.javatpoint.com/python-list-vs-tuple\"
				// target=\"_blank\">Go to Example.com</a>\r\n"
				+ "    <p>Thank you!</p>\r\n" + "</body>\r\n" + "</html>";
		emailModel.setBody(body);

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("hr@pransquare.in"); // Replace with your email
			message.setTo(emailModel.getToEmail());
			message.setSubject(emailModel.getSubject());
			message.setText(emailModel.getBody());
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, UTF_8);
			helper.setFrom("hr@pransquare.in");
			helper.setTo(emailModel.getToEmail());
			helper.setSubject(emailModel.getSubject());
			helper.setText(emailModel.getBody(), true); // true indicates that the body is HTML
			mailSender.send(mimeMessage);

			logger.info(MAIL_SENT_SUCCESS, emailModel.getToEmail());
		} catch (MailException e) {
			logger.error(FAILED_TO_SEND_EMAIL, emailModel.getToEmail(), e.getMessage());
		} catch (Exception e) {
			logger.error(UNEXPECTED_ERROR_OCCURRED, e.getMessage());
		}
	}

	public void sendTimesheetApprovalMail(EmailModel emailModel) {
		String body = "<html lang=\"en\">\r\n" + "<head>\r\n" + "    <meta charset=\"UTF-8\"></meta>\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></meta>\r\n"
				+ "    <title>Include URL Example</title>\r\n" + "<body>\r\n" + "    <p>Dear "
				+ emailModel.getAddressTo() + "</p>\r\n"
				+ "    <p>Your Timesheet Approval request is Approved for the dates " + emailModel.getBody()
				+ "</p>\r\n"
				// + " <p>Click here to approve the Timesheet request</p>\r\n"
				// + " <a href=\"https://www.javatpoint.com/python-list-vs-tuple\"
				// target=\"_blank\">Go to Example.com</a>\r\n"
				+ "    <p>Thank you!</p>\r\n" + "</body>\r\n" + "</html>";
		emailModel.setBody(body);
		logger.info("emailModel {}", emailModel);
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("hr@pransquare.in"); // Replace with your email
			message.setTo(emailModel.getToEmail());
			message.setSubject(emailModel.getSubject());
			message.setText(emailModel.getBody());
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, UTF_8);
			helper.setFrom("hr@pransquare.in");
			helper.setTo(emailModel.getToEmail());
			helper.setSubject(emailModel.getSubject());
			helper.setText(emailModel.getBody(), true); // true indicates that the body is HTML
			mailSender.send(mimeMessage);

			logger.info(MAIL_SENT_SUCCESS, emailModel.getToEmail());
		} catch (MailException e) {
			logger.error(FAILED_TO_SEND_EMAIL, emailModel.getToEmail(), e.getMessage());
		} catch (Exception e) {
			logger.error(UNEXPECTED_ERROR_OCCURRED, e.getMessage());
		}
	}

	public void sendTimesheetRejectMail(EmailModel emailModel) {
		String body = "<html lang=\"en\">\r\n" + "<head>\r\n" + "    <meta charset=\"UTF-8\"></meta>\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></meta>\r\n"
				+ "    <title>Include URL Example</title>\r\n" + "<body>\r\n" + "    <p>Dear "
				+ emailModel.getAddressTo() + "</p>\r\n"
				+ "    <p>Your Timesheet Approval request is Rejected for the dates " + emailModel.getBody()
				+ "</p>\r\n"
				// + " <p>Click here to approve the Timesheet request</p>\r\n"
				// + " <a href=\"https://www.javatpoint.com/python-list-vs-tuple\"
				// target=\"_blank\">Go to Example.com</a>\r\n"
				+ "    <p>Thank you!</p>\r\n" + "</body>\r\n" + "</html>";
		emailModel.setBody(body);
		logger.info("emailModel {}", emailModel);
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("hr@pransquare.in"); // Replace with your email
			message.setTo(emailModel.getToEmail());
			message.setSubject(emailModel.getSubject());
			message.setText(emailModel.getBody());
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, UTF_8);
			helper.setFrom("hr@pransquare.in");
			helper.setTo(emailModel.getToEmail());
			helper.setSubject(emailModel.getSubject());
			helper.setText(emailModel.getBody(), true); // true indicates that the body is HTML
			mailSender.send(mimeMessage);

			logger.info(MAIL_SENT_SUCCESS, emailModel.getToEmail());
		} catch (MailException e) {
			logger.error(FAILED_TO_SEND_EMAIL, emailModel.getToEmail(), e.getMessage());
		} catch (Exception e) {
			logger.error(UNEXPECTED_ERROR_OCCURRED, e.getMessage());
		}
	}

	
	@Scheduled(cron = "${timesheetRemind}") 
	public void sendEmailForBackup() {
EmailModel emailModel=new EmailModel();
		String body = "<html lang=\"en\">\r\n" + "<head>\r\n" + "    <meta charset=\"UTF-8\"></meta>\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></meta>\r\n"
				+ "    <title>Include URL Example</title>\r\n" + "<body>\r\n" + "    <p>Dear "
				+"IT Team, " + "</p>\r\n" + "    <p>Reminder!, it's time to take weekly DB backup. </p>\r\n"


				// + " <p>Click here to approve the Timesheet request</p>\r\n"
				// + " <a href=\"https://www.javatpoint.com/python-list-vs-tuple\"
				// target=\"_blank\">Go to Example.com</a>\r\n"
				+ "    <p>Thank you!</p>\r\n" + "</body>\r\n" + "</html>";

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("hr@pransquare.in"); // Replace with your email
			message.setTo("ithelpdesk@pransquare.in");
			message.setSubject("Weekly DB backup Reminder");
			message.setText(body);
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, UTF_8);
			helper.setFrom("hr@pransquare.in");
			helper.setTo("ithelpdesk@pransquare.in");
			helper.setSubject("Weekly DB backup Reminder");
			helper.setText(body, true); // true indicates that the body is HTML
			mailSender.send(mimeMessage);

			logger.info(MAIL_SENT_SUCCESS, emailModel.getToEmail());
		} catch (MailException e) {
			logger.error(FAILED_TO_SEND_EMAIL, emailModel.getToEmail(), e.getMessage());

		} catch (Exception e) {
			logger.error(UNEXPECTED_ERROR_OCCURRED, e.getMessage());

		}
	}

	public void sendEmailForAppraisal(EmailModel emailModel, MultipartFile file) {
		String body = "<html lang=\"en\">\r\n" + "<head>\r\n" + "    <meta charset=\"UTF-8\"></meta>\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></meta>\r\n"
				+ "    <title>Include URL Example</title>\r\n" + "<body>\r\n" + "    <p>Dear "
				+ emailModel.getAddressTo() + "</p>\r\n"
				+ "    <p>Your Appraisal for the year is Approved by Management</p>\r\n"
				// + " <p>Click here to approve the Timesheet request</p>\r\n"
				// + " <a href=\"https://www.javatpoint.com/python-list-vs-tuple\"
				// target=\"_blank\">Go to Example.com</a>\r\n"
				+ "    <p>Thank you!</p>\r\n" + "</body>\r\n" + "</html>";

		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, UTF_8);
			helper.setFrom("hr@pransquare.in");
			helper.setTo(emailModel.getToEmail());
			helper.setSubject(emailModel.getSubject());
			helper.setText(body, true); // true indicates that the body is HTML
			if (file != null) {
				String originalFilename = file.getOriginalFilename();
				if (originalFilename != null) {
					helper.addAttachment(originalFilename, file);
				}
			}
			mailSender.send(mimeMessage);

			logger.info(MAIL_SENT_SUCCESS, emailModel.getToEmail());
		} catch (MailException e) {
			logger.error(FAILED_TO_SEND_EMAIL, emailModel.getToEmail(), e.getMessage());

		} catch (Exception e) {
			logger.error(UNEXPECTED_ERROR_OCCURRED, e.getMessage());

		}
	}

	public void sendEmailToEmployee(String toEmail, String employeeName, String password, String employeeCode) {
		try {
			// Create MimeMessage using JavaMailSender
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

			// Set email details
			helper.setFrom(mailId); // Replace with your email address
			helper.setTo(toEmail);
			helper.setSubject("Your Login Credentials for PranNexus");


			// Email body with dynamic placeholders
			String emailBody = String.format(
					"Dear %s,<br><br>" +
							"Welcome to the Pransquare! Below are your login credentials for PranNexus:<br><br>" +
							"<strong>Username:</strong> %s<br>" +
							"<strong>Password:</strong> %s<br>" +
							"<strong>Employee Code:</strong> %s<br><br>" +
							"Please use these credentials to log in to the employee portal.<br><br>" +

							"<a href='https://pransquare.in/' target='_blank'>Click here to access PranNexus</a><br><br>" +


							"Best regards,<br>" +
							"Pransquare HR Team",
					employeeName, toEmail, password, employeeCode);

			// Set the email body content as HTML
			helper.setText(emailBody, true); // `true` enables HTML content

			// Send the email
			mailSender.send(mimeMessage);
			logger.info(MAIL_SENT_SUCCESS, toEmail);
		} catch (Exception e) {
			logger.info(FAILED_TO_SEND_EMAIL, e.fillInStackTrace());
		}
	}


	 public void sendProjectAllocationEmail(String candidateEmail,String employeeName,EmployeeProjectConfigEntity entity) {
	        try {
	            MimeMessage mimeMessage = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);


	            helper.setFrom(mailId);
	            helper.setTo(candidateEmail);
	            helper.setSubject("Project Allocation Notification");

	            String emailBody = String.format(
	                "Dear %s,<br><br>" +
	                "You have been allocated a new project.<br><br>" +
	                "<strong>Project Code:</strong> %s<br>" +
	                "<strong>Start Date:</strong> %s<br>" +
	                "<strong>End Date:</strong> %s<br><br>" +
	                "Best regards,<br>" +
	                "Pransquare HR Team",
	                employeeName, entity.getProjectCode(), entity.getAllocationStartDate(), entity.getAllocationEndDate()
	            );

	            helper.setText(emailBody, true);
	            mailSender.send(mimeMessage);
	            logger.info("Project allocation email sent to: {}", candidateEmail);

	        } catch (Exception e) {
	            logger.error("Failed to send project allocation email", e);
	        }
	    }
	 public void goalInitiationEmail(EmailModel emailModel) {
			String body = "<html lang=\"en\">\r\n" + "<head>\r\n" + "    <meta charset=\"UTF-8\"></meta>\r\n"
					+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></meta>\r\n"
					+ "    <title>Include URL Example</title>\r\n" + "<body>\r\n" + "    <p>Dear "
					+ emailModel.getAddressTo() + "</p>\r\n"
					+ "    <p>Your Goal setup for the year has been initiated, please complete goal setup ASAP. </p>\r\n"
					// + " <p>Click here to approve the Timesheet request</p>\r\n"
					// + " <a href=\"https://www.javatpoint.com/python-list-vs-tuple\"
					// target=\"_blank\">Go to Example.com</a>\r\n"
					+ "    <p>Thank you!</p>\r\n" + "</body>\r\n" + "</html>";

			try {
				MimeMessage mimeMessage = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, UTF_8);
				helper.setFrom("hr@pransquare.in");
				helper.setTo(emailModel.getToEmail());
				helper.setSubject(emailModel.getSubject());
				helper.setText(body, true); // true indicates that the body is HTML
			
				mailSender.send(mimeMessage);

				logger.info(MAIL_SENT_SUCCESS, emailModel.getToEmail());
			} catch (MailException e) {
				logger.error(FAILED_TO_SEND_EMAIL, emailModel.getToEmail(), e.getMessage());

			} catch (Exception e) {
				logger.error(UNEXPECTED_ERROR_OCCURRED, e.getMessage());

			}
		}
	 public void goalApprovalEmail(EmailModel emailModel) {
			String body = "<html lang=\"en\">\r\n" + "<head>\r\n" + "    <meta charset=\"UTF-8\"></meta>\r\n"
					+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></meta>\r\n"
					+ "    <title>Include URL Example</title>\r\n" + "<body>\r\n" + "    <p>Dear "
					+ emailModel.getAddressTo() + "</p>\r\n"
					+ "    <p>Goal setup for the year has been Accepted by Employee</p>\r\n"
					// + " <p>Click here to approve the Timesheet request</p>\r\n"
					// + " <a href=\"https://www.javatpoint.com/python-list-vs-tuple\"
					// target=\"_blank\">Go to Example.com</a>\r\n"
					+ "    <p>Thank you!</p>\r\n" + "</body>\r\n" + "</html>";

			try {
				MimeMessage mimeMessage = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, UTF_8);
				helper.setFrom("hr@pransquare.in");
				helper.setTo(emailModel.getToEmail());
				helper.setSubject(emailModel.getSubject());
				helper.setText(body, true); // true indicates that the body is HTML
			
				mailSender.send(mimeMessage);

				logger.info(MAIL_SENT_SUCCESS, emailModel.getToEmail());
			} catch (MailException e) {
				logger.error(FAILED_TO_SEND_EMAIL, emailModel.getToEmail(), e.getMessage());

			} catch (Exception e) {
				logger.error(UNEXPECTED_ERROR_OCCURRED, e.getMessage());

			}
		}
	 public void goalEmailToManager(EmailModel emailModel) {
			String body = "<html lang=\"en\">\r\n" + "<head>\r\n" + "    <meta charset=\"UTF-8\"></meta>\r\n"
					+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></meta>\r\n"
					+ "    <title>Include URL Example</title>\r\n" + "<body>\r\n" + "    <p>Dear "
					+ emailModel.getAddressTo() + "</p>\r\n"
					+ "    <p>Your have a Goal setup Review of "+ emailModel.getRequestor()+" , Kindly Review and Send it Employee. </p>\r\n"
					// + " <p>Click here to approve the Timesheet request</p>\r\n"
					// + " <a href=\"https://www.javatpoint.com/python-list-vs-tuple\"
					// target=\"_blank\">Go to Example.com</a>\r\n"
					+ "    <p>Thank you!</p>\r\n" + "</body>\r\n" + "</html>";

			try {
				MimeMessage mimeMessage = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, UTF_8);
				helper.setFrom("hr@pransquare.in");
				helper.setTo(emailModel.getToEmail());
				helper.setSubject(emailModel.getSubject());
				helper.setText(body, true); // true indicates that the body is HTML
			
				mailSender.send(mimeMessage);

				logger.info(MAIL_SENT_SUCCESS, emailModel.getToEmail());
			} catch (MailException e) {
				logger.error(FAILED_TO_SEND_EMAIL, emailModel.getToEmail(), e.getMessage());

			} catch (Exception e) {
				logger.error(UNEXPECTED_ERROR_OCCURRED, e.getMessage());

			}
		}
}

