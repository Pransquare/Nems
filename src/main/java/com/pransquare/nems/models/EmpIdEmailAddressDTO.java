package com.pransquare.nems.models;

public class EmpIdEmailAddressDTO {

	private Long empBasicDetailId;
	private String emailAddress;

	public EmpIdEmailAddressDTO(Long empBasicDetailId, String emailAddress) {
		super();
		this.empBasicDetailId = empBasicDetailId;
		this.emailAddress = emailAddress;
	}

	public Long getEmpBasicDetailId() {
		return empBasicDetailId;
	}

	public void setEmpBasicDetailId(Long empBasicDetailId) {
		this.empBasicDetailId = empBasicDetailId;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Override
	public String toString() {
		return "EmpIdEmailAddressModel [empBasicDetailId=" + empBasicDetailId + ", emailAddress=" + emailAddress + "]";
	}

}
