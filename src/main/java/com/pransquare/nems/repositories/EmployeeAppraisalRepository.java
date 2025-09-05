package com.pransquare.nems.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmployeeAppraisal;

@Repository
public interface EmployeeAppraisalRepository extends JpaRepository<EmployeeAppraisal, Integer> {
    EmployeeAppraisal findByEmployeeIdAndYearStartAndYearEnd(Long employeeId, LocalDate yearStart, LocalDate yearEnd);
}
