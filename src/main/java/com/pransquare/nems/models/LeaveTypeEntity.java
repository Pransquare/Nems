package com.pransquare.nems.models;

public class LeaveTypeEntity {

	private Integer leaveTypeId;
	private String leaveTypeCode;
	private String leaveTypeDescription;
	private String status;
	private String createdBy;
	private String modifiedBy;
	private String creditFrequency;
	private Float leaveCredit;

	public Integer getLeaveTypeId() {
		return leaveTypeId;
	}

	public void setLeaveTypeId(Integer leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}

	public String getLeaveTypeCode() {
		return leaveTypeCode;
	}

	public void setLeaveTypeCode(String leaveTypeCode) {
		this.leaveTypeCode = leaveTypeCode;
	}

	public String getLeaveTypeDescription() {
		return leaveTypeDescription;
	}

	public void setLeaveTypeDescription(String leaveTypeDescription) {
		this.leaveTypeDescription = leaveTypeDescription;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getCreditFrequency() {
		return creditFrequency;
	}

	public void setCreditFrequency(String creditFrequency) {
		this.creditFrequency = creditFrequency;
	}

	public Float getLeaveCredit() {
		return leaveCredit;
	}

	public void setLeaveCredit(Float leaveCredit) {
		this.leaveCredit = leaveCredit;
	}

	@Override
	public String toString() {
		return "LeaveTypeEntity [leaveTypeId=" + leaveTypeId + ", leaveTypeCode=" + leaveTypeCode
				+ ", leaveTypeDescription=" + leaveTypeDescription + ", status=" + status + ", createdBy=" + createdBy
				+ ", modifiedBy=" + modifiedBy + ", creditFrequency=" + creditFrequency + ", leaveCredit=" + leaveCredit
				+ "]";
	}

}
