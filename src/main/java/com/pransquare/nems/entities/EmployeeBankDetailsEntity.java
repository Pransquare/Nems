package com.pransquare.nems.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_bank_details")
public class EmployeeBankDetailsEntity {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMP_BANK_DETAIL_ID")
	private Integer empBankDetailId;

	@Id
	@Column(name = "EMP_BASIC_DETAIL_ID")
	private Long employeeId;

	@Column(name = "ACCOUNT_TYPE", length = 50)
	private String accountType;

	@Column(name = "BANK_ACCOUNT_NO", length = 100)
	private String bankAccountNo;

	@Column(name = "BANK_IFSC", length = 20)
	private String bankIfsc;

	@Column(name = "CREATEDDATE")
	private LocalDateTime createdDate;

	@Column(name = "CREATEDBY", length = 50)
	private String createdBy;

	@Column(name = "MODIFIEDBY", length = 50)
	private String modifiedBy;

	@Column(name = "MODIFIEDDATE")
	private LocalDateTime modifiedDate;
	
	@Column(name = "BANK_NAME")
	private String bankName;
	
	@Column(name="BRANCH_NAME")
	private String branchName;

	@Column(name="BANK_ADDRESS")
	private String bankAddress;

	public Integer getEmpBankDetailId() {
		return empBankDetailId;
	}

	public void setEmpBankDetailId(Integer empBankDetailId) {
		this.empBankDetailId = empBankDetailId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getBankIfsc() {
		return bankIfsc;
	}

	public void setBankIfsc(String bankIfsc) {
		this.bankIfsc = bankIfsc;
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

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBankAddress() {
		return bankAddress;
	}

	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}

	

}
