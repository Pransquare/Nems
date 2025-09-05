
package com.pransquare.nems.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;

public class VendorSearchDto {
	private String vendorName;
	private String client;
	private List<String> workflowStatuses;
	private Long managerId;
	private int page;
	private int size;
	private LocalDate startDate;
	private LocalDate endDate;
	private String vendorStatus;
	private String resource;

	// Constructors
	public VendorSearchDto() {
	}
	
	
	
	public VendorSearchDto(String vendorName, String client, List<String> workflowStatuses, Long managerId, int page,
			int size, LocalDate startDate, LocalDate endDate, String vendorStatus, String resource) {
		super();
		this.vendorName = vendorName;
		this.client = client;
		this.workflowStatuses = workflowStatuses;
		this.managerId = managerId;
		this.page = page;
		this.size = size;
		this.startDate = startDate;
		this.endDate = endDate;
		this.vendorStatus = vendorStatus;
		this.resource = resource;
	}



	public String getVendorStatus() {
		return vendorStatus;
	}

	public void setVendorStatus(String vendorStatus) {
		this.vendorStatus = vendorStatus;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	// Getters and Setters
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

	public List<String> getWorkflowStatuses() {
		return workflowStatuses;
	}

	public void setWorkflowStatuses(List<String> workflowStatuses) {
		this.workflowStatuses = workflowStatuses;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
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
