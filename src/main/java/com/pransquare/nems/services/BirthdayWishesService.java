package com.pransquare.nems.services;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.models.EmailModel;
import com.pransquare.nems.repositories.ApproverConfigRepository;
import com.pransquare.nems.repositories.EmployeeRepository;

import jakarta.mail.internet.MimeMessage;

@Service
public class BirthdayWishesService {
	
	private static final Logger logger = LoggerFactory.getLogger(BirthdayWishesService.class);
	
	
	EmployeeRepository employeeRepository;
	
	private JavaMailSender mailSender;
	
	@Value("${timesheetEmailId}")
    private String timesheetEmailId;
	
	public BirthdayWishesService(JavaMailSender mailSender,EmployeeRepository  employeeRepository) {
		this.mailSender = mailSender;
		this.employeeRepository=employeeRepository;
	}
	
	@Scheduled(cron ="${birthdaywishes}")
    public void sendBirthdayWishes() {
        LocalDate today = LocalDate.now();
        List<EmployeeEntity> employees = employeeRepository.findByStatus("108");
        
        employees = employees.stream()
        	    .filter(e -> e.getEmployeeWorkLocation() != null && 
        	                 !"WL003".equals(e.getEmployeeWorkLocation().getWorkLocationCode()))
        	    .toList();

        for (EmployeeEntity employee : employees) {
            LocalDate dob = employee.getDob();
            if (dob != null && dob.getMonth() == today.getMonth() && dob.getDayOfMonth() == today.getDayOfMonth()) {
            	EmailModel emailModel=new EmailModel();
            	emailModel.setAddressTo(employee.getFullName());
            	emailModel.setToEmail(employee.getEmailId());
            	emailModel.setFromEmail(timesheetEmailId);
            	emailModel.setSubject("Birthday Wishes");
            	sendBirthdayWishesMail(emailModel);
            }
        }
    }

    	public void sendBirthdayWishesMail(EmailModel emailModel) {
    	    String body = "<html lang=\"en\">\r\n"
    	    		+ "    	        <head>\r\n"
    	    		+ "    	            <meta charset=\"UTF-8\">\r\n"
    	    		+ "    	            <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
    	    		+ "    	            <title>Birthday Wishes</title>\r\n"
    	    		+ "    	        </head>\r\n"
    	    		+ "  \r\n"
    	    		+ "    <body>\r\n"
    	    		+ "        <p>Dear "+ emailModel.getAddressTo() +" ,</p>\r\n"
    	    		+ "        <p>Sigmasoft Wishing you a very <strong>Happy Birthday!</strong> 🎉🎂</p>\r\n"
    	    		+ "        <p>May your day be filled with joy, laughter, and memorable moments.</p>\r\n"
    	    		+ "        <p>We are grateful to have you as part of our team. Here's to another year of success and happiness!</p>\r\n"
    	    		+ "        <p>Have a fantastic birthday!</p>\r\n"
    	    		+ "        <p>Best regards,<br>HR Team</p>\r\n"
    	    		+ "    </body>\r\n"
    	    		+ "    </html>";
    	        
    


    	    emailModel.setBody(body);
    	    logger.info("emailModel {}", emailModel);

    	    try {
    	        MimeMessage mimeMessage = mailSender.createMimeMessage();
    	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
    	        helper.setFrom(timesheetEmailId);
    	        helper.setTo(emailModel.getToEmail());
    	        helper.setSubject(emailModel.getSubject());
    	        helper.setText(emailModel.getBody(), true); // HTML content

    	        mailSender.send(mimeMessage);
    	        logger.info("Birthday mail sent successfully to {}", emailModel.getToEmail());

    	    } catch (MailException e) {
    	        logger.error("Failed to send birthday email to {}: {}", emailModel.getToEmail(), e.getMessage());
    	    } catch (Exception e) {
    	        logger.error("Unexpected error occurred: {}", e.getMessage());
    	    }
    	}
    	
    	@Scheduled(cron ="${birthdaywishesUSA}")
        public void sendBirthdayWishesUSA() {
            LocalDate today = LocalDate.now();
            List<EmployeeEntity> employees = employeeRepository.findByStatus("108");
            
            employees = employees.stream()
            	    .filter(e -> e.getEmployeeWorkLocation() != null && 
            	                 "WL003".equals(e.getEmployeeWorkLocation().getWorkLocationCode()))
            	    .toList();

            for (EmployeeEntity employee : employees) {
                LocalDate dob = employee.getDob();
                if (dob != null && dob.getMonth() == today.getMonth() && dob.getDayOfMonth() == today.getDayOfMonth()) {
                	EmailModel emailModel=new EmailModel();
                	emailModel.setAddressTo(employee.getFullName());
                	emailModel.setToEmail(employee.getEmailId());
                	emailModel.setFromEmail(timesheetEmailId);
                	emailModel.setSubject("Birthday Wishes");
                	sendBirthdayWishesMail(emailModel);
                }
            }
        }

        	


    
    
}


