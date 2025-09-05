package com.pransquare.nems.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "performance_details")
public class PerformanceDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "performance_details_id")
	private Long performanceDetailsId;

	@Column(name = "performance_parameter")
	private String performanceParameter;

	@Column(name = "performance_group")
	private String performanceGroup;

	@Column(name = "performance_sub_group")
	private String performenceSubGroup;

	@Column(name = "comments")
	private String comments;

	@Column(name = "EMP_BASIC_DETAIL_ID")
	private Long employeeBasicDetais;

	@Column(name = "final_rating")
	private String finalRating;

	@Column(name = "final_comments")
	private String finalComments;

	@Column(name = "performance_review_id")
	private Long performanceReviewId;

	@Column(name = "status")
	private String status;

	@Column(name = "self_rating")
	private String selfRating;

	@Column(name = "manager_rating")
	private String managerRating;

	@Column(name = "manager_comments")
	private String managerComments;

	@Column(name = "CREATED_DATE")
	private LocalDate createdDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private LocalDateTime mofiedDate;

	@Column(name = "goal_percentage")
	private Integer goalPercentage;

	@Column(name = "achieved_percentage")
	private Integer achievedPercentage;

	@Column(name = "goal_description")
	private String goalDecription;

	@ManyToOne
	@JoinColumn(name = "performance_details_id", updatable = false, insertable = false)
	@JsonBackReference
	private PerformenceReviewEntity performanceReview;

	public Long getPerformanceDetailsId() {
		return performanceDetailsId;
	}

	public void setPerformanceDetailsId(Long performanceDetailsId) {
		this.performanceDetailsId = performanceDetailsId;
	}

	public String getPerformanceGroup() {
		return performanceGroup;
	}

	public void setPerformanceGroup(String performanceGroup) {
		this.performanceGroup = performanceGroup;
	}

	public String getPerformenceSubGroup() {
		return performenceSubGroup;
	}

	public void setPerformenceSubGroup(String performenceSubGroup) {
		this.performenceSubGroup = performenceSubGroup;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getEmployeeBasicDetais() {
		return employeeBasicDetais;
	}

	public void setEmployeeBasicDetais(Long employeeBasicDetais) {
		this.employeeBasicDetais = employeeBasicDetais;
	}

	public Long getPerformanceReviewId() {
		return performanceReviewId;
	}

	public void setPerformanceReviewId(Long performanceReviewId) {
		this.performanceReviewId = performanceReviewId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSelfRating() {
		return selfRating;
	}

	public void setSelfRating(String selfRating) {
		this.selfRating = selfRating;
	}

	public String getManagerRating() {
		return managerRating;
	}

	public void setManagerRating(String managerRating) {
		this.managerRating = managerRating;
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

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getPerformanceParameter() {
		return performanceParameter;
	}

	public void setPerformanceParameter(String performanceParameter) {
		this.performanceParameter = performanceParameter;
	}

	public String getFinalRating() {
		return finalRating;
	}

	public void setFinalRating(String finalRating) {
		this.finalRating = finalRating;
	}

	public String getFinalComments() {
		return finalComments;
	}

	public void setFinalComments(String finalComments) {
		this.finalComments = finalComments;
	}

	public PerformenceReviewEntity getPerformanceReview() {
		return performanceReview;
	}

	public void setPerformanceReview(PerformenceReviewEntity performanceReview) {
		this.performanceReview = performanceReview;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getMofiedDate() {
		return mofiedDate;
	}

	public void setMofiedDate(LocalDateTime mofiedDate) {
		this.mofiedDate = mofiedDate;
	}

	public Integer getGoalPercentage() {
		return goalPercentage;
	}

	public void setGoalPercentage(Integer goalPercentage) {
		this.goalPercentage = goalPercentage;
	}

	public Integer getAchievedPercentage() {
		return achievedPercentage;
	}

	public void setAchievedPercentage(Integer achievedPercentage) {
		this.achievedPercentage = achievedPercentage;
	}

	public String getGoalDecription() {
		return goalDecription;
	}

	public void setGoalDecription(String goalDecription) {
		this.goalDecription = goalDecription;
	}

}
