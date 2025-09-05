package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmployeeCtcEntity;

@Repository
public interface EmployeeCtcRepository extends JpaRepository<EmployeeCtcEntity, Integer> {
    // You can define custom query methods here if necessary
    EmployeeCtcEntity findByEmployeeIdAndStatus(Long a, String status);
}
