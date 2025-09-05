package com.pransquare.nems.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.pransquare.nems.dto.ApprisalCycleConfigDTO;
import com.pransquare.nems.dto.BasicGoalsConfigDTO;
import com.pransquare.nems.entities.BasicGoalsConfig;
import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.entities.EmployeeGoal;
import com.pransquare.nems.repositories.EmployeeGoalRepository;
import com.pransquare.nems.repositories.EmployeeRepository;

@Service
public class AppraisalService {

//	@Value("${weeklyBackUp}")
//	private String weeklyBackUp;
	// Blocking call with DTO

	private final EmployeeRepository employeeRepository;
	private final EmployeeGoalRepository employeeGoalRepository;
	private final JavaMailSender mailSender;
	WebClient webClient;
	
	@Value("${master-config-service.url}")
	private String basicgoalsUrl;

	public AppraisalService(EmployeeRepository employeeRepository, EmployeeGoalRepository employeeGoalRepository,
			JavaMailSender mailSender, WebClient.Builder webClientBuilder) {
		this.employeeRepository = employeeRepository;
		this.employeeGoalRepository = employeeGoalRepository;
		this.mailSender = mailSender;
		this.webClient = webClientBuilder.baseUrl(basicgoalsUrl+"/pransquare/MasterConfiguration/basicgoals/").build();
	}

	@Scheduled(cron = "0 0 9 * * ?") // Runs every day at 9 AM
	@Transactional
	public void checkAndSetupGoals() {
		ApprisalCycleConfigDTO cycleOpt = getActiveItems().get(0);
		List<BasicGoalsConfigDTO> activeGoals = getActiveGoals();

		LocalDate today = LocalDate.now();

//	        Optional<AppraisalCycle> cycleOpt = appraisalCycleRepository.findByStartDate(today);

		if (cycleOpt != null) {
			List<EmployeeEntity> employees = employeeRepository.findAll();

			for (EmployeeEntity employee : employees) {
				for (BasicGoalsConfigDTO activeGoal : activeGoals) {

					EmployeeGoal goal = new EmployeeGoal();
					goal.setGoal(activeGoal.getGoal());
					goal.setStatus(activeGoal.getStatus());
					employeeGoalRepository.save(goal);

					sendEmail(employee.getEmailId(), "Goal Setup",
							"Your appraisal goal has been set up. Please review and submit for approval.");
				}
			}
		}
	}

	public List<ApprisalCycleConfigDTO> getActiveItems() {
		return webClient.get().uri("/getActive").retrieve().bodyToFlux(ApprisalCycleConfigDTO.class) // Use Flux for a
																										// list
				.collectList() // Convert Flux to List
				.block(); // Block to get the result synchronously
	}

	public List<BasicGoalsConfigDTO> getActiveGoals() {
		return webClient.get().uri("goals").retrieve().bodyToFlux(BasicGoalsConfigDTO.class) // Use Flux for a list
				.collectList() // Convert Flux to List
				.block(); // Block to get the result synchronously
	}

	private void sendEmail(String to, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
	}

	
}
