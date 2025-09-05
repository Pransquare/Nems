package com.pransquare.nems.models;

public class EmailModel {

	private String fromEmail;
	private String toEmail;
	private String body;
	private String cc;
	private String addressTo;
	private String requestor;
	private String reason;
	private String subject;
	private String employeeCode;

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getAddressTo() {
		return addressTo;
	}

	public void setAddressTo(String addressTo) {
		this.addressTo = addressTo;
	}

	public String getRequestor() {
		return requestor;
	}

	public void setRequestor(String requestor) {
		this.requestor = requestor;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	@Override
	public String toString() {
		return "EmailModel [fromEmail=" + fromEmail + ", toEmail=" + toEmail + ", body=" + body + ", cc=" + cc
				+ ", addressTo=" + addressTo + ", requestor=" + requestor + ", reason=" + reason + ", subject="
				+ subject + ", employeeCode=" + employeeCode + "]";
	}

}
