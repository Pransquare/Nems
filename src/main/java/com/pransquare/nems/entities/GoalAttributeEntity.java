package com.pransquare.nems.entities;

import jakarta.persistence.*;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "emp_goal_attributes")
public class GoalAttributeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_goal_attribute_id")
    private Long empGoalAttributeId;

    @Column(name = "employee_rating")
    private Double employeeRating;

    @Column(name = "manager_rating")
    private Double managerRating;

    @Column(name = "final_rating")
    private Double finalRating;

    @Column(name = "approveddate")
    private LocalDate approvedDate;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "submitteddate")
    private LocalDate submittedDate;

    @Column(name = "approver_id")
    private Long approverId;

    @Column(name = "emp_basic_detail_id")
    private Long empBasicDetailId;

    @Column(name = "performance_review_id")
    private Long performanceReviewId;

    @Column(name = "approve_comments")
    private String approveComments;

    @Column(name = "employee_comments")
    private String employeeComments;

    @Column(name = "final_comments")
    private String finalComments;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "status")
    private String status;

    @Column(name = "attribute", length = 500)
    private String attribute;

    @Column(name = "attribute_description", length = 500)
    private String attributeDescription;

    @Column(name = "hr_id")
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

