package com.pransquare.nems.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity(name = "user_approver_configurations")
@Table(name = "user_approver_configurations")
public class UserApproverConfigurationsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_approver_config_id")
	private Long userApproverConfigId;

	@Column(name = "EMP_BASIC_DETAIL_ID")
	private Long empBasicDetailId;

	@Column(name = "comments")
	private String comments;

	@Column(name = "approver_id")
	private Long approverId;

	@Column(name = "approver_id_1")
	private Long approverId1;

	@Column(name = "approver_id_2")
	private Long approverId2;

	@Column(name = "module_name")
	private String moduleName;

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

	@Column(name = "workflow_status")
	private String workflowStatus;

	@OneToOne
	@JoinColumn(name = "EMP_BASIC_DETAIL_ID", referencedColumnName = "EMPLOYEE_BASIC_DETAIL_ID", insertable = false, updatable = false)
	private EmployeeEntity employeeBasicDetail;

	@OneToOne
	@JoinColumn(name = "approver_id", referencedColumnName = "EMPLOYEE_BASIC_DETAIL_ID", insertable = false, updatable = false)
	private EmployeeEntity approverEntity;

	@OneToMany(targetEntity = GroupEmailAddressEntity.class)
	@JoinColumn(name = "user_approver_conf_id", referencedColumnName = "user_approver_config_id")
	private List<GroupEmailAddressEntity> groupEmailAddressEntities;

	public String getEmployeeName() {
		return employeeBasicDetail != null
				? employeeBasicDetail.getFirstName() + " " + employeeBasicDetail.getLastName()
				: null;
	}

	public String getApproverName() {
		return approverEntity != null ? approverEntity.getFirstName() + " " + approverEntity.getLastName() : null;
	}

	public String getApproverCode() {
		return approverEntity != null ? approverEntity.getEmployeeCode() : null;
	}

	public String getApproverEmailId() {
		return approverEntity != null ? approverEntity.getEmailId() : null;
	}

	public Long getUserApproverConfigId() {
		return userApproverConfigId;
	}

	public void setUserApproverConfigId(Long userApproverConfigId) {
		this.userApproverConfigId = userApproverConfigId;
	}

	public Long getEmpBasicDetailId() {
		return empBasicDetailId;
	}

	public void setEmpBasicDetailId(Long empBasicDetailId) {
		this.empBasicDetailId = empBasicDetailId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getApproverId() {
		return approverId;
	}

	public void setApproverId(Long approverId) {
		this.approverId = approverId;
	}

	public Long getApproverId1() {
		return approverId1;
	}

	public void setApproverId1(Long approverId1) {
		this.approverId1 = approverId1;
	}

	public Long getApproverId2() {
		return approverId2;
	}

	public void setApproverId2(Long approverId2) {
		this.approverId2 = approverId2;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
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

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public EmployeeEntity getEmployeeBasicDetail() {
		return employeeBasicDetail;
	}

	public void setEmployeeBasicDetail(EmployeeEntity employeeBasicDetail) {
		this.employeeBasicDetail = employeeBasicDetail;
	}

	public EmployeeEntity getApproverEntity() {
		return approverEntity;
	}

	public void setApproverEntity(EmployeeEntity approverEntity) {
		this.approverEntity = approverEntity;
	}

	public List<GroupEmailAddressEntity> getGroupEmailAddressEntities() {
		return groupEmailAddressEntities;
	}

	public void setGroupEmailAddressEntities(List<GroupEmailAddressEntity> groupEmailAddressEntities) {
		this.groupEmailAddressEntities = groupEmailAddressEntities;
	}

	@Override
	public String toString() {
		return "UserApproverConfigurationsEntity [userApproverConfigId=" + userApproverConfigId + ", empBasicDetailId="
				+ empBasicDetailId + ", comments=" + comments + ", approverId=" + approverId + ", approverId1="
				+ approverId1 + ", approverId2=" + approverId2 + ", moduleName=" + moduleName + ", status=" + status
				+ ", createdDate=" + createdDate + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
				+ ", modifiedDate=" + modifiedDate + ", workflowStatus=" + workflowStatus + ", employeeBasicDetail="
				+ employeeBasicDetail + ", approverEntity=" + approverEntity + ", GroupEmailAddressEntities="
				+ groupEmailAddressEntities + "]";
	}

}
