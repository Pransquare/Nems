package com.pransquare.nems.models;

import java.time.LocalDate;

public class GoalSetUpDetails {

	private Long empGoalDetailId;
	private Integer goalSetUpId;
	private String goal;
	private String goalType;
	private String goalDescription;
	private Integer percentage;

	private Long goalSetUpDetailsId;

	private String comments;

	private Long employeeBasicDetails;

	private String finalComments;

	private String status;

	private String managerComments;
	private String managerConsent;
	private String hrConsent;

	private String createdBy;

	private LocalDate fromDate;
	private LocalDate toDate;

	public Long getGoalSetUpDetailsId() {
		return goalSetUpDetailsId;
	}

	public void setGoalSetUpDetailsId(Long goalSetUpDetailsId) {
		this.goalSetUpDetailsId = goalSetUpDetailsId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getEmployeeBasicDetails() {
		return employeeBasicDetails;
	}

	public void setEmployeeBasicDetails(Long employeeBasicDetails) {
		this.employeeBasicDetails = employeeBasicDetails;
	}

	public String getFinalComments() {
		return finalComments;
	}

	public void setFinalComments(String finalComments) {
		this.finalComments = finalComments;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getManagerComments() {
		return managerComments;
	}

	public void setManagerComments(String managerComments) {
		this.managerComments = managerComments;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getGoalSetUpId() {
		return goalSetUpId;
	}

	public void setGoalSetUpId(Integer goalSetUpId) {
		this.goalSetUpId = goalSetUpId;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public String getGoalType() {
		return goalType;
	}

	public void setGoalType(String goalType) {
		this.goalType = goalType;
	}

	public String getGoalDescription() {
		return goalDescription;
	}

	public void setGoalDescription(String goalDescription) {
		this.goalDescription = goalDescription;
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

	public Long getEmpGoalDetailId() {
		return empGoalDetailId;
	}

	public void setEmpGoalDetailId(Long empGoalDetailId) {
		this.empGoalDetailId = empGoalDetailId;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public String getManagerConsent() {
		return managerConsent;
	}

	public void setManagerConsent(String managerConsent) {
		this.managerConsent = managerConsent;
	}

	public String getHrConsent() {
		return hrConsent;
	}

	public void setHrConsent(String hrConsent) {
		this.hrConsent = hrConsent;
	}

}
