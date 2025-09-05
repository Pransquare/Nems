package com.pransquare.nems.models;

import java.sql.Date;
import java.util.List;

public class PerformenceReviewModel {

	private long performrnceReviewId;
	private int approvedBy;
	private Date approvedDate;
	private String approverComments;
	private String comments;
	private long emplBasicId;
	private long approverId;
	private String status;
	private Date submittedDate;
	private Double incrementPercent;
	private String newDesignation;
	private Date createdDate;
	private String createdBy;
	private String modifiedBy;
	private Date modifiedDate;
	private Float percentageOfHike;

	List<PerformanceDetailModel> performanceDetails;

	public long getPerformrnceReviewId() {
		return performrnceReviewId;
	}

	public void setPerformrnceReviewId(long performrnceReviewId) {
		this.performrnceReviewId = performrnceReviewId;
	}

	public int getApprovedBy() {
		return approvedBy;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public List<PerformanceDetailModel> getPerformanceDetails() {
		return performanceDetails;
	}

	public void setPerformanceDetails(List<PerformanceDetailModel> performanceDetails) {
		this.performanceDetails = performanceDetails;
	}

	public Float getPercentageOfHike() {
		return percentageOfHike;
	}

	public void setPercentageOfHike(Float percentageOfHike) {
		this.percentageOfHike = percentageOfHike;
	}

}
