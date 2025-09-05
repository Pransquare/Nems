
package com.pransquare.nems.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_project_details")
public class EmployeeProjectDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMP_PROJECT_ID")
	private Long empProjectId;

	@Column(name = "CLIENT_CODE")
	private String clientCode;

	@Column(name = "PROJECT_CODE")
	private String projectCode;

	@Column(name = "REPORTING_MANAGER")
	private String reportingManager;

	@Column(name = "ONSHORE_STATUS")
	private String onshoreStatus;

	@Column(name = "REPORTING_LOCATION")
	private String reportingLocation;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name = "EMP_BASIC_DETAIL_ID")
	private Long empBasicDetailId;

	public Long getEmpProjectId() {
		return empProjectId;
	}

	public void setEmpProjectId(Long empProjectId) {
		this.empProjectId = empProjectId;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getReportingManager() {
		return reportingManager;
	}

	public void setReportingManager(String reportingManager) {
		this.reportingManager = reportingManager;
	}

	public String getOnshoreStatus() {
		return onshoreStatus;
	}

	public void setOnshoreStatus(String onshoreStatus) {
		this.onshoreStatus = onshoreStatus;
	}

	public String getReportingLocation() {
		return reportingLocation;
	}

	public void setReportingLocation(String reportingLocation) {
		this.reportingLocation = reportingLocation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
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

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getEmpBasicDetailId() {
		return empBasicDetailId;
	}

	public void setEmpBasicDetailId(Long empBasicDetailId) {
		this.empBasicDetailId = empBasicDetailId;
	}

	@Override
	public String toString() {
		return "EmployeeProjectDetailsEntity [empProjectId=" + empProjectId + ", clientCode=" + clientCode
				+ ", projectCode=" + projectCode + ", reportingManager=" + reportingManager + ", onshoreStatus="
				+ onshoreStatus + ", reportingLocation=" + reportingLocation + ", status=" + status + ", createdDate="
				+ createdDate + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", modifiedDate="
				+ modifiedDate + ", empBasicDetailId=" + empBasicDetailId + "]";
	}

}