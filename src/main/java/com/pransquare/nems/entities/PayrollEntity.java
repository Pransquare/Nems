package com.pransquare.nems.entities;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "payroll")
public class PayrollEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payroll_id")
	private Long payrollId;

	@Column(name = "pay_period_year")
	private String payPeriodYear;

	@Column(name = "pay_period_month")
	private String payPeriodMonth;

	@Column(name = "EMP_BASIC_DETAIL_ID")
	private Long empBasicDetailId;

	@Column(name = "basic_salary")
	private Double basicSalary;

	@Column(name = "conveyance_allowance")
	private Double conveyanceAllowance;

	@Column(name = "HRA")
	private Double hra;

	@Column(name = "income_tax")
	private Double incomeTax;

	@Column(name = "LOP")
	private Double lop;

	@Column(name = "medical_allowance")
	private Double medicalAllowance;

	@Column(name = "net_pay")
	private Double netPay;

	@Column(name = "no_of_days")
	private Double noOfDays;

	@Column(name = "other_allowance")
	private Double otherAllowance;

	@Column(name = "PF")
	private Double pf;

	@Column(name = "pay_date")
	private LocalDateTime payDate;

	@Column(name = "professional_tax")
	private Double professionalTax;

	@Column(name = "special_allowance")
	private Double specialAllowance;

	@Column(name = "total_deductions")
	private Double totalDeductions;

	@Column(name = "total_earnings")
	private Double totalEarnings;

	@Column(name = "variable_pay")
	private Double variablePay;

	@Column(name = "DOJ")
	private String doj;

	@Column(name = "v_pay_range")
	private String vPayRange;

	@Column(name = "file_id")
	private String fileId;


	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "EMP_BASIC_DETAIL_ID", referencedColumnName = "EMPLOYEE_BASIC_DETAIL_ID", insertable = false, updatable = false)
	private EmployeeEntity employeeEntity;

	public Long getPayrollId() {
		return payrollId;
	}

	public void setPayrollId(Long payrollId) {
		this.payrollId = payrollId;
	}

	public String getPayPeriodYear() {
		return payPeriodYear;
	}

	public void setPayPeriodYear(String payPeriodYear) {
		this.payPeriodYear = payPeriodYear;
	}

	public String getPayPeriodMonth() {
		return payPeriodMonth;
	}

	public void setPayPeriodMonth(String payPeriodMonth) {
		this.payPeriodMonth = payPeriodMonth;
	}

	public Long getEmpBasicDetailId() {
		return empBasicDetailId;
	}

	public void setEmpBasicDetailId(Long empBasicDetailId) {
		this.empBasicDetailId = empBasicDetailId;
	}

	public Double getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(Double basicSalary) {
		this.basicSalary = basicSalary;
	}

	public Double getConveyanceAllowance() {
		return conveyanceAllowance;
	}

	public void setConveyanceAllowance(Double conveyanceAllowance) {
		this.conveyanceAllowance = conveyanceAllowance;
	}

	public Double getHra() {
		return hra;
	}

	public void setHra(Double hra) {
		this.hra = hra;
	}

	public Double getIncomeTax() {
		return incomeTax;
	}

	public void setIncomeTax(Double incomeTax) {
		this.incomeTax = incomeTax;
	}

	public Double getLop() {
		return lop;
	}

	public void setLop(Double lop) {
		this.lop = lop;
	}

	public Double getMedicalAllowance() {
		return medicalAllowance;
	}

	public void setMedicalAllowance(Double medicalAllowance) {
		this.medicalAllowance = medicalAllowance;
	}

	public Double getNetPay() {
		return netPay;
	}

	public void setNetPay(Double netPay) {
		this.netPay = netPay;
	}

	public Double getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(Double noOfDays) {
		this.noOfDays = noOfDays;
	}

	public Double getOtherAllowance() {
		return otherAllowance;
	}

	public void setOtherAllowance(Double otherAllowance) {
		this.otherAllowance = otherAllowance;
	}

	public Double getPf() {
		return pf;
	}

	public void setPf(Double pf) {
		this.pf = pf;
	}

	public LocalDateTime getPayDate() {
		return payDate;
	}

	public void setPayDate(LocalDateTime payDate) {
		this.payDate = payDate;
	}

	public Double getProfessionalTax() {
		return professionalTax;
	}

	public void setProfessionalTax(Double professionalTax) {
		this.professionalTax = professionalTax;
	}

	public Double getSpecialAllowance() {
		return specialAllowance;
	}

	public void setSpecialAllowance(Double specialAllowance) {
		this.specialAllowance = specialAllowance;
	}

	public Double getTotalDeductions() {
		return totalDeductions;
	}

	public void setTotalDeductions(Double totalDeductions) {
		this.totalDeductions = totalDeductions;
	}

	public Double getTotalEarnings() {
		return totalEarnings;
	}

	public void setTotalEarnings(Double totalEarnings) {
		this.totalEarnings = totalEarnings;
	}

	public Double getVariablePay() {
		return variablePay;
	}

	public void setVariablePay(Double variablePay) {
		this.variablePay = variablePay;
	}

	public String getDoj() {
		return doj;
	}

	public void setDoj(String doj) {
		this.doj = doj;
	}

	public String getvPayRange() {
		return vPayRange;
	}

	public void setvPayRange(String vPayRange) {
		this.vPayRange = vPayRange;
	}

	public EmployeeEntity getEmployeeEntity() {
		return employeeEntity;
	}

	public void setEmployeeEntity(EmployeeEntity employeeEntity) {
		this.employeeEntity = employeeEntity;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	@Override
	public String toString() {
		return "PayrollEntity [payrollId=" + payrollId + ", payPeriodYear=" + payPeriodYear + ", payPeriodMonth="
				+ payPeriodMonth + ", empBasicDetailId=" + empBasicDetailId + ", basicSalary=" + basicSalary
				+ ", conveyanceAllowance=" + conveyanceAllowance + ", hra=" + hra + ", incomeTax=" + incomeTax
				+ ", lop=" + lop + ", medicalAllowance=" + medicalAllowance + ", netPay=" + netPay + ", noOfDays="
				+ noOfDays + ", otherAllowance=" + otherAllowance + ", pf=" + pf + ", payDate=" + payDate
				+ ", professionalTax=" + professionalTax + ", specialAllowance=" + specialAllowance
				+ ", totalDeductions=" + totalDeductions + ", totalEarnings=" + totalEarnings + ", variablePay="
				+ variablePay + ", doj=" + doj + ", vPayRange=" + vPayRange + ", fileId=" + fileId + ", employeeEntity="
				+ employeeEntity + "]";
	}

	

}
