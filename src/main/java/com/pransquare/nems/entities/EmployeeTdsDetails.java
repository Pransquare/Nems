package com.pransquare.nems.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_tds_details")
public class EmployeeTdsDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "employee_tds_code", nullable = false, length = 45)
    private String employeeTdsCode;

    @Column(name = "tds_section_code", nullable = false, length = 45)
    private String tdsSectionCode;

    @Column(name = "tds_sub_section_code", nullable = false, length = 45)
    private String tdsSubSectionCode;

    @Column(name = "declared_expenditure", nullable = false)
    private int declaredExpenditure;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_by", length = 100)
    private String modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "submited_expenditure")
    private Integer submitedExpenditure;

    @Column(name = "final_expenditure")
    private Integer finalExpenditure;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployeeTdsCode() {
        return employeeTdsCode;
    }

    public void setEmployeeTdsCode(String employeeTdsCode) {
        this.employeeTdsCode = employeeTdsCode;
    }

    public String getTdsSectionCode() {
        return tdsSectionCode;
    }

    public void setTdsSectionCode(String tdsSectionCode) {
        this.tdsSectionCode = tdsSectionCode;
    }

    public String getTdsSubSectionCode() {
        return tdsSubSectionCode;
    }

    public void setTdsSubSectionCode(String tdsSubSectionCode) {
        this.tdsSubSectionCode = tdsSubSectionCode;
    }

    public int getDeclaredExpenditure() {
        return declaredExpenditure;
    }

    public void setDeclaredExpenditure(int declaredExpenditure) {
        this.declaredExpenditure = declaredExpenditure;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSubmitedExpenditure() {
        return submitedExpenditure;
    }

    public void setSubmitedExpenditure(Integer submitedExpenditure) {
        this.submitedExpenditure = submitedExpenditure;
    }

    public Integer getFinalExpenditure() {
        return finalExpenditure;
    }

    public void setFinalExpenditure(Integer finalExpenditure) {
        this.finalExpenditure = finalExpenditure;
    }

    // Getters and Setters

}
