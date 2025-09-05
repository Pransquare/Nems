package com.pransquare.nems.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.entities.GroupSubgroupConfigEntity;
import com.pransquare.nems.models.SaveGoalsForGroupModel;
import com.pransquare.nems.models.SearchGoalsModel;
import com.pransquare.nems.services.GoalsConfigService;

@RestController
@RequestMapping("/Pransquare/nems/Goals")
public class GoalsConfigController {


	private final GoalsConfigService goalsConfigService; // injecting the service layer here. Please replace with actual service
														// class.
	public GoalsConfigController(GoalsConfigService goalsConfigService) {
		this.goalsConfigService = goalsConfigService;
	}
	
	@PostMapping(value = "/saveGoalsforGroup")
	public ResponseEntity<Map<String, Object>> saveGoalsForGroup(
			@RequestBody SaveGoalsForGroupModel saveGoalsForGroupModel) {
		Map<String, Object> returnResponse = goalsConfigService.saveGoalsForGroup(saveGoalsForGroupModel);
		return ResponseEntity.ok().body(returnResponse);
	}

	@PostMapping(value = "/searchGoalsByGroupAndSubgroup")
	public ResponseEntity<Map<String, Object>> searchGoalsByGroupAndSubgroup(
			@RequestBody SearchGoalsModel searchGoalsModel) {
		Map<String, Object> returnResponse = new HashMap<>();
		Page<GroupSubgroupConfigEntity> response = goalsConfigService
				.searchGoalsByGroupAndSubgroup(searchGoalsModel);
		returnResponse.put("response", response);
		return ResponseEntity.ok().body(returnResponse);
	}

}
