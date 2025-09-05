package com.pransquare.nems.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.entities.EmployeeProjectConfigEntity;
import com.pransquare.nems.entities.EmployeeProjectTaskConfig;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.models.EmployeeProjectConfigModel;
import com.pransquare.nems.repositories.EmployeeProjectConfigRepository;
import com.pransquare.nems.repositories.EmployeeRepository;

import jakarta.transaction.Transactional;

@Service
public class EmployeeProjectConfigService {

   
	private EmployeeProjectConfigRepository employeeProjectConfigRepository;
    private EmailService emailService;
    private EmployeeRepository employeeRepository;

    public EmployeeProjectConfigService(EmployeeProjectConfigRepository employeeProjectConfigRepository,
            EmailService emailService, EmployeeRepository employeeRepository) {
		this.employeeProjectConfigRepository = employeeProjectConfigRepository;
        this.emailService = emailService;
        this.employeeRepository = employeeRepository;
	}

	public List<EmployeeProjectConfigEntity> saveOrUpdateEmployeeProjectConfig(
            EmployeeProjectConfigModel employeeProjectConfigModel) {
        try {
            List<EmployeeProjectConfigEntity> existingEmployeeProjectConfig = employeeProjectConfigRepository
                    .findByEmployeeIdAndStatus(employeeProjectConfigModel.getEmployeeId(), "108");

            List<String> newProjectCodes = employeeProjectConfigModel.getProjects();

            // Handle case where no existing configuration is found
            if (existingEmployeeProjectConfig.isEmpty()) {
                List<EmployeeProjectConfigEntity> newConfigs = newProjectCodes.stream().map(projectCode -> {
                    EmployeeProjectConfigEntity entity = new EmployeeProjectConfigEntity();
                    entity.setCreatedBy(employeeProjectConfigModel.getUser());
                    entity.setCreatedDate(LocalDate.now());
                    entity.setEmployeeId(employeeProjectConfigModel.getEmployeeId());
                    entity.setProjectCode(projectCode);
                    entity.setStatus("108");
                    return entity;
                }).toList();
                return employeeProjectConfigRepository.saveAll(newConfigs);
            }

            // Mark existing projects not in the new list as inactive
            existingEmployeeProjectConfig.forEach(config -> {
                if (!newProjectCodes.contains(config.getProjectCode())) {
                    config.setStatus("109");
                    config.setModifiedBy(employeeProjectConfigModel.getUser());
                    config.setModifiedDate(LocalDate.now());
                }
            });

            // Add new projects that are not in the existing configuration
            List<EmployeeProjectConfigEntity> newConfigs = newProjectCodes.stream()
                    .filter(newProjectCode -> existingEmployeeProjectConfig.stream()
                            .noneMatch(existingConfig -> existingConfig.getProjectCode().equals(newProjectCode)))
                    .map(projectCode -> {
                        EmployeeProjectConfigEntity entity = new EmployeeProjectConfigEntity();
                        entity.setCreatedBy(employeeProjectConfigModel.getUser());
                        entity.setCreatedDate(LocalDate.now());
                        entity.setEmployeeId(employeeProjectConfigModel.getEmployeeId());
                        entity.setProjectCode(projectCode);
                        entity.setStatus("108");
                        return entity;
                    }).toList();

            // Combine existing and new configurations
            existingEmployeeProjectConfig.addAll(newConfigs);

            return employeeProjectConfigRepository.saveAll(existingEmployeeProjectConfig);
        } catch (Exception e) {
            throw new IllegalStateException("Couldn't save employee project config", e);
        }
    }

