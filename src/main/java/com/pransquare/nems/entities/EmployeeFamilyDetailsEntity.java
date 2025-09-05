package com.pransquare.nems.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee_family_details")
public class EmployeeFamilyDetailsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMP_FAMILY_DETAIL_ID")
	private Long empFamilyDetailId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DOB")
	private Date dob;

	@Column(name = "RELATION")
	private String relation;

	@Column(name = "IS_INSURANCE_REQUIRED")
	private String isInsuranceRequired;

	@Column(name = "CREATEDDATE")
	private Date createdDate;

	@Column(name = "CREATEDBY")
	private String createdBy;

	@Column(name = "MODIFIEDBY")
	private String modifiedBy;

	@Column(name = "MODIFIEDDATE")
	private Date modifiedDate;

	@Column(name = "EMP_BASIC_DETAIL_ID")
	private Long empBasicDetailId;

	public Long getEmpFamilyDetailId() {
		return empFamilyDetailId;
	}

	public void setEmpFamilyDetailId(Long empFamilyDetailId) {
		this.empFamilyDetailId = empFamilyDetailId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getIsInsuranceRequired() {
		return isInsuranceRequired;
	}

	public void setIsInsuranceRequired(String isInsuranceRequired) {
		this.isInsuranceRequired = isInsuranceRequired;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
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

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getEmpBasicDetailId() {
		return empBasicDetailId;
	}

	public void setEmpBasicDetailId(Long empBasicDetailId) {
		this.empBasicDetailId = empBasicDetailId;
	}

	@Override
	public String toString() {
		return "EmployeeFamilyDetailsEntity [empFamilyDetailId=" + empFamilyDetailId + ", name=" + name + ", dob=" + dob
				+ ", relation=" + relation + ", isInsuranceRequired=" + isInsuranceRequired + ", createdDate="
				+ createdDate + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy + ", modifiedDate="
				+ modifiedDate + ", empBasicDetailId=" + empBasicDetailId + "]";
	}

}
