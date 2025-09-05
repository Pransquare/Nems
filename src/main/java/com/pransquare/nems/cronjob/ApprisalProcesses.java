package com.pransquare.nems.cronjob;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.pransquare.nems.dto.ApprisalCycleConfigDTO;
import com.pransquare.nems.dto.BasicGoalsConfigDTO;
import com.pransquare.nems.entities.EmployeeAppraisal;
import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.entities.EmployeeGoal;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.repositories.EmployeeAppraisalRepository;
import com.pransquare.nems.repositories.EmployeeRepository;

@Service
public class ApprisalProcesses {

    @Value("${master-config-service.url}")
    private String masterConfigServiceUrl;

    private RestTemplate restTemplate;
    private EmployeeRepository employeeRepository;
    private EmployeeAppraisalRepository employeeAppraisalRepository;

    ApprisalProcesses(RestTemplate restTemplate, EmployeeRepository employeeRepository,
            EmployeeAppraisalRepository employeeAppraisalRepository) {
        this.restTemplate = restTemplate;
        this.employeeRepository = employeeRepository;
        this.employeeAppraisalRepository = employeeAppraisalRepository;
    }

    @Scheduled(cron = "0 0 12 * * ?")
    public void processApprisal() {
        try {
            ResponseEntity<ApprisalCycleConfigDTO> response = getApprisalMasterData();
            ApprisalCycleConfigDTO apprisalCycleConfigDTO = response.getBody();
            List<Map<String, LocalDate>> apprisalCycleDates = new ArrayList<>();
            LocalDate today = LocalDate.now(); // Get the current date

            if (apprisalCycleConfigDTO != null) {
                LocalDate yearStart = apprisalCycleConfigDTO.getYearStart();
                LocalDate yearEnd = apprisalCycleConfigDTO.getYearEnd();
                ApprisalCycleConfigDTO.ApprisalType apprisalType = apprisalCycleConfigDTO.getApprisalType();

                LocalDate currentStart = yearStart;

                while (!currentStart.isAfter(yearEnd)) {
                    LocalDate cycleEnd;

                    switch (apprisalType) {
                        case ANNUALLY:
                            cycleEnd = currentStart.plusYears(1).minusDays(1);
                            break;
                        case HALFYEARLY:
                            cycleEnd = currentStart.plusMonths(6).minusDays(1);
                            break;
                        case QUARTERLY:
                            cycleEnd = currentStart.plusMonths(3).minusDays(1);
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown Apprisal Type");
                    }

                    // Ensure cycleEnd does not exceed yearEnd
                    if (cycleEnd.isAfter(yearEnd)) {
                        cycleEnd = yearEnd;
                    }

                    // Store in map
                    Map<String, LocalDate> cyclePeriod = new HashMap<>();
                    cyclePeriod.put("start_date", currentStart);
                    cyclePeriod.put("end_date", cycleEnd);
                    apprisalCycleDates.add(cyclePeriod);

                    if (currentStart.equals(today)) {
                        triggerCycleStart(currentStart, cycleEnd);
                        return; // Exit loop after triggering the method
                    }

                    // Move to next period
                    currentStart = cycleEnd.plusDays(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ResponseEntity<ApprisalCycleConfigDTO> getApprisalMasterData() {
        try {
            return restTemplate.getForEntity(
                    masterConfigServiceUrl + "/pransquare/MasterConfiguration/apprisal_config/getActive",
                    ApprisalCycleConfigDTO.class);
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.fillInStackTrace());
        }
    }

    private void triggerCycleStart(LocalDate cycleStart, LocalDate cycleEnd) {
        List<EmployeeEntity> employees = employeeRepository.findByStatus("108");
        ResponseEntity<List<BasicGoalsConfigDTO>> responseEntity = getBasicGoalsConfig();
        List<BasicGoalsConfigDTO> basicGoalsConfig = responseEntity.getBody();
        if (basicGoalsConfig == null || basicGoalsConfig.isEmpty()) {
            return;
        }

        for (EmployeeEntity employee : employees) {
            EmployeeAppraisal employeeAppraisal = employeeAppraisalRepository
                    .findByEmployeeIdAndYearStartAndYearEnd(employee.getEmployeeBasicDetailId(), cycleStart, cycleEnd);

            if (employeeAppraisal == null) {
                employeeAppraisal = new EmployeeAppraisal();
                employeeAppraisal.setEmployeeId(employee.getEmployeeBasicDetailId());
                employeeAppraisal.setYearStart(cycleStart);
                employeeAppraisal.setYearEnd(cycleEnd);
                employeeAppraisal.setAppraisalName(cycleStart.toString() + " - " + cycleEnd.toString());
                employeeAppraisal.setCurrentCtc(BigDecimal.valueOf(employee.getEmployeeCtcEntity().getCtc()));
                employeeAppraisal.setStatus("100");

                // **Set Goals**
                List<EmployeeGoal> employeeGoals = new ArrayList<>();
                for (BasicGoalsConfigDTO goalConfig : basicGoalsConfig) {
                    EmployeeGoal goal = new EmployeeGoal();
                    goal.setEmployeeAppraisal(employeeAppraisal); // Associate goal with appraisal
                    goal.setGoal(goalConfig.getGoal());
                    goal.setGoalDescription("");
                    goal.setSelfRating(BigDecimal.ZERO);
                    goal.setManagerRating(BigDecimal.ZERO);
                    goal.setFinalRating(BigDecimal.ZERO);
                    goal.setSelfComments("");
                    goal.setManagerComments("");
                    goal.setFinalComments("");
                    goal.setStatus("100");
                    employeeGoals.add(goal);
                }

                employeeAppraisal.setGoals(employeeGoals); // Set goals in appraisal
                employeeAppraisalRepository.save(employeeAppraisal);
            }
        }
    }

    private ResponseEntity<List<BasicGoalsConfigDTO>> getBasicGoalsConfig() {
        try {
            return restTemplate.exchange(
                    masterConfigServiceUrl + "/pransquare/MasterConfiguration/basic_goals/getActive",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<BasicGoalsConfigDTO>>() {
                    });
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.fillInStackTrace());
        }
    }
}
