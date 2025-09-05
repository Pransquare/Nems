package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmployeeAddressEntity;

import java.util.List;

@Repository
public interface EmployeeAddressRepository extends JpaRepository<EmployeeAddressEntity, Integer> {
    List<EmployeeAddressEntity> findEmployeeAddressByEmployeeId(Integer employeeId);
}
