package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmployeeWorkLocationEntity;

import java.util.List;

@Repository
public interface EmployeeWorkLocationRepository extends JpaRepository<EmployeeWorkLocationEntity, Long> {
    EmployeeWorkLocationEntity findByEmployeeIdAndStatus(Long employeeId, String status);

    List<EmployeeWorkLocationEntity> findByWorkLocationCode(String workLocationCode);
}
