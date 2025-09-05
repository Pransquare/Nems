package com.pransquare.nems.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.pransquare.nems.entities.EmpDocumentDetailsEntity;
import com.pransquare.nems.entities.EmployeeWorkLocationEntity;

public class EmployeeModel {
	private Long employeeId;
	private String employeeCode;
	private String firstName;
	private String middleName;
	private String lastName;
	private String gender;
	private LocalDate dob;
	private String designation;
	private String maritalStatus;
	private String mobileNo;
	private String alternateNumber;
	private String emergencyNo;
	private String emailId;
	private String bloodGroup;
	private String panNo;
	private String adharNo;
	private String uanNo;
	private LocalDateTime createdDate;
	private String createdBy;
	private String modifiedBy;
	private LocalDateTime modifiedDate;
	private String status;
	private String workflowStatus;
	private LocalDate dateOfJoining;
	private LocalDate lastWorkingDay;
	private String group;
	private String subGroup;
	private String personalEmail;
	private String nationality;
	private String workType;
	private String documentType;
	private String documentNumber;
	private List<EmployeeAddressModel> employeeAddress;
	private EmployeeBankDetailsModel employeeBankDetails;
	private String user; // The user who created or modified the employee record
	private EmployeeWorkLocationEntity employeeWorkLocation;
	private List<EmpDocumentDetailsEntity> empDocumentDetails;
	private Double ctc;
	private String jiraId;

	public String getJiraId() {
		return jiraId;
	}

	public void setJiraId(String jiraId) {
		this.jiraId = jiraId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<EmpDocumentDetailsEntity> getEmpDocumentDetails() {
		return empDocumentDetails;
	}

	public void setEmpDocumentDetails(List<EmpDocumentDetailsEntity> empDocumentDetails) {
		this.empDocumentDetails = empDocumentDetails;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getAlternateNumber() {
		return alternateNumber;
	}

	public void setAlternateNumber(String alternateNumber) {
		this.alternateNumber = alternateNumber;
	}

	public String getEmergencyNo() {
		return emergencyNo;
	}

	public void setEmergencyNo(String emergencyNo) {
		this.emergencyNo = emergencyNo;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getAdharNo() {
		return adharNo;
	}

	public void setAdharNo(String adharNo) {
		this.adharNo = adharNo;
	}

	public String getUanNo() {
		return uanNo;
	}

	public void setUanNo(String uanNo) {
		this.uanNo = uanNo;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public LocalDate getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(LocalDate dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public LocalDate getLastWorkingDay() {
		return lastWorkingDay;
	}

	public void setLastWorkingDay(LocalDate lastWorkingDay) {
		this.lastWorkingDay = lastWorkingDay;
	}

	public List<EmployeeAddressModel> getEmployeeAddress() {
		return employeeAddress;
	}

	public void setEmployeeAddress(List<EmployeeAddressModel> employeeAddress) {
		this.employeeAddress = employeeAddress;
	}

	public EmployeeBankDetailsModel getEmployeeBankDetails() {
		return employeeBankDetails;
	}

	public void setEmployeeBankDetails(EmployeeBankDetailsModel employeeBankDetails) {
		this.employeeBankDetails = employeeBankDetails;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(String subGroup) {
		this.subGroup = subGroup;
	}

	public void setPersonalEmail(String personalEmail) {
		this.personalEmail = personalEmail;
	}

	public String getPersonalEmail() {
		return personalEmail;
	}

	public EmployeeWorkLocationEntity getEmployeeWorkLocation() {
		return employeeWorkLocation;
	}

	public void setEmployeeWorkLocation(EmployeeWorkLocationEntity employeeWorkLocation) {
		this.employeeWorkLocation = employeeWorkLocation;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public Double getCtc() {
		return ctc;
	}

	public void setCtc(Double ctc) {
		this.ctc = ctc;
	}

}