package com.pransquare.nems.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GoalAttributeModel {

	private Long empGoalAttributeId;
	private Double employeeRating;
	private Double managerRating;
	private Double finalRating;
	private LocalDate approvedDate;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	private LocalDate submittedDate;
	private Long approverId;
	private Long empBasicDetailId;
	private Long performanceReviewId;
	private String approveComments;
	private String employeeComments;
	private String finalComments;
	private String createdBy;
	private String modifiedBy;
	private String status;
	private String attribute;
	private String attributeDescription;
	private Integer hrId;

	public Long getEmpGoalAttributeId() {
		return empGoalAttributeId;
	}

	public void setEmpGoalAttributeId(Long empGoalAttributeId) {
		this.empGoalAttributeId = empGoalAttributeId;
	}

	public Double getEmployeeRating() {
		return employeeRating;
	}

	public void setEmployeeRating(Double employeeRating) {
		this.employeeRating = employeeRating;
	}

	public Double getManagerRating() {
		return managerRating;
	}

	public void setManagerRating(Double managerRating) {
		this.managerRating = managerRating;
	}

	public Double getFinalRating() {
		return finalRating;
	}

	public void setFinalRating(Double finalRating) {
		this.finalRating = finalRating;
	}

	public LocalDate getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(LocalDate approvedDate) {
		this.approvedDate = approvedDate;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public LocalDate getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(LocalDate submittedDate) {
		this.submittedDate = submittedDate;
	}

	public Long getApproverId() {
		return approverId;
	}

	public void setApproverId(Long approverId) {
		this.approverId = approverId;
	}

	public Long getEmpBasicDetailId() {
		return empBasicDetailId;
	}

	public void setEmpBasicDetailId(Long empBasicDetailId) {
		this.empBasicDetailId = empBasicDetailId;
	}

	public Long getPerformanceReviewId() {
		return performanceReviewId;
	}

	public void setPerformanceReviewId(Long performanceReviewId) {
		this.performanceReviewId = performanceReviewId;
	}

	public String getApproveComments() {
		return approveComments;
	}

	public void setApproveComments(String approveComments) {
		this.approveComments = approveComments;
	}

	public String getEmployeeComments() {
		return employeeComments;
	}

	public void setEmployeeComments(String employeeComments) {
		this.employeeComments = employeeComments;
	}

	public String getFinalComments() {
		return finalComments;
	}

	public void setFinalComments(String finalComments) {
		this.finalComments = finalComments;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getAttributeDescription() {
		return attributeDescription;
	}

	public void setAttributeDescription(String attributeDescription) {
		this.attributeDescription = attributeDescription;
	}

	public Integer getHrId() {
		return hrId;
	}

	public void setHrId(Integer hrId) {
		this.hrId = hrId;
	}

}
