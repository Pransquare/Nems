package com.pransquare.nems.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.pransquare.nems.dto.BasicGoalsConfigDTO;
import com.pransquare.nems.entities.EmpGoalDetailsEntity;
import com.pransquare.nems.entities.EmpGoalSetupEntity;
import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.entities.GoalCommentsEntity;
import com.pransquare.nems.entities.NotificationEntity;
import com.pransquare.nems.entities.UserApproverConfigurationsEntity;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.models.EmailModel;
import com.pransquare.nems.models.GoalSearchModel;
import com.pransquare.nems.models.GoalSetUpDetails;
import com.pransquare.nems.models.GoalSetUpSaveModel;
import com.pransquare.nems.repositories.ApproverConfigRepository;
import com.pransquare.nems.repositories.EmployeeRepository;
import com.pransquare.nems.repositories.GoalCommentsRepository;
import com.pransquare.nems.repositories.GoalDetailsRepository;
import com.pransquare.nems.repositories.GoalSetUpRepository;
import com.pransquare.nems.repositories.NotificationRepository;
import com.pransquare.nems.utils.Constants;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;

@Service
public class GoalSetUpService {

	private final GoalSetUpRepository goalSetUpRepository;
	private final GoalDetailsRepository goalDetailsRepository;
	private final EmployeeRepository employeeRepository;
	private final EntityManager entityManager;
	private final ApproverConfigRepository approverConfigRepository;
	private final AppraisalService appraisalService;
	private WebClient webClient;
	private GoalCommentsRepository goalCommentsRepository;
	private NotificationRepository notificationRepository;

	@Value("${master-config-service.url}")
	private String basicgoalsUrl;

	@Autowired
	private WebClient.Builder webClientBuilder;

	@Autowired
	EmailService emailService;

	@PostConstruct
	public void init() {
		this.webClient = webClientBuilder.baseUrl(basicgoalsUrl + "/Pransquare/MasterConfiguration/basicgoals/goals").build();
	}

	@Autowired
	public GoalSetUpService(GoalSetUpRepository goalSetUpRepository, GoalDetailsRepository goalDetailsRepository,
			EmployeeRepository employeeRepository, EntityManager entityManager,
			ApproverConfigRepository approverConfigRepository, AppraisalService appraisalService,
			GoalCommentsRepository goalCommentsRepository, WebClient.Builder webClientBuilder,
			NotificationRepository notificationRepository) {

		this.goalSetUpRepository = goalSetUpRepository;
		this.goalDetailsRepository = goalDetailsRepository;
		this.employeeRepository = employeeRepository;
		this.entityManager = entityManager;
		this.approverConfigRepository = approverConfigRepository;
		this.appraisalService = appraisalService;
		this.goalCommentsRepository = goalCommentsRepository;
		this.webClient = webClientBuilder.baseUrl(basicgoalsUrl).build();
		this.notificationRepository= notificationRepository;
	}

