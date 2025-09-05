package com.pransquare.nems.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "employee_work_location")
public class EmployeeWorkLocationEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Id
    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "work_location_code", nullable = false, length = 45)
    private String workLocationCode;

    @Column(name = "status", length = 45)
    private String status;

    // Constructors
    public EmployeeWorkLocationEntity() {
    }

    public EmployeeWorkLocationEntity(Long employeeId, String workLocationCode, String status) {
        this.employeeId = employeeId;
        this.workLocationCode = workLocationCode;
        this.status = status;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWorkLocationCode() {
        return workLocationCode;
    }

    public void setWorkLocationCode(String workLocationCode) {
        this.workLocationCode = workLocationCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString method
    @Override
    public String toString() {
        return "EmployeeWorkLocationEntity{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", workLocationCode='" + workLocationCode + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
