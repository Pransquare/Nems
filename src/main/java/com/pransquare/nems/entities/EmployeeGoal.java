package com.pransquare.nems.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_goals")
public class EmployeeGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "appraisal_id", nullable = false)
    private EmployeeAppraisal employeeAppraisal;

    @Column(name = "goal", nullable = false)
    private String goal;

    @Column(name = "goal_description")
    private String goalDescription;

    @Column(name = "self_rating")
    private BigDecimal selfRating;

    @Column(name = "manager_rating")
    private BigDecimal managerRating;

    @Column(name = "final_rating")
    private BigDecimal finalRating;

    @Column(name = "self_comments")
    private String selfComments;

    @Column(name = "manager_comments")
    private String managerComments;

    @Column(name = "final_comments")
    private String finalComments;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EmployeeAppraisal getEmployeeAppraisal() {
        return employeeAppraisal;
    }

    public void setEmployeeAppraisal(EmployeeAppraisal employeeAppraisal) {
        this.employeeAppraisal = employeeAppraisal;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getGoalDescription() {
        return goalDescription;
    }

    public void setGoalDescription(String goalDescription) {
        this.goalDescription = goalDescription;
    }

    public BigDecimal getSelfRating() {
        return selfRating;
    }

    public void setSelfRating(BigDecimal selfRating) {
        this.selfRating = selfRating;
    }

    public BigDecimal getManagerRating() {
        return managerRating;
    }

    public void setManagerRating(BigDecimal managerRating) {
        this.managerRating = managerRating;
    }

    public BigDecimal getFinalRating() {
        return finalRating;
    }

    public void setFinalRating(BigDecimal finalRating) {
        this.finalRating = finalRating;
    }

    public String getSelfComments() {
        return selfComments;
    }

    public void setSelfComments(String selfComments) {
        this.selfComments = selfComments;
    }

    public String getManagerComments() {
        return managerComments;
    }

    public void setManagerComments(String managerComments) {
        this.managerComments = managerComments;
    }

    public String getFinalComments() {
        return finalComments;
    }

    public void setFinalComments(String finalComments) {
        this.finalComments = finalComments;
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

    // Getters and Setters...

}
