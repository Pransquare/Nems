package com.pransquare.nems.models;

import java.time.LocalDate;

public class EmployeeLeaveModel {

    private Long employeeId;
    private String employeeCode;
    private Long employeeLeaveId;
    private String leaveType;
    private LocalDate leaveFrom;
    private LocalDate leaveTo;
    private Float noOfDays;
    private String reason;
    private String remarks;
    private String fromLeaveType;
    private String toLeaveType;
    private String approver1;
    private String approver2;
    private String approver3;
    private String user;
    private String status;
    private String year;
    
    private Integer page; 
    private Integer size; 


    // Getters and Setters
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    // Getters and Setters

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getApprover1() {
        return approver1;
    }

    public void setApprover1(String approver1) {
        this.approver1 = approver1;
    }

    public String getApprover2() {
        return approver2;
    }

    public void setApprover2(String approver2) {
        this.approver2 = approver2;
    }

    public String getApprover3() {
        return approver3;
    }

    public void setApprover3(String approver3) {
        this.approver3 = approver3;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getEmployeeLeaveId() {
        return employeeLeaveId;
    }

    public void setEmployeeLeaveId(Long employeeLeaveId) {
        this.employeeLeaveId = employeeLeaveId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFromLeaveType() {
        return fromLeaveType;
    }

    public void setFromLeaveType(String fromLeaveType) {
        this.fromLeaveType = fromLeaveType;
    }

    public String getToLeaveType() {
        return toLeaveType;
    }

    public void setToLeaveType(String toLeaveType) {
        this.toLeaveType = toLeaveType;
    }

    public Float getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(Float noOfDays) {
        this.noOfDays = noOfDays;
    }

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
    

}