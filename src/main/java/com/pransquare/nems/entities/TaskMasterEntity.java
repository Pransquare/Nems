package com.pransquare.nems.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "task_master")
public class TaskMasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_master_id")
    private Integer taskMasterId;
    @Column(name = "task_code")
    private String taskCode;
    @Column(name = "task_description")
    private String taskDescription;

    public Integer getTaskMasterId() {
        return taskMasterId;
    }

    public void setTaskMasterId(Integer taskMasterId) {
        this.taskMasterId = taskMasterId;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

}