	public List<BasicGoalsConfigDTO> fetchBasicGoals() {
		return webClient.get().retrieve().bodyToFlux(BasicGoalsConfigDTO.class) // Use Flux for a list
				.collectList() // Convert Flux to List
				.block(); // Block to get the result synchronously
	}

//    public String setUpGoals(GoalSetUpSaveModel goalSetUpSaveModel) {
//    	
//    	
////    	EmployeeEntity employeeEntity=employeeRepository.findByEmployeeBasicDetailIdAndGoalSetup()d
//    			
//        // Create and populate EmpGoalSetupEntity
//        EmpGoalSetupEntity setupEntity = new EmpGoalSetupEntity();
//        setupEntity.setApprovedBy(goalSetUpSaveModel.getApprovedBy());
//        setupEntity.setApprovedDate(goalSetUpSaveModel.getApprovedDate());
//        setupEntity.setApproverComments(goalSetUpSaveModel.getApproverComments());
//        setupEntity.setComments(goalSetUpSaveModel.getComments());
//        setupEntity.setEmpBasicDetailId(goalSetUpSaveModel.getEmplBasicId());
//        setupEntity.setApproverId(goalSetUpSaveModel.getApproverId());
//        setupEntity.setStatus(goalSetUpSaveModel.getStatus());
//        setupEntity.setSubmittedDate(goalSetUpSaveModel.getSubmittedDate());
//        setupEntity.setCreatedDate(LocalDateTime.now());
//        setupEntity.setCreatedBy(goalSetUpSaveModel.getCreatedBy());
//        setupEntity.setFromDate(goalSetUpSaveModel.getFromDate());
//        setupEntity.setFromDate(goalSetUpSaveModel.getToDate());
//
//        EmpGoalSetupEntity savedSetup = goalSetUpRepository.save(setupEntity);
//
//        // Create and persist goal detail entries
//        List<EmpGoalDetailsEntity> detailEntities = new ArrayList<>();
//        for (GoalSetUpDetails modelDetail : goalSetUpSaveModel.getGoalSetUpDetails()) {
//            EmpGoalDetailsEntity detail = new EmpGoalDetailsEntity();
//            detail.setComments(modelDetail.getComments());
//            detail.setEmpBasicDetailId(modelDetail.getEmployeeBasicDetails());
//            detail.setFinalComments(modelDetail.getFinalComments());
//            detail.setManagerComments(modelDetail.getManagerComments());
//            detail.setStatus(modelDetail.getStatus());
//            detail.setCreatedBy(modelDetail.getCreatedBy());
//            detail.setCreatedDate(LocalDate.now());
//            detail.setGoal(modelDetail.getGoal());
//            detail.setGoalType(modelDetail.getGoalType());
//            detail.setGoalDescription(modelDetail.getGoalDescription());
//            detail.setEmpGoalSetupId(savedSetup.getEmpGoalSetupId());
//            detailEntities.add(detail);
//        }
//
//        goalDetailsRepository.saveAll(detailEntities);
//
//        return "Goal setup saved successfully with ID: " + savedSetup.getEmpGoalSetupId();
//    }

//	public String setUpGoals(GoalSetUpSaveModel goalSetUpSaveModel) {
//
//		EmpGoalSetupEntity setupEntity;
//
//		// 🧠 Step 1: Check if we're updating or inserting a new parent
//		if (goalSetUpSaveModel.getEmpGoalSetupId() != null && goalSetUpSaveModel.getEmpGoalSetupId() > 0) {
//			Optional<EmpGoalSetupEntity> existingSetupOpt = goalSetUpRepository
//					.findById(goalSetUpSaveModel.getEmpGoalSetupId());
//
//			if (existingSetupOpt.isPresent()) {
//				EmployeeEntity employeeEntity = employeeRepository
//						.findByEmployeeBasicDetailId(goalSetUpSaveModel.getEmplBasicId());
//				employeeEntity.setGoalSetup(goalSetUpSaveModel.getStatus());
//				employeeRepository.save(employeeEntity);
//				setupEntity = existingSetupOpt.get();
//				setupEntity.setModifiedBy(goalSetUpSaveModel.getCreatedBy());
//				setupEntity.setModifiedDate(LocalDateTime.now());
//			} else {
//				setupEntity = new EmpGoalSetupEntity();
//
//				setupEntity.setCreatedBy(goalSetUpSaveModel.getCreatedBy());
//				setupEntity.setCreatedDate(LocalDateTime.now());
//			}
//		} else {
//			setupEntity = new EmpGoalSetupEntity(); // New insert
//			EmployeeEntity employeeEntity = employeeRepository
//					.findByEmployeeBasicDetailId(goalSetUpSaveModel.getEmplBasicId());
//			employeeEntity.setGoalSetup(goalSetUpSaveModel.getStatus());
//			employeeRepository.save(employeeEntity);
//			setupEntity.setCreatedBy(goalSetUpSaveModel.getCreatedBy());
//			setupEntity.setCreatedDate(LocalDateTime.now());
//		}
//
//		// 🧾 Populate common parent fields
//		setupEntity.setApprovedBy(goalSetUpSaveModel.getApprovedBy());
//		setupEntity.setApprovedDate(goalSetUpSaveModel.getApprovedDate());
//		setupEntity.setApproverComments(goalSetUpSaveModel.getApproverComments());
//		setupEntity.setComments(goalSetUpSaveModel.getComments());
//		setupEntity.setEmpBasicDetailId(goalSetUpSaveModel.getEmplBasicId());
//		setupEntity.setApproverId(goalSetUpSaveModel.getApproverId());
//		setupEntity.setStatus(goalSetUpSaveModel.getStatus());
//		setupEntity.setSubmittedDate(goalSetUpSaveModel.getSubmittedDate());
//		setupEntity.setFromDate(goalSetUpSaveModel.getFromDate());
//		setupEntity.setToDate(goalSetUpSaveModel.getToDate());
//
//		// 💾 Save parent
//		EmpGoalSetupEntity savedSetup = goalSetUpRepository.save(setupEntity);
//
//		// Step 2: Handle children
//		List<EmpGoalDetailsEntity> detailEntities = new ArrayList<>();
//
//		for (GoalSetUpDetails modelDetail : goalSetUpSaveModel.getGoalSetUpDetails()) {
//			EmpGoalDetailsEntity detail;
//
//			if (modelDetail.getEmpGoalDetailId() != null && modelDetail.getEmpGoalDetailId() > 0) {
//				Optional<EmpGoalDetailsEntity> existingDetail = goalDetailsRepository
//						.findById(modelDetail.getEmpGoalDetailId());
//				if (existingDetail.isPresent()) {
//					detail = existingDetail.get();
//					detail.setModifiedBy(goalSetUpSaveModel.getCreatedBy());
//					detail.setModifiedDate(LocalDateTime.now());
//				} else {
//					detail = new EmpGoalDetailsEntity(); // fallback to new
//					detail.setCreatedBy(modelDetail.getCreatedBy());
//					detail.setCreatedDate(LocalDate.now());
//				}
//			} else {
//				detail = new EmpGoalDetailsEntity();
//				detail.setCreatedBy(modelDetail.getCreatedBy());
//				detail.setCreatedDate(LocalDate.now());
//			}
//
//			detail.setComments(modelDetail.getComments());
//			detail.setEmpBasicDetailId(modelDetail.getEmployeeBasicDetails());
//			detail.setFinalComments(modelDetail.getFinalComments());
//			detail.setManagerComments(modelDetail.getManagerComments());
//			detail.setStatus(modelDetail.getStatus());
//            detail.setPercentage(modelDetail.getPercentage());
//            detail.setManagerConcent(modelDetail.getManagerConsent());
//            detail.setHrConcent(modelDetail.getHrConsent());
//			detail.setGoal(modelDetail.getGoal());
//			detail.setGoalType(modelDetail.getGoalType());
//			detail.setGoalDescription(modelDetail.getGoalDescription());
//			detail.setEmpGoalSetupId(savedSetup.getEmpGoalSetupId());
//
//			detailEntities.add(detail);
//		}
//
//		goalDetailsRepository.saveAll(detailEntities);
//		if(goalSetUpSaveModel.getStatus().equals("129")){
//			 EmailModel emailModel=new EmailModel();
//		        emailModel.setAddressTo(setupEntity.getFullName());
//		        emailModel.setToEmail(setupEntity.getEmailId());
//		        emailModel.setSubject("Goal setup for the Year - "+(goalSetUpSaveModel.getFromDate().getYear()));
//		        emailService.goalApprovalEmail(emailModel);
//			
//		}
//		else 	if(goalSetUpSaveModel.getStatus().equals("128")){
//			EmployeeEntity managerEnt = employeeRepository
//					.findByEmployeeBasicDetailId(setupEntity.getApproverId());
//			 EmailModel emailModel=new EmailModel();
//		        emailModel.setAddressTo(managerEnt.getFullName());
//		        emailModel.setRequestor(setupEntity.getFullName());
//		        emailModel.setToEmail(managerEnt.getEmailId());
//		        emailModel.setSubject("Approval Request: Goal setup for the Year - "+(goalSetUpSaveModel.getFromDate().getYear()));
//		        emailService.goalEmailToManager(emailModel);
//			
//		}
//		
//
//		return "Goal setup " + (goalSetUpSaveModel.getEmpGoalSetupId() != null ? "updated" : "saved")
//				+ " successfully with ID: " + savedSetup.getEmpGoalSetupId();
//	}
//	public String setUpGoals(GoalSetUpSaveModel goalSetUpSaveModel) {
//
//		EmpGoalSetupEntity setupEntity;
//
//		// 🧠 Step 1: Check if we're updating or inserting a new parent
//		if (goalSetUpSaveModel.getEmpGoalSetupId() != null && goalSetUpSaveModel.getEmpGoalSetupId() > 0) {
//			Optional<EmpGoalSetupEntity> existingSetupOpt = goalSetUpRepository
//					.findById(goalSetUpSaveModel.getEmpGoalSetupId());
//
//			if (existingSetupOpt.isPresent()) {
//				EmployeeEntity employeeEntity = employeeRepository
//						.findByEmployeeBasicDetailId(goalSetUpSaveModel.getEmplBasicId());
//				employeeEntity.setGoalSetup(goalSetUpSaveModel.getStatus());
//				employeeRepository.save(employeeEntity);
//				setupEntity = existingSetupOpt.get();
//				setupEntity.setModifiedBy(goalSetUpSaveModel.getCreatedBy());
//				setupEntity.setModifiedDate(LocalDateTime.now());
//			} else {
//				setupEntity = new EmpGoalSetupEntity();
//				setupEntity.setCreatedBy(goalSetUpSaveModel.getCreatedBy());
//				setupEntity.setCreatedDate(LocalDateTime.now());
//			}
//		} else {
//			setupEntity = new EmpGoalSetupEntity(); // New insert
//			EmployeeEntity employeeEntity = employeeRepository
//					.findByEmployeeBasicDetailId(goalSetUpSaveModel.getEmplBasicId());
//			employeeEntity.setGoalSetup(goalSetUpSaveModel.getStatus());
//			employeeRepository.save(employeeEntity);
//			setupEntity.setCreatedBy(goalSetUpSaveModel.getCreatedBy());
//			setupEntity.setCreatedDate(LocalDateTime.now());
//		}
//
//		// 🧾 Populate common parent fields
//		setupEntity.setApprovedBy(goalSetUpSaveModel.getApprovedBy());
//		setupEntity.setApprovedDate(goalSetUpSaveModel.getApprovedDate());
////		setupEntity.setApproverComments(goalSetUpSaveModel.getApproverComments());
//		GoalCommentsEntity goalCommentsEntity = new GoalCommentsEntity();
//		goalCommentsEntity.setComments(goalSetUpSaveModel.getComments());
//		goalCommentsEntity.setCreatedBy(goalSetUpSaveModel.getCreatedBy());
//		goalCommentsEntity.setCreatedDate(LocalDateTime.now());
//		goalCommentsEntity.setEmpGoalSetupId(goalSetUpSaveModel.getEmpGoalSetupId() );
//		goalCommentsRepository.save(goalCommentsEntity);
////		setupEntity.setComments(goalSetUpSaveModel.getComments());
//		setupEntity.setEmpBasicDetailId(goalSetUpSaveModel.getEmplBasicId());
//		setupEntity.setApproverId(goalSetUpSaveModel.getApproverId());
//		setupEntity.setStatus(goalSetUpSaveModel.getStatus());
//		setupEntity.setSubmittedDate(goalSetUpSaveModel.getSubmittedDate());
//		setupEntity.setFromDate(goalSetUpSaveModel.getFromDate());
//		setupEntity.setToDate(goalSetUpSaveModel.getToDate());
//
//		// 💾 Save parent
//		EmpGoalSetupEntity savedSetup = goalSetUpRepository.save(setupEntity);
//
//		// 🧹 Deletion: remove children not present in the input
//		if (goalSetUpSaveModel.getEmpGoalSetupId() != null && goalSetUpSaveModel.getEmpGoalSetupId() > 0) {
//			List<EmpGoalDetailsEntity> existingChildren = goalDetailsRepository
//					.findByEmpGoalSetupId(savedSetup.getEmpGoalSetupId());
//			Set<Long> incomingIds = goalSetUpSaveModel.getGoalSetUpDetails().stream()
//					.map(GoalSetUpDetails::getEmpGoalDetailId).filter(Objects::nonNull).collect(Collectors.toSet());
//
//			List<EmpGoalDetailsEntity> toDelete = existingChildren.stream()
//					.filter(e -> !incomingIds.contains(e.getEmpGoalDetailsId())).collect(Collectors.toList());
//
//			if (!toDelete.isEmpty()) {
//				goalDetailsRepository.deleteAll(toDelete);
//			}
//		}
//
//		// Step 2: Handle children (insert/update)
//		List<EmpGoalDetailsEntity> detailEntities = new ArrayList<>();
//
//		for (GoalSetUpDetails modelDetail : goalSetUpSaveModel.getGoalSetUpDetails()) {
//			EmpGoalDetailsEntity detail;
//
//			if (modelDetail.getEmpGoalDetailId() != null && modelDetail.getEmpGoalDetailId() > 0) {
//				Optional<EmpGoalDetailsEntity> existingDetail = goalDetailsRepository
//						.findById(modelDetail.getEmpGoalDetailId());
//				if (existingDetail.isPresent()) {
//					detail = existingDetail.get();
//					detail.setModifiedBy(goalSetUpSaveModel.getCreatedBy());
//					detail.setModifiedDate(LocalDateTime.now());
//				} else {
//					detail = new EmpGoalDetailsEntity(); // fallback to new
//					detail.setCreatedBy(modelDetail.getCreatedBy());
//					detail.setCreatedDate(LocalDate.now());
//				}
//			} else {
//				detail = new EmpGoalDetailsEntity();
//				detail.setCreatedBy(modelDetail.getCreatedBy());
//				detail.setCreatedDate(LocalDate.now());
//			}
//			GoalCommentsEntity goalComments = new GoalCommentsEntity();
//			goalComments.setComments(modelDetail.getComments());
//			goalComments.setCreatedBy(goalSetUpSaveModel.getCreatedBy());
//			goalComments.setCreatedDate(LocalDateTime.now());
//			goalComments.setEmpGoalSetupId(goalSetUpSaveModel.getEmpGoalSetupId() );
//			goalCommentsRepository.save(goalComments);
////			detail.setComments(modelDetail.getComments());
//			detail.setEmpBasicDetailId(modelDetail.getEmployeeBasicDetails());
//			detail.setFinalComments(modelDetail.getFinalComments());
//			detail.setManagerComments(modelDetail.getManagerComments());
//			detail.setStatus(modelDetail.getStatus());
//			detail.setPercentage(modelDetail.getPercentage());
//			detail.setManagerConcent(modelDetail.getManagerConsent());
//			detail.setHrConcent(modelDetail.getHrConsent());
//			detail.setGoal(modelDetail.getGoal());
//			detail.setGoalType(modelDetail.getGoalType());
//			detail.setGoalDescription(modelDetail.getGoalDescription());
//			detail.setEmpGoalSetupId(savedSetup.getEmpGoalSetupId());
//
//			detailEntities.add(detail);
//		}
//
//		goalDetailsRepository.saveAll(detailEntities);
//
//		// ✉️ Email logic
//		if (goalSetUpSaveModel.getStatus().equals("129")) {
//			EmailModel emailModel = new EmailModel();
//			emailModel.setAddressTo(setupEntity.getFullName());
//			emailModel.setToEmail(setupEntity.getEmailId());
//			emailModel.setSubject("Goal setup for the Year - " + (goalSetUpSaveModel.getFromDate().getYear()));
//			emailService.goalApprovalEmail(emailModel);
//		} else if (goalSetUpSaveModel.getStatus().equals("128")) {
//			EmployeeEntity managerEnt = employeeRepository.findByEmployeeBasicDetailId(setupEntity.getApproverId());
//			EmailModel emailModel = new EmailModel();
//			emailModel.setAddressTo(managerEnt.getFullName());
//			emailModel.setRequestor(setupEntity.getFullName());
//			emailModel.setToEmail(managerEnt.getEmailId());
//			emailModel.setSubject(
//					"Approval Request: Goal setup for the Year - " + (goalSetUpSaveModel.getFromDate().getYear()));
//			emailService.goalEmailToManager(emailModel);
//		}
//
//		return "Goal setup " + (goalSetUpSaveModel.getEmpGoalSetupId() != null ? "updated" : "saved")
//				+ " successfully with ID: " + savedSetup.getEmpGoalSetupId();
//	}
	
