package com.pransquare.nems.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pransquare.nems.entities.ContractEntity;
import com.pransquare.nems.entities.ProjectEntity;
import com.pransquare.nems.models.ContractModel;
import com.pransquare.nems.models.ProjectModelNew;
import com.pransquare.nems.repositories.ContractRepository;
import com.pransquare.nems.repositories.ProjectRepository;

@Service
public class ContractService {

	@Autowired
	ContractRepository contractRepository;
	
	 @Autowired
	    private ProjectRepository projectRepository;

	public ContractEntity saveOrUpdateContract(ContractModel model) {
		ContractEntity contract;

		if (model.getId() != null) {
			Optional<ContractEntity> optionalContract = contractRepository.findById(model.getId());
			contract = optionalContract.orElse(new ContractEntity());
		} else {
			contract = new ContractEntity();
			contract.setCreatedDate(LocalDateTime.now());
		}

		// Map fields from model to entity
		contract.setId(model.getId() != null ? model.getId() : null);
		contract.setContractId(model.getContractId());
		contract.setCustomerGroup(model.getCustomerGroup());
		contract.setCustomerEntity(model.getCustomerEntity());
		contract.setContractPaymentTerm(model.getContractPaymentTerm());
		contract.setContractName(model.getContractName());
		contract.setContractCurrency(model.getContractCurrency());
		contract.setContractStartDate(model.getContractStartDate());
		contract.setContractEndDate(model.getContractEndDate());
		contract.setContractValue(model.getContractValue() != null ? model.getContractValue() : null);
		contract.setCreatedBy(model.getCreatedBy());
		contract.setStatus(model.getStatus());
		contract.setModifiedDate(LocalDateTime.now());
		contract.setModifiedBy(model.getCreatedBy()); // or set separately if available

		return contractRepository.save(contract);
	}

	public ContractEntity getContractDetails(String contractId) {
		// TODO Auto-generated method stub
		contractRepository.findByContractId(contractId);
		return null;
	}



	    public ProjectEntity saveOrUpdateProject(ProjectModelNew model) {
	    	ProjectEntity project;

	        if (model.getId() != null) {
	            Optional<ProjectEntity> existing = projectRepository.findById(model.getId());
	            project = existing.orElse(new ProjectEntity());
	        } else {
	            project = new ProjectEntity();
	        }

	        project.setProjectName(model.getProjectName());
	        project.setProjectCode(model.getProjectCode());
	        project.setProjectType(model.getProjectType());
	        project.setProjectStartDate(model.getStartDate());
	        project.setProjectEndDate(model.getEndDate());
	        project.setProjectValue(model.getProjectValue());
	        project.setMilestone(model.getMilestone());
	        project.setLocation(model.getLocation());

	        return projectRepository.save(project);
	    }
	
}
