package com.pransquare.nems.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "expense")
public class ExpenseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "from_dt", nullable = false)
    private LocalDate from;

    @Column(name = "to_dt", nullable = false)
    private LocalDate to;

    @Column(name = "status", length = 45, nullable = false)
    private String status;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "manager_id", nullable = false)
    private Long managerId;

    @Column(name = "expense_amt_type", nullable = false)
    private String expenseAmtType;

    @Column(name = "expense_amount", nullable = false)
    private BigDecimal expenseAmount;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "expesne_name", nullable = false)
    private String expenseName;

    @Column(name = "comments", nullable = true, length = 400)
    private String comments;

    @Column(name = "approvedBy", nullable = true, length = 200)
    private String approvedBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(LocalDate to) {
        this.to = to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public List<ExpenseDetails> getExpenseDetails() {
        return expenseDetails;
    }

    public void setExpenseDetails(List<ExpenseDetails> expenseDetails) {
        this.expenseDetails = expenseDetails;
    }

    public String getExpenseAmtType() {
        return expenseAmtType;
    }

    public void setExpenseAmtType(String expenseAmtType) {
        this.expenseAmtType = expenseAmtType;
    }

    public BigDecimal getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(BigDecimal expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<ExpenseDetails> expenseDetails;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "EMPLOYEE_BASIC_DETAIL_ID", insertable = false, updatable = false)
    private EmployeeEntity employeeEntity;

    public String getEmpoyeeName() {
        return employeeEntity != null ? employeeEntity.getFullName() : null;
    }

    public String getEmployeeCode() {
        return employeeEntity != null ? employeeEntity.getEmployeeCode() : null;
    }

    public String getEmailId() {
        return employeeEntity != null ? employeeEntity.getEmailId() : null;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    @OneToOne
    @JoinColumn(name = "status", referencedColumnName = "code", updatable = false, insertable = false)
    private StatusMasterEntity statusMasterEntity;

    public String getStatusDescription() {
        return statusMasterEntity != null ? statusMasterEntity.getDescription() : null;
    }
}