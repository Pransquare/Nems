package com.pransquare.nems.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "timesheet_master")
@Table(name = "timesheet_master")
public class TimesheetMasterEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long timesheetMasterId;

	@Column(name = "task_date")
	private LocalDate taskDate;

	@Column(name = "employee_code")
	private String employeeCode;

	@Column(name = "employee_name")
	private String employeeName;

	@Column(name = "project_code")
	private String projectCode;

	@Column(name = "project_name")
	private String projectName;

	@Column(name = "total_time")
	private String totalTime;

	@Column(name = "project_lead")
	private String projectLead;

	@Column(name = "project_manager")
	private String projectManager;

	@Column(name = "task_name")
	private String taskName;

	@Column(name = "client_name")
	private String clientName;

	@Column(name = "status")
	private String status;

	@Column(name = "workflow_status")
	private String workflowStatus;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;

	@Column(name = "emp_basic_detail_id")
	private Long empBasicDetailId;

	public Long getTimesheetMasterId() {
		return timesheetMasterId;
	}

	public void setTimesheetMasterId(Long timesheetMasterId) {
		this.timesheetMasterId = timesheetMasterId;
	}

	public LocalDate getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(LocalDate taskDate) {
		this.taskDate = taskDate;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public String getProjectLead() {
		return projectLead;
	}

	public void setProjectLead(String projectLead) {
		this.projectLead = projectLead;
	}

	public String getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
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

	public Long getEmpBasicDetailId() {
		return empBasicDetailId;
	}

	public void setEmpBasicDetailId(Long empBasicDetailId) {
		this.empBasicDetailId = empBasicDetailId;
	}

	@Override
	public String toString() {
		return "TimesheetMasterEntity [timesheetMasterId=" + timesheetMasterId + ", taskDate=" + taskDate
				+ ", employeeCode=" + employeeCode + ", employeeName=" + employeeName + ", projectCode=" + projectCode
				+ ", projectName=" + projectName + ", totalTime=" + totalTime + ", projectLead=" + projectLead
				+ ", projectManager=" + projectManager + ", taskName=" + taskName + ", clientName=" + clientName
				+ ", status=" + status + ", workflowStatus=" + workflowStatus + ", createdDate=" + createdDate
				+ ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate
				+ ", empBasicDetailId=" + empBasicDetailId + "]";
	}

}
