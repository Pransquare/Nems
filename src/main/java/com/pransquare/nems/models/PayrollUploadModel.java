package com.pransquare.nems.models;

public class PayrollUploadModel {
	private String createdBy;
	private String fileName;
	private String fileId;
	private String year;
	private String month;

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

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	@Override
	public String toString() {
		return "PayrollUploadModel [createdBy=" + createdBy + ", fileName=" + fileName + ", fileId=" + fileId
				+ ", year=" + year + ", month=" + month + "]";
	}
}
