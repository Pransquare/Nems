package com.pransquare.nems.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_leave_details")
public class EmployeeLeaveEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMP_LEAVE_ID")
	private Long empLeaveId;

	@Column(name = "EMP_BASIC_DETAIL_ID")
	private Long employeeId;

	@Column(name = "LEAVE_TYPE")
	private String leaveType;

	@Column(name = "LEAVE_FROM")
	private LocalDate leaveFrom;

	@Column(name = "LEAVE_TO")
	private LocalDate leaveTo;

	@Column(name = "NO_OF_DAYS")
	private Float noOfDays;

	@Column(name = "APPROVER_1")
	private Long approver1;

	@Column(name = "APPROVER_2")
	private String approver2;

	@Column(name = "APPROVER_3")
	private String approver3;

	@Column(name = "WORKFLOW_STATUS")
	private String workflowStatus;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "CREATED_DATE")
	private LocalDateTime createdDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Column(name = "MODIFIED_DATE")
	private LocalDateTime modifiedDate;

	@Column(name = "FROM_LEAVE_TYPE")
	private String fromLeaveType;

	@Column(name = "TO_LEAVE_TYPE")
	private String toLeaveType;

	@Column(name = "REASON")
	private String reason;

	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "REMINDER_SENT_DATE")
    private LocalDateTime reminderSentDate;

    @Column(name = "IS_REMINDER_SENT")
    private Boolean isReminderSent = false;

	@OneToOne()
	@JoinColumn(name = "APPROVER_1", referencedColumnName = "EMPLOYEE_BASIC_DETAIL_ID", insertable = false, updatable = false)
	private EmployeeEntity approverEmployeeBasicDetails;

	@OneToOne()
	@JoinColumn(name = "EMP_BASIC_DETAIL_ID", referencedColumnName = "EMPLOYEE_BASIC_DETAIL_ID", insertable = false, updatable = false)
	private EmployeeEntity employeeBasicDetails;

	@OneToOne
	@JoinColumn(name = "STATUS", referencedColumnName = "CODE", insertable = false, updatable = false)
	private StatusMasterEntity statusMasterEntity;

	public String getApproverName() {
		return approverEmployeeBasicDetails != null
				? approverEmployeeBasicDetails.getFirstName() + " " + approverEmployeeBasicDetails.getLastName()
				: null;
	}

	public String getEmployeeName() {
		return employeeBasicDetails != null
				? employeeBasicDetails.getFirstName() + " " + employeeBasicDetails.getLastName()
				: null;
	}

	public Long getEmpLeaveId() {
		return empLeaveId;
	}

	public void setEmpLeaveId(Long empLeaveId) {
		this.empLeaveId = empLeaveId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public void setApprover1(Long approver1) {
		this.approver1 = approver1;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public LocalDate getLeaveFrom() {
		return leaveFrom;
	}

	public void setLeaveFrom(LocalDate leaveFrom) {
		this.leaveFrom = leaveFrom;
	}

	public LocalDate getLeaveTo() {
		return leaveTo;
	}

	public void setLeaveTo(LocalDate leaveTo) {
		this.leaveTo = leaveTo;
	}

	public String getApprover2() {
		return approver2;
	}

	public void setApprover2(String approver2) {
		this.approver2 = approver2;
	}

	public String getApprover3() {
		return approver3;
	}

	public void setApprover3(String approver3) {
		this.approver3 = approver3;
	}

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
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

	public String getFromLeaveType() {
		return fromLeaveType;
	}

	public void setFromLeaveType(String fromLeaveType) {
		this.fromLeaveType = fromLeaveType;
	}

	public String getToLeaveType() {
		return toLeaveType;
	}

	public void setToLeaveType(String toLeaveType) {
		this.toLeaveType = toLeaveType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Float getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(Float noOfDays) {
		this.noOfDays = noOfDays;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public Long getApprover1() {
		return approver1;
	}

	public StatusMasterEntity getStatusMasterEntity() {
		return statusMasterEntity;
	}

	public void setStatusMasterEntity(StatusMasterEntity statusMasterEntity) {
		this.statusMasterEntity = statusMasterEntity;
	}
	

	public LocalDateTime getReminderSentDate() {
		return reminderSentDate;
	}

	public void setReminderSentDate(LocalDateTime reminderSentDate) {
		this.reminderSentDate = reminderSentDate;
	}

	public Boolean getIsReminderSent() {
		return isReminderSent;
	}

	public void setIsReminderSent(Boolean isReminderSent) {
		this.isReminderSent = isReminderSent;
	}

	public EmployeeEntity getApproverEmployeeBasicDetails() {
		return approverEmployeeBasicDetails;
	}

	public void setApproverEmployeeBasicDetails(EmployeeEntity approverEmployeeBasicDetails) {
		this.approverEmployeeBasicDetails = approverEmployeeBasicDetails;
	}

	public EmployeeEntity getEmployeeBasicDetails() {
		return employeeBasicDetails;
	}

	public void setEmployeeBasicDetails(EmployeeEntity employeeBasicDetails) {
		this.employeeBasicDetails = employeeBasicDetails;
	}

	@Override
	public String toString() {
		return "EmployeeLeaveEntity [empLeaveId=" + empLeaveId + ", employeeId=" + employeeId + ", leaveType="
				+ leaveType + ", leaveFrom=" + leaveFrom + ", leaveTo=" + leaveTo + ", noOfDays=" + noOfDays
				+ ", approver1=" + approver1 + ", approver2=" + approver2 + ", approver3=" + approver3
				+ ", workflowStatus=" + workflowStatus + ", status=" + status + ", createdDate=" + createdDate
				+ ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate
				+ ", fromLeaveType=" + fromLeaveType + ", toLeaveType=" + toLeaveType + ", reason=" + reason
				+ ", remarks=" + remarks + ", reminderSentDate=" + reminderSentDate + ", isReminderSent="
				+ isReminderSent + ", approverEmployeeBasicDetails=" + approverEmployeeBasicDetails
				+ ", employeeBasicDetails=" + employeeBasicDetails + ", statusMasterEntity=" + statusMasterEntity + "]";
	}

	

}