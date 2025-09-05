package com.pransquare.nems.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "emp_goal_attributes")
public class EmpGoalAttribute {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emp_goal_attribute_id")
	private Long empGoalAttributeId;

	@Column(name = "employee_rating")
	private Double employeeRating;

	@Column(name = "manager_rating")
	private Double managerRating;

	@Column(name = "final_rating")
	private Double finalRating;

	@Column(name = "approveddate")
	private LocalDate approvedDate;

	@Column(name = "created_date")
	private LocalDate createdDate;

	@Column(name = "modified_date")
	private LocalDateTime modifiedDate;

	@Column(name = "submitteddate")
	private LocalDate submittedDate;

	@Column(name = "approver_id")
	private Long approverId;

	@Column(name = "emp_basic_detail_id")
	private Long empBasicDetailId;

	@Column(name = "emp_goal_setup_id")
	private Long empGoalSetupId;

	@Column(name = "approve_comments")
	private String approveComments;

	@Column(name = "employee_comments")
	private String employeeComments;

	@Column(name = "final_comments")
	private String finalComments;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "status")
	private String status;

	@Column(name = "attribute", length = 500)
	private String attribute;

	@Column(name = "attribute_description", length = 500)
	private String attributeDescription;

	@Column(name = "hr_id")
	private Integer hrId;

	// You can optionally add @ManyToOne relationships here if you have foreign key
	// mappings
}
