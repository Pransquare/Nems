package com.pransquare.nems.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_project_task_config")
public class EmployeeProjectTaskConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emp_project_config_id", nullable = false, foreignKey = @ForeignKey(name = "emp_project_config_id"))
    @JsonBackReference
    private EmployeeProjectConfigEntity employeeProjectConfig;

    @Column(name = "task_code", length = 45, nullable = false)
    private String taskCode;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeProjectConfigEntity getEmployeeProjectConfig() {
        return employeeProjectConfig;
    }

    public void setEmployeeProjectConfig(EmployeeProjectConfigEntity employeeProjectConfig) {
        this.employeeProjectConfig = employeeProjectConfig;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }
}
