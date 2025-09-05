package com.pransquare.nems.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.entities.UserApproverConfigurationsEntity;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.models.ApproverMailModel;
import com.pransquare.nems.models.ApproverSearchModel;
import com.pransquare.nems.models.GroupEmailAddressDTO;
import com.pransquare.nems.models.SaveApproverConfigModel;
import com.pransquare.nems.services.ApproverConfigService;

@RestController
@RequestMapping("/Pransquare/nems/ApproverConfig")
public class ApproverConfigController {

	private static final String NO_APPROVER_CONFIGURED_MESSAGE = "No Approver Configured for this User";

	private final ApproverConfigService approverConfigService;

	public ApproverConfigController(ApproverConfigService approverConfigService) {
		this.approverConfigService = approverConfigService;
	}

	@PostMapping(value = "/getApproverByEmpIdAndModule")
	public ResponseEntity<UserApproverConfigurationsEntity> getApproverByEmpIdAndModule(
			ApproverSearchModel approverSearchModel) {
		UserApproverConfigurationsEntity userApproverConfigurationsEntity = approverConfigService
				.getApproverByEmpIdAndModule(approverSearchModel);
		if (userApproverConfigurationsEntity == null) {
			throw new ResourceNotFoundException(NO_APPROVER_CONFIGURED_MESSAGE);
		} else {
			return ResponseEntity.accepted().body(userApproverConfigurationsEntity);
		}
	}

	@PostMapping(value = "/getApproverByEmpId")
	public ResponseEntity<List<UserApproverConfigurationsEntity>> getApproverByEmpId(
			ApproverSearchModel approverSearchModel) {
		List<UserApproverConfigurationsEntity> userApproverConfigurationsEntity = approverConfigService
				.getApprgetApproverByEmpIdoverByEmpIdAndModule(approverSearchModel);
		if (userApproverConfigurationsEntity == null) {
			throw new ResourceNotFoundException(NO_APPROVER_CONFIGURED_MESSAGE);
		} else {
			return ResponseEntity.accepted().body(userApproverConfigurationsEntity);
		}
	}

	@GetMapping("getEmployeesByApproverId/{approverId}/{module}")
	public List<UserApproverConfigurationsEntity> getEmployeesByApproverId(@PathVariable Long approverId,
			@PathVariable String module) {
		return approverConfigService.getEmployeesByApproverId(approverId, module);
	}

	@PostMapping(value = "/saveApproverConfig")
	public ResponseEntity<Map<String, Object>> saveApproverConfig(
			@RequestBody SaveApproverConfigModel saveApproverConfigModel) {
		Map<String, Object> returnResult = new HashMap<>();
		String result = approverConfigService.saveApproverConfig(saveApproverConfigModel);
		returnResult.put("response", result);

		return ResponseEntity.ok().body(returnResult);
	}

	@PostMapping(value = "/getAllByEmpId")
	public List<UserApproverConfigurationsEntity> getAllByEmpId(@RequestBody ApproverSearchModel approverSearchModel) {
		List<UserApproverConfigurationsEntity> userApproverConfigurationsEntity = approverConfigService
				.getAllByEmpId(approverSearchModel);
		if (userApproverConfigurationsEntity == null) {
			throw new ResourceNotFoundException(NO_APPROVER_CONFIGURED_MESSAGE);
		} else {
			return userApproverConfigurationsEntity;
		}
	}

	@PostMapping(value = "/getEmailAddressByEmpIdAndModule")
	public ResponseEntity<GroupEmailAddressDTO> getEmailAddressByEmpIdAndModule(
			@RequestBody ApproverMailModel approverMailModel) {
		GroupEmailAddressDTO userApproverConfigurationsEntity = approverConfigService
				.getEmailAddressByEmpIdAndModule(approverMailModel);
		if (userApproverConfigurationsEntity == null) {
			throw new ResourceNotFoundException(NO_APPROVER_CONFIGURED_MESSAGE);
		} else {
			return ResponseEntity.accepted().body(userApproverConfigurationsEntity);
		}
	}
}