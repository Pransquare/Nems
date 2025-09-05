
package com.pransquare.nems.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "vendor")
public class Vendor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "vendor_name", nullable = false)
	private String vendorName;

	@Column(name = "client", nullable = false)
	private String client;

	@Column(name = "resource", nullable = false)
	private String resource;

	@Column(name = "contract_type", nullable = false)
	private String contractType;

	@Column(name = "client_rate", nullable = false)
	private Double clientRate;

	@Column(name = "ssit_rate", nullable = false)
	private Double ssitRate;

	@Column(name = "rate_margin")
	private Double rateMargin;

	@Column(name = "vendor_status")
	private String vendorStatus;

	@Column(name = "workflow_status")
	private String workflowStatus;

	@Column(name = "MANAGER_ID")
	private Long managerId;

	@Column(name = "emp_id")
	private Long empId;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@OneToOne
	@JoinColumn(name = "workflow_status", referencedColumnName = "CODE", insertable = false, updatable = false)
	private StatusMasterEntity status;
	
	@Column(name = "remarks")
	private String remarks;

	public String getRemarks() {
	    return remarks;
	}

	public void setRemarks(String remarks) {
	    this.remarks = remarks;
	}


	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public Double getClientRate() {
		return clientRate;
	}

	public void setClientRate(Double clientRate) {
		this.clientRate = clientRate;
	}

	public Double getSsitRate() {
		return ssitRate;
	}

	public void setSsitRate(Double ssitRate) {
		this.ssitRate = ssitRate;
	}

	public Double getRateMargin() {
		return rateMargin;
	}

	public void setRateMargin(Double rateMargin) {
		this.rateMargin = rateMargin;
	}

	public String getVendorStatus() {
		return vendorStatus;
	}

	public void setVendorStatus(String vendorStatus) {
		this.vendorStatus = vendorStatus;
	}

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public StatusMasterEntity getStatus() {
		return status;
	}

	public void setStatus(StatusMasterEntity status) {
		this.status = status;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
}

