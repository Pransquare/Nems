package com.pransquare.nems.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pransquare.nems.entities.EmployeeWorkLocationEntity;
import com.pransquare.nems.repositories.EmployeeWorkLocationRepository;

@Service
public class EmployeeWorkLocationService {

    private final EmployeeWorkLocationRepository employeeWorkLocationRepository;

    public EmployeeWorkLocationService(EmployeeWorkLocationRepository employeeWorkLocationRepository) {
        this.employeeWorkLocationRepository = employeeWorkLocationRepository;
    }

    // Get all records
    public List<EmployeeWorkLocationEntity> findAll() {
        return employeeWorkLocationRepository.findAll();
    }

    // Get by ID
    public Optional<EmployeeWorkLocationEntity> findById(Long id) {
        return employeeWorkLocationRepository.findById(id);
    }

    // Get by employee ID
    public EmployeeWorkLocationEntity findByEmployeeId(Long employeeId) {
        return employeeWorkLocationRepository.findByEmployeeIdAndStatus(employeeId, "108");
    }

    // Get by work location code
    public List<EmployeeWorkLocationEntity> findByWorkLocationCode(String workLocationCode) {
        return employeeWorkLocationRepository.findByWorkLocationCode(workLocationCode);
    }

    // Create or update work location record
    public EmployeeWorkLocationEntity saveWorkLocation(EmployeeWorkLocationEntity workLocation) {
        return employeeWorkLocationRepository.save(workLocation);
    }

    // Delete a record
    public void deleteWorkLocation(Long id) {
        employeeWorkLocationRepository.deleteById(id);
    }
}
