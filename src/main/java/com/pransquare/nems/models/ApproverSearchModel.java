package com.pransquare.nems.models;

public class ApproverSearchModel {

	private Long employeeBasicDetailId;
	private String moduleName;
	private String createdBy;

	public Long getEmployeeBasicDetailId() {
		return employeeBasicDetailId;
	}

	public void setEmployeeBasicDetailId(Long employeeBasicDetailId) {
		this.employeeBasicDetailId = employeeBasicDetailId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public String toString() {
		return "ApproverSearchModel [employeeBasicDetailId=" + employeeBasicDetailId + ", moduleName=" + moduleName
				+ ", createdBy=" + createdBy + "]";
	}
}
