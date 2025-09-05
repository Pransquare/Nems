package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmployeeBankDetailsEntity;

@Repository
public interface EmployeeBankDetailsRepository extends JpaRepository<EmployeeBankDetailsEntity, Integer> {
    EmployeeBankDetailsEntity findByEmployeeId(Integer employeeId);

    EmployeeBankDetailsEntity findByEmpBankDetailId(Integer empBankDetailId);
}