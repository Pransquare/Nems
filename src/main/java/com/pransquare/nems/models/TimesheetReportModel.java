package com.pransquare.nems.models;

import java.time.LocalDate;

public class TimesheetReportModel {
	private LocalDate fromDate;
	private LocalDate toDate;
	private String employeeCode;

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	@Override
	public String toString() {
		return "TimesheetReportModel [fromDate=" + fromDate + ", toDate=" + toDate + ", employeeCode=" + employeeCode
				+ "]";
	}

}
