package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmployeeTdsDetails;

@Repository
public interface EmployeeTdsDetailsRepository extends JpaRepository<EmployeeTdsDetails, Integer> {
    // Custom query methods (if any) can be added here
}
