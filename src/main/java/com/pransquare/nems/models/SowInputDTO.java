package com.pransquare.nems.models;

import java.time.LocalDate;
import java.util.List;

public class SowInputDTO {
	LocalDate fromDate;
	LocalDate toDate;
	String status;
	List<Integer> deliveryManagerId;
	public LocalDate getFromDate() {
		return fromDate;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}
	public LocalDate getToDate() {
		return toDate;
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Integer> getDeliveryManagerId() {
		return deliveryManagerId;
	}
	public void setDeliveryManagerId(List<Integer> deliveryManagerId) {
		this.deliveryManagerId = deliveryManagerId;
	}
	
	

}
