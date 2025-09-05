package com.pransquare.nems.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "group_subgroup_goal")
public class GroupSubgroupGoalEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "group_subgroup_goal_id")
	private Long groupSubgroupGoalId;

	@Column(name = "group_subgroup_config_id")
	private Long groupSubgroupConfigId;

	@Column(name = "group_subgroup_config_history_id")
	private Long groupSubgroupConfigHistoryId;

	@Column(name = "group_sub_group_code")
	private String groupSubGroupCode;

	@Column(name = "status")
	private String status;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;

	@Column(name = "created_date")
	private LocalDateTime createdDate;

	@Column(name = "goal")
	private String goal;

	public Long getGroupSubgroupGoalId() {
		return groupSubgroupGoalId;
	}

	public void setGroupSubgroupGoalId(Long groupSubgroupGoalId) {
		this.groupSubgroupGoalId = groupSubgroupGoalId;
	}

	public Long getGroupSubgroupConfigId() {
		return groupSubgroupConfigId;
	}

	public void setGroupSubgroupConfigId(Long groupSubgroupConfigId) {
		this.groupSubgroupConfigId = groupSubgroupConfigId;
	}

	public Long getGroupSubgroupConfigHistoryId() {
		return groupSubgroupConfigHistoryId;
	}

	public void setGroupSubgroupConfigHistoryId(Long groupSubgroupConfigHistoryId) {
		this.groupSubgroupConfigHistoryId = groupSubgroupConfigHistoryId;
	}

	public String getGroupSubGroupCode() {
		return groupSubGroupCode;
	}

	public void setGroupSubGroupCode(String groupSubGroupCode) {
		this.groupSubGroupCode = groupSubGroupCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	@Override
	public String toString() {
		return "GroupSubgroupGoalEntity [groupSubgroupGoalId=" + groupSubgroupGoalId + ", groupSubgroupConfigId="
				+ groupSubgroupConfigId + ", groupSubgroupConfigHistoryId=" + groupSubgroupConfigHistoryId
				+ ", groupSubGroupCode=" + groupSubGroupCode + ", status=" + status + ", createdBy=" + createdBy
				+ ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate + ", createdDate=" + createdDate
				+ ", goal=" + goal + "]";
	}

}
