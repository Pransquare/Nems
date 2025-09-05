package com.pransquare.nems.models;

import java.util.ArrayList;
import java.util.List;

public class PerformanceParameterGroupModel {
    private Long empBasicDetailId;
    private String performanceGroup;
    private String performanceSubGroup;
    private List<PerformanceParameterModel> performanceParameters;

    // Constructor, Getters, and Setters
    public PerformanceParameterGroupModel(Long empBasicDetailId, String performanceGroup, String performanceSubGroup) {
        this.empBasicDetailId = empBasicDetailId;
        this.performanceGroup = performanceGroup;
        this.performanceSubGroup = performanceSubGroup;
        this.performanceParameters = new ArrayList<>();
    }

    public Long getEmpBasicDetailId() {
        return empBasicDetailId;
    }

    public String getPerformanceGroup() {
        return performanceGroup;
    }

    public String getPerformanceSubGroup() {
        return performanceSubGroup;
    }

    public List<PerformanceParameterModel> getPerformanceParameters() {
        return performanceParameters;
    }

    public void addPerformanceParameter(PerformanceParameterModel parameter) {
        this.performanceParameters.add(parameter);
    }
}
