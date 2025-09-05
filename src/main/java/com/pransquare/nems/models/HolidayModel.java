package com.pransquare.nems.models;

import java.time.LocalDate;

public class HolidayModel {

    private Long holidayMasterId;
    private String country;
    private String state;
    private String status;
    private String holidayDescription;
    private String createdBy;
    private LocalDate holidayDate;
    private String workLocationCode;

    public Long getHolidayMasterId() {
        return holidayMasterId;
    }

    public void setHolidayMasterId(Long holidayMasterId) {
        this.holidayMasterId = holidayMasterId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHolidayDescription() {
        return holidayDescription;
    }

    public void setHolidayDescription(String holidayDescription) {
        this.holidayDescription = holidayDescription;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }

    @Override
    public String toString() {
        return "HolidayModel [holidayMasterId=" + holidayMasterId + ", country=" + country + ", state=" + state
                + ", status=" + status + ", holidayDescription=" + holidayDescription + ", createdBy=" + createdBy
                + ", holidayDate=" + holidayDate + "]";
    }

    public String getWorkLocationCode() {
        return workLocationCode;
    }

    public void setWorkLocationCode(String workLocationCode) {
        this.workLocationCode = workLocationCode;
    }

}
