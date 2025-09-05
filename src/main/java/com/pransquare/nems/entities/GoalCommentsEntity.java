package com.pransquare.nems.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "goal_comments")
public class GoalCommentsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "goal_comment_id")
	private Long goalCommentId;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "submitteddate")
	private LocalDate submittedDate;

	@Column(name = "emp_goal_setup_id")
	private Long empGoalSetupId;

	@Column(name = "emp_basic_detail_id")
	private Long empBasicDetailId;

	@Column(name = "performance_review_id")
	private Long performanceReviewId;

	@Column(name = "comments")
	private String comments;

	@Column(name = "emp_name")
	private String empName;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "status")
	private String status;

	public Long getGoalCommentId() {
		return goalCommentId;
	}

	public void setGoalCommentId(Long goalCommentId) {
		this.goalCommentId = goalCommentId;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDate getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(LocalDate submittedDate) {
		this.submittedDate = submittedDate;
	}

	public Long getEmpGoalSetupId() {
		return empGoalSetupId;
	}

	public void setEmpGoalSetupId(Long empGoalSetupId) {
		this.empGoalSetupId = empGoalSetupId;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "GoalCommentsEntity [goalCommentId=" + goalCommentId + ", createdDate=" + createdDate
				+ ", submittedDate=" + submittedDate + ", empGoalSetupId=" + empGoalSetupId + ", empBasicDetailId="
				+ empBasicDetailId + ", performanceReviewId=" + performanceReviewId + ", comments=" + comments
				+ ", empName=" + empName + ", createdBy=" + createdBy + ", status=" + status + "]";
	}

}
