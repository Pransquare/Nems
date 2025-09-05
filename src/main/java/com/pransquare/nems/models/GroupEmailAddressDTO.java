package com.pransquare.nems.models;

import java.util.List;

public class GroupEmailAddressDTO {

	private Long loggedInEmpId;
	private List<EmpIdEmailAddressDTO> empIdEmailAddressDTOs;



	public Long getLoggedInEmpId() {
		return loggedInEmpId;
	}

	public void setLoggedInEmpId(Long loggedInEmpId) {
		this.loggedInEmpId = loggedInEmpId;
	}

	public List<EmpIdEmailAddressDTO> getEmpIdEmailAddressDTOs() {
		return empIdEmailAddressDTOs;
	}

	public void setEmpIdEmailAddressDTOs(List<EmpIdEmailAddressDTO> empIdEmailAddressDTOs) {
		this.empIdEmailAddressDTOs = empIdEmailAddressDTOs;
	}

}
