package com.pransquare.nems.entities;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_project_config")
public class EmployeeProjectConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_project_config_id")
    private Long employeeProjectConfigId;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "project_code")
    private String projectCode;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_date")
    private LocalDate modifiedDate;

    @Column(name = "status")
    private String status;

    @Column(name = "allocation_start_date")
    private LocalDate allocationStartDate;

    @Column(name = "allocation_end_date")
    private LocalDate allocationEndDate;

    @OneToMany(mappedBy = "employeeProjectConfig", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<EmployeeProjectTaskConfig> tasks;

    public Long getEmployeeProjectConfigId() {
        return employeeProjectConfigId;
    }

    public void setEmployeeProjectConfigId(Long employeeProjectConfigId) {
        this.employeeProjectConfigId = employeeProjectConfigId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDate getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDate modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getAllocationStartDate() {
        return allocationStartDate;
    }

    public void setAllocationStartDate(LocalDate allocationStartDate) {
        this.allocationStartDate = allocationStartDate;
    }

    public LocalDate getAllocationEndDate() {
        return allocationEndDate;
    }

    public void setAllocationEndDate(LocalDate allocationEndDate) {
        this.allocationEndDate = allocationEndDate;
    }

    public List<EmployeeProjectTaskConfig> getTasks() {
        return tasks;
    }

    public void setTasks(List<EmployeeProjectTaskConfig> tasks) {
        this.tasks = tasks;
    }
}
