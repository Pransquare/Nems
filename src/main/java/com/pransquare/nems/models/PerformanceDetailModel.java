package com.pransquare.nems.models;

import java.sql.Date;

public class PerformanceDetailModel {
	
    private Long performanceDetailsId;

    private String performanceParameter;

    private String performanceGroup;

    private String performanceSubGroup;

    private String comments;

    private Long employeeBasicDetails;

    private String finalRating;

    private String finalComments;

    private String status;

    private String selfRating;

    private String managerRating;

    private String managerComments;

    private Date createdDate;

    private String createdBy;

    private String modifiedBy;

    private Date modifiedDate;
    
    private Integer achievedPercentege;

	public Long getPerformanceDetailsId() {
		return performanceDetailsId;
	}

	public void setPerformanceDetailsId(Long performanceDetailsId) {
		this.performanceDetailsId = performanceDetailsId;
	}

	public String getPerformanceParameter() {
		return performanceParameter;
	}

	public void setPerformanceParameter(String performanceParameter) {
		this.performanceParameter = performanceParameter;
	}

	public String getPerformanceGroup() {
		return performanceGroup;
	}

	public void setPerformanceGroup(String performanceGroup) {
		this.performanceGroup = performanceGroup;
	}

	public String getPerformanceSubGroup() {
		return performanceSubGroup;
	}

	public void setPerformanceSubGroup(String performanceSubGroup) {
		this.performanceSubGroup = performanceSubGroup;
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

	public Integer getAchievedPercentege() {
		return achievedPercentege;
	}

	public void setAchievedPercentege(Integer achievedPercentege) {
		this.achievedPercentege = achievedPercentege;
	}
    
    

}
