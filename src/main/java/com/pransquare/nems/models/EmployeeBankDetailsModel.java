package com.pransquare.nems.models;

import java.time.LocalDateTime;

public class EmployeeBankDetailsModel {

    private Integer empBankDetailId;
    private String accountType;
    private String bankAccountNo;
    private String bankIfsc;
    private String modifiedBy;
    private LocalDateTime modifiedDate;
    private String bankName;
    private String branchName;
    private String bankAddress;

    // Getters and Setters

    public Integer getEmpBankDetailId() {
        return empBankDetailId;
    }

    public void setEmpBankDetailId(Integer empBankDetailId) {
        this.empBankDetailId = empBankDetailId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getBankIfsc() {
        return bankIfsc;
    }

    public void setBankIfsc(String bankIfsc) {
        this.bankIfsc = bankIfsc;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

}
