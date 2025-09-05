package com.pransquare.nems.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_designation_and_manager_details")
public class EmployeeDesignationAndManagerDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMP_MANAGE_ID")
	private Long empManageId;
	
	@Column(name = "PERFORMANCE_GROUP")
	private String performanceGroup;
	
	@Column(name = "PERFORMANCE_SUB_GROUP")
	private String performanceSubGroup;
	
	@Column(name = "HR_MANAGER")
	private String hrManager;

	@Column(name = "PROJECT_MANAGER")
	private String projectManager;

	@Column(name = "REPORTING_LEVEL1")
	private String reportingLevel1;

	@Column(name = "REPORTING_LEVEL2")
	private String reportingLevel2;

	@Column(name = "REPORTING_LEVEL3")
	private String reportingLevel3;

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

	public Long getEmpManageId() {
		return empManageId;
	}

	public void setEmpManageId(Long empManageId) {
		this.empManageId = empManageId;
	}

	public String getHrManager() {
		return hrManager;
	}

	public void setHrManager(String hrManager) {
		this.hrManager = hrManager;
	}

	public String getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}

	public String getReportingLevel1() {
		return reportingLevel1;
	}

	public void setReportingLevel1(String reportingLevel1) {
		this.reportingLevel1 = reportingLevel1;
	}

	public String getReportingLevel2() {
		return reportingLevel2;
	}

	public void setReportingLevel2(String reportingLevel2) {
		this.reportingLevel2 = reportingLevel2;
	}

	public String getReportingLevel3() {
		return reportingLevel3;
	}

	public void setReportingLevel3(String reportingLevel3) {
		this.reportingLevel3 = reportingLevel3;
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
	
	

	public String getPerformanceGroup() {
		return performanceGroup;
	}

	public void setPerformanceGroup(String performanceGroup) {
		this.performanceGroup = performanceGroup;
	}

	public String getPerformanceSubGroup() {
		return performanceSubGroup;
	}

	public void setPerformanceSubGroup(String performanceSubGroup) {
		this.performanceSubGroup = performanceSubGroup;
	}

	@Override
	public String toString() {
		return "EmployeeDesignationAndManagerDetailsEntity [empManageId=" + empManageId + ", hrManager=" + hrManager
				+ ", projectManager=" + projectManager + ", reportingLevel1=" + reportingLevel1 + ", reportingLevel2="
				+ reportingLevel2 + ", reportingLevel3=" + reportingLevel3 + ", status=" + status + ", createdDate="
				+ createdDate + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", modifiedDate="
				+ modifiedDate + ", empBasicDetailId=" + empBasicDetailId + "]";
	}

}
