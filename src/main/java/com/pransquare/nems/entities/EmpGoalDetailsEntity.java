package com.pransquare.nems.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "emp_goal_details")
public class EmpGoalDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_goal_details_id")
	private Long empGoalDetailsId;

	@Column(name = "emp_basic_detail_id")
	private Long empBasicDetailId;

	@Column(name = "emp_goal_setup_id")
	private Long empGoalSetupId;

	@Column(name = "created_date")
	private LocalDate createdDate;

	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "comments")
	private String comments;

	@Column(name = "self_comments")
	private String selfComments;

	@Column(name = "manager_comments")
	private String managerComments;

	@Column(name = "final_comments")
	private String finalComments;

	@Column(name = "status")
	private String status;

	@Column(name = "goal")
	private String goal;

	@Column(name = "goal_type")
	private String goalType;

	@Column(name = "goal_description")
	private String goalDescription;

	@Column(name = "from_date")
	private LocalDate fromDate;

	@Column(name = "to_date")
	private LocalDate toDate;

	@Column(name = "percentage")
	private Integer percentage;

	@Column(name = "manager_concent")
	private String managerConcent;

	@Column(name = "hr_concent")
	private String hrConcent;

	// Getters and Setters

//	@ManyToOne
//	@JoinColumn(name = "emp_goal_details_id", updatable = false, insertable = false)
//	@JsonBackReference
//	private EmpGoalSetupEntity empGoalSetupEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_goal_setup_id", insertable = false, updatable = false)
	@JsonBackReference
	private EmpGoalSetupEntity empGoalSetupEntity;

	public Long getEmpGoalDetailsId() {
		return empGoalDetailsId;
	}

	public void setEmpGoalDetailsId(Long empGoalDetailsId) {
		this.empGoalDetailsId = empGoalDetailsId;
	}

	public Long getEmpBasicDetailId() {
		return empBasicDetailId;
	}

	public void setEmpBasicDetailId(Long empBasicDetailId) {
		this.empBasicDetailId = empBasicDetailId;
	}

	public Long getEmpGoalSetupId() {
		return empGoalSetupId;
	}

	public void setEmpGoalSetupId(Long empGoalSetupId) {
		this.empGoalSetupId = empGoalSetupId;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getSelfComments() {
		return selfComments;
	}

	public void setSelfComments(String selfComments) {
		this.selfComments = selfComments;
	}

	public String getManagerComments() {
		return managerComments;
	}

	public void setManagerComments(String managerComments) {
		this.managerComments = managerComments;
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

	public EmpGoalSetupEntity getEmpGoalSetupEntity() {
		return empGoalSetupEntity;
	}

	public void setEmpGoalSetupEntity(EmpGoalSetupEntity empGoalSetupEntity) {
		this.empGoalSetupEntity = empGoalSetupEntity;
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

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public String getManagerConcent() {
		return managerConcent;
	}

	public void setManagerConcent(String managerConcent) {
		this.managerConcent = managerConcent;
	}

	public String getHrConcent() {
		return hrConcent;
	}

	public void setHrConcent(String hrConcent) {
		this.hrConcent = hrConcent;
	}

}
