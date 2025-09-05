package com.pransquare.nems.models;

public class ApproverConfigSubModel {
	private Long approverId;
	private String module;
	private String status;
	private Long userApproverConfigId;

	public Long getApproverId() {
		return approverId;
	}

	public void setApproverId(Long approverId) {
		this.approverId = approverId;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getUserApproverConfigId() {
		return userApproverConfigId;
	}

	public void setUserApproverConfigId(Long userApproverConfigId) {
		this.userApproverConfigId = userApproverConfigId;
	}

	@Override
	public String toString() {
		return "ApproverConfigSubModel [approverId=" + approverId + ", module=" + module + ", status=" + status
				+ ", userApproverConfigId=" + userApproverConfigId + "]";
	}

}
