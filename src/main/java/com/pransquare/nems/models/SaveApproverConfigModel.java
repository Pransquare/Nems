package com.pransquare.nems.models;

import java.util.List;

public class SaveApproverConfigModel {

	private String createdBy;
	private Long empBasicDetailId;
	private List<ApproverConfigSubModel> approverConfigSubModels;


	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getEmpBasicDetailId() {
		return empBasicDetailId;
	}

	public void setEmpBasicDetailId(Long empBasicDetailId) {
		this.empBasicDetailId = empBasicDetailId;
	}

	public List<ApproverConfigSubModel> getApproverConfigSubModels() {
		return approverConfigSubModels;
	}

	public void setApproverConfigSubModels(List<ApproverConfigSubModel> approverConfigSubModels) {
		this.approverConfigSubModels = approverConfigSubModels;
	}

	@Override
	public String toString() {
		return "SaveApproverConfigModel [createdBy=" + createdBy + ", empBasicDetailId=" + empBasicDetailId
				+ ", approverConfigSubModels=" + approverConfigSubModels + "]";
	}

}
