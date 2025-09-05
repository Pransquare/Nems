package com.pransquare.nems.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmployeeLeaveDetailsConfigEntity;

@Repository
public interface EmployeeLeaveDetailsConfigRepository extends JpaRepository<EmployeeLeaveDetailsConfigEntity, Long> {

    List<EmployeeLeaveDetailsConfigEntity> findByEmployeeBasicDetailsId(Long employeeId);

	Optional<EmployeeLeaveDetailsConfigEntity> findByEmployeeBasicDetailsIdAndLeaveCode(Long employeeId,
			String leaveType);
}