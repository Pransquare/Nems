package com.pransquare.nems.dto;

public class BasicGoalsConfigDTO {

	private Integer id;
	private String goal;
	private String status;
	private String goalType;
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
		return "BasicGoalsConfigDTO [id=" + id + ", goal=" + goal + ", status=" + status + ", goalType=" + goalType
				+ ", goalDescription=" + goalDescription + "]";
	}

}
