package com.pransquare.nems.models;

public class AttendanceIdModel {

	private Long attendanceId;
	private String status;
	private String workflowStatus;

	public Long getAttendanceId() {
		return attendanceId;
	}

	public void setAttendanceId(Long attendanceId) {
		this.attendanceId = attendanceId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	@Override
	public String toString() {
		return "AttendanceIdModel [attendanceId=" + attendanceId + ", Status=" + status + ", workflowStatus="
				+ workflowStatus + "]";
	}

}