	public String setUpGoals(GoalSetUpSaveModel goalSetUpSaveModel) {
		goalSetUpSaveModel.getCreatedBy();
		List<EmployeeEntity> users=employeeRepository.findByEmailId(goalSetUpSaveModel.getCreatedBy());
		String nameForComments=users.get(0).getFullName();
	    // 🔒 Ensure parent ID is provided for update
	    if (goalSetUpSaveModel.getEmpGoalSetupId() == null || goalSetUpSaveModel.getEmpGoalSetupId() <= 0) {
	        throw new IllegalArgumentException("Goal Setup ID must be provided for update.");
	    }

	    // 🔍 Fetch and validate existing parent
	    Optional<EmpGoalSetupEntity> existingSetupOpt = goalSetUpRepository.findById(goalSetUpSaveModel.getEmpGoalSetupId());
	    if (!existingSetupOpt.isPresent()) {
	        throw new ResourceNotFoundException("Goal Setup not found with ID: " + goalSetUpSaveModel.getEmpGoalSetupId());
	    }

	    EmpGoalSetupEntity setupEntity = existingSetupOpt.get();

	    // 🔄 Update metadata
	    setupEntity.setModifiedBy(goalSetUpSaveModel.getCreatedBy());
	    setupEntity.setModifiedDate(LocalDateTime.now());

	    // 👤 Update employee status
	    EmployeeEntity employeeEntity = employeeRepository.findByEmployeeBasicDetailId(goalSetUpSaveModel.getEmplBasicId());
	    employeeEntity.setGoalSetup(goalSetUpSaveModel.getStatus());
	    employeeRepository.save(employeeEntity);

	    // 🧾 Update parent fields
	    setupEntity.setApprovedBy(goalSetUpSaveModel.getApprovedBy());
	    setupEntity.setApprovedDate(goalSetUpSaveModel.getApprovedDate());
	    setupEntity.setEmpBasicDetailId(goalSetUpSaveModel.getEmplBasicId());
	    setupEntity.setApproverId(goalSetUpSaveModel.getApproverId());
	    setupEntity.setStatus(goalSetUpSaveModel.getStatus());
	    setupEntity.setSubmittedDate(goalSetUpSaveModel.getSubmittedDate());
	    setupEntity.setFromDate(goalSetUpSaveModel.getFromDate());
	    setupEntity.setToDate(goalSetUpSaveModel.getToDate());

	    // 💬 Save comment
	    GoalCommentsEntity goalComment = new GoalCommentsEntity();
	    goalComment.setComments(goalSetUpSaveModel.getComments());
	    goalComment.setCreatedBy(goalSetUpSaveModel.getCreatedBy());
	    goalComment.setCreatedDate(LocalDateTime.now());
	    goalComment.setEmpGoalSetupId(setupEntity.getEmpGoalSetupId());
	    goalComment.setEmpBasicDetailId(goalSetUpSaveModel.getEmplBasicId());
	    goalComment.setStatus(goalSetUpSaveModel.getStatus());
	    goalComment.setEmpName(nameForComments);
	    goalCommentsRepository.save(goalComment);

	    // 💾 Save parent
	    EmpGoalSetupEntity savedSetup = goalSetUpRepository.save(setupEntity);

	    // 🧹 Delete removed goal details
	    List<EmpGoalDetailsEntity> existingChildren = goalDetailsRepository.findByEmpGoalSetupId(savedSetup.getEmpGoalSetupId());
	    Set<Long> incomingIds = goalSetUpSaveModel.getGoalSetUpDetails().stream()
	            .map(GoalSetUpDetails::getEmpGoalDetailId)
	            .filter(Objects::nonNull)
	            .collect(Collectors.toSet());

	    List<EmpGoalDetailsEntity> toDelete = existingChildren.stream()
	            .filter(e -> !incomingIds.contains(e.getEmpGoalDetailsId()))
	            .collect(Collectors.toList());

	    if (!toDelete.isEmpty()) {
	        goalDetailsRepository.deleteAll(toDelete);
	    }

	    // 🔄 Insert or update goal details
	    List<EmpGoalDetailsEntity> detailEntities = new ArrayList<>();
	    for (GoalSetUpDetails modelDetail : goalSetUpSaveModel.getGoalSetUpDetails()) {
	        EmpGoalDetailsEntity detail;

	        if (modelDetail.getEmpGoalDetailId() != null && modelDetail.getEmpGoalDetailId() > 0) {
	            Optional<EmpGoalDetailsEntity> existingDetailOpt = goalDetailsRepository.findById(modelDetail.getEmpGoalDetailId());
	            detail = existingDetailOpt.orElseGet(EmpGoalDetailsEntity::new); // fallback to new if ID is stale
	            detail.setModifiedBy(goalSetUpSaveModel.getCreatedBy());
	            detail.setModifiedDate(LocalDateTime.now());
	        } else {
	            detail = new EmpGoalDetailsEntity();
	            detail.setCreatedBy(goalSetUpSaveModel.getCreatedBy());
	            detail.setCreatedDate(LocalDate.now());
	        }

	        // 🧾 Save detail comment
//	        GoalCommentsEntity detailComment = new GoalCommentsEntity();
//	        detailComment.setComments(modelDetail.getComments());
//	        detailComment.setCreatedBy(goalSetUpSaveModel.getCreatedBy());
//	        detailComment.setCreatedDate(LocalDateTime.now());
//	        detailComment.setEmpGoalSetupId(savedSetup.getEmpGoalSetupId());
//	        detailComment.setEmpBasicDetailId(modelDetail.getEmployeeBasicDetails());
//	        detailComment.setStatus(modelDetail.getStatus());
//	        goalCommentsRepository.save(detailComment);

	        // ✏️ Populate detail
	        detail.setEmpBasicDetailId(modelDetail.getEmployeeBasicDetails());
	        detail.setFinalComments(modelDetail.getFinalComments());
	        detail.setManagerComments(modelDetail.getManagerComments());
	        detail.setStatus(modelDetail.getStatus());
	        detail.setPercentage(modelDetail.getPercentage());
	        detail.setManagerConcent(modelDetail.getManagerConsent());
	        detail.setHrConcent(modelDetail.getHrConsent());
	        detail.setGoal(modelDetail.getGoal());
	        detail.setGoalType(modelDetail.getGoalType());
	        detail.setGoalDescription(modelDetail.getGoalDescription());
	        detail.setEmpGoalSetupId(savedSetup.getEmpGoalSetupId());

	        detailEntities.add(detail);
	    }

	    goalDetailsRepository.saveAll(detailEntities);

	    // ✉️ Email notifications
	    if ("128".equals(goalSetUpSaveModel.getStatus())) {
	        EmailModel emailModel = new EmailModel();
	        emailModel.setAddressTo(setupEntity.getFullName());
	        emailModel.setToEmail(setupEntity.getEmailId());
	        emailModel.setSubject("Goal setup for the Year - " + goalSetUpSaveModel.getFromDate().getYear());
	        emailService.goalInitiationEmail(emailModel);
	    	NotificationEntity notificationEntity = new NotificationEntity();
			notificationEntity.setEmployeeId(setupEntity.getApproverId());
			notificationEntity.setModule("Performence");
			notificationEntity.setPath(Constants.APPRISAL_PATH);
			notificationEntity.setMessage("New Goal setup is available for you. Kindly review and Confirm.");
			notificationEntity.setStatus("115");
			notificationRepository.save(notificationEntity);
	    } else if ("129".equals(goalSetUpSaveModel.getStatus())) {
	        EmployeeEntity managerEnt = employeeRepository.findByEmployeeBasicDetailId(setupEntity.getApproverId());
	        EmailModel emailModel = new EmailModel();
	        emailModel.setAddressTo(managerEnt.getFullName());
	        emailModel.setRequestor(setupEntity.getFullName());
	        emailModel.setToEmail(managerEnt.getEmailId());
	        emailModel.setSubject("Goal setup for the Year - " + goalSetUpSaveModel.getFromDate().getYear());
	        emailService.goalEmailToManager(emailModel);
	    	NotificationEntity notificationEntity = new NotificationEntity();
			notificationEntity.setEmployeeId(setupEntity.getApproverId());
			notificationEntity.setModule("Performence");
			notificationEntity.setPath(Constants.APPRISAL_PATH);
			notificationEntity.setMessage("Employee has Rejected the Goal, please Review It");
			notificationEntity.setStatus("115");
			notificationRepository.save(notificationEntity);
	    }
	    else if ("130".equals(goalSetUpSaveModel.getStatus())) {
	        EmployeeEntity managerEnt = employeeRepository.findByEmployeeBasicDetailId(setupEntity.getApproverId());
	        EmailModel emailModel = new EmailModel();
	        emailModel.setAddressTo(managerEnt.getFullName());
	        emailModel.setRequestor(setupEntity.getFullName());
	        emailModel.setToEmail(managerEnt.getEmailId());
	        emailModel.setSubject("Goal setup for the Year - " + goalSetUpSaveModel.getFromDate().getYear());
	        emailService.goalApprovalEmail(emailModel);
	    	NotificationEntity notificationEntity = new NotificationEntity();
			notificationEntity.setEmployeeId(setupEntity.getApproverId());
			notificationEntity.setModule("Performence");
			notificationEntity.setPath(Constants.APPRISAL_PATH);
			notificationEntity.setMessage("Employee Accepted the Goal");
			notificationEntity.setStatus("115");
			notificationRepository.save(notificationEntity);
	    }

	    return "Goal setup updated successfully with ID: " + savedSetup.getEmpGoalSetupId();
	}



