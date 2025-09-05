package com.pransquare.nems.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_leave_details_config")
public class EmployeeLeaveDetailsConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DETAILS_CONFIG_ID")
    private Long detailsConfigId;

    @Column(name = "EMPLOYEE_BASIC_DETAILS_ID")
    private Long employeeBasicDetailsId;

    @Column(name = "LEAVE_CODE")
    private String leaveCode;

    @Column(name = "OPENING")
    private Float opening;

    @Column(name = "CREDITED")
    private Float credited;

    @Column(name = "USED")
    private Float used;

    @Column(name = "REMAINING")
    private Float remaining;

    @Column(name = "PENDING")
    private Float pending;

    @Column(name = "VALID_FROM")
    private LocalDate validFrom;

    @Column(name = "VALID_TO")
    private LocalDate validTo;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_DATE")
    private LocalDate createdDate;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Column(name = "MODIFIED_DATE")
    private LocalDate modifiedDate;

    @Column(name = "UNLIMITED")
    private Boolean unlimited;

    public Long getDetailsConfigId() {
        return detailsConfigId;
    }

    public void setDetailsConfigId(Long detailsConfigId) {
        this.detailsConfigId = detailsConfigId;
    }

    public Long getEmployeeBasicDetailsId() {
        return employeeBasicDetailsId;
    }

    public void setEmployeeBasicDetailsId(Long employeeBasicDetailsId) {
        this.employeeBasicDetailsId = employeeBasicDetailsId;
    }

    public String getLeaveCode() {
        return leaveCode;
    }

    public void setLeaveCode(String leaveCode) {
        this.leaveCode = leaveCode;
    }

    public Float getOpening() {
        return opening;
    }

    public void setOpening(Float opening) {
        this.opening = opening;
    }

    public Float getCredited() {
        return credited;
    }

    public void setCredited(Float credited) {
        this.credited = credited;
    }

    public Float getUsed() {
        return used;
    }

    public void setUsed(Float used) {
        this.used = used;
    }

    public Float getRemaining() {
        return remaining;
    }

    public void setRemaining(Float remaining) {
        this.remaining = remaining;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
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

    public Float getPending() {
        return pending;
    }

    public void setPending(Float pending) {
        this.pending = pending;
    }
    // Getters and setters for all fields

    public Boolean getUnlimited() {
        return unlimited;
    }

    public void setUnlimited(Boolean unlimited) {
        this.unlimited = unlimited;
    }

}