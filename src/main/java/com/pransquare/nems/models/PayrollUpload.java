package com.pransquare.nems.models;

public class PayrollUpload {

	private String createdBy;
	private String fileName;
	private String month;
	private String year;
	private String isSecondFile;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public String getIsSecondFile() {
		return isSecondFile;
	}

	public void setIsSecondFile(String isSecondFile) {
		this.isSecondFile = isSecondFile;
	}

	@Override
	public String toString() {
		return "PayrollUpload [createdBy=" + createdBy + ", fileName=" + fileName + ", month=" + month + ", year="
				+ year + ", isSecondFile=" + isSecondFile + "]";
	}

}
