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
import jakarta.persistence.Table;

@Entity
@Table(name = "group_subgroup_config")
public class GroupSubgroupConfigEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "group_subgroup_config_id")
	private Long groupSubgroupConfigId;

	@Column(name = "emp_group", nullable = false)
	private String group;

	@Column(name = "emp_sub_group")
	private String subGroup;

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

	@OneToMany(targetEntity = GroupSubgroupGoalEntity.class)
	@JoinColumn(name = "group_subgroup_config_id", referencedColumnName = "group_subgroup_config_id")
	private List<GroupSubgroupGoalEntity> groupSubgroupGoalEntity;

	public Long getGroupSubgroupConfigId() {
		return groupSubgroupConfigId;
	}

	public void setGroupSubgroupConfigId(Long groupSubgroupConfigId) {
		this.groupSubgroupConfigId = groupSubgroupConfigId;
	}

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

	public List<GroupSubgroupGoalEntity> getGroupSubgroupGoalEntity() {
		return groupSubgroupGoalEntity;
	}

	public void setGroupSubgroupGoalEntity(List<GroupSubgroupGoalEntity> groupSubgroupGoalEntity) {
		this.groupSubgroupGoalEntity = groupSubgroupGoalEntity;
	}

	@Override
	public String toString() {
		return "GroupSubgroupConfigEntity [groupSubgroupConfigId=" + groupSubgroupConfigId + ", group=" + group
				+ ", subGroup=" + subGroup + ", groupSubGroupCode=" + groupSubGroupCode + ", status=" + status
				+ ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate
				+ ", createdDate=" + createdDate + ", groupSubgroupGoalEntity=" + groupSubgroupGoalEntity + "]";
	}

}
