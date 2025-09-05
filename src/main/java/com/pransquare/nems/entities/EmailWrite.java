package com.pransquare.nems.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "email_write")
public class EmailWrite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "email_write_id", nullable = false)
	private Long emailWriteId;

	@Column(name = "subject", length = 64)
	private String subject;

	@Column(name = "recieved_at")
	private LocalDateTime receivedAt;

	@Column(name = "created_by", length = 255)
	private String createdBy;

	@Column(name = "modified_by", length = 255)
	private String modifiedBy;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "modified_date", columnDefinition = "DATETIME(6)")
	private LocalDateTime modifiedDate;

	@Column(name = "message_id")
	private String messageId;

	// Getters and Setters
	public Long getEmailWriteId() {
		return emailWriteId;
	}

	public void setEmailWriteId(Long emailWriteId) {
		this.emailWriteId = emailWriteId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public LocalDateTime getReceivedAt() {
		return receivedAt;
	}

	public void setReceivedAt(LocalDateTime receivedAt) {
		this.receivedAt = receivedAt;
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

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	@Override
	public String toString() {
		return "EmailWrite [emailWriteId=" + emailWriteId + ", subject=" + subject + ", receivedAt=" + receivedAt
				+ ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", createdDate=" + createdDate
				+ ", modifiedDate=" + modifiedDate + ", messageId=" + messageId + "]";
	}

}
