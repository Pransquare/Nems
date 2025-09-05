package com.pransquare.nems.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "emp_goal_setup")
public class EmpGoalSetupEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_goal_setup_id")
	private Long empGoalSetupId;

	@Column(name = "approvedby")
	private Integer approvedBy;

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

	@Column(name = "approvercomments")
	private String approverComments;

	@Column(name = "comments")
	private String comments;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "status")
	private String status;

	@Column(name = "from_date")
	private LocalDate fromDate;

	@Column(name = "to_date")
	private LocalDate toDate;
	
	@Column(name = "hr_id")
	private Long hrId;

	@OneToMany(targetEntity = EmpGoalDetailsEntity.class)
	@OrderBy("createdDate ASC")
	@JsonManagedReference
	@JoinColumn(name = "emp_goal_setup_id", referencedColumnName = "emp_goal_setup_id")
	private List<EmpGoalDetailsEntity> empGoalDetailsEntity;
	
	@OneToMany(targetEntity = GoalCommentsEntity.class)
	@OrderBy("createdDate ASC")
	@JsonManagedReference
	@JoinColumn(name = "emp_goal_setup_id", referencedColumnName = "emp_goal_setup_id")
	private List<GoalCommentsEntity> goalCommentsEntities;
	
	

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_basic_detail_id", referencedColumnName = "EMPLOYEE_BASIC_DETAIL_ID", updatable = false, insertable = false)
	private EmployeeEntity employeeEntity;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status", referencedColumnName = "CODE", updatable = false, insertable = false)
	private StatusMasterEntity statusMasterEntity;

	// Getters and Setters

	public Long getEmpGoalSetupId() {
		return empGoalSetupId;
	}

	public void setEmpGoalSetupId(Long empGoalSetupId) {
		this.empGoalSetupId = empGoalSetupId;
	}

	public Integer getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Integer approvedBy) {
		this.approvedBy = approvedBy;
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

	public String getEmployeeCode() {
		return employeeEntity != null ? employeeEntity.getEmployeeCode() : null;
	}

	public String getEmailId() {
		return employeeEntity != null ? employeeEntity.getEmailId() : null;
	}

	public String getFullName() {
		return employeeEntity != null ? employeeEntity.getFullName() : null;
	}

	public String getStatusDescription() {
		return statusMasterEntity != null ? statusMasterEntity.getDescription() : null;
	}

	public String getDesignation() {
		return employeeEntity != null ? employeeEntity.getDesignation() : null;
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

	public List<EmpGoalDetailsEntity> getEmpGoalDetailsEntity() {
		return empGoalDetailsEntity;
	}

	public void setEmpGoalDetailsEntity(List<EmpGoalDetailsEntity> empGoalDetailsEntity) {
		this.empGoalDetailsEntity = empGoalDetailsEntity;
	}

	public EmployeeEntity getEmployeeEntity() {
		return employeeEntity;
	}

	public void setEmployeeEntity(EmployeeEntity employeeEntity) {
		this.employeeEntity = employeeEntity;
	}

	public StatusMasterEntity getStatusMasterEntity() {
		return statusMasterEntity;
	}

	public void setStatusMasterEntity(StatusMasterEntity statusMasterEntity) {
		this.statusMasterEntity = statusMasterEntity;
	}

	public Long getHrId() {
		return hrId;
	}

	public void setHrId(Long hrId) {
		this.hrId = hrId;
	}

	public List<GoalCommentsEntity> getGoalCommentsEntities() {
		return goalCommentsEntities;
	}

	public void setGoalCommentsEntities(List<GoalCommentsEntity> goalCommentsEntities) {
		this.goalCommentsEntities = goalCommentsEntities;
	}

}
