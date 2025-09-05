package com.pransquare.nems.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "timesheet_dates_approval")
public class TimesheetDatesApprovalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "approver_id")
    private Long approverId;

    @Column(name = "task_date")
    private LocalDate taskDate;

    @Column(name = "status")
    private String status;

    @OneToOne()
    @JoinColumn(name = "employee_id", referencedColumnName = "EMPLOYEE_BASIC_DETAIL_ID", insertable = false, updatable = false)
    private EmployeeEntity employeeEntity;

    @OneToOne
    @JoinColumn(name = "status", referencedColumnName = "CODE", insertable = false, updatable = false)
    private StatusMasterEntity statusMasterEntity;

    public String getEmployeeName() {
        return employeeEntity != null ? employeeEntity.getFullName() : null;
    }

    public String getStatusDes() {
        return statusMasterEntity != null ? statusMasterEntity.getDescription() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public LocalDate getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(LocalDate taskDate) {
        this.taskDate = taskDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
