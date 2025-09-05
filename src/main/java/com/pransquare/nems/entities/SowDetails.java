package com.pransquare.nems.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sow_details")
public class SowDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String account;
	private String sowId;

	private String sowName;
	private String po;
	private String milestoneMonth;
	private String currency;
	private Double milestoneAmount;
	private Double approxAmount;
	private LocalDate sowStartDate;
	private LocalDate sowEndDate;

	@Column(name = "delivery_manager_id")
	private Long deliveryManagerId;

	private String status = "active"; // Default to 'active'

	@ManyToOne
	@JoinColumn(name = "delivery_manager_id", referencedColumnName = "EMPLOYEE_BASIC_DETAIL_ID", updatable = false, insertable = false)
	private EmployeeEntity deliveryManager;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSowId() {
		return sowId;
	}

	public void setSowId(String sowId) {
		this.sowId = sowId;
	}

	public String getSowName() {
		return sowName;
	}

	public void setSowName(String sowName) {
		this.sowName = sowName;
	}

	public String getPo() {
		return po;
	}

	public void setPo(String po) {
		this.po = po;
	}

	public String getMilestoneMonth() {
		return milestoneMonth;
	}

	public void setMilestoneMonth(String milestoneMonth) {
		this.milestoneMonth = milestoneMonth;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getMilestoneAmount() {
		return milestoneAmount;
	}

	public void setMilestoneAmount(Double milestoneAmount) {
		this.milestoneAmount = milestoneAmount;
	}

	public Double getApproxAmount() {
		return approxAmount;
	}

	public void setApproxAmount(Double approxAmount) {
		this.approxAmount = approxAmount;
	}

	public LocalDate getSowStartDate() {
		return sowStartDate;
	}

	public void setSowStartDate(LocalDate sowStartDate) {
		this.sowStartDate = sowStartDate;
	}

	public LocalDate getSowEndDate() {
		return sowEndDate;
	}

	public void setSowEndDate(LocalDate sowEndDate) {
		this.sowEndDate = sowEndDate;
	}

	public Long getDeliveryManagerId() {
		return deliveryManagerId;
	}

	public void setDeliveryManagerId(Long deliveryManagerId) {
		this.deliveryManagerId = deliveryManagerId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public EmployeeEntity getDeliveryManager() {
		return deliveryManager;
	}

	public void setDeliveryManager(EmployeeEntity deliveryManager) {
		this.deliveryManager = deliveryManager;
	}

}
