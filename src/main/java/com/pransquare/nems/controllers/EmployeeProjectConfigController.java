package com.pransquare.nems.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.entities.EmployeeProjectConfigEntity;
import com.pransquare.nems.models.EmployeeProjectConfigModel;
import com.pransquare.nems.services.EmployeeProjectConfigService;

@RestController
@RequestMapping("/Pransquare/nems/employeeProjectContoller")
public class EmployeeProjectConfigController {

	private final EmployeeProjectConfigService employeeProjectConfigService;

	public EmployeeProjectConfigController(EmployeeProjectConfigService employeeProjectConfigService) {
		this.employeeProjectConfigService = employeeProjectConfigService;
	}

	@GetMapping("/employeeProjects/{employeeId}")
	public List<EmployeeProjectConfigEntity> getEmployeeProjectConfig(@PathVariable Long employeeId) {
		return employeeProjectConfigService.getEmployeeProjectConfig(employeeId);
	}

	@PostMapping("/saveOrUpdateEmployeeProjectConfig")
	public List<EmployeeProjectConfigEntity> saveOrUpdateEmployeeProjectConfig(
			@RequestBody EmployeeProjectConfigModel employeeProjectConfigModel) {
		return employeeProjectConfigService.saveOrUpdateEmployeeProjectConfig(employeeProjectConfigModel);
	}

	@PostMapping("/saveOrUpdate")
	public ResponseEntity<List<EmployeeProjectConfigEntity>> saveOrUpdate(
			@RequestBody List<EmployeeProjectConfigEntity> input) {
		return employeeProjectConfigService.saveOrUpdate(input);
	}

}