    public List<EmployeeProjectConfigEntity> getEmployeeProjectConfig(Long employeeId) {
        try {
            return employeeProjectConfigRepository.findByEmployeeIdAndStatus(employeeId, "108");
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<List<EmployeeProjectConfigEntity>> saveOrUpdate(List<EmployeeProjectConfigEntity> input) {
        try {
            for (EmployeeProjectConfigEntity employeeProjectConfigEntity : input) {
                // Fetch existing entity if present
                if (employeeProjectConfigEntity.getEmployeeProjectConfigId() != null) {
                    Optional<EmployeeProjectConfigEntity> existingEntityOpt = employeeProjectConfigRepository
                            .findById(employeeProjectConfigEntity.getEmployeeProjectConfigId());

                    if (existingEntityOpt.isPresent()) {
                        EmployeeProjectConfigEntity existingEntity = existingEntityOpt.get();

                        // Update fields except tasks (handled separately)
                        existingEntity.setEmployeeId(employeeProjectConfigEntity.getEmployeeId());
                        existingEntity.setProjectCode(employeeProjectConfigEntity.getProjectCode());
                        existingEntity.setModifiedBy(employeeProjectConfigEntity.getModifiedBy());
                        existingEntity.setModifiedDate(LocalDate.now());
                        existingEntity.setStatus(employeeProjectConfigEntity.getStatus());
                        existingEntity.setAllocationStartDate(employeeProjectConfigEntity.getAllocationStartDate());
                        existingEntity.setAllocationEndDate(employeeProjectConfigEntity.getAllocationEndDate());

                        // Handle Tasks: Update, Add, or Remove
                        List<EmployeeProjectTaskConfig> existingTasks = existingEntity.getTasks();
                        List<EmployeeProjectTaskConfig> newTasks = employeeProjectConfigEntity.getTasks();

                        // Remove tasks that are no longer present
                        existingTasks.removeIf(existingTask -> newTasks.stream()
                                .noneMatch(newTask -> newTask.getTaskCode().equals(existingTask.getTaskCode())));

                        // Add new tasks
                        for (EmployeeProjectTaskConfig newTask : newTasks) {
                            if (existingTasks.stream().noneMatch(t -> t.getTaskCode().equals(newTask.getTaskCode()))) {
                                newTask.setEmployeeProjectConfig(existingEntity);
                                existingTasks.add(newTask);
                            }
                        }

                        // Save updated entity
                        employeeProjectConfigRepository.save(existingEntity);
                    }
                } else {
                    // New entity - Set task references and save
                    for (EmployeeProjectTaskConfig employeeProjectTaskConfig : employeeProjectConfigEntity.getTasks()) {
                        employeeProjectTaskConfig.setEmployeeProjectConfig(employeeProjectConfigEntity);
                    }
                    employeeProjectConfigRepository.save(employeeProjectConfigEntity);
                }
            }

            // Send emails after saving all entities
			/*
			 * for (EmployeeProjectConfigEntity entity : input) { EmployeeEntity e =
			 * employeeRepository.findByEmployeeBasicDetailId(entity.getEmployeeId());
			 * emailService.sendProjectAllocationEmail(e.getEmailId(), e.getFullName(),
			 * entity); }
			 */
            for (EmployeeProjectConfigEntity entity : input) {
                if (entity.getEmployeeProjectConfigId() != null) {
                    Optional<EmployeeProjectConfigEntity> oldEntityOpt =
                            employeeProjectConfigRepository.findById(entity.getEmployeeProjectConfigId());

                    if (oldEntityOpt.isPresent()) {
                        EmployeeProjectConfigEntity oldEntity = oldEntityOpt.get();

                        boolean isProjectSame = Objects.equals(oldEntity.getProjectCode(), entity.getProjectCode());
                        boolean isOnlyDateChanged =
                                Objects.equals(oldEntity.getEmployeeId(), entity.getEmployeeId()) &&
                                isProjectSame &&
                                Objects.equals(oldEntity.getStatus(), entity.getStatus()) &&
                                Objects.equals(oldEntity.getTasks(), entity.getTasks()) &&
                                (!Objects.equals(oldEntity.getAllocationStartDate(), entity.getAllocationStartDate()) ||
                                 !Objects.equals(oldEntity.getAllocationEndDate(), entity.getAllocationEndDate()));

                        // Skip email if only dates have changed and project remains the same
                        if (isOnlyDateChanged) {
                            continue;
                        }
                    }
                }

                // New entry or significant change: send email
                EmployeeEntity e = employeeRepository.findByEmployeeBasicDetailId(entity.getEmployeeId());
                emailService.sendProjectAllocationEmail(e.getEmailId(), e.getFullName(), entity);
            }


            return ResponseEntity.ok(input);
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.fillInStackTrace());
        }
    }

}
