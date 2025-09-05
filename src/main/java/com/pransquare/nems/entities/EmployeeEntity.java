package com.pransquare.nems.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_basic_details")
public class EmployeeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMPLOYEE_BASIC_DETAIL_ID")
	private Long employeeBasicDetailId;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "MIDDLE_NAME")
	private String middleName;

	@Column(name = "FULL_NAME")
	private String fullName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "GENDER")
	private String gender;

	@Column(name = "DOB")
	private LocalDate dob;

	@Column(name = "DESIGNATION")
	private String designation;

	@Column(name = "MARITAL_STATUS")
	private String maritalStatus;

	@Column(name = "MOBILE_NO")
	private String mobileNo;

	@Column(name = "EMAIL_ID")
	private String emailId;

	@Column(name = "BLOOD_GROUP")
	private String bloodGroup;

	@Column(name = "PAN_NO")
	private String panNo;

	@Column(name = "ADHAR_NO")
	private String adharNo;

	@Column(name = "UAN_NO")
	private String uanNo;

	@Column(name = "CREATEDDATE")
	private LocalDateTime createdDate;

	@Column(name = "CREATEDBY")
	private String createdBy;

	@Column(name = "MODIFIEDBY")
	private String modifiedBy;

	@Column(name = "MODIFIEDDATE")
	private LocalDateTime modifiedDate;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "WORKFLOW_STATUS")
	private String workflowStatus;

	@Column(name = "DATE_OF_JOINING")
	private LocalDate dateOfJoining;

	@Column(name = "LAST_WORKING_DAY")
	private LocalDate lastWorkingDay;

	@Column(name = "EMPLOYEE_CODE", unique = true)
	private String employeeCode;

	@Column(name = "EMP_GROUP")
	private String group;

	@Column(name = "EMP_SUB_GROUP")
	private String subGroup;

	@Column(name = "PERSONAL_EMAIL")
	private String personalEmail;

	@Column(name = "NATIONALITY")
	private String nationality;

	@Column(name = "WORK_TYPE")
	private String workType;

	@Column(name = "DOCUMENT_TYPE")
	private String documentType;

	@Column(name = "DOCUMENT_NUMBER")
	private String documentNumber;

	@Column(name = "jira_id")
	private String jiraId;

	@Column(name = "ALTERNATE_NUMBER")
	private String alternateNumber;

	@Column(name = "EMERGENCY_NUMBER")
	private String emergencyNo;

	@Column(name = "IS_TAXDECLARATION_ENABLED")
	private Integer isTaxdeclarationEnabled;

	@Column(name = "PROFILE_PIC_PATH")
	private String profilePicPath;

	@Column(name = "is_proofdeclaration_enabled")
	private Integer isProofdeclarationEnabled;

	@Column(name = "proof_submission_enabled_date")
	private LocalDateTime proofSubmissionEnabledDate;

	@Column(name = "tax_declaration_enabled_date")
	private LocalDateTime taxDeclarationEnabledDate;

	@Column(name = "test_profile")
	private Boolean testProfile;

	@Column(name = "generic_profile")
	private Boolean genericProfile;
	
	@Column(name = "goal_setup")
	private String goalSetup;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPLOYEE_BASIC_DETAIL_ID", referencedColumnName = "employee_id")
	private EmployeeCtcEntity employeeCtcEntity;

	@OneToMany(targetEntity = EmployeeAddressEntity.class, fetch = FetchType.EAGER)
	@JoinColumn(name = "EMP_BASIC_DETAIL_ID", referencedColumnName = "EMPLOYEE_BASIC_DETAIL_ID")
	private List<EmployeeAddressEntity> employeeAddressEntityList;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPLOYEE_BASIC_DETAIL_ID", referencedColumnName = "EMP_BASIC_DETAIL_ID")
	private EmployeeBankDetailsEntity employeeBankDetailsEntity;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPLOYEE_BASIC_DETAIL_ID", referencedColumnName = "EMP_BASIC_DETAIL_ID")
	private EmployeeDesignationAndManagerDetailsEntity employeeDesignationAndManagerDetailsEntity;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "EMPLOYEE_BASIC_DETAIL_ID", referencedColumnName = "employee_id")
	private EmployeeWorkLocationEntity employeeWorkLocation;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "employee_id", referencedColumnName = "EMPLOYEE_BASIC_DETAIL_ID")
	private List<EmpDocumentDetailsEntity> employeeDocumentDetails;

	public String getWorkLocationCode() {
		return employeeWorkLocation != null ? employeeWorkLocation.getWorkLocationCode() : null;
	}

	public Long getEmployeeBasicDetailId() {
		return employeeBasicDetailId;
	}

	public void setEmployeeBasicDetailId(Long employeeBasicDetailId) {
		this.employeeBasicDetailId = employeeBasicDetailId;
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailId() {
		return emailId;
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

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
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

	public List<EmployeeAddressEntity> getEmployeeAddressEntityList() {
		return employeeAddressEntityList;
	}

	public void setEmployeeAddressEntityList(List<EmployeeAddressEntity> employeeAddressEntityList) {
		this.employeeAddressEntityList = employeeAddressEntityList;
	}

	public EmployeeBankDetailsEntity getEmployeeBankDetailsEntity() {
		return employeeBankDetailsEntity;
	}

	public void setEmployeeBankDetailsEntity(EmployeeBankDetailsEntity employeeBankDetailsEntity) {
		this.employeeBankDetailsEntity = employeeBankDetailsEntity;
	}

	public EmployeeDesignationAndManagerDetailsEntity getEmployeeDesignationAndManagerDetailsEntity() {
		return employeeDesignationAndManagerDetailsEntity;
	}

	public void setEmployeeDesignationAndManagerDetailsEntity(
			EmployeeDesignationAndManagerDetailsEntity employeeDesignationAndManagerDetailsEntity) {
		this.employeeDesignationAndManagerDetailsEntity = employeeDesignationAndManagerDetailsEntity;
	}

	public String getPersonalEmail() {
		return personalEmail;
	}

	public void setPersonalEmail(String personalEmail) {
		this.personalEmail = personalEmail;
	}

	public List<EmpDocumentDetailsEntity> getEmployeeDocumentDetails() {
		return employeeDocumentDetails;
	}

	public void setEmployeeDocumentDetails(List<EmpDocumentDetailsEntity> employeeDocumentDetails) {
		this.employeeDocumentDetails = employeeDocumentDetails;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
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

	public EmployeeWorkLocationEntity getEmployeeWorkLocation() {
		return employeeWorkLocation;
	}

	public void setEmployeeWorkLocation(EmployeeWorkLocationEntity employeeWorkLocation) {
		this.employeeWorkLocation = employeeWorkLocation;
	}

	public String getJiraId() {
		return jiraId;
	}

	public void setJiraId(String jiraId) {
		this.jiraId = jiraId;
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

	public Integer getIsTaxdeclarationEnabled() {
		return isTaxdeclarationEnabled;
	}

	public void setIsTaxdeclarationEnabled(Integer isTaxdeclarationEnabled) {
		this.isTaxdeclarationEnabled = isTaxdeclarationEnabled;
	}

	public Integer getIsProofdeclarationEnabled() {
		return isProofdeclarationEnabled;
	}

	public void setIsProofdeclarationEnabled(Integer isProofdeclarationEnabled) {
		this.isProofdeclarationEnabled = isProofdeclarationEnabled;
	}

	public LocalDateTime getProofSubmissionEnabledDate() {
		return proofSubmissionEnabledDate;
	}

	public void setProofSubmissionEnabledDate(LocalDateTime proofSubmissionEnabledDate) {
		this.proofSubmissionEnabledDate = proofSubmissionEnabledDate;
	}

	public LocalDateTime getTaxDeclarationEnabledDate() {
		return taxDeclarationEnabledDate;
	}

	public void setTaxDeclarationEnabledDate(LocalDateTime taxDeclarationEnabledDate) {
		this.taxDeclarationEnabledDate = taxDeclarationEnabledDate;
	}

	@Override
	public String toString() {
		return "EmployeeEntity [employeeBasicDetailId=" + employeeBasicDetailId + ", firstName=" + firstName
				+ ", middleName=" + middleName + ", fullName=" + fullName + ", lastName=" + lastName + ", gender="
				+ gender + ", dob=" + dob + ", designation=" + designation + ", maritalStatus=" + maritalStatus
				+ ", mobileNo=" + mobileNo + ", emailId=" + emailId + ", bloodGroup=" + bloodGroup + ", panNo=" + panNo
				+ ", adharNo=" + adharNo + ", uanNo=" + uanNo + ", createdDate=" + createdDate + ", createdBy="
				+ createdBy + ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate + ", status=" + status
				+ ", workflowStatus=" + workflowStatus + ", dateOfJoining=" + dateOfJoining + ", lastWorkingDay="
				+ lastWorkingDay + ", employeeCode=" + employeeCode + ", group=" + group + ", subGroup=" + subGroup
				+ ", personalEmail=" + personalEmail + ", nationality=" + nationality + ", workType=" + workType
				+ ", documentType=" + documentType + ", documentNumber=" + documentNumber + ", jiraId=" + jiraId
				+ ", isTaxdeclarationEnabled=" + isTaxdeclarationEnabled + ", profilePicPath=" + profilePicPath
				+ ", isProofdeclarationEnabled=" + isProofdeclarationEnabled + ", proofSubmissionEnabledDate="
				+ proofSubmissionEnabledDate + ", taxDeclarationEnabledDate=" + taxDeclarationEnabledDate
				+ ", testProfile=" + testProfile + ", employeeAddressEntityList=" + employeeAddressEntityList
				+ ", employeeBankDetailsEntity=" + employeeBankDetailsEntity
				+ ", employeeDesignationAndManagerDetailsEntity=" + employeeDesignationAndManagerDetailsEntity
				+ ", employeeWorkLocation=" + employeeWorkLocation + ", employeeDocumentDetails="
				+ employeeDocumentDetails + "]";
	}

	public String getProfilePicPath() {
		return profilePicPath;
	}

	public void setProfilePicPath(String profilePicPath) {
		this.profilePicPath = profilePicPath;
	}

	public Boolean getTestProfile() {
		return testProfile;
	}

	public void setTestProfile(Boolean testProfile) {
		this.testProfile = testProfile;
	}

	public EmployeeCtcEntity getEmployeeCtcEntity() {
		return employeeCtcEntity;
	}

	public void setEmployeeCtcEntity(EmployeeCtcEntity employeeCtcEntity) {
		this.employeeCtcEntity = employeeCtcEntity;
	}

	public Boolean getGenericProfile() {
		return genericProfile;
	}

	public void setGenericProfile(Boolean genericProfile) {
		this.genericProfile = genericProfile;
	}

	public String getGoalSetup() {
		return goalSetup;
	}

	public void setGoalSetup(String goalSetup) {
		this.goalSetup = goalSetup;
	}

}