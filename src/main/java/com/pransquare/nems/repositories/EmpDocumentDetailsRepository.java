package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmpDocumentDetailsEntity;

import java.util.List;

@Repository
public interface EmpDocumentDetailsRepository extends JpaRepository<EmpDocumentDetailsEntity, Integer> {

    // Custom methods can be added if needed
    List<EmpDocumentDetailsEntity> findByEmployeeId(Integer employeeId);

}
