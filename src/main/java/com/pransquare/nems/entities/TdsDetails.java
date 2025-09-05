package com.pransquare.nems.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tds_details")
public class TdsDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "regime_code", nullable = false, length = 45)
	private String regimeCode;

	@Column(name = "tds_section_code", nullable = false, length = 45)
	private String tdsSectionCode;

	@Column(name = "tds_sub_section_code", nullable = false, length = 45)
	private String tdsSubSectionCode;

	@Column(name = "declared_value", length = 45)
	private String declaredValue;

	@Column(name = "approved_value", length = 45)
	private String approvedValue;

	@Column(name = "created_by", length = 100)
	private String createdBy;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "modified_by", length = 100)
	private String modifiedBy;

	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;

	@Column(name = "status", nullable = false, length = 25)
	private String status;
	@Column(name = "employee_code")
	private String employeeCode;

	@Column(name = "financial_year_code")
	private String financialYearCode;

	@OneToMany
	@JoinColumn(name = "tds_details_id", referencedColumnName = "id")
	List<TdsProofDetails> tdsProofDetails;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getDeclaredValue() {
		return declaredValue;
	}

	public void setDeclaredValue(String declaredValue) {
		this.declaredValue = declaredValue;
	}

	public String getApprovedValue() {
		return approvedValue;
	}

	public void setApprovedValue(String approvedValue) {
		this.approvedValue = approvedValue;
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

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
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

	public List<TdsProofDetails> getTdsProofDetails() {
		return tdsProofDetails;
	}

	public void setTdsProofDetails(List<TdsProofDetails> tdsProofDetails) {
		this.tdsProofDetails = tdsProofDetails;
	}

}
