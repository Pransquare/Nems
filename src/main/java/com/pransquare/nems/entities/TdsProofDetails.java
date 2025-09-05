package com.pransquare.nems.entities;
import jakarta.persistence.*;

@Entity
@Table(name = "tds_proof_details")
public class TdsProofDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tds_details_id", nullable = false)
    private Long tdsDetailsId;

    @Column(name = "document_path", nullable = false)
    private String documentPath;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTdsDetailsId() {
        return tdsDetailsId;
    }

    public void setTdsDetailsId(Long tdsDetailsId) {
        this.tdsDetailsId = tdsDetailsId;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }
}
