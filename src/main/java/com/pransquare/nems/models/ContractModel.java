package com.pransquare.nems.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;

public class ContractModel {

	private Long id;
	private String contractId;
	private String customerGroup;
	private String customerEntity;
	private String contractPaymentTerm;
	private String contractName;
	private String contractCurrency;
	private LocalDate contractStartDate;
	private LocalDate contractEndDate;
	private Double contractValue;
	private String createdBy;
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getCustomerGroup() {
		return customerGroup;
	}

	public void setCustomerGroup(String customerGroup) {
		this.customerGroup = customerGroup;
	}

	public String getCustomerEntity() {
		return customerEntity;
	}

	public void setCustomerEntity(String customerEntity) {
		this.customerEntity = customerEntity;
	}

	public String getContractPaymentTerm() {
		return contractPaymentTerm;
	}

	public void setContractPaymentTerm(String contractPaymentTerm) {
		this.contractPaymentTerm = contractPaymentTerm;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getContractCurrency() {
		return contractCurrency;
	}

	public void setContractCurrency(String contractCurrency) {
		this.contractCurrency = contractCurrency;
	}

	public LocalDate getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(LocalDate contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public LocalDate getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(LocalDate contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public Double getContractValue() {
		return contractValue;
	}

	public void setContractValue(Double contractValue) {
		this.contractValue = contractValue;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
