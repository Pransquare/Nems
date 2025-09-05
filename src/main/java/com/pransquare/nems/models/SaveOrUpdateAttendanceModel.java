package com.pransquare.nems.models;

import java.time.LocalDate;
import java.util.Date;

public class SaveOrUpdateAttendanceModel {

	private Long attendanceId;
	private int approvedBy;
	private String approvercomments;
	private String clientCode;
	private String projectCode;
	private String comments;
	private Long empBasicDetailId;
	private String timeInvested;
	private Long approverId;
	private String taskName;
	private String location;
	private String status;
	private Date submittedDate;
	private String createdBy;
	private String workflowStatus;
	private LocalDate taskDate;
	private Boolean isBillable;

	public Long getAttendanceId() {
		return attendanceId;
	}

	public void setAttendanceId(Long attendanceId) {
		this.attendanceId = attendanceId;
	}

	public int getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(int approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getApprovercomments() {
		return approvercomments;
	}

	public void setApprovercomments(String approvercomments) {
		this.approvercomments = approvercomments;
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

	public String getTimeInvested() {
		return timeInvested;
	}

	public void setTimeInvested(String timeInvested) {
		this.timeInvested = timeInvested;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	@Override
	public String toString() {
		return "SaveOrUpdateAttendanceModel [attendanceId=" + attendanceId + ", approvedBy=" + approvedBy
				+ ", approvercomments=" + approvercomments + ", clientCode=" + clientCode + ", projectCode="
				+ projectCode + ", comments=" + comments + ", empBasicDetailId=" + empBasicDetailId + ", timeInvested="
				+ timeInvested + ", approverId=" + approverId + ", taskName=" + taskName + ", location=" + location
				+ ", status=" + status + ", submittedDate=" + submittedDate + ", createdBy=" + createdBy
				+ ", workflowStatus=" + workflowStatus + "]";
	}

	public Long getEmpBasicDetailId() {
		return empBasicDetailId;
	}

	public void setEmpBasicDetailId(Long empBasicDetailId) {
		this.empBasicDetailId = empBasicDetailId;
	}

	public LocalDate getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(LocalDate taskDate) {
		this.taskDate = taskDate;
	}

	public Boolean getIsBillable() {
		return isBillable;
	}

	public void setIsBillable(Boolean isBillable) {
		this.isBillable = isBillable;
	}

}
