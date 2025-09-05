package com.pransquare.nems.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.dto.BasicGoalsConfigDTO;
import com.pransquare.nems.entities.EmpGoalSetupEntity;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.models.GoalSearchModel;
import com.pransquare.nems.models.GoalSetUpSaveModel;
import com.pransquare.nems.services.AppraisalService;
import com.pransquare.nems.services.GoalSetUpService;

@RestController
@RequestMapping("/Pransquare/nems/goalsetup")
public class GoalsSetUpController {
	
	@Autowired
	AppraisalService appraisalService;
	
	@Autowired
	GoalSetUpService goalSetUpService;
	
    @PostMapping("/searchBasicGoals")//incomplete
    public ResponseEntity<List<BasicGoalsConfigDTO>> searchBasicGoals() {
    	List<BasicGoalsConfigDTO> result=new ArrayList<>();
    	result=goalSetUpService.fetchBasicGoals();
    	if(result.isEmpty()) {
    		throw new ResourceNotFoundException("No goals found");
    	}
    	else {
    	return ResponseEntity.ok(result );
    	}
    }
    @PostMapping("/initiateGoalSetup")
    public ResponseEntity<String> initiateGoalSetup(@RequestBody List<GoalSetUpSaveModel> goalSetUpSaveModels) {
    	String result="";
    	result=goalSetUpService.initiateGoalSetup(goalSetUpSaveModels);
    	
    	return ResponseEntity.ok(result );
    	}
    
    
    @PostMapping("/setUpGoals")
    public ResponseEntity<String> setUpGoals(@RequestBody GoalSetUpSaveModel goalSetUpSaveModel) {
    	String result="";
    	result=goalSetUpService.setUpGoals(goalSetUpSaveModel);
    	
    	return ResponseEntity.ok(result );
    	}
    
    
@PostMapping("/searchGoals")
public ResponseEntity<Page<EmpGoalSetupEntity>> searchGoals(@RequestBody GoalSearchModel goalSearchModel) {
	Page<EmpGoalSetupEntity> result;
	result=goalSetUpService.searchGoals(goalSearchModel);
	if(result.isEmpty()) {
		throw new ResourceNotFoundException("No goals found");
	}
	else {
	return ResponseEntity.ok(result );
	}
}
}  


