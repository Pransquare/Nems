package com.pransquare.nems.entities;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "payroll_staging")
public class PayrollStagingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payroll_staging_id")
	private Long payrollStagingId;

	@Column(name = "employee_code")
	private String employeeCode;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "month")
	private String month;

	@Column(name = "working_days")
	private Double noOfDays;

	@Column(name = "lop")
	private Double lop;

	@Column(name = "basic_salary")
	private Double basicSalary;

	@Column(name = "hra")
	private Double hra;

	@Column(name = "medical_allowance")
	private Double medicalAllowance;

	@Column(name = "special_allowance")
	private Double specialAllowance;

	@Column(name = "other_allowance")
	private Double otherAllowance;

	@Column(name = "employer_pf")
	private Double employerPf;

	@Column(name = "employee_pf")
	private Double employeePf;

	@Column(name = "pt")
	private Double pt;

	@Column(name = "insurance")
	private Double insurance;

	@Column(name = "variable_pay_reserve")
	private Double variablePayReserve;

	@Column(name = "variable_pay_release")
	private Double variablePayRelease;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "status")
	private String status;

	@Column(name = "gross_salary")
	private Double grossSalary;

	@Column(name = "net_salary")
	private Double netSalary;

	@Column(name = "year")
	private String year;

	@Column(name = "file_id")
	private String fileId;

	@Column(name = "error_details")
	private String errorDetails;

	@Column(name = "conveyance")
	private Double conveyance;

	@Column(name = "deductions")
	private Double deductions;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "status", referencedColumnName = "CODE", insertable = false, updatable = false)
	private StatusMasterEntity statusMasterEntity;

	public Long getPayrollStagingId() {
		return payrollStagingId;
	}

	public void setPayrollStagingId(Long payrollStagingId) {
		this.payrollStagingId = payrollStagingId;
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

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Double getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(Double noOfDays) {
		this.noOfDays = noOfDays;
	}

	public Double getLop() {
		return lop;
	}

	public void setLop(Double lop) {
		this.lop = lop;
	}

	public Double getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(Double basicSalary) {
		this.basicSalary = basicSalary;
	}

	public Double getHra() {
		return hra;
	}

	public void setHra(Double hra) {
		this.hra = hra;
	}

	public Double getMedicalAllowance() {
		return medicalAllowance;
	}

	public void setMedicalAllowance(Double medicalAllowance) {
		this.medicalAllowance = medicalAllowance;
	}

	public Double getSpecialAllowance() {
		return specialAllowance;
	}

	public void setSpecialAllowance(Double specialAllowance) {
		this.specialAllowance = specialAllowance;
	}

	public Double getOtherAllowance() {
		return otherAllowance;
	}

	public void setOtherAllowance(Double otherAllowance) {
		this.otherAllowance = otherAllowance;
	}

	public Double getEmployerPf() {
		return employerPf;
	}

	public void setEmployerPf(Double employerPf) {
		this.employerPf = employerPf;
	}

	public Double getEmployeePf() {
		return employeePf;
	}

	public void setEmployeePf(Double employeePf) {
		this.employeePf = employeePf;
	}

	public Double getPt() {
		return pt;
	}

	public void setPt(Double pt) {
		this.pt = pt;
	}

	public Double getInsurance() {
		return insurance;
	}

	public void setInsurance(Double insurance) {
		this.insurance = insurance;
	}

	public Double getVariablePayReserve() {
		return variablePayReserve;
	}

	public void setVariablePayReserve(Double variablePayReserve) {
		this.variablePayReserve = variablePayReserve;
	}

	public Double getVariablePayRelease() {
		return variablePayRelease;
	}

	public void setVariablePayRelease(Double variablePayRelease) {
		this.variablePayRelease = variablePayRelease;
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

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getGrossSalary() {
		return grossSalary;
	}

	public void setGrossSalary(Double grossSalary) {
		this.grossSalary = grossSalary;
	}

	public Double getNetSalary() {
		return netSalary;
	}

	public void setNetSalary(Double netSalary) {
		this.netSalary = netSalary;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}

	public StatusMasterEntity getStatusMasterEntity() {
		return statusMasterEntity;
	}

	public void setStatusMasterEntity(StatusMasterEntity statusMasterEntity) {
		this.statusMasterEntity = statusMasterEntity;
	}

	public Double getConveyance() {
		return conveyance;
	}

	public void setConveyance(Double conveyance) {
		this.conveyance = conveyance;
	}

	public Double getDeductions() {
		return deductions;
	}

	public void setDeductions(Double deductions) {
		this.deductions = deductions;
	}

}
