package com.pransquare.nems.models;

import java.util.List;

public class SowSearchInputModel {

	private String account;
	private String milestoneMonth;
	private String status;
	private String sowId;
	private int page;
	private int size;
	private List<Long> managerId;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	

	public String getSowId() {
		return sowId;
	}

	public void setSowId(String sowId) {
		this.sowId = sowId;
	}

	public String getMilestoneMonth() {
		return milestoneMonth;
	}

	public void setMilestoneMonth(String milestoneMonth) {
		this.milestoneMonth = milestoneMonth;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

	public List<Long> getManagerId() {
		return managerId;
	}

	public void setManagerId(List<Long> managerId) {
		this.managerId = managerId;
	}

}
