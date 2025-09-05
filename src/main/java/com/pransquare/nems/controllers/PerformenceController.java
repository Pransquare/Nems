package com.pransquare.nems.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.entities.PerformenceReviewEntity;
import com.pransquare.nems.models.AppraisalUploadModel;
import com.pransquare.nems.models.ApprisalInitiateModel;
import com.pransquare.nems.models.GoalAttributeModel;
import com.pransquare.nems.models.PayrollUploadModel;
import com.pransquare.nems.models.PerformanceSearchModel;
import com.pransquare.nems.models.PerformenceReviewModel;
import com.pransquare.nems.services.PerformenceReviewService;

@RestController
@RequestMapping("/Pransquare/nems/employee")
public class PerformenceController {

	private final PerformenceReviewService performenceReviewService;

	public PerformenceController(PerformenceReviewService performenceReviewService) {
		this.performenceReviewService = performenceReviewService;
	}

	@PostMapping("/initiate")
	public String saveSelfRating(@RequestBody ApprisalInitiateModel performenceReviewModels) {
		return performenceReviewService.InitiateApprisal(performenceReviewModels);
	}

	@GetMapping("/employeeGroup")
	public List<EmployeeEntity> getEmployeesByPerformanceGroup(@RequestParam String performanceGroup,
			@RequestParam String performanceSubGroup) {
		return performenceReviewService.getEmployeesByPerformanceGroup(performanceGroup, performanceSubGroup);
	}
	
	@GetMapping("/employeeGroupNew")
	public List<EmployeeEntity> employeeGroupNew(@RequestParam String performanceGroup,
			@RequestParam String performanceSubGroup) {
		return performenceReviewService.getEmployeesByPerformanceGroupNew(performanceGroup, performanceSubGroup);
	}

	// @GetMapping("/employeeSubGroup")
	// public List<EmployeeEntity> getEmployeesByPerformanceSubGroup(@RequestParam
	// String performenceSubGroup) {
	// return
	// performenceReviewService.getEmployeesByPerformanceSubGroup(performenceSubGroup);
	// }

	// @GetMapping("/getEmploeesbyManager")
	// public List<EmployeeEntity> getEmployeesByManager(@RequestParam String
	// performanceGroup,@RequestParam String performanceSubGroup,@RequestParam Long
	// managerId) {
	// return
	// performenceReviewService.getEmployeesByManager(performanceGroup,performanceSubGroup,managerId);
	// }

	// @GetMapping("/getParamsByEmployeeId")
	// public List<PerformanceParameterGroupModel>
	// getParamsByEmployeeId(@RequestParam Long empId) {
	// return performenceReviewService.getEmployeePerformanceDetails(empId);
	// }

	@GetMapping("/getParamsByEmployeeId")
	public PerformenceReviewEntity getParamsByEmployeeId(@RequestParam Long empId, @RequestParam String employeeType) {
		return performenceReviewService.getPerformanceReviewEntity(empId, employeeType);
	}

	@PostMapping("/updateApprisalDetails")
	public ResponseEntity<String> updateApprisalDetails(@RequestBody PerformenceReviewModel entity) {
		return ResponseEntity.ok().body(performenceReviewService.updateApprisalDetails(entity));
	}

	@PostMapping("/searchPerformanceReviews")
	public Page<PerformenceReviewEntity> searchPerformanceReviews(@RequestBody PerformanceSearchModel entity) {
		return performenceReviewService.searchPerformanceReviews(entity);
	}
	
	@PostMapping(value = "/uploadAppraisalLetter", consumes = "multipart/form-data")
	public ResponseEntity<Map<String, Object>> uploadAppraisalLetter(@RequestParam MultipartFile multipartFile,
			@ModelAttribute AppraisalUploadModel payrollUploadModel) {
		Map<String, Object> response = performenceReviewService.uploadAppraisalLetter(multipartFile, payrollUploadModel);
		return ResponseEntity.ok().body(response);
	}
	@PostMapping("/updateApprisalDetailsNew")
	public ResponseEntity<String> updateApprisalDetailsNew(@ModelAttribute PerformenceReviewModel entity,@RequestParam MultipartFile multipartFile) {
		return ResponseEntity.ok().body(performenceReviewService.updateApprisalDetailsNew(entity,multipartFile));
	}
	
	@PostMapping("/updateAttributeDetails")
	public ResponseEntity<String> updateAttributeDetails(@RequestBody List<GoalAttributeModel> goalAttributeModel) {
		return ResponseEntity.ok().body(performenceReviewService.updateAttributeDetails(goalAttributeModel));
	}
	@PostMapping("/getActiveAttributes")
	public ResponseEntity<List<String>> getActiveAttributes() {
		return performenceReviewService.getActiveAttributes();
	}
	@GetMapping("/employeeGroupAppraisal")
	public List<EmployeeEntity> employeeGroupAppraisal(@RequestParam String performanceGroup,
			@RequestParam String performanceSubGroup) {
		return performenceReviewService.employeeGroupAppraisal(performanceGroup, performanceSubGroup);
	}
}
