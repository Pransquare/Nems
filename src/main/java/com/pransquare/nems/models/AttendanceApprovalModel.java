package com.pransquare.nems.models;

import java.util.List;

public class AttendanceApprovalModel {

	private List<AttendanceIdModel> attendanceEmpIdModels;
	private Long basicEmpDetailId;
	private String comments;

	public List<AttendanceIdModel> getAttendanceEmpIdModels() {
		return attendanceEmpIdModels;
	}

	public void setAttendanceEmpIdModels(List<AttendanceIdModel> attendanceEmpIdModels) {
		this.attendanceEmpIdModels = attendanceEmpIdModels;
	}

	public Long getBasicEmpDetailId() {
		return basicEmpDetailId;
	}

	public void setBasicEmpDetailId(Long basicEmpDetailId) {
		this.basicEmpDetailId = basicEmpDetailId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "AttendanceApprovalModel [attendanceEmpIdModels=" + attendanceEmpIdModels + ", basicEmpDetailId="
				+ basicEmpDetailId + ", comments=" + comments + "]";
	}

}
