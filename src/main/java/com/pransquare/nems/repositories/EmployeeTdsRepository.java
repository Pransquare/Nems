package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmployeeTdsEntity;

@Repository
public interface EmployeeTdsRepository extends JpaRepository<EmployeeTdsEntity, Integer> {
    // Custom query methods (if any) can be added here
}
