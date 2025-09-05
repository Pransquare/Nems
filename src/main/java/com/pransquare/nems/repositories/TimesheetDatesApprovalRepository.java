package com.pransquare.nems.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pransquare.nems.entities.TimesheetDatesApprovalEntity;

public interface TimesheetDatesApprovalRepository extends JpaRepository<TimesheetDatesApprovalEntity, Long>,
        JpaSpecificationExecutor<TimesheetDatesApprovalEntity> {

    TimesheetDatesApprovalEntity findByEmployeeIdAndTaskDate(Long employeeId, LocalDate taskDate);
}
