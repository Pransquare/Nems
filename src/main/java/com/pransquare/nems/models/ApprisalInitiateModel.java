package com.pransquare.nems.models;

import java.time.LocalDate;
import java.util.List;

public class ApprisalInitiateModel {
    private String group;
    private String subGroup;
    private String user;
    private LocalDate date;
    private List<PerformenceReviewModel> performenceReviewModel;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(String subGroup) {
        this.subGroup = subGroup;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<PerformenceReviewModel> getPerformenceReviewModel() {
        return performenceReviewModel;
    }

    public void setPerformenceReviewModel(List<PerformenceReviewModel> performenceReviewModel) {
        this.performenceReviewModel = performenceReviewModel;
    }

}
