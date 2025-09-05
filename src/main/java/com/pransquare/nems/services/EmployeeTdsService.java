package com.pransquare.nems.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pransquare.nems.entities.EmployeeTdsEntity;
import com.pransquare.nems.repositories.EmployeeTdsRepository;

@Service
public class EmployeeTdsService {

    private EmployeeTdsRepository employeeTdsRepository;

    public EmployeeTdsService(EmployeeTdsRepository employeeTdsRepository) {
		this.employeeTdsRepository = employeeTdsRepository;
	}

	public List<EmployeeTdsEntity> getAllEmployeeTds() {
        return employeeTdsRepository.findAll();
    }

    public Optional<EmployeeTdsEntity> getEmployeeTdsById(int id) {
        return employeeTdsRepository.findById(id);
    }

    public EmployeeTdsEntity createEmployeeTds(EmployeeTdsEntity employeeTds) {
        return employeeTdsRepository.save(employeeTds);
    }

    public EmployeeTdsEntity updateEmployeeTds(int id, EmployeeTdsEntity updatedEmployeeTds) {
        return employeeTdsRepository.findById(id).map(employeeTds -> {
            employeeTds.setEmployeeId(updatedEmployeeTds.getEmployeeId());
            employeeTds.setAcademicYear(updatedEmployeeTds.getAcademicYear());
            employeeTds.setStatus(updatedEmployeeTds.getStatus());
            employeeTds.setCode(updatedEmployeeTds.getCode());
            employeeTds.setCreatedBy(updatedEmployeeTds.getCreatedBy());
            employeeTds.setCreatedDate(updatedEmployeeTds.getCreatedDate());
            employeeTds.setModifiedBy(updatedEmployeeTds.getModifiedBy());
            employeeTds.setModifiedDate(updatedEmployeeTds.getModifiedDate());
            return employeeTdsRepository.save(employeeTds);
        }).orElseThrow(() -> new RuntimeException("EmployeeTds not found"));
    }

    public void deleteEmployeeTds(int id) {
        employeeTdsRepository.deleteById(id);
    }
}
