
package com.pransquare.nems.dto;

import java.time.LocalDate;

import jakarta.persistence.Column;

public class VendorInputDto {
    private String vendorName;
    private String client;
    private String resource;
    private String contractType;
    private Double clientRatePerHour;
    private Double ssItRatePerHour;
    private Double rateMarginPerHour;
    private String vendorStatus;
    private String workflowStatus;
    private String status;
    private Long empId; // Employee ID (used to fetch manager)
    private LocalDate startDate;
	private LocalDate endDate;
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
	public Double getClientRatePerHour() {
		return clientRatePerHour;
	}
	public void setClientRatePerHour(Double clientRatePerHour) {
		this.clientRatePerHour = clientRatePerHour;
	}
	public Double getSsItRatePerHour() {
		return ssItRatePerHour;
	}
	public void setSsItRatePerHour(Double ssItRatePerHour) {
		this.ssItRatePerHour = ssItRatePerHour;
	}
	public Double getRateMarginPerHour() {
		return rateMarginPerHour;
	}
	public void setRateMarginPerHour(Double rateMarginPerHour) {
		this.rateMarginPerHour = rateMarginPerHour;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
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
