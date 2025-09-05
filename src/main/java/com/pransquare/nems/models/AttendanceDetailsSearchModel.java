package com.pransquare.nems.models;

import java.time.LocalDate;
import java.util.List;

public class AttendanceDetailsSearchModel {

	private Long empBasicDetailId;
	private Long approverId;
	private String employeeCode;
	private String projectCode;
	private String yearAndMonth;
	private List<String> status;
	private int size;
	private int page;
	private LocalDate taskDate;
	private List<String> worklocation;
	private LocalDate fromDate;
	private LocalDate toDate;

	public Long getEmpBasicDetailId() {
		return empBasicDetailId;
	}

	public void setEmpBasicDetailId(Long empBasicDetailId) {
		this.empBasicDetailId = empBasicDetailId;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getYearAndMonth() {
		return yearAndMonth;
	}

	public void setYearAndMonth(String yearAndMonth) {
		this.yearAndMonth = yearAndMonth;
	}

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public Long getApproverId() {
		return approverId;
	}

	public void setApproverId(Long approverId) {
		this.approverId = approverId;
	}

	public LocalDate getTaskDate() {
		return taskDate;
	}

	public void setTaskDate(LocalDate taskDate) {
		this.taskDate = taskDate;
	}

	public List<String> getWorklocation() {
		return worklocation;
	}

	public void setWorklocation(List<String> worklocation) {
		this.worklocation = worklocation;
	}

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

	@Override
	public String toString() {
		return "AttendanceDetailsSearchModel [empBasicDetailId=" + empBasicDetailId + ", approverId=" + approverId
				+ ", employeeCode=" + employeeCode + ", projectCode=" + projectCode + ", yearAndMonth=" + yearAndMonth
				+ ", status=" + status + ", size=" + size + ", page=" + page + ", taskDate=" + taskDate
				+ ", worklocation=" + worklocation + ", fromDate=" + fromDate + ", toDate=" + toDate + "]";
	}

}
