package com.pransquare.nems.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.models.EmailModel;
import com.pransquare.nems.models.EmployeeLeaveModel;
import com.pransquare.nems.services.EmailService;
import com.pransquare.nems.services.TimesheetReminder;

@RestController
@RequestMapping(value = "/Pransquare/nems/email")
public class EmailController {
	public static final String EMAIL_SEND_FAILURE = "Failed to send email: ";
	public static final String EMAIL_SENT_SUCCESS = "Email sent successfully!";
	private final EmailService emailService;
	private final TimesheetReminder timesheetReminder;

	public EmailController(EmailService emailService, TimesheetReminder timesheetReminder) {
		this.emailService = emailService;
		this.timesheetReminder = timesheetReminder;
	}

	@GetMapping("/sendEmail")
	public String sendEmail(@RequestParam String toEmail) {
		try {
			EmailModel emailModel = new EmailModel();
			emailModel.setBody("This is a test email sent from Spring Boot.");
			emailModel.setSubject("Test");
			emailModel.setToEmail(toEmail);
			emailService.sendSimpleEmail(emailModel);
			return EMAIL_SENT_SUCCESS;
		} catch (RuntimeException e) {
			return EMAIL_SEND_FAILURE + e.getMessage();
		}
	}

	@PostMapping("/sendTimesheetMail")
	public String sendTimesheetMail(@RequestBody EmailModel emailModel) {
		try {
			// Compose the email body with text and an HTML link

			// Send the email using your email service
			emailService.sendTimesheetMailRequest(emailModel);
			return EMAIL_SENT_SUCCESS;
		} catch (RuntimeException e) {
			return EMAIL_SEND_FAILURE + e.getMessage();
		}
	}

	@PostMapping("/sendLeaveRequestMail")
	public String sendLeaveRequestMail(@RequestBody EmployeeLeaveModel emailModel) {
		try {

			// Send the email using your email service
			emailService.sendLeaveRequestMail(emailModel);
			return EMAIL_SENT_SUCCESS;
		} catch (RuntimeException e) {
			return EMAIL_SEND_FAILURE + e.getMessage();
		}
	}


	


//	@PostMapping("/timeSheetRemindMonth")
//	public String timeSheetRemindMonth(@RequestParam Long employyeid) {
//		try {
//			
//			timesheetReminder.timeSheetRemindMonth(employyeid);
//			return EMAIL_SENT_SUCCESS;
//		} catch (RuntimeException e) {
//			return EMAIL_SEND_FAILURE + e.getMessage();
//		}
//	}
	// @PostMapping("/timeSheetRemind")
	// public String timeSheetRemind(@RequestParam Long employyeid) {
	// 	try {
			
	// 		timesheetReminder.timeSheetRemind(employyeid);
	// 		return EMAIL_SENT_SUCCESS;
	// 	} catch (RuntimeException e) {
	// 		return EMAIL_SEND_FAILURE + e.getMessage();
	// 	}
	// }
}
