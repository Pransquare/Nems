package com.pransquare.nems.batch;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pransquare.nems.entities.EmployeeProjectConfigEntity;
import com.pransquare.nems.models.ProjectMetaDataResModel;
import com.pransquare.nems.repositories.EmployeeProjectConfigRepository;
import com.pransquare.nems.repositories.EmployeeRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ProjectReminderScheduler {

    private RestTemplate restTemplate;
    private EmployeeProjectConfigRepository employeeProjectConfigRepository;
    private JavaMailSender mailSender;
    private EmployeeRepository employeeRepository;

    ProjectReminderScheduler(RestTemplate restTemplate, EmployeeRepository employeeRepository,
            EmployeeProjectConfigRepository employeeProjectConfigRepository, JavaMailSender mailSender) {
        this.restTemplate = restTemplate;
        this.employeeProjectConfigRepository = employeeProjectConfigRepository;
        this.mailSender = mailSender;
        this.employeeRepository = employeeRepository;
    }

    @Value("${master-config-service.url}")
    private String masterConfigServiceUrl;

    private List<ProjectMetaDataResModel> projectMetaDataResModels = new ArrayList<>();

//    @Scheduled(cron = "0 0 19 * * ?")
    public void runTaskWithCron() {
        projectMetaDataResModels = restTemplate.exchange(
                masterConfigServiceUrl + "/pransquare/MasterConfiguration/projects/getAllProjects",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProjectMetaDataResModel>>() {
                }).getBody();
        if (projectMetaDataResModels != null) {
            List<EmployeeProjectConfigEntity> employeeProjectConfigEntities = employeeProjectConfigRepository
                    .findByStatus("108");
            for (EmployeeProjectConfigEntity employeeProjectConfigEntity : employeeProjectConfigEntities.stream()
                    .filter(a -> {
                        Optional<ProjectMetaDataResModel> projectMetaData = projectMetaDataResModels.stream()
                                .filter(b -> b.getProjectCode().equals(a.getProjectCode())).findFirst();
                        return projectMetaData.isPresent()
                                && projectMetaData.get().getEndDate().minusDays(15).isEqual(LocalDate.now());
                    })
                    .toList()) {
                sendReminder(employeeProjectConfigEntity);
            }
        }
    }

    private void sendReminder(EmployeeProjectConfigEntity employeeProjectConfigEntity) {
        projectMetaDataResModels.stream()
                .filter(b -> b.getProjectCode().equals(employeeProjectConfigEntity.getProjectCode()))
                .findFirst()
                .ifPresent(projectMetaData -> {
                    String projectName = projectMetaData.getProjectName();
                    String mailSubject = "Project Reminder: " + projectName;
                    String mailBody = "Dear Employee,\n\n"
                            + "This is a reminder that the project \"" + projectName + "\" is due in 15 days on "
                            + LocalDate.now().plusDays(15).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".\n\n"
                            + "Please ensure that all necessary tasks and deliverables are completed by the due date.\n\n"
                            + "If you have any questions or need further assistance, feel free to reach out.\n\n"

                            + "Best regards,\n"
                            + "The Project Management Team\n"
                            + "SigmaSoft";
                    MimeMessage mimeMessage = mailSender.createMimeMessage();
                    MimeMessageHelper helper;
                    try {
                        helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                        employeeRepository.findById(employeeProjectConfigEntity.getEmployeeId()).ifPresent(a -> {
                            try {
                                helper.setFrom("hr@pransquare.in");
                                helper.setTo(a.getEmailId());
                                helper.setSubject(mailSubject);
                                helper.setText(mailBody, false); // true indicates that the body is HTML
                                mailSender.send(mimeMessage);
                            } catch (MessagingException e) {
                                e.printStackTrace();
                            }
                        });
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                });
    }
}
