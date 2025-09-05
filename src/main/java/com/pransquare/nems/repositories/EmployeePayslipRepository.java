package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmployeePayslip;

import java.util.List;

@Repository
public interface EmployeePayslipRepository extends JpaRepository<EmployeePayslip, Long> {
    List<EmployeePayslip> findByEmployeeId(Long employeeId);
}
