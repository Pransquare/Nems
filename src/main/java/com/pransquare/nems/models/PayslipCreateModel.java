package com.pransquare.nems.models;

public class PayslipCreateModel {

	private Long empBasicDetailId;
	private String month;
	private String year;

	public Long getEmpBasicDetailId() {
		return empBasicDetailId;
	}

	public void setEmpBasicDetailId(Long empBasicDetailId) {
		this.empBasicDetailId = empBasicDetailId;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "PayslipCreateModel [empBasicDetailId=" + empBasicDetailId + ", month=" + month + ", year=" + year + "]";
	}

}
