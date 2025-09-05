package com.pransquare.nems.models;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class GoalSetUpSaveModel {
	
	private Long empGoalSetupId;
	private int approvedBy;
	private LocalDate approvedDate;
	private String approverComments;
	private String comments;
	private long emplBasicId;
	private long approverId;
	private String status;
	private LocalDate submittedDate;
	private String createdBy;
	private LocalDate fromDate;
	private LocalDate toDate;

	List<GoalSetUpDetails> goalSetUpDetails;

	public int getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(int approvedBy) {
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public long getEmplBasicId() {
		return emplBasicId;
	}

	public void setEmplBasicId(long emplBasicId) {
		this.emplBasicId = emplBasicId;
	}

	public long getApproverId() {
		return approverId;
	}

	public void setApproverId(long approverId) {
		this.approverId = approverId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(LocalDate submittedDate) {
		this.submittedDate = submittedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public List<GoalSetUpDetails> getGoalSetUpDetails() {
		return goalSetUpDetails;
	}

	public void setGoalSetUpDetails(List<GoalSetUpDetails> goalSetUpDetails) {
		this.goalSetUpDetails = goalSetUpDetails;
	}

	public Long getEmpGoalSetupId() {
		return empGoalSetupId;
	}

	public void setEmpGoalSetupId(Long empGoalSetupId) {
		this.empGoalSetupId = empGoalSetupId;
	}

}
