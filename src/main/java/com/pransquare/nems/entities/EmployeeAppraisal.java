package com.pransquare.nems.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Year;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "employee_appraisal")
public class EmployeeAppraisal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "year_start", nullable = false)
    private LocalDate yearStart;

    @Column(name = "year_end", nullable = false)
    private LocalDate yearEnd;

    @Column(name = "appraisal_name", nullable = false)
    private String appraisalName;

    @Column(name = "current_ctc", nullable = false)
    private BigDecimal currentCtc;

    @Column(name = "approved_ctc")
    private BigDecimal approvedCtc;

    @Column(name = "appraisal_percentage")
    private BigDecimal appraisalPercentage;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "employeeAppraisal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeGoal> goals;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getYearStart() {
        return yearStart;
    }

    public void setYearStart(LocalDate yearStart) {
        this.yearStart = yearStart;
    }

    public LocalDate getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(LocalDate yearEnd) {
        this.yearEnd = yearEnd;
    }

    public String getAppraisalName() {
        return appraisalName;
    }

    public void setAppraisalName(String appraisalName) {
        this.appraisalName = appraisalName;
    }

    public BigDecimal getCurrentCtc() {
        return currentCtc;
    }

    public void setCurrentCtc(BigDecimal currentCtc) {
        this.currentCtc = currentCtc;
    }

    public BigDecimal getApprovedCtc() {
        return approvedCtc;
    }

    public void setApprovedCtc(BigDecimal approvedCtc) {
        this.approvedCtc = approvedCtc;
    }

    public BigDecimal getAppraisalPercentage() {
        return appraisalPercentage;
    }

    public void setAppraisalPercentage(BigDecimal appraisalPercentage) {
        this.appraisalPercentage = appraisalPercentage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<EmployeeGoal> getGoals() {
        return goals;
    }

    public void setGoals(List<EmployeeGoal> goals) {
        this.goals = goals;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
