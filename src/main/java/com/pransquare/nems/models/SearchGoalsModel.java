package com.pransquare.nems.models;

public class SearchGoalsModel {
	private String group;
	private String subGroup;
	private Integer page;
	private Integer size;
	private String status;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(String subGroup) {
		this.subGroup = subGroup;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "searchGoalsModel [group=" + group + ", subGroup=" + subGroup + ", page=" + page + ", size=" + size
				+ ", status=" + status + "]";
	}

}
