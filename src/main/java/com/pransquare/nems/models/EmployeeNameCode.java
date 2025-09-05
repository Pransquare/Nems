package com.pransquare.nems.models;

public class EmployeeNameCode {
    private String fullName;
    private String employeeCode;
    private String emailId;
    private Long employeeBasicDetailId;
    private String workLocationCode;

    // Getters and setters

    public EmployeeNameCode(String employeeCode, String fullName, String emailId, Long employeeBasicDetailId,
            String workLocationCode) {
        this.fullName = fullName;
        this.employeeCode = employeeCode;
        this.emailId = emailId;
        this.employeeBasicDetailId = employeeBasicDetailId; // Assuming employeeBasicDetailId is null by default
        this.workLocationCode = workLocationCode;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Long getEmployeeBasicDetailId() {
        return employeeBasicDetailId;
    }

    public void setEmployeeBasicDetailId(Long employeeBasicDetailId) {
        this.employeeBasicDetailId = employeeBasicDetailId;
    }

    public String getWorkLocationCode() {
        return workLocationCode;
    }

    public void setWorkLocationCode(String workLocationCode) {
        this.workLocationCode = workLocationCode;
    }

}