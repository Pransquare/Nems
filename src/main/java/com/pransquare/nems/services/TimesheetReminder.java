package com.pransquare.nems.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.pransquare.nems.entities.AttendanceEntity;
import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.entities.EmployeeLeaveEntity;
import com.pransquare.nems.repositories.ApproverConfigRepository;
import com.pransquare.nems.repositories.AttendanceRepository;
import com.pransquare.nems.repositories.EmployeeLeaveRepository;
import com.pransquare.nems.repositories.EmployeeRepository;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class TimesheetReminder {

	private static final Logger logger = LoggerFactory.getLogger(TimesheetReminder.class);

	@Value("${spring.mail.host}")
	private String SMTP_HOST;

	@Value("${spring.mail.username}")
	private String FROM_EMAIL;

	@Value("${spring.mail.password}")
	private String FROM_PASSWORD;

	AttendanceRepository attendanceRepository;

	EmployeeRepository employeeRepository;

	ApproverConfigRepository approverConfigRepository;

	EmployeeLeaveRepository employeeLeaveRepository;

	public TimesheetReminder(AttendanceRepository attendanceRepository, EmployeeRepository employeeRepository,
			ApproverConfigRepository approverConfigRepository, EmployeeLeaveRepository employeeLeaveRepository) {
		this.attendanceRepository = attendanceRepository;
		this.employeeRepository = employeeRepository;
		this.approverConfigRepository = approverConfigRepository;
		this.employeeLeaveRepository = employeeLeaveRepository;
	}

//	    private static final String SMTP_HOST = "smtp.example.com"; // Update with your SMTP host
//	    private static final String FROM_EMAIL = "your-email@example.com"; // Your email
//	    private static final String FROM_PASSWORD = "your-password"; // Your email password

	@Scheduled(cron = "${timesheetRemind}") // Runs every minute
//@SchedulerLock(name = "timesheetRemind", lockAtMostFor = "5m", lockAtLeastFor = "1m")
	public void timeSheetRemind() {
		List<EmployeeEntity> employeeEntities = employeeRepository.findByStatus("108");
		System.out.println("line63" + employeeEntities);
		if (!employeeEntities.isEmpty()) {
			for (EmployeeEntity employeeEnt : employeeEntities) {
				System.out.println("line66" + employeeEnt);
				List<String> missingDatesFromTimesheet = getMissingTimesheetDates(employeeEnt.getEmployeeBasicDetailId());
				List<String> leaveDates=getLeaveApprovedDates(employeeEnt.getEmployeeBasicDetailId());
				List<String> missingDates = missingDatesFromTimesheet.stream()
					    .filter(date -> !leaveDates.contains(date))
					    .collect(Collectors.toList());
				System.out.println("line68" + missingDates);
				logger.info(String.valueOf(missingDates));
				if (!missingDates.isEmpty()) {
					System.out.println("line71" + missingDates);
					Optional<EmployeeEntity> employeeEntity = employeeRepository
							.findById(employeeEnt.getEmployeeBasicDetailId());
					String employeeEmail = employeeEntity.get().getEmailId(); // Retrieve employee email
					Long approverId = approverConfigRepository.findByEmpBasicDetailIdAndModuleName(
							employeeEntity.get().getEmployeeBasicDetailId(), "timesheet").getEmpBasicDetailId();
					String approverEmail = employeeRepository.findById(approverId).get().getEmailId();
					String addressTo = employeeEntity.get().getFullName();
					logger.info(String.valueOf(employeeEmail));
					List<String> excludedEmails = Arrays.asList("prasad@sigmasoft.ai", "sri.vaddadi@ssitsol.com");

					if (!excludedEmails.contains(employeeEmail)) {
						sendReminderEmail(employeeEmail, addressTo, missingDates, approverEmail);
					}
				}
			}
		}
	}

	// Method to fetch missing timesheet dates excluding weekends
	public List<String> getMissingTimesheetDates(Long employeeId) {
		List<String> missingDates = new ArrayList<>();
		LocalDate today = LocalDate.now();
		LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);

		for (LocalDate date = startOfWeek; date.isBefore(today); date = date.plusDays(1)) {
			if (isWeekday(date) && !isTimesheetFilled(employeeId, date)) {
				missingDates.add(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			}
		}
		return missingDates;
	}

	public List<String> getLeaveApprovedDates(Long employeeId) {
		List<String> missingDates = new ArrayList<>();
		List<LocalDate> totalDates = new ArrayList<>();
		List<EmployeeLeaveEntity> leaves = employeeLeaveRepository.findByEmployeeIdAndStatus(employeeId, "108");
		for (EmployeeLeaveEntity leave : leaves) {
			List<LocalDate> leaveDates = getLeaveDatesBetween(leave.getLeaveFrom(), leave.getLeaveTo());
			totalDates.addAll(leaveDates);
		}
		for (LocalDate date : totalDates) {
			missingDates.add(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		}
		return missingDates;
	}

	// Check if the date is a weekday
	private boolean isWeekday(LocalDate date) {
		DayOfWeek day = date.getDayOfWeek();
		return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
	}

	// Mock method to check if the timesheet was filled (replace with actual check)
	private boolean isTimesheetFilled(Long employeeId, LocalDate date) {
		List<String> status = new ArrayList<>();
		status.add("103");
		List<AttendanceEntity> attendanceEntities = attendanceRepository
				.findByEmpBasicDetailIdAndTaskDateAndStatusNotIn(employeeId, date, status);
		if (attendanceEntities.isEmpty()) {
			return false; // Placeholder
		} else {
			return true;
		}
	}

	// Send email with missing dates
	public void sendReminderEmail(String to, String addressTo, List<String> missingDates, String ccEmail) {
		String subject = "Timesheet Reminder: Please Complete Your Timesheets";
		String messageBody = "Dear " + addressTo
				+ ",\n\nYou have not filled your timesheets for the following days:\n\n";
//		String ccEmail="arun.goenka@sigmasoft.ai";

		for (String date : missingDates) {
			messageBody += date + "\n";
		}
		messageBody += "\nPlease complete your timesheets as soon as possible.\n\nBest Regards,\nHR Team";

		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", SMTP_HOST);
		properties.put("mail.smtp.port", "587");

		Session session = Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(FROM_EMAIL, FROM_PASSWORD);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(FROM_EMAIL));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail));
			message.setSubject(subject);
			message.setText(messageBody);
			logger.info(String.valueOf(message));
			Transport.send(message);
			logger.info("Reminder email sent successfully!");
		} catch (MessagingException e) {
			logger.error("Error sending email to {}: {}", to, e.getMessage(), e);
		}
	}

	@Scheduled(cron = "${timesheetRemindMonth}")
	public void timeSheetRemindMonth() {
		List<EmployeeEntity> employeeEntities = employeeRepository.findByStatus("108");
		System.out.println("line63" + employeeEntities);
		if (!employeeEntities.isEmpty()) {
			for (EmployeeEntity employeeEnt : employeeEntities) {
				Long employeeId = employeeEnt.getEmployeeBasicDetailId();

				System.out.println("line66" + employeeEnt);
				List<String> missingDatesFromTimesheet = getMissingTimesheetDatesMonth(employeeId);
				logger.info("Missing dates: {}", missingDatesFromTimesheet);
				List<String> leaveDates=getLeaveApprovedDates(employeeEnt.getEmployeeBasicDetailId());
				List<String> missingDates = missingDatesFromTimesheet.stream()
					    .filter(date -> !leaveDates.contains(date))
					    .collect(Collectors.toList());
				if (!missingDates.isEmpty()) {
					Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(employeeId);
					if (employeeEntity.isPresent()) {
						String employeeEmail = employeeEntity.get().getEmailId();
						String addressTo = employeeEntity.get().getFullName();
						Long approverId = approverConfigRepository.findByEmpBasicDetailIdAndModuleName(
								employeeEntity.get().getEmployeeBasicDetailId(), "timesheet").getEmpBasicDetailId();
						String approverEmail = employeeRepository.findById(approverId).get().getEmailId();
						;
						logger.info("Sending email to: {}", employeeEmail);
						List<String> excludedEmails = Arrays.asList("prasad@sigmasoft.ai", "sri.vaddadi@ssitsol.com");

						if (!excludedEmails.contains(employeeEmail)) {
							sendReminderEmailMonth(employeeEmail, addressTo, missingDates, approverEmail);
						}
					}
				}
			}
		}
	}

	// Method to fetch missing timesheet dates from start of the month until today,
	// excluding weekends
	public List<String> getMissingTimesheetDatesMonth(Long employeeId) {
		List<String> missingDates = new ArrayList<>();
		LocalDate today = LocalDate.now().minusDays(0);
		LocalDate startOfMonth = today.withDayOfMonth(1); // Start from the first day of the month

		for (LocalDate date = startOfMonth; date.isBefore(today); date = date.plusDays(1)) {
			if (isWeekdayMonth(date) && !isTimesheetFilledMonth(employeeId, date)) {
				missingDates.add(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			}
		}
		return missingDates;
	}

	// Check if the date is a weekday
	private boolean isWeekdayMonth(LocalDate date) {
		DayOfWeek day = date.getDayOfWeek();
		return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
	}

	// Check if timesheet is filled by querying attendance repository for the date
	private boolean isTimesheetFilledMonth(Long employeeId, LocalDate date) {
		List<String> excludedStatuses = List.of("103");
		List<AttendanceEntity> attendanceEntities = attendanceRepository
				.findByEmpBasicDetailIdAndTaskDateAndStatusNotIn(employeeId, date, excludedStatuses);
		return !attendanceEntities.isEmpty();
	}

	// Send email with missing dates
	public void sendReminderEmailMonth(String to, String addressTo, List<String> missingDates, String ccEmail) {
		String subject = "Monthly Timesheet Reminder";
//		String ccEmail="arun.goenka@sigmasoft.ai";
		StringBuilder messageBody = new StringBuilder("Dear " + addressTo
				+ " ,\n\nYou have not filled your timesheets for the following dates this month:\n\n");

		for (String date : missingDates) {
			messageBody.append(date).append("\n");
		}

		messageBody.append("\nPlease Submit your timesheets as soon as possible.\n\nBest Regards,\nHR Team");

		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", SMTP_HOST);
		properties.put("mail.smtp.port", "587");

		Session session = Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(FROM_EMAIL, FROM_PASSWORD);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(FROM_EMAIL));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail));
			message.setSubject(subject);
			message.setText(messageBody.toString());
			logger.info("Sending email: " + message);
			Transport.send(message);
			System.out.println("Monthly timesheet reminder email sent successfully!");
		} catch (MessagingException e) {
			logger.error("Error sending email: ", e);
		}
	}

	public List<LocalDate> getLeaveDatesBetween(LocalDate from, LocalDate to) {
		List<LocalDate> dates = new ArrayList<>();
		if (from != null && to != null && !from.isAfter(to)) {
			LocalDate current = from;
			while (!current.isAfter(to)) {
				dates.add(current);
				current = current.plusDays(1);
			}
		}
		return dates;
	}
}
