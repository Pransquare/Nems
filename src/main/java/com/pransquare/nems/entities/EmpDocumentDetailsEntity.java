package com.pransquare.nems.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "emp_document_details")
public class EmpDocumentDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "document_name", nullable = false, length = 200)
    private String documentName;

    @Column(name = "document_number", nullable = false, length = 200)
    private String documentNumber;

    // Constructors
    public EmpDocumentDetailsEntity() {
    }

    public EmpDocumentDetailsEntity(Long employeeId, String documentName, String documentNumber) {
        this.employeeId = employeeId;
        this.documentName = documentName;
        this.documentNumber = documentNumber;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    // toString method
    @Override
    public String toString() {
        return "EmpDocumentDetailsEntity{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", documentName='" + documentName + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                '}';
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
