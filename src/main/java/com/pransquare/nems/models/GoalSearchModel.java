package com.pransquare.nems.models;

import java.time.LocalDate;
import java.util.List;

public class GoalSearchModel {

	private String employee_code;
	private Long empBasicDetailId;
	private LocalDate fromDate;
	private LocalDate toDate;
	private Long approverId;
	private List<String> status;
	private Long hrId;
	private int page;
	private int size;

	public String getEmployee_code() {
		return employee_code;
	}

	public void setEmployee_code(String employee_code) {
		this.employee_code = employee_code;
	}

	public Long getEmpBasicDetailId() {
		return empBasicDetailId;
	}

	public void setEmpBasicDetailId(Long empBasicDetailId) {
		this.empBasicDetailId = empBasicDetailId;
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

	public Long getApproverId() {
		return approverId;
	}

	public void setApproverId(Long approverId) {
		this.approverId = approverId;
	}

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Long getHrId() {
		return hrId;
	}

	public void setHrId(Long hrId) {
		this.hrId = hrId;
	}

	@Override
	public String toString() {
		return "GoalSearchModel [employee_code=" + employee_code + ", empBasicDetailId=" + empBasicDetailId
				+ ", fromDate=" + fromDate + ", toDate=" + toDate + ", approverId=" + approverId + ", status=" + status
				+ ", hrId=" + hrId + ", page=" + page + ", size=" + size + "]";
	}

}
