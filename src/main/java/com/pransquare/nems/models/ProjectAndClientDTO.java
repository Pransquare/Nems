package com.pransquare.nems.models;

public class ProjectAndClientDTO {

	private String projectName;
	private String clientName;
	private String projectCode;

	public ProjectAndClientDTO(String projectName, String clientName, String projectCode) {
		this.projectName = projectName;
		this.clientName = clientName;
		this.projectCode = projectCode;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	@Override
	public String toString() {
		return "ProjectAndClientDTO [projectName=" + projectName + ", clientName=" + clientName + "]";
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

}
