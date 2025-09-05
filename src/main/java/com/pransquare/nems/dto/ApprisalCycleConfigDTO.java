package com.pransquare.nems.dto;

import java.time.LocalDate;

public class ApprisalCycleConfigDTO {
    private Long id;
    private LocalDate yearStart;
    private LocalDate yearEnd;
    private ApprisalType apprisalType;
    private Boolean isActive;
    private Integer goalSetupPeriod;
    private Integer apprisalInitiatingPeriod;
    private Integer selfRatingPeriod;
    private Integer managerRatingPeriod;
    private Integer alligiblePeriod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public ApprisalType getApprisalType() {
        return apprisalType;
    }

    public void setApprisalType(ApprisalType apprisalType) {
        this.apprisalType = apprisalType;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getGoalSetupPeriod() {
        return goalSetupPeriod;
    }

    public void setGoalSetupPeriod(Integer goalSetupPeriod) {
        this.goalSetupPeriod = goalSetupPeriod;
    }

    public Integer getApprisalInitiatingPeriod() {
        return apprisalInitiatingPeriod;
    }

    public void setApprisalInitiatingPeriod(Integer apprisalInitiatingPeriod) {
        this.apprisalInitiatingPeriod = apprisalInitiatingPeriod;
    }

    public Integer getSelfRatingPeriod() {
        return selfRatingPeriod;
    }

    public void setSelfRatingPeriod(Integer selfRatingPeriod) {
        this.selfRatingPeriod = selfRatingPeriod;
    }

    public Integer getManagerRatingPeriod() {
        return managerRatingPeriod;
    }

    public void setManagerRatingPeriod(Integer managerRatingPeriod) {
        this.managerRatingPeriod = managerRatingPeriod;
    }

    public Integer getAlligiblePeriod() {
        return alligiblePeriod;
    }

    public void setAlligiblePeriod(Integer alligiblePeriod) {
        this.alligiblePeriod = alligiblePeriod;
    }

    public enum ApprisalType {
        ANNUALLY,
        QUARTERLY,
        HALFYEARLY
    }
}