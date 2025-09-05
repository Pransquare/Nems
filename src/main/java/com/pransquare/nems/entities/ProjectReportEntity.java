package com.pransquare.nems.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vw_project_employee_report")
public class ProjectReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // only required if the view includes a unique ID
    private Long id;

    @Column(name = "project_code")
    private String projectId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "employee_code")
    private String employeeCode;

    @Column(name = "full_name")
    private String employeeName;

    @Column(name = "allocation_start_date")
    private LocalDate startDate;

    @Column(name = "allocation_end_date")
    private LocalDate endDate;
    
    @Column(name = "client_code") 
    private String clientCode;

    // Getters & setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
    
}
