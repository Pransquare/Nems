package com.pransquare.nems.models;

import java.math.BigDecimal;

import com.pransquare.nems.models.PayslipConfigDTO.AmountType;

public class PayslipResModal {
    private Long id;
    private String labelName;
    private BigDecimal amount;
    private String currency;
    private String validator;
    private AmountType amountType;

    public PayslipResModal() {
    }

    public PayslipResModal(Long id, String labelName, BigDecimal amount, String currency, String validator,
            AmountType amountType) {
        this.id = id;
        this.labelName = labelName;
        this.amount = amount;
        this.currency = currency;
        this.validator = validator;
        this.amountType = amountType;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getValidator() {
        return validator;
    }

    public void setValidator(String validator) {
        this.validator = validator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public AmountType getAmountType() {
        return amountType;
    }

    public void setAmountType(AmountType amountType) {
        this.amountType = amountType;
    }

}
