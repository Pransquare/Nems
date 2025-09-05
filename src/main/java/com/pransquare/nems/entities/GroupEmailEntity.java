package com.pransquare.nems.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "group_emails")
public class GroupEmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "group_name", nullable = false, length = 100)
    private String groupName;

    @Column(name = "email_id", nullable = false, length = 200)
    private String emailId;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Override
    public String toString() {
        return "GroupEmail{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", emailId='" + emailId + '\'' +
                '}';
    }
}
