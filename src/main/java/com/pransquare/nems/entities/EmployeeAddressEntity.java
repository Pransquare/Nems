package com.pransquare.nems.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_address_details")
public class EmployeeAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMP_ADDRESS_DETAIL_ID")
    private Integer empAddressDetailId;

    @Column(name = "EMP_BASIC_DETAIL_ID")
    private Long employeeId;
    
    @Column(name = "ADDRESS_TYPE", length = 50)
    private String addressType;

    @Column(name = "ADDRESS_LINE_1", length = 200)
    private String addressLine1;

    @Column(name = "ADDRESS_LINE_2", length = 200)
    private String addressLine2;

    @Column(name = "ADDRESS_LINE_3", length = 200)
    private String addressLine3;

    @Column(name = "DISTRICT", length = 100)
    private String district;

    @Column(name = "PIN_CODE", length = 50)
    private String pinCode;

    @Column(name = "STATE", length = 100)
    private String state;

    @Column(name = "COUNTRY", length = 100)
    private String country;

    @Column(name = "CREATEDDATE")
    private LocalDateTime createdDate;

    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;

    @Column(name = "MODIFIED_BY", length = 50)
    private String modifiedBy;

    @Column(name = "MODIFIEDDATE")
    private LocalDateTime modifiedDate;
    

	public Integer getEmpAddressDetailId() {
		return empAddressDetailId;
	}

	public void setEmpAddressDetailId(Integer empAddressDetailId) {
		this.empAddressDetailId = empAddressDetailId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

}
