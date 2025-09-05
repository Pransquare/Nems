package com.pransquare.nems.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pransquare.nems.entities.AttendanceEntity;
import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.entities.EmployeeLeaveDetailsConfigEntity;
import com.pransquare.nems.entities.EmployeeLeaveEntity;
import com.pransquare.nems.entities.EmployeeLeaveViewEntity;
import com.pransquare.nems.entities.NotificationEntity;
import com.pransquare.nems.entities.UserApproverConfigurationsEntity;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.models.EmployeeLeaveDetailsConfigModel;
import com.pransquare.nems.models.EmployeeLeaveModel;
import com.pransquare.nems.models.LeaveTypeEntity;
import com.pransquare.nems.repositories.ApproverConfigRepository;
import com.pransquare.nems.repositories.AttendanceRepository;
import com.pransquare.nems.repositories.EmployeeLeaveDetailsConfigRepository;
import com.pransquare.nems.repositories.EmployeeLeaveRepository;
import com.pransquare.nems.repositories.EmployeeRepository;
import com.pransquare.nems.repositories.NotificationRepository;
import com.pransquare.nems.utils.Constants;
import com.pransquare.nems.utils.IntegerUtils;
import com.pransquare.nems.utils.StringUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import reactor.core.publisher.Mono;

@Service
public class EmployeeLeaveService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeLeaveService.class);
	public static final String EMPLOYEE_ID_CANNOT_BE_NULL = "Employee ID cannot be null";
	public static final String EMPLOYEE_LEAVE_CONFIG_DATA_MISSING = "Employee leave config data is missing.";
	public static final String TIMESHEET_ALREADY_FILLED_PREFIX = "Time sheet is already filled for ";

	public static final String SCHEDULER = "Scheduler";

	private EmployeeLeaveRepository employeeLeaveRepository;

	private ApproverConfigRepository userApproverConfigurationRepository;

	private EmployeeLeaveDetailsConfigRepository leaveDetailsConfigRepository;

	@PersistenceContext
	private EntityManager entityManager;

	EmailService emailService;

	NotificationRepository notificationRepository;

	EmployeeRepository employeeRepository;

	AttendanceRepository attendanceRepository;

	EmployeeTimeSheetService employeeTimeSheetService;

	@Value("${slCredit}")
	private Float slCredit;

	@Value("${plCredit}")
	private Float plCredit;

	@Value("${master-config-service.url}")
	private String masterConfigUrl;

	public EmployeeLeaveService(EmployeeLeaveRepository employeeLeaveRepository,
			ApproverConfigRepository userApproverConfigurationRepository,
			EmployeeLeaveDetailsConfigRepository leaveDetailsConfigRepository, EmailService emailService,
			NotificationRepository notificationRepository, EmployeeRepository employeeRepository,
			AttendanceRepository attendanceRepository, EmployeeTimeSheetService employeeTimeSheetService) {
		this.employeeLeaveRepository = employeeLeaveRepository;
		this.userApproverConfigurationRepository = userApproverConfigurationRepository;
		this.leaveDetailsConfigRepository = leaveDetailsConfigRepository;
		this.emailService = emailService;
		this.notificationRepository = notificationRepository;
		this.employeeRepository = employeeRepository;
		this.attendanceRepository = attendanceRepository;
		this.employeeTimeSheetService = employeeTimeSheetService;
	}

	public List<EmployeeLeaveEntity> findEmployeeLeaveDetails(Integer employeeId) {
		if (employeeId == null) {
			throw new IllegalArgumentException(EMPLOYEE_ID_CANNOT_BE_NULL);
		}

		List<EmployeeLeaveEntity> employeeLeaveEntity = employeeLeaveRepository.findByEmployeeIdAndStatus(employeeId,
				"101");
		return employeeLeaveEntity;
	}

	@Transactional
	public EmployeeLeaveEntity createOrUpdateEmployeeLeaveEntity(EmployeeLeaveModel employeeLeaveModel) {
		try {
			if (employeeLeaveModel == null) {
				throw new IllegalArgumentException("Employee leave model cannot be null");
			}
			// Deleting existing records
			if (employeeLeaveModel.getEmployeeLeaveId() != null) {
				deleteEmployeeLeaveEntity(employeeLeaveModel.getEmployeeLeaveId());
			}
			if (isDuplicateLeave(employeeLeaveModel)) {
				throw new IllegalArgumentException("Leave has already been applied for this date.");
			}

			timesheetValidation(employeeLeaveModel);

			EmployeeLeaveEntity employeeLeaveEntity = new EmployeeLeaveEntity();
			NotificationEntity notificationEntity = new NotificationEntity();
			boolean isNewEntity = !IntegerUtils.isNotNull(employeeLeaveModel.getEmployeeLeaveId());

			if (isNewEntity) {
				initializeNewLeave(employeeLeaveModel, employeeLeaveEntity, notificationEntity);
			} else {
				employeeLeaveEntity = updateExistingLeave(employeeLeaveModel, notificationEntity);
			}

			EmployeeLeaveDetailsConfigEntity empLeaveDetailsConfigEntity = getLeaveConfigEntity(employeeLeaveModel);

			if (Boolean.FALSE.equals(empLeaveDetailsConfigEntity.getUnlimited())) {
				validateLeaveBalance(employeeLeaveModel, empLeaveDetailsConfigEntity);
				updateLeaveBalances(employeeLeaveModel, empLeaveDetailsConfigEntity);
			}

			populateLeaveEntityFields(employeeLeaveModel, employeeLeaveEntity);

			if ("101".equals(employeeLeaveEntity.getStatus())) {
				emailService.sendLeaveRequestMail(employeeLeaveModel);
			}

			prepareAndSendNotification(employeeLeaveEntity, notificationEntity);

			return employeeLeaveRepository.save(employeeLeaveEntity);

		} catch (Exception e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	private void initializeNewLeave(EmployeeLeaveModel employeeLeaveModel, EmployeeLeaveEntity employeeLeaveEntity,
			NotificationEntity notificationEntity) {
		employeeLeaveEntity.setEmployeeId(employeeLeaveModel.getEmployeeId());
		employeeLeaveEntity.setCreatedBy(employeeLeaveModel.getUser());
		employeeLeaveEntity.setCreatedDate(LocalDateTime.now());
		employeeLeaveEntity.setStatus("101");

		UserApproverConfigurationsEntity approverConfig = userApproverConfigurationRepository
				.findByEmpBasicDetailIdAndModuleName(employeeLeaveModel.getEmployeeId(), "leave");

		if (approverConfig != null) {
			employeeLeaveEntity.setApprover1(approverConfig.getApproverId());
			notificationEntity.setEmployeeId(approverConfig.getApproverId());
		} else {
			throw new ResourceNotFoundException("Please assign the leave manager");
		}
	}

	private EmployeeLeaveEntity updateExistingLeave(EmployeeLeaveModel employeeLeaveModel,
			NotificationEntity notificationEntity) {
		EmployeeLeaveEntity existingLeave = employeeLeaveRepository.findById(employeeLeaveModel.getEmployeeLeaveId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Employee Leave details are missing for id: " + employeeLeaveModel.getEmployeeLeaveId()));

		existingLeave.setModifiedBy(employeeLeaveModel.getUser());
		existingLeave.setModifiedDate(LocalDateTime.now());
		notificationEntity.setEmployeeId(existingLeave.getApprover1());

		if (employeeLeaveModel.getStatus() != null) {
			existingLeave.setStatus(employeeLeaveModel.getStatus());
		}

		return existingLeave;
	}

	private EmployeeLeaveDetailsConfigEntity getLeaveConfigEntity(EmployeeLeaveModel employeeLeaveModel) {
		return leaveDetailsConfigRepository
				.findByEmployeeBasicDetailsId(employeeLeaveModel.getEmployeeId())
				.stream()
				.filter(data -> data.getLeaveCode().equals(employeeLeaveModel.getLeaveType()))
				.findFirst()
				.orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_LEAVE_CONFIG_DATA_MISSING));
	}

	private void validateLeaveBalance(EmployeeLeaveModel employeeLeaveModel,
			EmployeeLeaveDetailsConfigEntity empLeaveDetailsConfigEntity) {
		if (employeeLeaveModel.getNoOfDays() > empLeaveDetailsConfigEntity.getRemaining()) {
			throw new ResourceNotFoundException("You don't have enough leave balance.");
		}
	}

	private void updateLeaveBalances(EmployeeLeaveModel employeeLeaveModel,
			EmployeeLeaveDetailsConfigEntity empLeaveDetailsConfigEntity) {
		empLeaveDetailsConfigEntity.setRemaining(
				empLeaveDetailsConfigEntity.getRemaining() - employeeLeaveModel.getNoOfDays());
		empLeaveDetailsConfigEntity.setPending(
				empLeaveDetailsConfigEntity.getPending() + employeeLeaveModel.getNoOfDays());
		leaveDetailsConfigRepository.save(empLeaveDetailsConfigEntity);
	}

	private void populateLeaveEntityFields(EmployeeLeaveModel employeeLeaveModel,
			EmployeeLeaveEntity employeeLeaveEntity) {
		employeeLeaveEntity.setLeaveFrom(employeeLeaveModel.getLeaveFrom());
		employeeLeaveEntity.setLeaveTo(employeeLeaveModel.getLeaveTo());
		employeeLeaveEntity.setLeaveType(employeeLeaveModel.getLeaveType());
		employeeLeaveEntity.setNoOfDays(employeeLeaveModel.getNoOfDays());
		employeeLeaveEntity.setReason(employeeLeaveModel.getReason());
		employeeLeaveEntity.setRemarks(employeeLeaveModel.getRemarks());
		employeeLeaveEntity.setFromLeaveType(employeeLeaveModel.getFromLeaveType());
		employeeLeaveEntity.setToLeaveType(employeeLeaveModel.getToLeaveType());
	}

	private void prepareAndSendNotification(EmployeeLeaveEntity employeeLeaveEntity,
			NotificationEntity notificationEntity) {
		Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(employeeLeaveEntity.getEmployeeId());

		if (employeeEntity.isPresent()) {
			notificationEntity
					.setMessage("You got a Leave Approval request from " + employeeEntity.get().getFullName());
		} else {
			throw new ResourceNotFoundException("Employee details are missing.");
		}

		notificationEntity.setStatus("115");
		notificationEntity.setModule("Leave");
		notificationEntity.setPath(Constants.LEAVE_PATH);
		notificationRepository.save(notificationEntity);
	}

	private void timesheetValidation(EmployeeLeaveModel leaveModel) {
		try {
			for (LocalDate date = leaveModel.getLeaveFrom(); !date.isAfter(leaveModel.getLeaveTo()); date = date
					.plusDays(1)) {
				List<AttendanceEntity> attendanceEntities = attendanceRepository
						.findByEmpBasicDetailIdAndTaskDateAndStatusNotIn(
								leaveModel.getEmployeeId(), date, Arrays.asList("109", "103"));

				int totalTimeInvested = attendanceEntities.stream()
						.mapToInt(attendanceEntity -> employeeTimeSheetService
								.convertTimeToMinutes(attendanceEntity.getTimeInvested()))
						.sum();

				if (totalTimeInvested == 0) {
					continue; // Skip if no time invested
				}

				boolean isFullDayLeave = (date.equals(leaveModel.getLeaveFrom())
						&& "full".equals(leaveModel.getFromLeaveType())) ||
						(date.equals(leaveModel.getLeaveTo()) && "full".equals(leaveModel.getToLeaveType()));

				if (totalTimeInvested > 6 * 60 || isFullDayLeave) {
					throw new IllegalArgumentException(TIMESHEET_ALREADY_FILLED_PREFIX + date);
				}
			}
		} catch (Exception e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	public EmployeeLeaveEntity deleteEmployeeLeaveEntity(Long employeeLeaveId) {
		try {
			Optional<EmployeeLeaveEntity> employeeLeaveEntity = employeeLeaveRepository.findById(employeeLeaveId);
			if (!employeeLeaveEntity.isPresent()) {
				throw new ResourceNotFoundException("Employee Leave details is missing for id: " + employeeLeaveId);
			}
			EmployeeLeaveEntity leaveEntity = employeeLeaveEntity.get();
			leaveEntity.setStatus("109");
			EmployeeLeaveDetailsConfigEntity empLeaveDetailsConfigEntity = leaveDetailsConfigRepository
					.findByEmployeeBasicDetailsId(leaveEntity.getEmployeeId()).stream()
					.filter(data -> data.getLeaveCode().equals(leaveEntity.getLeaveType())).findFirst()
					.orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_LEAVE_CONFIG_DATA_MISSING));
			if (Boolean.FALSE.equals(empLeaveDetailsConfigEntity.getUnlimited())) {
				empLeaveDetailsConfigEntity
						.setRemaining(empLeaveDetailsConfigEntity.getRemaining() + leaveEntity.getNoOfDays());
				empLeaveDetailsConfigEntity
						.setPending(empLeaveDetailsConfigEntity.getPending() - leaveEntity.getNoOfDays());
			}
			leaveDetailsConfigRepository.save(empLeaveDetailsConfigEntity);
			return employeeLeaveRepository.save(leaveEntity);
		} catch (Exception e) {
			throw new RuntimeException(e.fillInStackTrace());
		}
	}

	public List<EmployeeLeaveEntity> findEmployeeLeaveDetails(EmployeeLeaveModel employeeLeaveModel) {
		if (employeeLeaveModel.getEmployeeId() == null) {
			throw new IllegalArgumentException(EMPLOYEE_ID_CANNOT_BE_NULL);
		}

		List<EmployeeLeaveEntity> resultList = new ArrayList<>();

		try {
			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<EmployeeLeaveEntity> criteriaQuery = criteriaBuilder.createQuery(EmployeeLeaveEntity.class);
			Root<EmployeeLeaveEntity> root = criteriaQuery.from(EmployeeLeaveEntity.class);

			List<Predicate> predicateList = new ArrayList<>();

			predicateList.add(criteriaBuilder.equal(root.get("employeeId"), employeeLeaveModel.getEmployeeId()));

			if (employeeLeaveModel.getLeaveType() != null) {
				predicateList.add(criteriaBuilder.equal(root.get("leaveType"), employeeLeaveModel.getLeaveType()));
			}

			if (IntegerUtils.isNotNull(employeeLeaveModel.getNoOfDays())) {
				predicateList.add(criteriaBuilder.equal(root.get("noOfDays"), employeeLeaveModel.getNoOfDays()));
			}

			if (employeeLeaveModel.getStatus() != null) {
				predicateList.add(criteriaBuilder.equal(root.get("status"), employeeLeaveModel.getStatus()));
			}

			if (employeeLeaveModel.getYear() != null) {
				Year year = Year.of(Integer.parseInt(employeeLeaveModel.getYear()));
				LocalDate startOfYear = year.atDay(1);
				LocalDate endOfYear = year.atMonth(12).atEndOfMonth();

				Predicate leaveFromInRange = criteriaBuilder.between(root.get("leaveFrom"), startOfYear, endOfYear);
				Predicate leaveToInRange = criteriaBuilder.between(root.get("leaveTo"), startOfYear, endOfYear);
				Predicate leaveSpanYear = criteriaBuilder.and(
						criteriaBuilder.lessThanOrEqualTo(root.get("leaveFrom"), endOfYear),
						criteriaBuilder.greaterThanOrEqualTo(root.get("leaveTo"), startOfYear));

				predicateList.add(criteriaBuilder.or(leaveFromInRange, leaveToInRange, leaveSpanYear));
			}
			predicateList.add(criteriaBuilder.notEqual(root.get("status"), "109"));

			criteriaQuery.where(predicateList.toArray(new Predicate[0]));

			criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate")));
			resultList = entityManager.createQuery(criteriaQuery).getResultList();
		} catch (Exception e) {
			// You can throw a custom exception or handle the error as per your requirements
			throw new RuntimeException("Error occurred while finding employee leave details", e);
		}

		return resultList;
	}

	public List<EmployeeLeaveEntity> findEmployeeLeaveDetailsByApproverId(Long approverId) {
		try {
			if (approverId == null) {
				throw new IllegalArgumentException(EMPLOYEE_ID_CANNOT_BE_NULL);
			}
			return employeeLeaveRepository.findByApprover1AndStatus(approverId, "101");
		} catch (Exception e) {
			throw new RuntimeException("Error occurred while finding employee leave details by approverId", e);
		}
	}

	@Transactional
	public EmployeeLeaveEntity changeEmployeeLeaveStatus(long employeeLeaveId, String status,
			EmployeeLeaveModel employeeLeave) {
		try {

			if (!IntegerUtils.isNotNull(employeeLeaveId) && status == null) {
				throw new IllegalArgumentException("EmployeeLeaveId, status must be not null");
			}
			EmployeeLeaveEntity employeeLeaveEntity = employeeLeaveRepository.findById(employeeLeaveId)
					.orElseThrow(() -> new ResourceNotFoundException("EmployeeLeave Data missing"));
			employeeLeaveEntity.setStatus(status);
			if (employeeLeave != null && StringUtil.isNotNull(employeeLeave.getRemarks())) {
				employeeLeaveEntity.setRemarks(employeeLeave.getRemarks());
			}
			EmployeeLeaveDetailsConfigEntity empLeaveDetailsConfigEntity = leaveDetailsConfigRepository
					.findByEmployeeBasicDetailsId(employeeLeaveEntity.getEmployeeId()).stream()
					.filter(data -> data.getLeaveCode().equals(employeeLeaveEntity.getLeaveType())).findFirst()
					.orElseThrow(() -> new ResourceNotFoundException(EMPLOYEE_LEAVE_CONFIG_DATA_MISSING));
			NotificationEntity notificationEntity = new NotificationEntity();
			notificationEntity.setEmployeeId(employeeLeaveEntity.getEmployeeId());
			notificationEntity.setModule("Leave");
			notificationEntity.setPath(Constants.LEAVE_PATH);
			notificationEntity.setStatus("115");
			if (status.equals("102")) {
				if (Boolean.FALSE.equals(empLeaveDetailsConfigEntity.getUnlimited())) {
					empLeaveDetailsConfigEntity
							.setUsed(empLeaveDetailsConfigEntity.getUsed() + employeeLeaveEntity.getNoOfDays());
					empLeaveDetailsConfigEntity
							.setPending(empLeaveDetailsConfigEntity.getPending() - employeeLeaveEntity.getNoOfDays());
				}
				notificationEntity.setMessage("Your leave request is approved");
			} else if (status.equals("103")) {
				if (Boolean.FALSE.equals(empLeaveDetailsConfigEntity.getUnlimited())) {
					empLeaveDetailsConfigEntity.setRemaining(
							empLeaveDetailsConfigEntity.getRemaining() + employeeLeaveEntity.getNoOfDays());
					empLeaveDetailsConfigEntity
							.setPending(empLeaveDetailsConfigEntity.getPending() - employeeLeaveEntity.getNoOfDays());
				}
				notificationEntity.setMessage("Your leave request is rejected");
			}
			leaveDetailsConfigRepository.save(empLeaveDetailsConfigEntity);
			notificationRepository.save(notificationEntity);
			return employeeLeaveRepository.save(employeeLeaveEntity);
		} catch (Exception e) {
			throw new IllegalStateException(e.fillInStackTrace());
		}
	}

	public List<EmployeeLeaveDetailsConfigEntity> getEmployeeLeaveConfigDetails(
			EmployeeLeaveDetailsConfigModel detailsConfigModel) {
		try {
			if (!IntegerUtils.isNotNull(detailsConfigModel.getEmployeeId())) {
				throw new IllegalArgumentException("EmployeeId cannot be null");
			}

			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<EmployeeLeaveDetailsConfigEntity> criteriaQuery = criteriaBuilder
					.createQuery(EmployeeLeaveDetailsConfigEntity.class);
			Root<EmployeeLeaveDetailsConfigEntity> root = criteriaQuery.from(EmployeeLeaveDetailsConfigEntity.class);

			List<Predicate> predicateList = new ArrayList<>();
			predicateList
					.add(criteriaBuilder.equal(root.get("employeeBasicDetailsId"), detailsConfigModel.getEmployeeId()));
			if (detailsConfigModel.getLeaveCode() != null) {
				predicateList.add(criteriaBuilder.equal(root.get("leaveCode"), detailsConfigModel.getLeaveCode()));
			}
			if (StringUtil.isNotNull(detailsConfigModel.getValidFrom())
					&& StringUtil.isNotNull(detailsConfigModel.getValidTo())) {
				Predicate validFromPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("validFrom"),
						detailsConfigModel.getValidFrom());
				Predicate validToPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("validTo"),
						detailsConfigModel.getValidTo());
				predicateList.add(criteriaBuilder.and(validFromPredicate, validToPredicate));
			} else {
				LocalDate currentYearStart = LocalDate.now().withDayOfYear(1);
				LocalDate currentYearEnd = LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear());
				Predicate validFromPredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("validFrom"),
						currentYearStart);
				Predicate validToPredicate = criteriaBuilder.lessThanOrEqualTo(root.get("validTo"), currentYearEnd);
				predicateList.add(criteriaBuilder.and(validFromPredicate, validToPredicate));
			}
			criteriaQuery.where(predicateList.toArray(new Predicate[0]));
			return entityManager.createQuery(criteriaQuery).getResultList();
		} catch (Exception e) {
			throw new IllegalStateException(e.fillInStackTrace());
		}
	}

	public boolean isDuplicateLeave(EmployeeLeaveModel employeeLeaveModel) {
		try {
			// Validate input parameters
			if (employeeLeaveModel.getEmployeeId() == null
					|| employeeLeaveModel.getLeaveFrom() == null
					|| employeeLeaveModel.getLeaveTo() == null
					|| employeeLeaveModel.getFromLeaveType() == null
					|| employeeLeaveModel.getToLeaveType() == null) {
				throw new IllegalArgumentException("Invalid input parameters");
			}

			// Fetch overlapping leaves
			List<EmployeeLeaveEntity> overlappingLeaves = employeeLeaveRepository.findOverlappingLeaves(
					employeeLeaveModel.getEmployeeId(), employeeLeaveModel.getLeaveFrom(),
					employeeLeaveModel.getLeaveTo());
			// Check for duplicates
			for (EmployeeLeaveEntity existingLeave : overlappingLeaves) {
				if (isSameDateOverlap(employeeLeaveModel, existingLeave)
						|| isDifferentDateOverlap(employeeLeaveModel, existingLeave)) {
					return true;
				}
			}

			return false;

		} catch (Exception e) {
			throw new IllegalStateException(e.fillInStackTrace());
		}
	}

	private boolean isSameDateOverlap(EmployeeLeaveModel newLeave, EmployeeLeaveEntity existingLeave) {
		// Full day overlaps with any part of the day
		if (newLeave.getFromLeaveType().equalsIgnoreCase("full")
				|| existingLeave.getFromLeaveType().equalsIgnoreCase("full")) {
			return true;
		}

		// Check for first-half and second-half overlaps
		return newLeave.getFromLeaveType().equalsIgnoreCase(existingLeave.getFromLeaveType())
				&& newLeave.getToLeaveType().equalsIgnoreCase(existingLeave.getToLeaveType());
	}

	private boolean isDifferentDateOverlap(EmployeeLeaveModel newLeave, EmployeeLeaveEntity existingLeave) {
		// Check for full-full, full-first, second-full, second-first overlaps
		if (newLeave.getLeaveFrom().isEqual(existingLeave.getLeaveTo())
				&& newLeave.getFromLeaveType().equalsIgnoreCase("second")
				&& existingLeave.getToLeaveType().equalsIgnoreCase("first")) {
			return false; // no overlap
		}

		if (existingLeave.getLeaveFrom().isEqual(newLeave.getLeaveTo())
				&& existingLeave.getFromLeaveType().equalsIgnoreCase("second")
				&& newLeave.getToLeaveType().equalsIgnoreCase("first")) {
			return false; // no overlap
		}

		// Any other case is an overlap
		return true;
	}

	public List<EmployeeLeaveEntity> getEmployeesByLeave(LocalDate leave) {
		return employeeLeaveRepository.findByLeaveFromGreaterThanEqualAndLeaveToIsLessThanEqual(leave, leave);
	}

	// @Scheduled(cron = "${leaveCredit}") // Runs every minute
	// @SchedulerLock(name = "leaveCredit", lockAtMostFor = "5m", lockAtLeastFor =
	// "1m")
	public void performTask() {
		logger.info("leaveCredit cron started");

		List<EmployeeEntity> employeeEntities = employeeRepository.findByStatus("108");
		if (employeeEntities.isEmpty()) {
			return;
		}

		for (EmployeeEntity employee : employeeEntities) {
			List<EmployeeLeaveDetailsConfigEntity> leaveDetails = leaveDetailsConfigRepository
					.findByEmployeeBasicDetailsId(employee.getEmployeeBasicDetailId());

			for (EmployeeLeaveDetailsConfigEntity leave : leaveDetails) {
				if (isSupportedLeaveCode(leave.getLeaveCode())) {
					if (LocalDate.now().isAfter(leave.getValidTo())) {
						updateLeaveValidity(leave);
					}
					creditLeave(leave);
					leave.setModifiedBy(SCHEDULER);
					leave.setModifiedDate(LocalDate.now());
					leaveDetailsConfigRepository.save(leave);
				}
			}
		}

		logger.info("leaveCredit cron ended successfully");
	}

	private boolean isSupportedLeaveCode(String leaveCode) {
		return "SL".equals(leaveCode) || "PL".equals(leaveCode);
	}

	private void updateLeaveValidity(EmployeeLeaveDetailsConfigEntity leave) {
		leave.setValidFrom(leave.getValidFrom().plusYears(1));
		leave.setValidTo(leave.getValidTo().plusYears(1));
	}

	private void creditLeave(EmployeeLeaveDetailsConfigEntity leave) {
		Float credit = "SL".equals(leave.getLeaveCode()) ? slCredit : plCredit;
		leave.setCredited(leave.getCredited() + credit);
		leave.setRemaining(leave.getRemaining() + credit);
	}

	// public List<LeaveTypeEntity> performTask2() {
	// logger.info("Task execution started at517: {}", LocalDateTime.now());
	// List<LeaveTypeEntity> leaveTypeList = new ArrayList<>();
	// try {
	// // Define the URL for the GET request
	// URL url = new URL(
	// "http://103.77.26.221:8080/MasterConfiguration-0.0.1-SNAPSHOT/PRANSQUARE/MasterConfiguration/leaveType/getAllLeaveTypes");
	// // Replace
	// // //
	// // actual
	// // server
	// logger.info("Task execution started at523: {}", LocalDateTime.now()); // URL
	// // Open a connection
	// HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	//
	// // Set the request method to GET
	// connection.setRequestMethod("GET");
	//
	// // Optional: Set any headers if needed
	// connection.setRequestProperty("Accept", "application/json");
	//
	// // Get the response code
	// int responseCode = connection.getResponseCode();
	// logger.error("Response Code: {}", responseCode);
	//
	// if (responseCode == 200) {
	// // Read the response
	// BufferedReader in = new BufferedReader(new
	// InputStreamReader(connection.getInputStream()));
	// String inputLine;
	// StringBuilder response = new StringBuilder();
	//
	// while ((inputLine = in.readLine()) != null) {
	// response.append(inputLine);
	// }
	// in.close();
	//
	// // Print the raw JSON response
	// logger.info("Response: {}", response);
	//
	// // Convert the JSON response to a list of LeaveTypeEntity objects
	// Gson gson = new Gson();
	// Type listType = new TypeToken<List<LeaveTypeEntity>>() {
	// }.getType();
	// leaveTypeList = gson.fromJson(response.toString(), listType);
	// logger.info("leaveTypeList {}", leaveTypeList);
	// // Print the LeaveTypeEntity objects
	// for (LeaveTypeEntity leaveType : leaveTypeList) {
	// logger.info("leaveType {}", leaveType);
	// }
	// } else {
	// logger.error("Failed to fetch data. HTTP response code: {}", responseCode);
	// }
	//
	// } catch (Exception e) {
	// logger.error("An error occurred while performing the task: ", e);
	//
	// }
	// return leaveTypeList;
	// }

	public List<LeaveTypeEntity> performTask2() {
		logger.info("Task execution started at517: {}", LocalDateTime.now());
		List<LeaveTypeEntity> leaveTypeList = new ArrayList<>();

		try {
			String url = masterConfigUrl + "/pransquare/MasterConfiguration/leaveType/getAllLeaveTypes";

			WebClient webClient = WebClient.builder()
					.baseUrl(url)
					.defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
					.build();

			// Make the GET request
			Mono<String> responseMono = webClient.get()
					.retrieve()
					.bodyToMono(String.class);

			// Block to get the response (optional: add timeout)
			String jsonResponse = responseMono.block();
			logger.info("Response JSON: {}", jsonResponse);

			// Parse JSON using Gson
			Gson gson = new Gson();
			Type listType = new TypeToken<List<LeaveTypeEntity>>() {
			}.getType();
			leaveTypeList = gson.fromJson(jsonResponse, listType);

			logger.info("Parsed leaveTypeList: {}", leaveTypeList);

		} catch (Exception e) {
			logger.error("An error occurred while performing the task: ", e);
		}

		return leaveTypeList;
	}

	@Scheduled(cron = "${leaveCredit}") // Runs every minute
	@SchedulerLock(name = "leaveCredit", lockAtMostFor = "5m", lockAtLeastFor = "1m")
	public void leaveCredit() {

		logger.info("leaveCredit cron started");

		List<LeaveTypeEntity> activeLeaveTypes = performTask2().stream()
				.filter(l -> l.getStatus().equals("active"))
				.toList();

		List<EmployeeEntity> employeeEntities = employeeRepository.findByStatus("108");
		if (employeeEntities.isEmpty() || activeLeaveTypes.isEmpty()) {
			return;
		}

		for (EmployeeEntity employee : employeeEntities) {
			List<EmployeeLeaveDetailsConfigEntity> leaveDetails = leaveDetailsConfigRepository
					.findByEmployeeBasicDetailsId(employee.getEmployeeBasicDetailId());

			if (!leaveDetails.isEmpty()) {
				processEmployeeLeave(leaveDetails, activeLeaveTypes);
			}
		}

		logger.info("leaveCredit cron ended successfully");
	}

	private void processEmployeeLeave(List<EmployeeLeaveDetailsConfigEntity> leaveDetails,
			List<LeaveTypeEntity> activeLeaveTypes) {
		for (EmployeeLeaveDetailsConfigEntity leave : leaveDetails) {
			activeLeaveTypes.stream()
					.filter(leaveType -> leave.getLeaveCode().equals(leaveType.getLeaveTypeCode()))
					.forEach(leaveType -> updateLeaveDetails(leave, leaveType));
		}

	}

	private void updateLeaveDetails(EmployeeLeaveDetailsConfigEntity leave, LeaveTypeEntity leaveType) {
		if (LocalDate.now().isAfter(leave.getValidTo())) {
			leave.setValidFrom(leave.getValidFrom().plusYears(1));
			leave.setValidTo(leave.getValidTo().plusYears(1));
		}

		leave.setCredited(leave.getCredited() + leaveType.getLeaveCredit());
		leave.setRemaining(leave.getRemaining() + leaveType.getLeaveCredit());
		leave.setModifiedBy(SCHEDULER);
		leave.setModifiedDate(LocalDate.now());

		leaveDetailsConfigRepository.save(leave);
	}

	public byte[] generateLeaveReport(EmployeeLeaveModel leaveModel) throws IOException {
		StringBuilder jpql = new StringBuilder(
				"SELECT e FROM EmployeeLeaveViewEntity e WHERE e.leaveFrom <= :toDate AND e.leaveTo >= :fromDate");

		if (leaveModel.getEmployeeCode() != null && !leaveModel.getEmployeeCode().isEmpty()) {
			jpql.append(" AND e.employeeCode = :employeeCode");
		}

		if (leaveModel.getApprover1() != null) {
			jpql.append(" AND e.approverId = :approverId");
		}

		TypedQuery<EmployeeLeaveViewEntity> query = entityManager.createQuery(jpql.toString(),
				EmployeeLeaveViewEntity.class);
		query.setParameter("fromDate", leaveModel.getLeaveFrom());
		query.setParameter("toDate", leaveModel.getLeaveTo());

		if (leaveModel.getEmployeeCode() != null && !leaveModel.getEmployeeCode().isEmpty()) {
			query.setParameter("employeeCode", leaveModel.getEmployeeCode());
		}

		if (leaveModel.getApprover1() != null) {
			query.setParameter("approverId", leaveModel.getApprover1());
		}

		List<EmployeeLeaveViewEntity> leaveRecords = query.getResultList();

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Leave Report");

		Row headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("Employee Name");
		headerRow.createCell(1).setCellValue("Designation");
		headerRow.createCell(2).setCellValue("Leave Type");
		headerRow.createCell(3).setCellValue("Leave From");
		headerRow.createCell(4).setCellValue("Leave To");
		headerRow.createCell(5).setCellValue("No. of Days");
		headerRow.createCell(6).setCellValue("Status");

		int rowNum = 1;
		for (EmployeeLeaveViewEntity leave : leaveRecords) {
			Row row = sheet.createRow(rowNum++);
			row.createCell(0).setCellValue(leave.getFullName());
			row.createCell(1).setCellValue(leave.getDesignation());
			row.createCell(2).setCellValue(leave.getLeaveType());
			row.createCell(3).setCellValue(leave.getLeaveFrom().toString());
			row.createCell(4).setCellValue(leave.getLeaveTo().toString());
			row.createCell(5).setCellValue(leave.getNoOfDays());
			row.createCell(6).setCellValue(leave.getDescription());
		}

		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			workbook.write(byteArrayOutputStream);
			return byteArrayOutputStream.toByteArray();
		} finally {
			workbook.close();
		}
	}

	public Page<EmployeeLeaveEntity> findEmployeeLeaveDetailsForGrid(EmployeeLeaveModel employeeLeaveModel) {

		List<EmployeeLeaveEntity> resultList = new ArrayList<>();
		long totalCount = 0;

		try {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();

			// Build the base predicates for filtering
			CriteriaQuery<EmployeeLeaveEntity> cq = cb.createQuery(EmployeeLeaveEntity.class);
			Root<EmployeeLeaveEntity> root = cq.from(EmployeeLeaveEntity.class);

			List<Predicate> predicates = buildPredicates(employeeLeaveModel, cb, root);

			// Apply filters and sorting
			cq.where(predicates.toArray(new Predicate[0]));
			cq.orderBy(cb.desc(root.get("createdDate")));

			// Fetch paginated result
			resultList = entityManager.createQuery(cq)
					.setFirstResult(employeeLeaveModel.getPage() * employeeLeaveModel.getSize())
					.setMaxResults(employeeLeaveModel.getSize())
					.getResultList();

			// Build count query
			CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
			Root<EmployeeLeaveEntity> countRoot = countQuery.from(EmployeeLeaveEntity.class);
			countQuery.select(cb.count(countRoot));
			countQuery.where(buildPredicates(employeeLeaveModel, cb, countRoot).toArray(new Predicate[0]));

			totalCount = entityManager.createQuery(countQuery).getSingleResult();

		} catch (Exception e) {
			throw new RuntimeException("Error occurred while finding employee leave details", e);
		}

		return new PageImpl<>(resultList, PageRequest.of(employeeLeaveModel.getPage(), employeeLeaveModel.getSize()),
				totalCount);
	}

	private List<Predicate> buildPredicates(EmployeeLeaveModel model, CriteriaBuilder cb,
			Root<EmployeeLeaveEntity> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (model.getApprover1() != null) {
			predicates.add(cb.equal(root.get("approver1"), model.getApprover1()));
		}
		if (model.getLeaveType() != null) {
			predicates.add(cb.equal(root.get("leaveType"), model.getLeaveType()));
		}
		if (model.getEmployeeId() != null) {
			predicates.add(cb.equal(root.get("employeeId"), model.getEmployeeId()));
		}
		if (IntegerUtils.isNotNull(model.getNoOfDays())) {
			predicates.add(cb.equal(root.get("noOfDays"), model.getNoOfDays()));
		}
		if (model.getStatus() != null) {
			predicates.add(cb.equal(root.get("status"), model.getStatus()));
		}
		if (model.getYear() != null) {
			Year year = Year.of(Integer.parseInt(model.getYear()));
			LocalDate startOfYear = year.atDay(1);
			LocalDate endOfYear = year.atMonth(12).atEndOfMonth();

			Predicate leaveFromInRange = cb.between(root.get("leaveFrom"), startOfYear, endOfYear);
			Predicate leaveToInRange = cb.between(root.get("leaveTo"), startOfYear, endOfYear);
			Predicate leaveSpansYear = cb.and(
					cb.lessThanOrEqualTo(root.get("leaveFrom"), endOfYear),
					cb.greaterThanOrEqualTo(root.get("leaveTo"), startOfYear));

			predicates.add(cb.or(leaveFromInRange, leaveToInRange, leaveSpansYear));
		}

		predicates.add(cb.notEqual(root.get("status"), "109"));

		return predicates;
	}
}