package com.pransquare.nems.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.entities.ContractEntity;
import com.pransquare.nems.entities.ProjectEntity;
import com.pransquare.nems.models.ContractModel;
import com.pransquare.nems.models.ProjectModelNew;
import com.pransquare.nems.services.ContractService;

@RestController
@RequestMapping("/Pransquare/nems/contracts")
public class ContractController {

	@Autowired
	ContractService contractService;

	@PostMapping("/saveOrUpdate")
	public ResponseEntity<?> saveOrUpdate(@RequestBody ContractModel contractModel) {
		ContractEntity saved = contractService.saveOrUpdateContract(contractModel);
		return ResponseEntity.ok(saved);
	}

	@GetMapping("/getContractDetails")
	public ResponseEntity<?> getContractDetails(@RequestParam String contractId) {
		ContractEntity saved = contractService.getContractDetails(contractId);
		return ResponseEntity.ok(saved);
	}
	
	@PostMapping("/saveProjectDetails")
    public ResponseEntity<ProjectEntity> saveProjectDetails(@RequestBody ProjectModelNew projectModel) {
		ProjectEntity saved = contractService.saveOrUpdateProject(projectModel);
        return ResponseEntity.ok(saved);
    }

}
