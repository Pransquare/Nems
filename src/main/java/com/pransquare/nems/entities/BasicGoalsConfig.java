package com.pransquare.nems.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "basic_goal_config")
@Entity
public class BasicGoalsConfig {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Integer id;

	@Column
	private String goal;

	@Column
	private String status;

	@Column(name = "goal_type")
	private String goalType;

	@Column(name = "goal_description")
	private String goalDescription;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGoalType() {
		return goalType;
	}

	public void setGoalType(String goalType) {
		this.goalType = goalType;
	}

	public String getGoalDescription() {
		return goalDescription;
	}

	public void setGoalDescription(String goalDescription) {
		this.goalDescription = goalDescription;
	}

	@Override
	public String toString() {
		return "BasicGoalsConfig [id=" + id + ", goal=" + goal + ", status=" + status + ", goalType=" + goalType
				+ ", goalDescription=" + goalDescription + "]";
	}

}
