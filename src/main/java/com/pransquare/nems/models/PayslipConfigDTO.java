package com.pransquare.nems.models;

public class PayslipConfigDTO {
    private Long id;
    private String code;
    private String amountName;
    private AmountType amountType;
    private String country;
    private Status status;
    private String currency;

    public PayslipConfigDTO() {
    }

    public PayslipConfigDTO(Long id, String code, String amountName, AmountType amountType, String country,
            Status status, String currency) {
        this.id = id;
        this.code = code;
        this.amountName = amountName;
        this.amountType = amountType;
        this.country = country;
        this.status = status;
        this.currency = currency;
    }

    public enum Status {
        ACTIVE, INACTIVE
    }

    public enum AmountType {
        ADDITION, DEDUCTION
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAmountName() {
        return amountName;
    }

    public void setAmountName(String amountName) {
        this.amountName = amountName;
    }

    public AmountType getAmountType() {
        return amountType;
    }

    public void setAmountType(AmountType amountType) {
        this.amountType = amountType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
