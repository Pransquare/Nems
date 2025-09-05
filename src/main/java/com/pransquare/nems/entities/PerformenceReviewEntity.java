package com.pransquare.nems.entities;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "performance_review")
public class PerformenceReviewEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "performance_review_id")
	private Long performarnceReviewId;

	@Column(name = "APPROVEDBY")
	private int approvedBy;

	@Column(name = "APPROVEDDATE")
	private Date approvedDate;

	@Column(name = "APPROVERCOMMENTS")
	private String approverComments;

	@Column(name = "COMMENTS")
	private String comments;

	@Column(name = "EMP_BASIC_DETAIL_ID")
	private Long employeeBasicDetailId;

	@Column(name = "APPROVER_ID")
	private Long approverId;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "SUBMITTEDDATE")
	private Date submittedDate;

	@Column(name = "INCREMENT_PERCENTAGE")
	private Double incrementPercent;

	@Column(name = "NEW_DESIGNATION")
	private String newDesignation;

	@Column(name = "CREATED_DATE")
	private LocalDate createdDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private LocalDateTime modifiedDate;

	@Column(name = "hike_percentage")
	private Float hikePercentage;

	@Column(name = "offer_letter_path")
	private String offerLetterPath;

	public Float getHikePercentage() {
		return hikePercentage;
	}

	public void setHikePercentage(Float hikePercentage) {
		this.hikePercentage = hikePercentage;
	}

	@OneToMany(targetEntity = PerformanceDetailsEntity.class)
	@JsonManagedReference
	@JoinColumn(name = "performance_review_id", referencedColumnName = "performance_review_id")
	private List<PerformanceDetailsEntity> performanceDetails;

	@OneToMany(targetEntity = GoalAttributeEntity.class)
	@JsonManagedReference
	@JoinColumn(name = "performance_review_id", referencedColumnName = "performance_review_id")
	private List<GoalAttributeEntity> GoalAttributeEntities;

	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMP_BASIC_DETAIL_ID", referencedColumnName = "EMPLOYEE_BASIC_DETAIL_ID", updatable = false, insertable = false)
	private EmployeeEntity employeeEntity;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS", referencedColumnName = "CODE", updatable = false, insertable = false)
	private StatusMasterEntity statusMasterEntity;

	public String getEmployeeCode() {
		return employeeEntity != null ? employeeEntity.getEmployeeCode() : null;
	}

	public String getGroup() {
		return employeeEntity != null ? employeeEntity.getGroup() : null;
	}

	public String getSubGroup() {
		return employeeEntity != null ? employeeEntity.getSubGroup() : null;
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

	public int getApprovedBy() {
		return approvedBy;
	}

	public Long getPerformarnceReviewId() {
		return performarnceReviewId;
	}

	public void setPerformarnceReviewId(Long performarnceReviewId) {
		this.performarnceReviewId = performarnceReviewId;
	}

	public void setApprovedBy(int approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
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

	public Long getEmployeeBasicDetailId() {
		return employeeBasicDetailId;
	}

	public void setEmployeeBasicDetailId(Long employeeBasicDetailId) {
		this.employeeBasicDetailId = employeeBasicDetailId;
	}

	public Long getApproverId() {
		return approverId;
	}

	public void setApproverId(Long approverId) {
		this.approverId = approverId;
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

	public Date getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}

	public Double getIncrementPercent() {
		return incrementPercent;
	}

	public void setIncrementPercent(Double incrementPercent) {
		this.incrementPercent = incrementPercent;
	}

	public String getNewDesignation() {
		return newDesignation;
	}

	public void setNewDesignation(String newDesignation) {
		this.newDesignation = newDesignation;
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

	public List<PerformanceDetailsEntity> getPerformanceDetails() {
		return performanceDetails;
	}

	public void setPerformanceDetails(List<PerformanceDetailsEntity> performanceDetails) {
		this.performanceDetails = performanceDetails;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public String getOfferLetterPath() {
		return offerLetterPath;
	}

	public void setOfferLetterPath(String offerLetterPath) {
		this.offerLetterPath = offerLetterPath;
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

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public List<GoalAttributeEntity> getGoalAttributeEntities() {
		return GoalAttributeEntities;
	}

	public void setGoalAttributeEntities(List<GoalAttributeEntity> goalAttributeEntities) {
		GoalAttributeEntities = goalAttributeEntities;
	}

	@Override
	public String toString() {
		return "PerformenceReviewEntity [performarnceReviewId=" + performarnceReviewId + ", approvedBy=" + approvedBy
				+ ", approvedDate=" + approvedDate + ", approverComments=" + approverComments + ", comments=" + comments
				+ ", employeeBasicDetailId=" + employeeBasicDetailId + ", approverId=" + approverId + ", status="
				+ status + ", submittedDate=" + submittedDate + ", incrementPercent=" + incrementPercent
				+ ", newDesignation=" + newDesignation + ", createdDate=" + createdDate + ", createdBy=" + createdBy
				+ ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate + ", hikePercentage=" + hikePercentage
				+ ", offerLetterPath=" + offerLetterPath + ", performanceDetails=" + performanceDetails
				+ ", GoalAttributeEntities=" + GoalAttributeEntities + ", employeeEntity=" + employeeEntity
				+ ", statusMasterEntity=" + statusMasterEntity + "]";
	}

}
