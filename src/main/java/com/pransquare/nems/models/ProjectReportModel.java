package com.pransquare.nems.models;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class ProjectReportModel {

    private List<String> projectCode;
    private String clientCode;
    private LocalDate fromDate;
    private LocalDate toDate;

    // Getters and Setters

    public List<String> getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(List<String> projectCode) {
        this.projectCode = projectCode;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
}
