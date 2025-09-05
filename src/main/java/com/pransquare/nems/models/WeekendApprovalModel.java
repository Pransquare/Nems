package com.pransquare.nems.models;

import java.util.List;

public class WeekendApprovalModel {

    private Long empBasicDetailId;
    private Long approverID;
    private List<String> status;
    private int page;
    private int size;

    public Long getApproverID() {
        return approverID;
    }

    public void setApproverID(Long approverID) {
        this.approverID = approverID;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Long getEmpBasicDetailId() {
        return empBasicDetailId;
    }

    public void setEmpBasicDetailId(Long empBasicDetailId) {
        this.empBasicDetailId = empBasicDetailId;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

}
