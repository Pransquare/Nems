package com.pransquare.nems.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "attendancedetail")
public class AttendanceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "attendance_id")
	private Long attendanceId;

	@Column(name = "approved_by")
	private Long approvedBy;

	@Column(name = "approved_date")
	private LocalDate approvedDate;

	@Column(name = "approvercomments")
	private String approverComments;

	@Column(name = "client_code")
	private String clientCode;

	@Column(name = "project_code")
	private String projectCode;

	@Column(name = "comments")
	private String comments;

	@Column(name = "EMP_BASIC_DETAIL_ID")
	private Long empBasicDetailId;

	@Column(name = "hours")
	private int hours;

	@Column(name = "minutes")
	private int minutes;

	@Column(name = "time_invested")
	private String timeInvested;

	@Column(name = "approver_id")
	private Long approverId;

	@Column(name = "task_name")
	private String taskName;

	@Column(name = "location")
	private String location;

	@Column(name = "status")
	private String status;

	@Column(name = "submitted_date")
	private Date submittedDate;

	@Column(name = "CREATED_DATE")
	private LocalDateTime createdDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private LocalDateTime modifiedDate;

	@Column(name = "workflow_status")
	private String workflowStatus;

	@Column(name = "task_description")
	private String taskDescription;

	@Column(name = "task_date")
	private LocalDate taskDate;

	@Column(name = "is_billable")
	private Boolean isBillable;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "status", referencedColumnName = "CODE", insertable = false, updatable = false)
	private StatusMasterEntity statusMasterEntity;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "task_name", referencedColumnName = "task_code", insertable = false, updatable = false)
	private TaskMasterEntity taskMasterEntity;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "EMP_BASIC_DETAIL_ID", referencedColumnName = "EMPLOYEE_BASIC_DETAIL_ID", insertable = false, updatable = false)
	private EmployeeEntity employeeEntity;

	public Long getAttendanceId() {
		return attendanceId;
	}

	public void setAttendanceId(Long attendanceId) {
		this.attendanceId = attendanceId;
	}

	public Long getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Long approvedBy) {
		this.approvedBy = approvedBy;
	}

	public LocalDate getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(LocalDate approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getApproverComments() {
		return approverComments;
	}

	public void setApproverComments(String approverComments) {
		this.approverComments = approverComments;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getEmpBasicDetailId() {
		return empBasicDetailId;
	}

	public void setEmpBasicDetailId(Long empBasicDetailId) {
		this.empBasicDetailId = empBasicDetailId;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public Long getApproverId() {
		return approverId;
	}

	public void setApproverId(Long approverId) {
		this.approverId = approverId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
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

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public String getTimeInvested() {
		return timeInvested;
	}

	public void setTimeInvested(String timeInvested) {
		this.timeInvested = timeInvested;
	}

	public String getTaskDescription() {
		return taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}

	public Boolean getIsBillable() {
		return isBillable;
	}

	public void setIsBillable(Boolean isBillable) {
		this.isBillable = isBillable;
	}

	public StatusMasterEntity getStatusMasterEntity() {
		return statusMasterEntity;
	}

	public void setStatusMasterEntity(StatusMasterEntity statusMasterEntity) {
		this.statusMasterEntity = statusMasterEntity;
	}

	public EmployeeEntity getEmployeeEntity() {
		return employeeEntity;
	}

	public void setEmployeeEntity(EmployeeEntity employeeEntity) {
		this.employeeEntity = employeeEntity;
	}

	public TaskMasterEntity getTaskMasterEntity() {
		return taskMasterEntity;
	}

	public void setTaskMasterEntity(TaskMasterEntity taskMasterEntity) {
		this.taskMasterEntity = taskMasterEntity;
	}

	public LocalDate getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(LocalDate taskDate) {
		this.taskDate = taskDate;
	}

	@Override
	public String toString() {
		return "AttendanceEntity [attendanceId=" + attendanceId + ", approvedBy=" + approvedBy + ", approvedDate="
				+ approvedDate + ", approverComments=" + approverComments + ", clientCode=" + clientCode
				+ ", projectCode=" + projectCode + ", comments=" + comments + ", empBasicDetailId=" + empBasicDetailId
				+ ", hours=" + hours + ", minutes=" + minutes + ", timeInvested=" + timeInvested + ", approverId="
				+ approverId + ", taskName=" + taskName + ", location=" + location + ", status=" + status
				+ ", submittedDate=" + submittedDate + ", createdDate=" + createdDate + ", createdBy=" + createdBy
				+ ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate + ", workflowStatus=" + workflowStatus
				+ ", taskDescription=" + taskDescription + ", taskDate=" + taskDate + ", isBillable=" + isBillable
				+ ", statusMasterEntity=" + statusMasterEntity + ", taskMasterEntity=" + taskMasterEntity
				+ ", employeeEntity=" + employeeEntity + "]";
	}

}