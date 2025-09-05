package com.pransquare.nems.models;

public class AppraisalUploadModel {

	private String createdBy;
	private Long empBasicDetailId;
	private String Status;

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

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	@Override
	public String toString() {
		return "AppraisalUploadModel [createdBy=" + createdBy + ", empBasicDetailId=" + empBasicDetailId + ", Status="
				+ Status + "]";
	}

}