	private LocalDate convertToLocalDate(Date date) {
		return date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
	}

	private LocalDateTime convertToLocalDateTime(Date date) {
		return date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime() : null;
	}

	public Page<EmpGoalSetupEntity> searchGoals(GoalSearchModel goalSearchModel) {
		System.out.println("goalSearchModel" + goalSearchModel);
		int page = goalSearchModel.getPage();
		int size = goalSearchModel.getSize();
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));

		Specification<EmpGoalSetupEntity> spec = Specification.where(null);

		if (goalSearchModel.getEmpBasicDetailId() != null) {
			spec = spec.and(hasEmpBasicDetailId(goalSearchModel.getEmpBasicDetailId()));
		}

		if (goalSearchModel.getApproverId() != null) {
			spec = spec.and(hasApproverId(goalSearchModel.getApproverId()));
		}

		if (goalSearchModel.getStatus() != null && !goalSearchModel.getStatus().isEmpty()) {
			spec = spec.and(hasStatus(goalSearchModel.getStatus()));
		}

		if (goalSearchModel.getFromDate() != null && goalSearchModel.getToDate() != null) {
			spec = spec.and(hasDateRange(goalSearchModel.getFromDate(), goalSearchModel.getToDate()));
		}

		if (goalSearchModel.getHrId() != null) {
			spec = spec.and(hasHrId(goalSearchModel.getHrId()));
		}

		return goalSetUpRepository.findAll(spec, pageable);
	}

	public static Specification<EmpGoalSetupEntity> hasEmpBasicDetailId(Long empId) {
		return (root, query, cb) -> cb.equal(root.get("empBasicDetailId"), empId);
	}

	public static Specification<EmpGoalSetupEntity> hasApproverId(Long approverId) {
		return (root, query, cb) -> cb.equal(root.get("approverId"), approverId);
	}

	public static Specification<EmpGoalSetupEntity> hasHrId(Long hrId) {
		return (root, query, cb) -> cb.equal(root.get("hrId"), hrId);
	}

	public static Specification<EmpGoalSetupEntity> hasStatus(List<String> statusList) {
		return (root, query, cb) -> {
			if (statusList == null || statusList.isEmpty()) {
				return cb.conjunction(); // No filtering
			}
			return root.get("status").in(statusList);
		};
	}

	public static Specification<EmpGoalSetupEntity> hasDateRange(LocalDate from, LocalDate to) {
		return (root, query, cb) -> cb.between(root.get("fromDate"), from, to);
	}

	public String initiateGoalSetup(List<GoalSetUpSaveModel> goalSetUpSaveModels) {
		for (GoalSetUpSaveModel goalSetUpSaveModel : goalSetUpSaveModels) {
			EmpGoalSetupEntity empGoalSetup = goalSetUpRepository
					.findByEmpBasicDetailIdAndStatusNot(goalSetUpSaveModel.getEmplBasicId(), "131");
			if (empGoalSetup != null) {
				throw new ResourceNotFoundException("Goal Setup for the Employee already been initiated");
			}

			List<BasicGoalsConfigDTO> basicGoals = fetchBasicGoals();
//		List<BasicGoalsConfigDTO> basicGoals=appraisalService.getActiveGoals();
//	    	EmployeeEntity employeeEntity=employeeRepository.findByEmployeeBasicDetailIdAndGoalSetup()d
			UserApproverConfigurationsEntity userApprover = approverConfigRepository
					.findByEmpBasicDetailIdAndModuleName(goalSetUpSaveModel.getEmplBasicId(), "jobManager");
			UserApproverConfigurationsEntity hrApprover = approverConfigRepository
					.findByEmpBasicDetailIdAndModuleName(goalSetUpSaveModel.getEmplBasicId(), "hrProcess");
			// Create and populate EmpGoalSetupEntity
			EmpGoalSetupEntity setupEntity = new EmpGoalSetupEntity();
			setupEntity.setComments(goalSetUpSaveModel.getComments());
			setupEntity.setEmpBasicDetailId(goalSetUpSaveModel.getEmplBasicId());
			setupEntity.setApproverId(userApprover.getApproverId());
			setupEntity.setStatus("127");
			setupEntity.setCreatedDate(LocalDateTime.now());
			setupEntity.setCreatedBy(goalSetUpSaveModel.getCreatedBy());
			setupEntity.setFromDate(goalSetUpSaveModel.getFromDate());
			setupEntity.setFromDate(goalSetUpSaveModel.getToDate());
			setupEntity.setHrId(hrApprover.getApproverId());

			EmpGoalSetupEntity savedSetup = goalSetUpRepository.save(setupEntity);
			EmailModel emailModel = new EmailModel();
			emailModel.setAddressTo(setupEntity.getFullName());
			emailModel.setToEmail(setupEntity.getEmailId());
			emailModel.setSubject("Goal Initiation for the Year - " + (goalSetUpSaveModel.getFromDate().getYear()));
			emailService.goalEmailToManager(emailModel);

			// Create and persist goal detail entries
			List<EmpGoalDetailsEntity> detailEntities = new ArrayList<>();
			for (BasicGoalsConfigDTO basicGoal : basicGoals) {
				EmpGoalDetailsEntity detail = new EmpGoalDetailsEntity();
				detail.setEmpBasicDetailId(goalSetUpSaveModel.getEmplBasicId());
				detail.setStatus("127");
				detail.setCreatedBy(goalSetUpSaveModel.getCreatedBy());
				detail.setCreatedDate(LocalDate.now());
				detail.setGoal(basicGoal.getGoal());
				detail.setGoalType(basicGoal.getGoalType());
				detail.setGoalDescription(basicGoal.getGoalDescription());
				detail.setEmpGoalSetupId(savedSetup.getEmpGoalSetupId());
				detail.setFromDate(goalSetUpSaveModel.getFromDate());
				detail.setToDate(goalSetUpSaveModel.getToDate());
				detailEntities.add(detail);
			}
			NotificationEntity notificationEntity = new NotificationEntity();
			notificationEntity.setEmployeeId(userApprover.getApproverId());
			notificationEntity.setModule("Performence");
			notificationEntity.setPath(Constants.APPRISAL_PATH);
			notificationEntity.setMessage("New Goal setup is available. Kindly review and send it to Employee.");
			notificationEntity.setStatus("115");
			notificationRepository.save(notificationEntity);

			goalDetailsRepository.saveAll(detailEntities);
		}
		return "Goal setup saved successfully";
	}

}