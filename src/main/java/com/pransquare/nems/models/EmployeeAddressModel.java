package com.pransquare.nems.models;

import java.time.LocalDateTime;

public class EmployeeAddressModel {

    private Integer empAddressDetailId;
    private String addressType;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String district;
    private String pinCode;
    private String state;
    private String country;
    private String createdBy;
    private String modifiedBy;
    private LocalDateTime modifiedDate;

    // Getters and Setters


    public String getAddressLine2() {
        return addressLine2;
    }

    public EmployeeAddressModel setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        return this;
    }

    public Integer getEmpAddressDetailId() {
        return empAddressDetailId;
    }

    public EmployeeAddressModel setEmpAddressDetailId(Integer empAddressDetailId) {
        this.empAddressDetailId = empAddressDetailId;
        return this;
    }

    public String getAddressType() {
        return addressType;
    }

    public EmployeeAddressModel setAddressType(String addressType) {
        this.addressType = addressType;
        return this;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public EmployeeAddressModel setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
        return this;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public EmployeeAddressModel setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
        return this;
    }

    public String getDistrict() {
        return district;
    }

    public EmployeeAddressModel setDistrict(String district) {
        this.district = district;
        return this;
    }

    public String getPinCode() {
        return pinCode;
    }

    public EmployeeAddressModel setPinCode(String pinCode) {
        this.pinCode = pinCode;
        return this;
    }

    public String getState() {
        return state;
    }

    public EmployeeAddressModel setState(String state) {
        this.state = state;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public EmployeeAddressModel setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public EmployeeAddressModel setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public EmployeeAddressModel setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
        return this;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public EmployeeAddressModel setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
        return this;
    }
}
