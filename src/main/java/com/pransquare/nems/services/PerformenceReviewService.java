package com.pransquare.nems.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.pransquare.nems.entities.EmpGoalDetailsEntity;
import com.pransquare.nems.entities.EmpGoalSetupEntity;
import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.entities.GoalAttributeEntity;
import com.pransquare.nems.entities.NotificationEntity;
import com.pransquare.nems.entities.PerformanceDetailsEntity;
import com.pransquare.nems.entities.PerformenceReviewEntity;
import com.pransquare.nems.entities.UserApproverConfigurationsEntity;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.models.AppraisalUploadModel;
import com.pransquare.nems.models.ApprisalInitiateModel;
import com.pransquare.nems.models.EmailModel;
import com.pransquare.nems.models.GoalAttributeModel;
import com.pransquare.nems.models.GoalSearchModel;
import com.pransquare.nems.models.PerformanceParameterGroupModel;
import com.pransquare.nems.models.PerformanceParameterModel;
import com.pransquare.nems.models.PerformanceSearchModel;
import com.pransquare.nems.models.PerformenceReviewModel;
import com.pransquare.nems.repositories.ApproverConfigRepository;
import com.pransquare.nems.repositories.EmployeeRepository;
import com.pransquare.nems.repositories.GoalAttributeRepository;
import com.pransquare.nems.repositories.GoalSetUpRepository;
import com.pransquare.nems.repositories.GroupSubgroupConfigRepository;
import com.pransquare.nems.repositories.NotificationRepository;
import com.pransquare.nems.repositories.PerformenceDetailsRepository;
import com.pransquare.nems.repositories.PerformenceReviewRepository;
import com.pransquare.nems.utils.Constants;
import com.pransquare.nems.utils.IntegerUtils;
import com.pransquare.nems.utils.StringUtil;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class PerformenceReviewService {

	private static final Logger logger = LogManager.getLogger(PerformenceReviewService.class);

	PerformenceReviewRepository performenceReviewRepository;

	PerformenceDetailsRepository performenceDetailsRepository;

	private EmployeeRepository employeeRepository;

	GroupSubgroupConfigRepository groupSubgroupConfigRepository;

	ApproverConfigRepository approverConfigRepository;

	EntityManager entityManager;

	NotificationRepository notificationRepository;

	@Value("${appraisalfilepath}")
	private String uploadDirectory;

	EmailService emailService;

	GoalSetUpRepository goalSetUpRepository;

	private WebClient webClient;

	GoalAttributeRepository goalAttributeRepository;
	
	GoalSetUpService goalSetUpService;

	@Value("${master-config-service.url}")
	private String masterconfigurl;

	@Autowired
	private WebClient.Builder webClientBuilder;

	@PostConstruct
	public void init() {
		this.webClient = webClientBuilder.baseUrl(masterconfigurl).build();
	}

	public PerformenceReviewService(PerformenceReviewRepository performenceReviewRepository,
			PerformenceDetailsRepository performenceDetailsRepository, EmployeeRepository employeeRepository,
			GroupSubgroupConfigRepository groupSubgroupConfigRepository,
			ApproverConfigRepository approverConfigRepository, EntityManager entityManager,
			NotificationRepository notificationRepository, EmailService emailService,
			GoalSetUpRepository goalSetUpRepository, WebClient.Builder webClientBuilder,
			GoalAttributeRepository goalAttributeRepository,GoalSetUpService goalSetUpService) {
		this.performenceReviewRepository = performenceReviewRepository;
		this.performenceDetailsRepository = performenceDetailsRepository;
		this.employeeRepository = employeeRepository;
		this.groupSubgroupConfigRepository = groupSubgroupConfigRepository;
		this.approverConfigRepository = approverConfigRepository;
		this.entityManager = entityManager;
		this.notificationRepository = notificationRepository;
		this.emailService = emailService;
		this.goalSetUpRepository = goalSetUpRepository;
		this.webClient = webClientBuilder.baseUrl(masterconfigurl).build();
		this.goalAttributeRepository = goalAttributeRepository;
		this.goalSetUpService=goalSetUpService;
	}

	@Transactional
	public String InitiateApprisal(ApprisalInitiateModel apprisalInitiateModel) {
		try {
			for (PerformenceReviewModel performenceReviewModel : apprisalInitiateModel.getPerformenceReviewModel()) {
				PerformenceReviewEntity performenceReviewEntity = new PerformenceReviewEntity();
				List<PerformenceReviewEntity> existingEntites = performenceReviewRepository
						.findByEmployeeBasicDetailId(performenceReviewModel.getEmplBasicId());
				if (existingEntites.stream().filter(
						d -> d.getStatus().equals("112") || d.getStatus().equals("113") || d.getStatus().equals("114"))
						.count() != 0) {
					throw new IllegalArgumentException(
							"Appraisal is already initiated for this member: " + existingEntites.get(0).getFullName());
				}
				performenceReviewEntity.setStatus("112");
				performenceReviewEntity.setEmployeeBasicDetailId(performenceReviewModel.getEmplBasicId());
				UserApproverConfigurationsEntity approverConfigurationsEntity = approverConfigRepository
						.findByEmpBasicDetailIdAndModuleName(performenceReviewModel.getEmplBasicId(), "performance");
				if (approverConfigurationsEntity == null) {
					throw new IllegalStateException("Please assign performance manager to all the selected Employees.");
				}
				performenceReviewEntity.setApproverId(approverConfigurationsEntity.getApproverId());
				performenceReviewEntity.setCreatedBy(apprisalInitiateModel.getUser());
				performenceReviewEntity.setCreatedDate(LocalDate.now());
				performenceReviewEntity.setModifiedBy(apprisalInitiateModel.getUser());
				performenceReviewEntity.setModifiedDate(LocalDateTime.now());
				EmpGoalSetupEntity empGoalSetupEntity = goalSetUpRepository
						.findByEmpBasicDetailIdAndStatusNot(performenceReviewModel.getEmplBasicId(), "131");
				List<EmpGoalDetailsEntity> groupSubgroupConfigEntities = empGoalSetupEntity.getEmpGoalDetailsEntity();
//						.findByGroupAndSubGroupAndStatus(apprisalInitiateModel.getGroup(),
//								apprisalInitiateModel.getSubGroup(), "108");
				if (groupSubgroupConfigEntities == null) {
					throw new IllegalStateException("Goals are not found for the employee");
				}
				performenceReviewEntity = performenceReviewRepository.save(performenceReviewEntity);
				List<PerformanceDetailsEntity> performanceDetails = new ArrayList<>();
				for (EmpGoalDetailsEntity groupSubgroupGoalEntity : groupSubgroupConfigEntities) {
					PerformanceDetailsEntity performanceDetailsEntity = new PerformanceDetailsEntity();
					performanceDetailsEntity.setPerformanceReviewId(performenceReviewEntity.getPerformarnceReviewId());
					performanceDetailsEntity.setPerformanceParameter(groupSubgroupGoalEntity.getGoal());
					performanceDetailsEntity.setGoalDecription(groupSubgroupGoalEntity.getGoalDescription());
					performanceDetailsEntity.setGoalPercentage(groupSubgroupGoalEntity.getPercentage());
					performanceDetailsEntity.setPerformanceGroup(apprisalInitiateModel.getGroup());
					performanceDetailsEntity.setPerformenceSubGroup(apprisalInitiateModel.getSubGroup());
					performanceDetailsEntity.setEmployeeBasicDetais(performenceReviewModel.getEmplBasicId());
					performanceDetailsEntity.setStatus("112");
					performanceDetailsEntity.setCreatedDate(LocalDate.now());
					performanceDetailsEntity.setCreatedBy(apprisalInitiateModel.getUser());
					performanceDetailsEntity.setModifiedBy(apprisalInitiateModel.getUser());
					performanceDetailsEntity.setMofiedDate(LocalDateTime.now());
					performanceDetails.add(performanceDetailsEntity);
				}
				List<String> attributes = getActiveAttributes().getBody();
				if (attributes != null && !attributes.isEmpty()) {
				    for (String attribute : attributes) {
				        GoalAttributeEntity goalAttributeEntity = new GoalAttributeEntity();
				        goalAttributeEntity.setCreatedBy(apprisalInitiateModel.getUser());
				        goalAttributeEntity.setCreatedDate(LocalDateTime.now());
				        goalAttributeEntity.setEmpBasicDetailId(performenceReviewModel.getEmplBasicId());
				        goalAttributeEntity.setAttribute(attribute);
				        goalAttributeEntity.setPerformanceReviewId(performenceReviewEntity.getPerformarnceReviewId());
				        goalAttributeEntity.setApproverId(approverConfigurationsEntity.getApproverId());
				        
				        logger.info("Saving attribute: " + attribute); // Log each iteration

				        goalAttributeRepository.save(goalAttributeEntity);
				    }
				} else {
				    logger.warn("No attributes returned from getActiveAttributes()");
				}

				NotificationEntity notificationEntity = new NotificationEntity();
				notificationEntity.setEmployeeId(performenceReviewEntity.getEmployeeBasicDetailId());
				notificationEntity.setModule("Performence");
				notificationEntity.setPath(Constants.APPRISAL_PATH);
				notificationEntity.setMessage("New appraisal is available. Kindly submit with self rating.");
				notificationEntity.setStatus("115");
				notificationRepository.save(notificationEntity);
				performenceDetailsRepository.saveAll(performanceDetails);
			}
			return "Apprisal initiated successfully";
		} catch (Exception e) {
			logger.error("Error during appraisal initiation for group {} and subgroup {}: {}",
					apprisalInitiateModel.getGroup(), apprisalInitiateModel.getSubGroup(), e.getMessage());
			throw new ResourceNotFoundException("Failed to initiate apprisal: " + e.getMessage());
		}

	}

	public List<EmployeeEntity> getEmployeesByPerformanceGroup(String performanceGroup, String performanceSubGroup) {
		return employeeRepository.findByGroupAndSubGroupAndStatus(performanceGroup, performanceSubGroup, "108");
	}

	public List<Object[]> getParamsByElpoyeeId(Long empId) {
		return performenceDetailsRepository.findEmployeeAndPerformanceDetailsByEmpId(empId);
	}

	public List<PerformanceParameterGroupModel> getEmployeePerformanceDetails(Long empId) {
		List<Object[]> results = performenceDetailsRepository.findEmployeeAndPerformanceDetailsByEmpId(empId);
		Map<String, PerformanceParameterGroupModel> groupedDetails = new HashMap<>();

		for (Object[] result : results) {
			Long empBasicDetailId = (Long) result[0];
			String performanceGroup = (String) result[1];
			String performanceSubGroup = (String) result[2];
			String performanceParameter = (String) result[3];
			String comments = (String) result[4];
			String selfRating = (String) result[5]; // selfRating is varchar
			String managerRating = (String) result[6]; // managerRating is varchar
			String managerComments = (String) result[7];
			String finalRating = (String) result[8];
			String finalComments = (String) result[9];
			String key = empBasicDetailId + performanceGroup + performanceSubGroup;

			PerformanceParameterGroupModel groupModel = groupedDetails.computeIfAbsent(key,
					k -> new PerformanceParameterGroupModel(empBasicDetailId, performanceGroup, performanceSubGroup));

			PerformanceParameterModel parameterModel = new PerformanceParameterModel(performanceParameter, comments,
					selfRating, managerRating, managerComments, finalRating, finalComments);

			groupModel.addPerformanceParameter(parameterModel);
		}

		return new ArrayList<>(groupedDetails.values());
	}

	public PerformenceReviewEntity getPerformanceReviewEntity(Long empId, String employeeType) {
		try {
			if (StringUtil.isNotNull(employeeType)) {
				if (employeeType.equals("employee")) {
					return performenceReviewRepository.findByEmployeeBasicDetailIdAndStatus(empId, "112");
				} else {
					return performenceReviewRepository.findByEmployeeBasicDetailIdAndStatusNot(empId, "102");
				}
			} else {
				throw new IllegalStateException("Employee type not found");
			}

		} catch (Exception e) {
			throw new IllegalStateException("Error while getting PerformanceReviewEntity");
		}
	}

	// @Transactional
	// public String updateApprisalDetails(PerformenceReviewModel
	// performenceReviewModel) {
	// try {
	// if (!IntegerUtils.isNotNull(performenceReviewModel.getPerformrnceReviewId()))
	// {
	// throw new IllegalArgumentException("PerformrnceReviewId is required");
	// }
	//
	// Optional<PerformenceReviewEntity> performenceReviewEntity =
	// performenceReviewRepository
	// .findById(performenceReviewModel.getPerformrnceReviewId());
	// if (performenceReviewEntity.isPresent()) {
	// // if (StringUtil.isNotNull(performenceReviewModel.getModifiedBy()) &&
	// // performenceReviewModel
	// // .getModifiedBy().equals(performenceReviewEntity.get().getModifiedBy())) {
	// // throw new IllegalStateException("You dont have permission to perform this
	// // action");
	// // }
	// performenceReviewEntity.get().setStatus(performenceReviewModel.getStatus());
	// performenceReviewEntity.get().setModifiedBy(performenceReviewModel.getModifiedBy());
	// if (StringUtil.isNotNull(performenceReviewModel.getNewDesignation())) {
	// performenceReviewEntity.get().setNewDesignation(performenceReviewModel.getNewDesignation());
	// Optional<EmployeeEntity> employee = employeeRepository
	// .findById(performenceReviewEntity.get().getEmployeeBasicDetailId());
	// if (employee.isPresent()) {
	// employee.get().setDesignation(performenceReviewModel.getNewDesignation());
	// employeeRepository.save(employee.get());
	// }
	//
	// }
	// if (!performenceReviewEntity.get().getPerformanceDetails().isEmpty()) {
	// performenceReviewEntity.get().getPerformanceDetails().stream().forEach(d -> {
	// Optional<PerformanceDetailModel> performanceDetailModel =
	// performenceReviewModel
	// .getPerformanceDetails().stream()
	// .filter(a -> a.getPerformanceDetailsId() ==
	// d.getPerformanceDetailsId()).findFirst();
	// if (performanceDetailModel.isPresent()) {
	// d.setSelfRating(performanceDetailModel.get().getSelfRating());
	// d.setComments(performanceDetailModel.get().getComments());
	// d.setManagerRating(performanceDetailModel.get().getManagerRating());
	// d.setManagerComments(performanceDetailModel.get().getManagerComments());
	// d.setFinalRating(performanceDetailModel.get().getFinalRating());
	// d.setFinalComments(performanceDetailModel.get().getFinalComments());
	// d.setModifiedBy(performanceDetailModel.get().getModifiedBy()); // );
	// d.setStatus(performanceDetailModel.get().getStatus());
	// }
	// });
	// }
	// if (performenceReviewModel.getStatus().equals("113")) {
	// NotificationEntity notificationEntity = new NotificationEntity();
	// notificationEntity.setEmployeeId(performenceReviewEntity.get().getApproverId());
	// notificationEntity.setModule("Performence");
	// notificationEntity.setPath(Constants.APPRISAL_LIST_PATH);
	// notificationEntity.setMessage(performenceReviewEntity.get().getFullName()
	// + "'s apprisal is waiting for Manager Approval.");
	// notificationEntity.setStatus("115");
	// notificationRepository.save(notificationEntity);
	// }
	// performenceReviewRepository.save(performenceReviewEntity.get());
	// }
	// return "performenceReview is Updated";
	// } catch (Exception e) {
	// // TODO: handle exception
	// throw new IllegalStateException(e.getMessage());
	// }
	//
	// }

	@Transactional
	public String updateApprisalDetails(PerformenceReviewModel performenceReviewModel) {
		try {
			validateReviewId(performenceReviewModel.getPerformrnceReviewId());

			PerformenceReviewEntity performenceReviewEntity = performenceReviewRepository
					.findById(performenceReviewModel.getPerformrnceReviewId())
					.orElseThrow(() -> new IllegalArgumentException("Invalid PerformrnceReviewId"));

			// checkPermission(performenceReviewModel, performenceReviewEntity);

			updateReviewStatusAndModifiedBy(performenceReviewModel, performenceReviewEntity);
			updateEmployeeDesignationIfNeeded(performenceReviewModel, performenceReviewEntity);

			if (!performenceReviewEntity.getPerformanceDetails().isEmpty()) {
				updatePerformanceDetails(performenceReviewModel, performenceReviewEntity);
			}

			if (performenceReviewModel.getStatus().equals("113")) {
				createAndSaveNotification(performenceReviewEntity);
			}
			else if (performenceReviewModel.getStatus().equals("114")) {
				createAndSaveNotificationForHR(performenceReviewEntity);
			}

			performenceReviewRepository.save(performenceReviewEntity);

			return "performenceReview is Updated";
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

	private void validateReviewId(Long reviewId) {
		if (!IntegerUtils.isNotNull(reviewId)) {
			throw new IllegalArgumentException("PerformrnceReviewId is required");
		}
	}

	private void checkPermission(PerformenceReviewModel performenceReviewModel,
			PerformenceReviewEntity performenceReviewEntity) {
		if (StringUtil.isNotNull(performenceReviewModel.getModifiedBy())
				&& performenceReviewModel.getModifiedBy().equals(performenceReviewEntity.getModifiedBy())) {
			throw new IllegalStateException("You don't have permission to perform this action");
		}
	}

	private void updateReviewStatusAndModifiedBy(PerformenceReviewModel performenceReviewModel,
			PerformenceReviewEntity performenceReviewEntity) {
		performenceReviewEntity.setStatus(performenceReviewModel.getStatus());
		performenceReviewEntity.setModifiedBy(performenceReviewModel.getModifiedBy());
	}

	private void updateEmployeeDesignationIfNeeded(PerformenceReviewModel performenceReviewModel,
			PerformenceReviewEntity performenceReviewEntity) {
		if (StringUtil.isNotNull(performenceReviewModel.getNewDesignation())) {
			performenceReviewEntity.setNewDesignation(performenceReviewModel.getNewDesignation());

			employeeRepository.findById(performenceReviewEntity.getEmployeeBasicDetailId()).ifPresent(employee -> {
				employee.setDesignation(performenceReviewModel.getNewDesignation());
				employeeRepository.save(employee);
			});
		}
	}

	private void updatePerformanceDetails(PerformenceReviewModel performenceReviewModel,
			PerformenceReviewEntity performenceReviewEntity) {
		performenceReviewEntity.getPerformanceDetails().forEach(d -> {
			performenceReviewModel.getPerformanceDetails().stream()
					.filter(a -> a.getPerformanceDetailsId().equals(d.getPerformanceDetailsId())).findFirst()
					.ifPresent(performanceDetailModel -> {
						d.setSelfRating(performanceDetailModel.getSelfRating());
						d.setAchievedPercentage(performanceDetailModel.getAchievedPercentege());
						d.setComments(performanceDetailModel.getComments());
						d.setManagerRating(performanceDetailModel.getManagerRating());
						d.setManagerComments(performanceDetailModel.getManagerComments());
						d.setFinalRating(performanceDetailModel.getFinalRating());
						d.setFinalComments(performanceDetailModel.getFinalComments());
						d.setModifiedBy(performanceDetailModel.getModifiedBy());
						d.setStatus(performanceDetailModel.getStatus());
					});
		});
	}

	private void createAndSaveNotification(PerformenceReviewEntity performenceReviewEntity) {
		NotificationEntity notificationEntity = new NotificationEntity();
		notificationEntity.setEmployeeId(performenceReviewEntity.getApproverId());
		notificationEntity.setModule("Performance");
		notificationEntity.setPath(Constants.APPRISAL_LIST_PATH);
		notificationEntity
				.setMessage(performenceReviewEntity.getFullName() + "'s appraisal is waiting for Manager Approval.");
		notificationEntity.setStatus("115");
		notificationRepository.save(notificationEntity);
	}
	private void createAndSaveNotificationForHR(PerformenceReviewEntity performenceReviewEntity) {
		EmployeeEntity employeeEntity=employeeRepository.findByFullName("HR Admin");
		NotificationEntity notificationEntity = new NotificationEntity();
		notificationEntity.setEmployeeId(employeeEntity.getEmployeeBasicDetailId());
		notificationEntity.setModule("Performance");
		notificationEntity.setPath(Constants.APPRISAL_LIST_PATH);
		notificationEntity
				.setMessage(performenceReviewEntity.getFullName() + "'s appraisal is waiting for HR Approval.");
		notificationEntity.setStatus("115");
		notificationRepository.save(notificationEntity);
	}

	public Page<PerformenceReviewEntity> searchPerformanceReviews(PerformanceSearchModel searchModel) {
		try {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();

			// Query for fetching results
			CriteriaQuery<PerformenceReviewEntity> query = cb.createQuery(PerformenceReviewEntity.class);
			Root<PerformenceReviewEntity> root = query.from(PerformenceReviewEntity.class);

			// Create predicates for the main query
			List<Predicate> predicates = new ArrayList<>();
			if (IntegerUtils.isNotNull(searchModel.getEmployeeId())) {
				predicates.add(hasEmployeeId(searchModel.getEmployeeId(), cb, root));
			}
			if (IntegerUtils.isNotNull(searchModel.getManagerId())) {
				predicates.add(hasManagerId(searchModel.getManagerId(), cb, root));
			}
			if (searchModel.getStatus() != null && !searchModel.getStatus().isEmpty()) {
				predicates.add(hasStatus(searchModel.getStatus(), root));
			} else {
				predicates.add(cb.notEqual(root.get("status"), "102"));
			}

			// Combine all predicates for the main query
			query.where(predicates.toArray(new Predicate[0]));

			// Pagination setup
			int page = searchModel.getPage() != null ? searchModel.getPage() : 0;
			int size = searchModel.getSize() != null ? searchModel.getSize() : 10;
			PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "modifiedDate"));

			// Query for counting total results
			CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
			Root<PerformenceReviewEntity> countRoot = countQuery.from(PerformenceReviewEntity.class);

			// Create predicates for the count query
			List<Predicate> countPredicates = new ArrayList<>();
			if (IntegerUtils.isNotNull(searchModel.getEmployeeId())) {
				countPredicates.add(hasEmployeeId(searchModel.getEmployeeId(), cb, countRoot));
			}
			if (IntegerUtils.isNotNull(searchModel.getManagerId())) {
				countPredicates.add(hasManagerId(searchModel.getManagerId(), cb, countRoot));
			}

			if (searchModel.getStatus() != null && !searchModel.getStatus().isEmpty()) {
				countPredicates.add(hasStatus(searchModel.getStatus(), countRoot));
			} else {
				countPredicates.add(cb.notEqual(countRoot.get("status"), "102"));
			}

			// Combine all predicates for the count query
			countQuery.where(countPredicates.toArray(new Predicate[0]));
			countQuery.select(cb.count(countRoot));

			Long totalResults = entityManager.createQuery(countQuery).getSingleResult();

			// Execute main query with pagination
			List<PerformenceReviewEntity> resultList = entityManager.createQuery(query)
					.setFirstResult((int) pageRequest.getOffset()).setMaxResults(pageRequest.getPageSize())
					.getResultList();

			// Return results as a Page object
			return new PageImpl<>(resultList, pageRequest, totalResults);
		} catch (Exception e) {
			logger.error("Error while searching performance reviews for employee ID {} and manager ID {}: {}",
					searchModel.getEmployeeId(), searchModel.getManagerId(), e.getMessage());
			throw new RuntimeException(e.fillInStackTrace());
		}
	}

	private Predicate hasStatus(List<String> status, Root<PerformenceReviewEntity> root) {
		return root.get("status").in(status);
	}

	private Predicate hasEmployeeId(Long employeeId, CriteriaBuilder cb, Root<PerformenceReviewEntity> root) {
		return cb.equal(root.get("employeeBasicDetailId"), employeeId);
	}

	private Predicate hasManagerId(Long managerId, CriteriaBuilder cb, Root<PerformenceReviewEntity> root) {
		return cb.equal(root.get("approverId"), managerId);
	}

	public Map<String, Object> uploadAppraisalLetter(MultipartFile multipartFile,
			AppraisalUploadModel payrollUploadModel) {
		Map<String, Object> response = new HashMap<>();
		PerformenceReviewEntity performenceReviewEntity = performenceReviewRepository
				.findByEmployeeBasicDetailIdAndStatus(payrollUploadModel.getEmpBasicDetailId(),
						payrollUploadModel.getStatus());
		String filePath = null;
		logger.info("filePath {}", filePath);
		try {
			filePath = savePdfFile(multipartFile);
		} catch (IOException e) {
			logger.error("An IOException occurred: {}", e.getMessage(), e);
		}

		performenceReviewEntity.setOfferLetterPath(filePath);
		performenceReviewEntity.setModifiedBy(payrollUploadModel.getCreatedBy());
		performenceReviewEntity.setModifiedDate(LocalDateTime.now());
		performenceReviewEntity.setStatus(payrollUploadModel.getStatus());
		performenceReviewRepository.save(performenceReviewEntity);
		Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(payrollUploadModel.getEmpBasicDetailId());
		EmailModel emailModel = new EmailModel();
		if (employeeEntity.isPresent()) {
			emailModel.setToEmail(employeeEntity.get().getEmailId());
			emailModel.setSubject("Appraisal");
			emailModel.setAddressTo(employeeEntity.get().getFullName());
			emailService.sendEmailForAppraisal(emailModel, multipartFile);
		} else {
			logger.error("Employee not found with ID: {}", payrollUploadModel.getEmpBasicDetailId());
			throw new ResourceNotFoundException(
					"Employee not found with ID: " + payrollUploadModel.getEmpBasicDetailId());
		}
		response.put("response", "Appraisal Letter uploaded successfully");
		response.put("filePath", filePath);
		response.put("performenceReviewEntity", performenceReviewEntity);
		return response;
	}

	public String savePdfFile(MultipartFile file) throws IOException {

		String directoryPath = uploadDirectory + file.getOriginalFilename();
		// Create the directory if it doesn't exist
		logger.info("uploadDirectory {}", uploadDirectory);

		// Construct the full file path where the PDF will be saved
		String fileName = file.getOriginalFilename();
		// String filePath = Paths.get(directoryPath, fileName).toString();
		String filePath = directoryPath;

		// Save the file to the specified folder
		try (InputStream inputStream = file.getInputStream();
				FileOutputStream outputStream = new FileOutputStream(filePath)) {
			logger.info("outputStream {}", outputStream);
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
		}

		return filePath; // Return the full path of the stored file
	}

	@Transactional
	public String updateApprisalDetailsNew(PerformenceReviewModel performenceReviewModel, MultipartFile multipartFile) {
		try {
			validateReviewId(performenceReviewModel.getPerformrnceReviewId());

			PerformenceReviewEntity performenceReviewEntity = performenceReviewRepository
					.findById(performenceReviewModel.getPerformrnceReviewId())
					.orElseThrow(() -> new IllegalArgumentException("Invalid PerformrnceReviewId"));

			// checkPermission(performenceReviewModel, performenceReviewEntity);

			updateReviewStatusAndModifiedBy(performenceReviewModel, performenceReviewEntity);
			updateEmployeeDesignationIfNeeded(performenceReviewModel, performenceReviewEntity);

			if (!performenceReviewEntity.getPerformanceDetails().isEmpty()) {
				updatePerformanceDetails(performenceReviewModel, performenceReviewEntity);
			}

			if (performenceReviewModel.getStatus().equals("113")) {
				createAndSaveNotification(performenceReviewEntity);
			}
			AppraisalUploadModel appraisalUploadModel = new AppraisalUploadModel();
			appraisalUploadModel.setEmpBasicDetailId(performenceReviewModel.getEmplBasicId());
			appraisalUploadModel.setStatus("114");
			appraisalUploadModel.setCreatedBy(performenceReviewModel.getCreatedBy());
			uploadAppraisalLetter(multipartFile, appraisalUploadModel);
			performenceReviewRepository.save(performenceReviewEntity);

			return "performenceReview is Updated";
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

	@Transactional
	public String InitiateApprisalNew(ApprisalInitiateModel apprisalInitiateModel) {
		try {
			for (PerformenceReviewModel performenceReviewModel : apprisalInitiateModel.getPerformenceReviewModel()) {
				PerformenceReviewEntity performenceReviewEntity = new PerformenceReviewEntity();
				List<PerformenceReviewEntity> existingEntites = performenceReviewRepository
						.findByEmployeeBasicDetailId(performenceReviewModel.getEmplBasicId());
				if (existingEntites.stream().filter(
						d -> d.getStatus().equals("112") || d.getStatus().equals("113") || d.getStatus().equals("114"))
						.count() != 0) {
					throw new IllegalArgumentException(
							"Appraisal is already initiated for this member: " + existingEntites.get(0).getFullName());
				}
				performenceReviewEntity.setStatus("112");
				performenceReviewEntity.setEmployeeBasicDetailId(performenceReviewModel.getEmplBasicId());
				UserApproverConfigurationsEntity approverConfigurationsEntity = approverConfigRepository
						.findByEmpBasicDetailIdAndModuleName(performenceReviewModel.getEmplBasicId(), "performance");
				if (approverConfigurationsEntity == null) {
					throw new IllegalStateException("Please assign performance manager to all the selected Employees.");
				}
				performenceReviewEntity.setApproverId(approverConfigurationsEntity.getApproverId());
				performenceReviewEntity.setCreatedBy(apprisalInitiateModel.getUser());
				performenceReviewEntity.setCreatedDate(LocalDate.now());
				performenceReviewEntity.setModifiedBy(apprisalInitiateModel.getUser());
				performenceReviewEntity.setModifiedDate(LocalDateTime.now());
//				GroupSubgroupConfigEntity groupSubgroupConfigEntity = groupSubgroupConfigRepository
//						.findByGroupAndSubGroupAndStatus(apprisalInitiateModel.getGroup(),
//								apprisalInitiateModel.getSubGroup(), "108");
//				if (groupSubgroupConfigEntity == null) {
//					throw new IllegalStateException("Goals are not found for group: " + apprisalInitiateModel.getGroup()
//							+ " and subgroup: " + apprisalInitiateModel.getSubGroup());
//				}
//				performenceReviewEntity = performenceReviewRepository.save(performenceReviewEntity);
				List<PerformanceDetailsEntity> performanceDetails = new ArrayList<>();
//				for (GroupSubgroupGoalEntity groupSubgroupGoalEntity : groupSubgroupConfigEntity
//						.getGroupSubgroupGoalEntity()) {
				EmpGoalSetupEntity empGoalSetupEntity = goalSetUpRepository
						.findByEmpBasicDetailIdAndStatusNot(performenceReviewModel.getEmplBasicId(), "127");
				List<EmpGoalDetailsEntity> empGoalDetailsEntities = empGoalSetupEntity.getEmpGoalDetailsEntity();

				for (EmpGoalDetailsEntity empGoalDetailsEntity : empGoalDetailsEntities) {
					PerformanceDetailsEntity performanceDetailsEntity = new PerformanceDetailsEntity();
					performanceDetailsEntity.setPerformanceReviewId(performenceReviewEntity.getPerformarnceReviewId());
					performanceDetailsEntity.setPerformanceParameter(empGoalDetailsEntity.getGoal());
					performanceDetailsEntity.setPerformanceGroup(apprisalInitiateModel.getGroup());
					performanceDetailsEntity.setPerformenceSubGroup(apprisalInitiateModel.getSubGroup());
					performanceDetailsEntity.setEmployeeBasicDetais(performenceReviewModel.getEmplBasicId());
					performanceDetailsEntity.setStatus("112");
					performanceDetailsEntity.setCreatedDate(LocalDate.now());
					performanceDetailsEntity.setCreatedBy(apprisalInitiateModel.getUser());
					performanceDetailsEntity.setModifiedBy(apprisalInitiateModel.getUser());
					performanceDetailsEntity.setMofiedDate(LocalDateTime.now());
					performanceDetails.add(performanceDetailsEntity);
				}
				NotificationEntity notificationEntity = new NotificationEntity();
				notificationEntity.setEmployeeId(performenceReviewEntity.getEmployeeBasicDetailId());
				notificationEntity.setModule("Performance");
				notificationEntity.setPath(Constants.APPRISAL_PATH);
				notificationEntity.setMessage("New appraisal is available. Kindly submit with self rating.");
				notificationEntity.setStatus("115");
				notificationRepository.save(notificationEntity);
				performenceDetailsRepository.saveAll(performanceDetails);
			}
			return "Appraisal initiated successfully";
		} catch (Exception e) {
			logger.error("Error during appraisal initiation for group {} and subgroup {}: {}",
					apprisalInitiateModel.getGroup(), apprisalInitiateModel.getSubGroup(), e.getMessage());
			throw new ResourceNotFoundException("Failed to initiate appraisal: " + e.getMessage());
		}

	}

	public ResponseEntity<List<String>> getActiveAttributes() {
		try {
			List<String> result = webClient.get().uri("/pransquare/MasterConfiguration/attributes/getActiveAttributes")
					.retrieve().bodyToMono(new ParameterizedTypeReference<List<String>>() {
					}).block(); // Blocking for simplicity

			if (result != null && !result.isEmpty()) {
				return ResponseEntity.ok(result);
			} else {
				return ResponseEntity.status(404).body(List.of("No attributes found"));
			}

		} catch (WebClientResponseException e) {
			System.err.println("WebClient error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
			return ResponseEntity.status(e.getStatusCode()).body(List.of("WebClient error occurred"));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(List.of("Internal server error"));
		}
	}

	public String updateAttributeDetails(List<GoalAttributeModel> goalAttributeModels) {
		List<GoalAttributeEntity> updatedEntities = new ArrayList<>();

		for (GoalAttributeModel model : goalAttributeModels) {
			Optional<GoalAttributeEntity> optionalEntity = goalAttributeRepository
					.findById(model.getEmpGoalAttributeId());

			if (optionalEntity.isPresent()) {
				GoalAttributeEntity entity = optionalEntity.get();

				// Update fields
				entity.setEmployeeRating(model.getEmployeeRating());
				entity.setManagerRating(model.getManagerRating());
				entity.setFinalRating(model.getFinalRating());
				entity.setApprovedDate(model.getApprovedDate());
				entity.setSubmittedDate(model.getSubmittedDate());
				entity.setApproveComments(model.getApproveComments());
				entity.setEmployeeComments(model.getEmployeeComments());
				entity.setFinalComments(model.getFinalComments());
				entity.setModifiedBy(model.getModifiedBy());
				entity.setModifiedDate(LocalDateTime.now());
				entity.setStatus(model.getStatus());
				updatedEntities.add(entity);
			} else {
				// Optionally handle not found case
				throw new ResourceNotFoundException("Attribute not found for ID: " + model.getEmpGoalAttributeId());
			}
		}

		// Save all updated entities
		goalAttributeRepository.saveAll(updatedEntities);
		return "Attribute Details Updated Successfully";
	}

	public List<EmployeeEntity> employeeGroupAppraisal(String performanceGroup, String performanceSubGroup) {
	    // Step 1: Get employees by group and status
	    List<EmployeeEntity> employeesList = employeeRepository.findByGroupAndSubGroupAndStatusAndGenericProfile(
	    	    performanceGroup,
	    	    performanceSubGroup,
	    	    "108",
	    	    false
	    	);
logger.info("employeesList"+employeesList);
	    // Step 2: Set up the goal search model with specific statuses
	    List<String> statuses = List.of("112", "113", "114");
	    PerformanceSearchModel performanceSearchModel = new PerformanceSearchModel();
	    performanceSearchModel.setStatus(statuses);
	    performanceSearchModel.setSize(100);

	    // Step 3: Search goals
	    List<PerformenceReviewEntity> goals = searchPerformanceReviews(performanceSearchModel).toList();
	    logger.info("goals"+goals);
	    // Step 4: Extract employee IDs from goal results
	    Set<Long> goalEmpIds = goals.stream()
	            .map(PerformenceReviewEntity::getEmployeeBasicDetailId)
	            .collect(Collectors.toSet());

	    // Step 5: Filter employees whose basic detail ID is NOT in goalEmpIds
	    List<EmployeeEntity> filteredEmployees = employeesList.stream()
	            .filter(emp -> !goalEmpIds.contains(emp.getEmployeeBasicDetailId()))
	            .collect(Collectors.toList());

	    return filteredEmployees;
	}
	public List<EmployeeEntity> getEmployeesByPerformanceGroupNew(String performanceGroup, String performanceSubGroup) {
	    // Step 1: Get employees by group and status
	    List<EmployeeEntity> employeesList = employeeRepository.findByGroupAndSubGroupAndStatusAndGenericProfile(
	    	    performanceGroup,
	    	    performanceSubGroup,
	    	    "108",
	    	    false
	    	);
logger.info("employeesList"+employeesList);
	    // Step 2: Set up the goal search model with specific statuses
	    List<String> statuses = List.of("127", "128", "129", "130");
	    GoalSearchModel goalSearchModel = new GoalSearchModel();
	    goalSearchModel.setStatus(statuses);
	    goalSearchModel.setSize(100);

	    // Step 3: Search goals
	    List<EmpGoalSetupEntity> goals = goalSetUpService.searchGoals(goalSearchModel).toList();
	    logger.info("goals"+goals);
	    // Step 4: Extract employee IDs from goal results
	    Set<Long> goalEmpIds = goals.stream()
	            .map(EmpGoalSetupEntity::getEmpBasicDetailId)
	            .collect(Collectors.toSet());

	    // Step 5: Filter employees whose basic detail ID is NOT in goalEmpIds
	    List<EmployeeEntity> filteredEmployees = employeesList.stream()
	            .filter(emp -> !goalEmpIds.contains(emp.getEmployeeBasicDetailId()))
	            .collect(Collectors.toList());

	    return filteredEmployees;
	}


}
