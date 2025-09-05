package com.pransquare.nems.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payroll_upload_summary")
public class PayrollUploadSummaryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payroll_upload_summary_id")
	private Long payrollUploadSummaryId;

	@Column(name = "file_name", nullable = false)
	private String fileName;

	@Column(name = "file_path")
	private String filePath;

	@Column(name = "file_id")
	private String fileId;

	@Column(name = "status")
	private String status;

	@Column(name = "uploaded_by")
	private String uploadedBy;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "response_filepath")
	private String responseFilePath;

	// Getters and Setters

	public Long getPayrollUploadSummaryId() {
		return payrollUploadSummaryId;
	}

	public void setPayrollUploadSummaryId(Long payrollUploadSummaryId) {
		this.payrollUploadSummaryId = payrollUploadSummaryId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getResponseFilePath() {
		return responseFilePath;
	}

	public void setResponseFilePath(String responseFilePath) {
		this.responseFilePath = responseFilePath;
	}

	@Override
	public String toString() {
		return "PayrollUploadSummaryEntity [payrollUploadSummaryId=" + payrollUploadSummaryId + ", fileName=" + fileName
				+ ", filePath=" + filePath + ", fileId=" + fileId + ", status=" + status + ", uploadedBy=" + uploadedBy
				+ ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate
				+ ", createdDate=" + createdDate + ", responseFilePath=" + responseFilePath + "]";
	}

}
