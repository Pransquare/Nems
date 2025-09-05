package com.pransquare.nems.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contract")
public class ContractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "contract_code")
	private String contractId;

	@Column(name = "customer_group")
	private String customerGroup;

	@Column(name = "customer_entity")
	private String customerEntity;

	@Column(name = "contract_payment_term")
	private String contractPaymentTerm;

	@Column(name = "contract_name")
	private String contractName;

	@Column(name = "contract_currency")
	private String contractCurrency;

	@Column(name = "contract_start_date")
	private LocalDate contractStartDate;

	@Column(name = "contract_end_date")
	private LocalDate contractEndDate;

	@Column(name = "contract_value")
	private Double contractValue;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "status")
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

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ContractEntity [id=" + id + ", contractId=" + contractId + ", customerGroup=" + customerGroup
				+ ", customerEntity=" + customerEntity + ", contractPaymentTerm=" + contractPaymentTerm
				+ ", contractName=" + contractName + ", contractCurrency=" + contractCurrency + ", contractStartDate="
				+ contractStartDate + ", contractEndDate=" + contractEndDate + ", contractValue=" + contractValue
				+ ", createdDate=" + createdDate + ", modifiedDate=" + modifiedDate + ", createdBy=" + createdBy
				+ ", modifiedBy=" + modifiedBy + ", status=" + status + "]";
	}

}
