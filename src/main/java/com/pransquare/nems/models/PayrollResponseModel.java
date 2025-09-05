package com.pransquare.nems.models;

public class PayrollResponseModel {

	private String filePath;
	private String fileId;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	@Override
	public String toString() {
		return "PayrollResponseModel [filePath=" + filePath + ", fileId=" + fileId + "]";
	}

}
