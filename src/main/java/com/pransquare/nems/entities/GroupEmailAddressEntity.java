package com.pransquare.nems.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "group_email_address")
public class GroupEmailAddressEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "group_email_address_id")
	private Long groupEmailAddressId;

	@Column(name = "emp_basic_detail_id")
	private Long empBasicDetailId;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "status")
	private String status;

	@Column(name = "CREATED_DATE")
	private LocalDateTime createdDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private LocalDateTime modifiedDate;

	@Column(name = "user_approver_conf_id")
	private Long userApproverConfId;

	public Long getGroupEmailAddressId() {
		return groupEmailAddressId;
	}

	public void setGroupEmailAddressId(Long groupEmailAddressId) {
		this.groupEmailAddressId = groupEmailAddressId;
	}

	public Long getEmpBasicDetailId() {
		return empBasicDetailId;
	}

	public void setEmpBasicDetailId(Long empBasicDetailId) {
		this.empBasicDetailId = empBasicDetailId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
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

	public Long getUserApproverConfId() {
		return userApproverConfId;
	}

	public void setUserApproverConfId(Long userApproverConfId) {
		this.userApproverConfId = userApproverConfId;
	}

	@Override
	public String toString() {
		return "GroupEmailAddressEntity [groupEmailAddressId=" + groupEmailAddressId + ", empBasicDetailId="
				+ empBasicDetailId + ", emailId=" + emailId + ", status=" + status + ", createdDate=" + createdDate
				+ ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate
				+ ", userApproverConfId=" + userApproverConfId + "]";
	}

}
