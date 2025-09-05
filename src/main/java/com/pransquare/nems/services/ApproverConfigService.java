package com.pransquare.nems.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.entities.GroupEmailAddressEntity;
import com.pransquare.nems.entities.UserApproverConfigurationsEntity;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.models.ApproverConfigSubModel;
import com.pransquare.nems.models.ApproverMailModel;
import com.pransquare.nems.models.ApproverSearchModel;
import com.pransquare.nems.models.EmpIdEmailAddressDTO;
import com.pransquare.nems.models.GroupEmailAddressDTO;
import com.pransquare.nems.models.SaveApproverConfigModel;
import com.pransquare.nems.repositories.ApproverConfigRepository;
import com.pransquare.nems.repositories.EmployeeRepository;
import com.pransquare.nems.utils.IntegerUtils;

@Service
public class ApproverConfigService {

	private static final Logger logger = LogManager.getLogger(ApproverConfigService.class);

	private final ApproverConfigRepository approverConfigRepository;

	private final EmployeeRepository employeeRepository;

	public ApproverConfigService(ApproverConfigRepository approverConfigRepository,
			EmployeeRepository employeeRepository) {
		this.approverConfigRepository = approverConfigRepository;
		this.employeeRepository = employeeRepository;
	}

	public UserApproverConfigurationsEntity getApproverByEmpIdAndModule(ApproverSearchModel approverSearchModel) {
		return approverConfigRepository.findByEmpBasicDetailIdAndModuleName(
				approverSearchModel.getEmployeeBasicDetailId(), approverSearchModel.getModuleName());
	}

	public List<UserApproverConfigurationsEntity> getApprgetApproverByEmpIdoverByEmpIdAndModule(
			ApproverSearchModel approverSearchModel) {
		return approverConfigRepository.findByEmpBasicDetailId(approverSearchModel.getEmployeeBasicDetailId());
	}

	public List<UserApproverConfigurationsEntity> getEmployeesByApproverId(Long approverId, String module) {
		return approverConfigRepository.findByApproverIdAndModuleName(approverId, module);
	}

	public String saveApproverConfig(SaveApproverConfigModel saveApproverConfigModel) {
		try {
			List<ApproverConfigSubModel> approverConfigSubModels = saveApproverConfigModel.getApproverConfigSubModels();
			for (ApproverConfigSubModel config : approverConfigSubModels) {
				UserApproverConfigurationsEntity userApproverConfig;
				if (IntegerUtils.isNotNull(config.getUserApproverConfigId())) {
					Optional<UserApproverConfigurationsEntity> userApproverConfigOptional = approverConfigRepository
							.findById(config.getUserApproverConfigId());
					if (!userApproverConfigOptional.isPresent()) {
						throw new IllegalArgumentException("An error occurred while saving");
					}
					userApproverConfig = userApproverConfigOptional.get();
					userApproverConfig.setEmpBasicDetailId(saveApproverConfigModel.getEmpBasicDetailId());
					userApproverConfig.setApproverId(config.getApproverId());
					userApproverConfig.setModifiedBy(saveApproverConfigModel.getCreatedBy());
					userApproverConfig.setModifiedDate(LocalDateTime.now());
					approverConfigRepository.save(userApproverConfig);
					continue;
				}
				userApproverConfig = new UserApproverConfigurationsEntity();
				userApproverConfig.setEmpBasicDetailId(saveApproverConfigModel.getEmpBasicDetailId());
				userApproverConfig.setModuleName(config.getModule());
				userApproverConfig.setApproverId(config.getApproverId());
				userApproverConfig.setCreatedBy(saveApproverConfigModel.getCreatedBy());
				userApproverConfig.setCreatedDate(LocalDateTime.now());
				approverConfigRepository.save(userApproverConfig);
			}
			Optional<EmployeeEntity> employeeEntity = employeeRepository
					.findById(saveApproverConfigModel.getEmpBasicDetailId());
			if (employeeEntity.isPresent()) {
				employeeEntity.get().setStatus("108");
				employeeRepository.save(employeeEntity.get());
			}
			return "Record saved successfully";
		} catch (Exception e) {
			throw new IllegalArgumentException("An error occurred while saving");
		}
	}

	public List<UserApproverConfigurationsEntity> getAllByEmpId(ApproverSearchModel approverSearchModel) {
		return approverConfigRepository.findByEmpBasicDetailId(approverSearchModel.getEmployeeBasicDetailId());

	}

	public GroupEmailAddressDTO getEmailAddressByEmpIdAndModule(ApproverMailModel approverSearchModel) {
		EmployeeEntity employee = employeeRepository.findByEmployeeCode(approverSearchModel.getEmployeeCode());
		if (employee == null) {
			throw new ResourceNotFoundException("Employee Does not exist");
		}
		GroupEmailAddressDTO groupEmailAddressDTO = new GroupEmailAddressDTO();
		groupEmailAddressDTO.setLoggedInEmpId(employee.getEmployeeBasicDetailId());
		List<EmpIdEmailAddressDTO> empIdEmailAddressDTOs = new ArrayList<>();
		UserApproverConfigurationsEntity userConfig = approverConfigRepository.findByEmpBasicDetailIdAndModuleName(
				employee.getEmployeeBasicDetailId(), approverSearchModel.getModuleName());
		logger.info("userConfig {}", userConfig);
		if (userConfig != null) {
			Optional<EmployeeEntity> approver = employeeRepository.findById(userConfig.getApproverId());
			approver.ifPresent(a -> {
				logger.info("approver {}", a);
				EmpIdEmailAddressDTO empIdEmailAddressDTO1 = new EmpIdEmailAddressDTO(a.getEmployeeBasicDetailId(),
						a.getEmailId());
				empIdEmailAddressDTOs.add(empIdEmailAddressDTO1);
			});

		} else {
			throw new ResourceNotFoundException("User configuration not found for employee and module");
		}

		if (userConfig.getApproverId1() != null) {
			Optional<EmployeeEntity> approver1 = employeeRepository.findById(userConfig.getApproverId1());

			approver1.ifPresent(a1 -> {
				logger.info("approver1 {}", a1);
				EmpIdEmailAddressDTO empIdEmailAddressDTO2 = new EmpIdEmailAddressDTO(
						a1.getEmployeeBasicDetailId(), a1.getEmailId());
				empIdEmailAddressDTOs.add(empIdEmailAddressDTO2);
			});

		}
		if (userConfig.getApproverId2() != null) {
			Optional<EmployeeEntity> approver2 = employeeRepository.findById(userConfig.getApproverId2());

			approver2.ifPresent(a2 -> {
				logger.info("approver2 {}", a2);
				EmpIdEmailAddressDTO empIdEmailAddressDTO3 = new EmpIdEmailAddressDTO(
						a2.getEmployeeBasicDetailId(), a2.getEmailId());
				empIdEmailAddressDTOs.add(empIdEmailAddressDTO3);
			});

		}
		List<GroupEmailAddressEntity> groupEmails = userConfig.getGroupEmailAddressEntities();
		logger.info("groupEmails {}", groupEmails);
		if (!groupEmails.isEmpty()) {
			for (GroupEmailAddressEntity groupEmail : groupEmails) {
				EmpIdEmailAddressDTO empIdEmailAddressDTO = new EmpIdEmailAddressDTO(groupEmail.getEmpBasicDetailId(),
						groupEmail.getEmailId());
				empIdEmailAddressDTOs.add(empIdEmailAddressDTO);
			}
		}
		groupEmailAddressDTO.setEmpIdEmailAddressDTOs(empIdEmailAddressDTOs);
		return groupEmailAddressDTO;
	}

}