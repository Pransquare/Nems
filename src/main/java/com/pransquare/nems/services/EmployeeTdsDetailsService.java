package com.pransquare.nems.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pransquare.nems.entities.EmployeeTdsDetails;
import com.pransquare.nems.repositories.EmployeeTdsDetailsRepository;

@Service
public class EmployeeTdsDetailsService {

    private EmployeeTdsDetailsRepository employeeTdsDetailsRepository;
    

    public EmployeeTdsDetailsService(EmployeeTdsDetailsRepository employeeTdsDetailsRepository) {
		this.employeeTdsDetailsRepository = employeeTdsDetailsRepository;
	}

	public List<EmployeeTdsDetails> getAllEmployeeTdsDetails() {
        return employeeTdsDetailsRepository.findAll();
    }

    public Optional<EmployeeTdsDetails> getEmployeeTdsDetailsById(int id) {
        return employeeTdsDetailsRepository.findById(id);
    }

    public EmployeeTdsDetails createEmployeeTdsDetails(EmployeeTdsDetails employeeTdsDetails) {
        return employeeTdsDetailsRepository.save(employeeTdsDetails);
    }

    public EmployeeTdsDetails updateEmployeeTdsDetails(int id, EmployeeTdsDetails updatedEmployeeTdsDetails) {
        return employeeTdsDetailsRepository.findById(id).map(employeeTdsDetails -> {
            employeeTdsDetails.setEmployeeTdsCode(updatedEmployeeTdsDetails.getEmployeeTdsCode());
            employeeTdsDetails.setTdsSectionCode(updatedEmployeeTdsDetails.getTdsSectionCode());
            employeeTdsDetails.setTdsSubSectionCode(updatedEmployeeTdsDetails.getTdsSubSectionCode());
            employeeTdsDetails.setDeclaredExpenditure(updatedEmployeeTdsDetails.getDeclaredExpenditure());
            employeeTdsDetails.setCreatedBy(updatedEmployeeTdsDetails.getCreatedBy());
            employeeTdsDetails.setCreatedDate(updatedEmployeeTdsDetails.getCreatedDate());
            employeeTdsDetails.setModifiedBy(updatedEmployeeTdsDetails.getModifiedBy());
            employeeTdsDetails.setModifiedDate(updatedEmployeeTdsDetails.getModifiedDate());
            employeeTdsDetails.setStatus(updatedEmployeeTdsDetails.getStatus());
            employeeTdsDetails.setSubmitedExpenditure(updatedEmployeeTdsDetails.getSubmitedExpenditure());
            employeeTdsDetails.setFinalExpenditure(updatedEmployeeTdsDetails.getFinalExpenditure());
            return employeeTdsDetailsRepository.save(employeeTdsDetails);
        }).orElseThrow(() -> new RuntimeException("EmployeeTdsDetails not found"));
    }

    public void deleteEmployeeTdsDetails(int id) {
        employeeTdsDetailsRepository.deleteById(id);
    }
}
