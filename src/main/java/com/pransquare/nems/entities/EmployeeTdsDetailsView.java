package com.pransquare.nems.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_tds_details_view", schema = "emsportal")
public class EmployeeTdsDetailsView {

	@Id
	private Integer id;

	private String createdBy;
	private LocalDateTime createdDate;
	private String declaredValue;
	private String employeeCode;
	private String approvedValue;
	private String modifiedBy;
	private LocalDateTime modifiedDate;
	private String status;
	private String tdsSectionCode;
	private String tdsSubSectionCode;
	private String regimeCode;
	private String financialYearCode;
	private String tdsSectionDescription;
	private String tdsSubSectionDescription;
	private String regimeCodeDescription;
	private String financialYearDescription;

	@OneToMany
	@JoinColumn(name = "tds_details_id", referencedColumnName = "id")
	List<TdsProofDetails> tdsProofDetails;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getDeclaredValue() {
		return declaredValue;
	}

	public void setDeclaredValue(String declaredValue) {
		this.declaredValue = declaredValue;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getApprovedValue() {
		return approvedValue;
	}

	public void setApprovedValue(String approvedValue) {
		this.approvedValue = approvedValue;
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

	public String getTdsSectionCode() {
		return tdsSectionCode;
	}

	public void setTdsSectionCode(String tdsSectionCode) {
		this.tdsSectionCode = tdsSectionCode;
	}

	public String getTdsSubSectionCode() {
		return tdsSubSectionCode;
	}

	public void setTdsSubSectionCode(String tdsSubSectionCode) {
		this.tdsSubSectionCode = tdsSubSectionCode;
	}

	public String getRegimeCode() {
		return regimeCode;
	}

	public void setRegimeCode(String regimeCode) {
		this.regimeCode = regimeCode;
	}

	public String getFinancialYearCode() {
		return financialYearCode;
	}

	public void setFinancialYearCode(String financialYearCode) {
		this.financialYearCode = financialYearCode;
	}

	public String getTdsSectionDescription() {
		return tdsSectionDescription;
	}

	public void setTdsSectionDescription(String tdsSectionDescription) {
		this.tdsSectionDescription = tdsSectionDescription;
	}

	public String getTdsSubSectionDescription() {
		return tdsSubSectionDescription;
	}

	public void setTdsSubSectionDescription(String tdsSubSectionDescription) {
		this.tdsSubSectionDescription = tdsSubSectionDescription;
	}

	public String getRegimeCodeDescription() {
		return regimeCodeDescription;
	}

	public void setRegimeCodeDescription(String regimeCodeDescription) {
		this.regimeCodeDescription = regimeCodeDescription;
	}

	public String getFinancialYearDescription() {
		return financialYearDescription;
	}

	public void setFinancialYearDescription(String financialYearDescription) {
		this.financialYearDescription = financialYearDescription;
	}

	public List<TdsProofDetails> getTdsProofDetails() {
		return tdsProofDetails;
	}

	public void setTdsProofDetails(List<TdsProofDetails> tdsProofDetails) {
		this.tdsProofDetails = tdsProofDetails;
	}

}
