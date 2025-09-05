package com.pransquare.nems.entities;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "employee_leave_view")
public class EmployeeLeaveViewEntity {

    @Id
    @Column(name = "EMP_LEAVE_ID")  // Ensuring ID uniqueness
    private Long empLeaveId;

    @Column(name = "EMPLOYEE_CODE")
    private String employeeCode;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "LEAVE_FROM")
    private LocalDate leaveFrom;

    @Column(name = "LEAVE_TO")
    private LocalDate leaveTo;

    @Column(name = "NO_OF_DAYS")
    private Float noOfDays;

    @Column(name = "DESCRIPTION") // Status description
    private String description;

    @Column(name = "DESIGNATION_DESCRIPTION")
    private String designation;

    @Column(name = "LEAVE_TYPE_DESCRIPTION")
    private String leaveType;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "approver_id")
	private Long approverId;

	
	public Long getEmpLeaveId() {
		return empLeaveId;
	}

	public void setEmpLeaveId(Long empLeaveId) {
		this.empLeaveId = empLeaveId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public LocalDate getLeaveFrom() {
		return leaveFrom;
	}

	public void setLeaveFrom(LocalDate leaveFrom) {
		this.leaveFrom = leaveFrom;
	}

	public LocalDate getLeaveTo() {
		return leaveTo;
	}

	public void setLeaveTo(LocalDate leaveTo) {
		this.leaveTo = leaveTo;
	}

	public Float getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(Float noOfDays) {
		this.noOfDays = noOfDays;
	}

	
	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getApproverId() {
		return approverId;
	}

	public void setApproverId(Long approverId) {
		this.approverId = approverId;
	}
    
    
}
