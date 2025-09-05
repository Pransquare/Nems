package com.pransquare.nems.models;
 
public class EmployeeCountResponse {
 
    private long activeCount;
    private long inactiveCount;
 
    public EmployeeCountResponse(long activeCount, long inactiveCount) {
        this.activeCount = activeCount;
        this.inactiveCount = inactiveCount;
    }
 
    public long getActiveCount() {
        return activeCount;
    }
 
    public void setActiveCount(long activeCount) {
        this.activeCount = activeCount;
    }
 
    public long getInactiveCount() {
        return inactiveCount;
    }
 
    public void setInactiveCount(long inactiveCount) {
        this.inactiveCount = inactiveCount;
    }
}