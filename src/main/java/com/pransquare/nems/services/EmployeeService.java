
package com.pransquare.nems.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.pransquare.nems.entities.EmployeeAddressEntity;
import com.pransquare.nems.entities.EmployeeBankDetailsEntity;
import com.pransquare.nems.entities.EmployeeCtcEntity;
import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.entities.EmployeeLeaveDetailsConfigEntity;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.models.EmployeeAddressModel;
import com.pransquare.nems.models.EmployeeBankDetailsModel;
import com.pransquare.nems.models.EmployeeModel;
import com.pransquare.nems.models.EmployeeSearchModel;
import com.pransquare.nems.models.EmployeeUpdateEmailAndRolesModel;
import com.pransquare.nems.models.LoginRequestDTO;
import com.pransquare.nems.models.UserInputDTO;
import com.pransquare.nems.repositories.ApproverConfigRepository;
import com.pransquare.nems.repositories.EmpDocumentDetailsRepository;
import com.pransquare.nems.repositories.EmployeeAddressRepository;
import com.pransquare.nems.repositories.EmployeeBankDetailsRepository;
import com.pransquare.nems.repositories.EmployeeCtcRepository;
import com.pransquare.nems.repositories.EmployeeLeaveDetailsConfigRepository;
import com.pransquare.nems.repositories.EmployeeRepository;
import com.pransquare.nems.repositories.EmployeeWorkLocationRepository;
import com.pransquare.nems.repositories.NotificationRepository;
import com.pransquare.nems.repositories.EmployeeRepository.EmployeeBirthdayProjection;
import com.pransquare.nems.repositories.EmployeeRepository.EmployeeForGoal;
import com.pransquare.nems.repositories.EmployeeRepository.EmployeeNameCode;
import com.pransquare.nems.utils.IntegerUtils;
import com.pransquare.nems.utils.StringUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
public class EmployeeService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
	public static final String STATUS_DELETED = "deleted";
	public static final String EMPLOYEE_ID_MESSAGE_PREFIX = "Employee with ID ";
	public static final String NOT_FOUND_SUFFIX = " not found";
	public static final String ERROR_CREATING_OR_UPDATING_EMPLOYEE = "Error creating or updating employee";
	public static final String ERROR_CREATING_USER_CREDENTIALS = "Error creating user credentials";
	public static final String LEAVE_TYPE_UNLIMITED = "unlimited";
	public static final String MODULE_APPROVAL_JOBMANAGER = "jobManager";

	private EmployeeRepository employeeRepository;

	private EmployeeAddressRepository employeeAddressRepository;

	private EmployeeBankDetailsRepository employeeBankDetailsRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private EmployeeLeaveDetailsConfigRepository employeeLeaveDetailsConfigRepository;

	private WebClient.Builder webClientBuilder;

	NotificationRepository notificationRepository;

	EmployeeWorkLocationRepository employeeWorkLocationRepository;

	EmpDocumentDetailsRepository empDocumentDetailsRepository;

	EmployeeCtcRepository employeeCtcRepository;

	ApproverConfigRepository approverConfigRepository;
	private EmailService emailService;

	@Value("${user.management.url}")
	private String userMgmtUrl;
	@Value("${master-config-service.url}")
	private String masterConfigUrl;

	private RestTemplate restTemplate;

	public EmployeeService(EmployeeRepository employeeRepository, EmployeeAddressRepository employeeAddressRepository,
			EmployeeBankDetailsRepository employeeBankDetailsRepository,
			EmployeeLeaveDetailsConfigRepository employeeLeaveDetailsConfigRepository, Builder webClientBuilder,
			NotificationRepository notificationRepository,
			EmployeeWorkLocationRepository employeeWorkLocationRepository,

			EmpDocumentDetailsRepository empDocumentDetailsRepository, EmployeeCtcRepository employeeCtcRepository,
			ApproverConfigRepository approverConfigRepository, EmailService emailService, RestTemplate restTemplate) {

		this.employeeRepository = employeeRepository;
		this.employeeAddressRepository = employeeAddressRepository;
		this.employeeBankDetailsRepository = employeeBankDetailsRepository;
		this.employeeLeaveDetailsConfigRepository = employeeLeaveDetailsConfigRepository;
		this.webClientBuilder = webClientBuilder;
		this.notificationRepository = notificationRepository;
		this.employeeWorkLocationRepository = employeeWorkLocationRepository;
		this.empDocumentDetailsRepository = empDocumentDetailsRepository;
		this.employeeCtcRepository = employeeCtcRepository;
		this.approverConfigRepository = approverConfigRepository;
		this.emailService = emailService;
		this.restTemplate = restTemplate;
	}

	public Page<EmployeeEntity> getAllEmployees(EmployeeSearchModel employeeSearchModel) {
		try {
			logger.info("Retrieving all employees");
			Pageable page = PageRequest.of(employeeSearchModel.getPage(), employeeSearchModel.getSize());
			return employeeRepository.findAllByStatusNot(STATUS_DELETED, page);
		} catch (Exception e) {
			throw new RuntimeException("Error retrieving all employees", e);
		}
	}

	public Page<EmployeeEntity> getAllEmployeesByStatus(EmployeeSearchModel employeeSearchModel) {
		try {
			Specification<EmployeeEntity> spec = Specification.where(null);

			if (employeeSearchModel.getStatus() != null) {
				spec = spec.and(hasStatus(employeeSearchModel.getStatus()));
			}

			if (employeeSearchModel.getEmployeeCode() != null) {
				spec = spec.and(hasEmployeeCode(employeeSearchModel.getEmployeeCode()));
			}

			Pageable pageable = PageRequest.of(employeeSearchModel.getPage(), employeeSearchModel.getSize());

			return employeeRepository.findAll(spec, pageable);
		} catch (IllegalArgumentException e) {
			throw new ResourceNotFoundException(e.fillInStackTrace());
		}
	}

	public EmployeeEntity dedupeCheckWithEmployeeCode(String employeeCode) {
		try {
			logger.info("Checking for duplicate employee code: {}", employeeCode);
			return employeeRepository.findByEmployeeCodeAndStatusNot(employeeCode, STATUS_DELETED);
		} catch (Exception e) {
			throw new RuntimeException("Error checking for duplicate employee code", e);
		}
	}

	@Transactional
	public EmployeeEntity createOrUpdateEmployee(EmployeeModel employeeModel) {
		try {
			EmployeeEntity employeeEntity;
			if (!IntegerUtils.isNotNull(employeeModel.getEmployeeId())) {
				logger.info("Creating new employee");
				if (!employeeRepository.findByPersonalEmail(employeeModel.getPersonalEmail()).isEmpty()) {
					logger.error("Employee with email already exists");
					throw new ResourceNotFoundException("Employee with email already exists");
				}
				employeeEntity = new EmployeeEntity();
				employeeEntity.setStatus("110");
				employeeEntity.setCreatedBy(employeeModel.getUser());
				employeeEntity.setCreatedDate(LocalDateTime.now());
				employeeEntity.setEmployeeCode(
						(employeeModel.getWorkType().equals("permanent") ? "" : "C") + sequence());
			} else {
				Optional<EmployeeEntity> existingEntity = employeeRepository.findById(employeeModel.getEmployeeId());
				employeeEntity = existingEntity.orElseThrow(() -> new IllegalArgumentException(
						EMPLOYEE_ID_MESSAGE_PREFIX + employeeModel.getEmployeeId() + NOT_FOUND_SUFFIX));
				logger.info("Updating employee with ID: {}", employeeModel.getEmployeeId());
				employeeEntity.setModifiedBy(employeeModel.getUser());
				employeeEntity.setModifiedDate(LocalDateTime.now());
			}

			employeeEntity.setFirstName(employeeModel.getFirstName());
			employeeEntity.setMiddleName(employeeModel.getMiddleName());
			employeeEntity.setLastName(employeeModel.getLastName());
			employeeEntity.setFullName(
					Stream.of(employeeModel.getFirstName(), employeeModel.getMiddleName(), employeeModel.getLastName())
							.filter(StringUtil::isNotNull) // Method reference instead of lambda
							.reduce((name1, name2) -> name1 + " " + name2)
							.orElse(""));
			employeeEntity.setGender(employeeModel.getGender());
			employeeEntity.setDob(employeeModel.getDob());
			employeeEntity.setDesignation(employeeModel.getDesignation());
			employeeEntity.setMaritalStatus(employeeModel.getMaritalStatus());
			employeeEntity.setMobileNo(employeeModel.getMobileNo());
			employeeEntity.setAlternateNumber(employeeModel.getAlternateNumber());
			employeeEntity.setEmergencyNo(employeeModel.getEmergencyNo());
			employeeEntity.setEmailId(employeeModel.getEmailId());
			employeeEntity.setPersonalEmail(employeeModel.getPersonalEmail());
			employeeEntity.setBloodGroup(employeeModel.getBloodGroup());
			employeeEntity.setPanNo(employeeModel.getPanNo());
			employeeEntity.setAdharNo(employeeModel.getAdharNo());
			employeeEntity.setUanNo(employeeModel.getUanNo());
			employeeEntity.setDateOfJoining(employeeModel.getDateOfJoining());
			employeeEntity.setLastWorkingDay(employeeModel.getLastWorkingDay());
			employeeEntity.setGroup(employeeModel.getGroup());
			employeeEntity.setSubGroup(employeeModel.getSubGroup());
			employeeEntity.setNationality(employeeModel.getNationality());
			employeeEntity.setWorkType(employeeModel.getWorkType());
			employeeEntity.setDocumentType(employeeModel.getDocumentType());
			employeeEntity.setDocumentNumber(employeeModel.getDocumentNumber());
			employeeEntity.setJiraId(employeeModel.getJiraId());
			employeeEntity.setIsTaxdeclarationEnabled(0);
			employeeEntity.setIsProofdeclarationEnabled(0);
			// Save the employee entity
			employeeEntity = employeeRepository.save(employeeEntity);

			employeeEntity = updateAddressandBankDetails(employeeEntity, employeeModel);
			if (employeeModel.getEmployeeWorkLocation() != null) {
				employeeModel.getEmployeeWorkLocation().setEmployeeId(employeeEntity.getEmployeeBasicDetailId());
				employeeModel.getEmployeeWorkLocation().setStatus("108");
				employeeWorkLocationRepository.save(employeeModel.getEmployeeWorkLocation());
			}
			// if (employeeModel.getEmpDocumentDetails() != null &&
			// !employeeModel.getEmpDocumentDetails().isEmpty()) {
			// List<EmpDocumentDetailsEntity> updatedDetails = new ArrayList<>();
			// for (EmpDocumentDetailsEntity d : employeeModel.getEmpDocumentDetails()) {
			// d.setEmployeeId(employeeEntity.getEmployeeBasicDetailId());
			// updatedDetails.add(d);
			// }
			// employeeModel.setEmpDocumentDetails(updatedDetails);
			// empDocumentDetailsRepository.saveAll(updatedDetails);
			// }
			if (IntegerUtils.isNotNull(employeeModel.getCtc())) {
				EmployeeCtcEntity ctcEntity = new EmployeeCtcEntity();
				ctcEntity.setEmployeeId(employeeEntity.getEmployeeBasicDetailId());
				ctcEntity.setApprovedBy(employeeModel.getUser());
				ctcEntity.setApprovedDate(LocalDateTime.now());
				ctcEntity.setCtc(employeeModel.getCtc());
				ctcEntity.setStatus("108");
				employeeCtcRepository.save(ctcEntity);
			}
			return employeeEntity;
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException(e.fillInStackTrace());
		} catch (Exception e) {
			throw new RuntimeException(ERROR_CREATING_OR_UPDATING_EMPLOYEE, e);
		}
	}

	public EmployeeEntity deleteEmployee(Long employeeId) {
		try {
			if (!IntegerUtils.isNotNull(employeeId)) {
				throw new IllegalArgumentException("Employee ID cannot be null");
			}
			EmployeeEntity employeeEntity = employeeRepository.findById(employeeId)
					.orElseThrow(() -> new IllegalArgumentException(
							EMPLOYEE_ID_MESSAGE_PREFIX + employeeId + NOT_FOUND_SUFFIX));
			logger.info("Deleting employee with ID: {}", employeeId);
			employeeEntity.setStatus(STATUS_DELETED);
			return employeeRepository.save(employeeEntity);
		} catch (Exception e) {
			throw new RuntimeException("Error deleting employee", e);
		}
	}

	@Transactional
	public EmployeeEntity updateEmployee(EmployeeModel employeeModel) {
		try {
			EmployeeEntity employeeEntity;
			Optional<EmployeeEntity> existingEntity = employeeRepository.findById(employeeModel.getEmployeeId());
			employeeEntity = existingEntity.orElseThrow(() -> new IllegalArgumentException(
					EMPLOYEE_ID_MESSAGE_PREFIX + employeeModel.getEmployeeId() + NOT_FOUND_SUFFIX));
			logger.info("Updating employee with ID: {}", employeeModel.getEmployeeId());
			employeeEntity.setModifiedBy(employeeModel.getUser());
			employeeEntity.setModifiedDate(LocalDateTime.now());

			employeeEntity.setMobileNo(employeeModel.getMobileNo());
			employeeEntity.setBloodGroup(employeeModel.getBloodGroup());
			employeeEntity = employeeRepository.save(employeeEntity);

			employeeEntity = updateAddressandBankDetails(employeeEntity, employeeModel);

			return employeeEntity;
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException(e.fillInStackTrace());
		} catch (Exception e) {
			throw new RuntimeException(ERROR_CREATING_OR_UPDATING_EMPLOYEE, e);
		}
	}

	// public EmployeeEntity updateAddressandBankDetails(EmployeeEntity
	// employeeEntity, EmployeeModel employeeModel) {
	// try {
	// List<EmployeeAddressModel> addressModel = employeeModel.getEmployeeAddress();
	// if (addressModel != null) {
	// for (EmployeeAddressModel employeeAddressModel : addressModel) {
	// EmployeeAddressEntity employeeAddressEntity;
	// if (employeeAddressModel.getEmpAddressDetailId() == null) {
	// employeeAddressEntity = new EmployeeAddressEntity();
	// employeeAddressEntity.setCreatedDate(LocalDateTime.now());
	// employeeAddressEntity.setCreatedBy(employeeAddressModel.getCreatedBy());
	// employeeAddressEntity.setEmployeeId(employeeEntity.getEmployeeBasicDetailId());
	// } else {
	// employeeAddressEntity = employeeAddressRepository
	// .findById(employeeAddressModel.getEmpAddressDetailId()).orElseThrow(() -> {
	// String errorMessage = "Employee Address details are missing for id: "
	// + employeeAddressModel.getEmpAddressDetailId();
	// logger.error(errorMessage);
	// return new ResourceNotFoundException(errorMessage);
	// });
	// employeeAddressEntity.setModifiedDate(LocalDateTime.now());
	// employeeAddressEntity.setModifiedBy(employeeAddressModel.getModifiedBy());
	// }
	// employeeAddressEntity.setAddressType(employeeAddressModel.getAddressType());
	// employeeAddressEntity.setAddressLine1(employeeAddressModel.getAddressLine1());
	// employeeAddressEntity.setAddressLine2(employeeAddressModel.getAddressLine2());
	// employeeAddressEntity.setAddressLine3(employeeAddressModel.getAddressLine3());
	// employeeAddressEntity.setDistrict(employeeAddressModel.getDistrict());
	// employeeAddressEntity.setPinCode(employeeAddressModel.getPinCode());
	// employeeAddressEntity.setState(employeeAddressModel.getState());
	// employeeAddressEntity.setCountry(employeeAddressModel.getCountry());
	//
	// // Save the address entity
	// employeeAddressRepository.save(employeeAddressEntity);
	// }
	// } else {
	// logger.warn("EmployeeAddressModel is null, skipping address processing.");
	// }
	//
	// // Employee bank details processing
	// EmployeeBankDetailsModel employeeBankDetailsModel =
	// employeeModel.getEmployeeBankDetails();
	// if (employeeBankDetailsModel != null) {
	// EmployeeBankDetailsEntity employeeBankDetailsEntity;
	// if (employeeBankDetailsModel.getEmpBankDetailId() == null) {
	// employeeBankDetailsEntity = new EmployeeBankDetailsEntity();
	// employeeBankDetailsEntity.setCreatedDate(LocalDateTime.now());
	// employeeBankDetailsEntity.setCreatedBy(employeeBankDetailsModel.getModifiedBy());
	// employeeBankDetailsEntity.setEmployeeId(employeeEntity.getEmployeeBasicDetailId());
	// } else {
	// employeeBankDetailsEntity = employeeBankDetailsRepository
	// .findByEmpBankDetailId(employeeBankDetailsModel.getEmpBankDetailId());
	// if (employeeBankDetailsEntity == null) {
	//
	// String errorMessage = "Employee Bank details are missing for id: "
	// + employeeBankDetailsModel.getEmpBankDetailId();
	// logger.error(errorMessage);
	// throw new ResourceNotFoundException(errorMessage);
	//
	// }
	// employeeBankDetailsEntity.setModifiedDate(LocalDateTime.now());
	// employeeBankDetailsEntity.setModifiedBy(employeeBankDetailsModel.getModifiedBy());
	// }
	// employeeBankDetailsEntity.setAccountType(employeeBankDetailsModel.getAccountType());
	// employeeBankDetailsEntity.setBankAccountNo(employeeBankDetailsModel.getBankAccountNo());
	// employeeBankDetailsEntity.setBankIfsc(employeeBankDetailsModel.getBankIfsc());
	// employeeBankDetailsEntity.setBankName(employeeBankDetailsModel.getBankName());
	// employeeBankDetailsEntity.setBranchName(employeeBankDetailsModel.getBranchName());
	//
	// // Save the bank details entity
	// employeeBankDetailsRepository.save(employeeBankDetailsEntity);
	// } else {
	// logger.warn("EmployeeBankDetailsModel is null, skipping bank details
	// processing.");
	// }
	//
	// return employeeEntity;
	// } catch (ResourceNotFoundException e) {
	// logger.info(e.fillInStackTrace().toString());
	// throw new ResourceNotFoundException(e.fillInStackTrace());
	// } catch (Exception e) {
	// logger.error(ERROR_CREATING_OR_UPDATING_EMPLOYEE, e);
	// throw new RuntimeException(ERROR_CREATING_OR_UPDATING_EMPLOYEE, e);
	// }
	// }

	public EmployeeEntity updateAddressandBankDetails(EmployeeEntity employeeEntity, EmployeeModel employeeModel) {
		try {
			processEmployeeAddresses(employeeEntity, employeeModel.getEmployeeAddress());
			processEmployeeBankDetails(employeeEntity, employeeModel.getEmployeeBankDetails());
			return employeeEntity;
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException(e.fillInStackTrace());
		} catch (Exception e) {
			throw new RuntimeException(ERROR_CREATING_OR_UPDATING_EMPLOYEE, e);
		}
	}

	private void processEmployeeAddresses(EmployeeEntity employeeEntity, List<EmployeeAddressModel> addressModels) {
		if (addressModels != null) {
			for (EmployeeAddressModel addressModel : addressModels) {
				EmployeeAddressEntity addressEntity = updateOrCreateAddressEntity(employeeEntity, addressModel);
				populateAddressDetails(addressEntity, addressModel);
				employeeAddressRepository.save(addressEntity);
			}
		} else {
			logger.warn("EmployeeAddressModel is null, skipping address processing.");
		}
	}

	private EmployeeAddressEntity updateOrCreateAddressEntity(EmployeeEntity employeeEntity,
			EmployeeAddressModel addressModel) {
		EmployeeAddressEntity addressEntity;

		if (addressModel.getEmpAddressDetailId() == null) {
			addressEntity = new EmployeeAddressEntity();
			addressEntity.setCreatedDate(LocalDateTime.now());
			addressEntity.setCreatedBy(addressModel.getCreatedBy());
			addressEntity.setEmployeeId(employeeEntity.getEmployeeBasicDetailId());
		} else {
			addressEntity = employeeAddressRepository.findById(addressModel.getEmpAddressDetailId())
					.orElseThrow(() -> {
						String errorMessage = "Employee Address details are missing for id: "
								+ addressModel.getEmpAddressDetailId();
						logger.error(errorMessage);
						return new ResourceNotFoundException(errorMessage);
					});
			addressEntity.setModifiedDate(LocalDateTime.now());
			addressEntity.setModifiedBy(addressModel.getModifiedBy());
		}

		return addressEntity;
	}

	private void populateAddressDetails(EmployeeAddressEntity addressEntity, EmployeeAddressModel addressModel) {
		addressEntity.setAddressType(addressModel.getAddressType());
		addressEntity.setAddressLine1(addressModel.getAddressLine1());
		addressEntity.setAddressLine2(addressModel.getAddressLine2());
		addressEntity.setAddressLine3(addressModel.getAddressLine3());
		addressEntity.setDistrict(addressModel.getDistrict());
		addressEntity.setPinCode(addressModel.getPinCode());
		addressEntity.setState(addressModel.getState());
		addressEntity.setCountry(addressModel.getCountry());
	}

	private void processEmployeeBankDetails(EmployeeEntity employeeEntity, EmployeeBankDetailsModel bankDetailsModel) {
		if (bankDetailsModel != null) {
			EmployeeBankDetailsEntity bankDetailsEntity = updateOrCreateBankDetailsEntity(employeeEntity,
					bankDetailsModel);
			populateBankDetails(bankDetailsEntity, bankDetailsModel);
			employeeBankDetailsRepository.save(bankDetailsEntity);
		} else {
			logger.warn("EmployeeBankDetailsModel is null, skipping bank details processing.");
		}
	}

	private EmployeeBankDetailsEntity updateOrCreateBankDetailsEntity(EmployeeEntity employeeEntity,
			EmployeeBankDetailsModel bankDetailsModel) {
		EmployeeBankDetailsEntity bankDetailsEntity;

		if (bankDetailsModel.getEmpBankDetailId() == null) {
			bankDetailsEntity = new EmployeeBankDetailsEntity();
			bankDetailsEntity.setCreatedDate(LocalDateTime.now());
			bankDetailsEntity.setCreatedBy(bankDetailsModel.getModifiedBy());
			bankDetailsEntity.setEmployeeId(employeeEntity.getEmployeeBasicDetailId());
		} else {
			bankDetailsEntity = employeeBankDetailsRepository
					.findByEmpBankDetailId(bankDetailsModel.getEmpBankDetailId());
			if (bankDetailsEntity == null) {
				String errorMessage = "Employee Bank details are missing for id: "
						+ bankDetailsModel.getEmpBankDetailId();
				logger.error(errorMessage);
				throw new ResourceNotFoundException(errorMessage);
			}
			bankDetailsEntity.setModifiedDate(LocalDateTime.now());
			bankDetailsEntity.setModifiedBy(bankDetailsModel.getModifiedBy());
		}

		return bankDetailsEntity;
	}

	private void populateBankDetails(EmployeeBankDetailsEntity bankDetailsEntity,
			EmployeeBankDetailsModel bankDetailsModel) {
		bankDetailsEntity.setAccountType(bankDetailsModel.getAccountType());
		bankDetailsEntity.setBankAccountNo(bankDetailsModel.getBankAccountNo());
		bankDetailsEntity.setBankIfsc(bankDetailsModel.getBankIfsc());
		bankDetailsEntity.setBankName(bankDetailsModel.getBankName());
		bankDetailsEntity.setBranchName(bankDetailsModel.getBranchName());
		bankDetailsEntity.setBankAddress(bankDetailsModel.getBankAddress());
	}

	public Page<EmployeeEntity> getAllEmployeesForMailCreation(EmployeeSearchModel searchModel) {
		try {
			Specification<EmployeeEntity> spec = Specification.where(null);

			if (searchModel.getEmployeeCode() != null) {
				spec = spec.and(hasEmployeeCode(searchModel.getEmployeeCode()));
			}
			spec = spec.and(hasStatus("110"));
			Pageable pageable = PageRequest.of(searchModel.getPage(), searchModel.getSize());

			return employeeRepository.findAll(spec, pageable);
		} catch (IllegalArgumentException e) {
			throw new ResourceNotFoundException(e.fillInStackTrace());
		}
	}

	public static Specification<EmployeeEntity> hasStatus(String status) {
		return (Root<EmployeeEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
				.equal(root.get("status"), status);
	}

	public static Specification<EmployeeEntity> hasEmployeeCode(String code) {
		return (Root<EmployeeEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
				.equal(root.get("employeeCode"), code);
	}

	public ResponseEntity<String> updateEmployeeRolesAndEmail(EmployeeUpdateEmailAndRolesModel model) {
		try {
			if (model.getEmployeeCode() != null) {
				EmployeeEntity existingEntity = employeeRepository.findByEmployeeCode(model.getEmployeeCode());
				if (existingEntity != null) {
					existingEntity.setEmailId(model.getEmail());

					// createUser(existingEntity, model);

					createCandidateLeaveConfigEntity(existingEntity);
					existingEntity.setStatus("111");
					employeeRepository.save(existingEntity);

					return new ResponseEntity<>("Roles updated successfully", HttpStatus.OK);
				}
			}
			logger.warn("Employee code is missing in the request");
			return new ResponseEntity<>("Employee code is required", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			throw new ResourceNotFoundException(
					"Failed to update roles and email for employee: " + model.getEmployeeCode(), e);
		}

	}

	public synchronized void createUser(EmployeeEntity employee, EmployeeUpdateEmailAndRolesModel model) {

		logger.info("Before rest call for user creation");
		try {

			String url = userMgmtUrl + "/register";

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "application/json");
			headers.set("Authorization", model.getToken());

			String firstName = employee.getFirstName() != null ? employee.getFirstName() : "";
			String middleName = employee.getMiddleName() != null ? " " + employee.getMiddleName() : "";
			String lastName = employee.getLastName() != null ? " " + employee.getLastName() : "";

			String name = firstName + middleName + lastName;

			UserInputDTO userInputDTO = new UserInputDTO();
			userInputDTO.setName(name);
			userInputDTO.setEmail(employee.getEmailId());
			userInputDTO.setPassword(PasswordGenerator.generateRandomPassword());
			userInputDTO.setCreatedBy(model.getCreatedBy());
			userInputDTO.setEmpCode(employee.getEmployeeCode());
			userInputDTO.setRoles(model.getRoles());

			logger.info("user input dto ----  " + userInputDTO.toString());

			HttpEntity<UserInputDTO> requestEntity = new HttpEntity<>(userInputDTO, headers);

			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				logger.info("User created successfully.");
				emailService.sendEmailToEmployee(employee.getEmailId(), employee.getFullName(),
						userInputDTO.getPassword(), employee.getEmployeeCode());
			} else {
				throw new ResourceNotFoundException(ERROR_CREATING_USER_CREDENTIALS);
			}
		} catch (RestClientException e) {
			logger.error("Error in user creation", e.fillInStackTrace());
			throw new ResourceNotFoundException(e.fillInStackTrace());
		} catch (Exception e) {
			logger.error("Error in user creation", e.fillInStackTrace());
			throw new RuntimeException(ERROR_CREATING_USER_CREDENTIALS, e);
		}
	}

	public List<EmployeeEntity> getEmployeesByStatus(String status) {
		return employeeRepository.findByStatus(status);
	}

	public String sequence() {
		// Begin transaction
		// Create stored procedure query
		StoredProcedureQuery query = entityManager.createStoredProcedureQuery("CreateSequence")
				.registerStoredProcedureParameter("p_sequence", String.class, ParameterMode.OUT);

		// Execute stored procedure
		query.execute();
		return (String) query.getOutputParameterValue("p_sequence");

	}

	public List<EmployeeNameCode> getEmployeesByFirstNameStartingWithOrLastNameStartingWith(String input) {
		try {
			logger.info("Fetching employee details by first name starting with or last name starting with: {}", input);
			return employeeRepository.findByFullNameStartingWithIgnoreCase(input);
		} catch (Exception e) {
			throw new ResourceNotFoundException("Error fetching employee details by first name and last name",
					e.fillInStackTrace());
		}
	}

	private synchronized List<EmployeeLeaveDetailsConfigEntity> createCandidateLeaveConfigEntity(

			EmployeeEntity employee) {
		try {
			if (employee == null) {
				throw new IllegalArgumentException("Employee object is null");
			}

			List<EmployeeLeaveDetailsConfigEntity> configEntities = employeeLeaveDetailsConfigRepository
					.findByEmployeeBasicDetailsId(employee.getEmployeeBasicDetailId());
			 if (configEntities != null && !configEntities.isEmpty()) {
         
				return configEntities;
			} else {
				configEntities = new ArrayList<>();
			}

			// Static response replacing the RestTemplate call
			List<Map<String, Object>> leaveTypes = List.of(
					Map.ofEntries(
							Map.entry("leaveTypeId", 5),
							Map.entry("leaveTypeCode", "001"),
							Map.entry("leaveTypeDescription", "Sick Leave"),
							Map.entry("status", "active"),
							Map.entry("createdBy", "mani"),
							Map.entry("createdDate", "2024-10-04"),

							Map.entry("creditFrequency", "monthly"),
							Map.entry("leaveCredit", 0.5),
							Map.entry("unlimited", false)),
					Map.ofEntries(
							Map.entry("leaveTypeId", 6),
							Map.entry("leaveTypeCode", "002"),
							Map.entry("leaveTypeDescription", "Paid Leave"),
							Map.entry("status", "active"),
							Map.entry("createdBy", "mani"),
							Map.entry("createdDate", "2024-10-04"),

							Map.entry("creditFrequency", "monthly"),
							Map.entry("leaveCredit", 1.0),
							Map.entry("unlimited", false)));

			for (Map<String, Object> leaveType : leaveTypes) {
				EmployeeLeaveDetailsConfigEntity configEntity = new EmployeeLeaveDetailsConfigEntity();
				configEntity.setEmployeeBasicDetailsId(employee.getEmployeeBasicDetailId());

				// Null-safe leaveCode assignment
				Object leaveCodeObj = leaveType.get("leaveTypeCode");
				configEntity.setLeaveCode(leaveCodeObj != null ? leaveCodeObj.toString() : "UNKNOWN");

				// Null-safe unlimited field
				boolean isUnlimited = Boolean.TRUE.equals(leaveType.get("unlimited"));
				configEntity.setUnlimited(isUnlimited);
				configEntity.setOpening(isUnlimited ? 0.0f : 1.0f);
				configEntity.setRemaining(isUnlimited ? 0.0f : 1.0f);

				configEntity.setPending(0.0f);
				configEntity.setUsed(0.0f);
				configEntity.setCredited(0.0f);
				configEntity.setCreatedBy("system");
				configEntity.setCreatedDate(LocalDate.now());
				configEntity.setValidFrom(LocalDate.now().withDayOfYear(1));
				configEntity.setValidTo(LocalDate.now().withDayOfYear(LocalDate.now().lengthOfYear()));

				configEntities.add(configEntity);
			}

			return employeeLeaveDetailsConfigRepository.saveAll(configEntities);
		} catch (Exception e) {
			throw new RuntimeException("Error creating leave configuration", e);
		}
	}

	public String offboardMember(Long employeeId, LocalDate lastWorkingDate, HttpServletRequest request) {
		try {
			// Fetch employee by ID
			EmployeeEntity employee = employeeRepository.findById(employeeId)
					.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
			// Extract token from the current request's header
			String authToken = request.getHeader("Authorization");

			if (authToken == null || authToken.isEmpty()) {
				throw new IllegalStateException("Authorization token is missing");
			}

			// Prepare the payload for the external API call

			String offboardUrl = userMgmtUrl + "/offboardEmployee";

			LoginRequestDTO requestDTO = new LoginRequestDTO();
			requestDTO.setUsername(employee.getEmailId());
			// Call the external API to offboard the employee
			Mono<String> response = webClientBuilder.build().post().uri(offboardUrl)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // Set Content-Type
					.header(HttpHeaders.AUTHORIZATION, authToken).bodyValue(requestDTO).retrieve()
					.bodyToMono(String.class);

			// Block to wait for the API response (optional, for simplicity)
			String apiResponse = response.block(); // This will block until the API response is received

			// Check the response from the external API
			if (!"Offboard successfull".equals(apiResponse)) {
				throw new IllegalStateException("Failed to offboard employee via external API");
			}

			// If API call succeeds, update employee status
			employee.setStatus("117");
			employee.setLastWorkingDay(lastWorkingDate);
			employeeRepository.save(employee);

			return EMPLOYEE_ID_MESSAGE_PREFIX + employeeId + " has been offboarded successfully";
		} catch (Exception e) {
			throw new IllegalStateException(e.fillInStackTrace());
		}
	}

	public Double getEmployeeCtc(Long employeeId) {
		try {
			EmployeeCtcEntity employeeCtcEntity = employeeCtcRepository.findByEmployeeIdAndStatus(employeeId, "108");
			if (employeeCtcEntity == null) {
				throw new ResourceNotFoundException("CTC Details is missing");
			}
			return employeeCtcEntity.getCtc();
		} catch (Exception e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
	}

	public List<EmployeeBirthdayProjection> getBirthdayList() {
		return employeeRepository.findBirthdaysWithinNextWeek(LocalDate.now(), LocalDate.now().plusDays(7));

	}

	public List<Long> getReportingIds(Long managerId) {
		return approverConfigRepository.findByApproverIdAndModuleName(managerId, MODULE_APPROVAL_JOBMANAGER).stream()
				.map(approveConfig -> approveConfig.getEmpBasicDetailId()).collect(Collectors.toList());
	}

	public long getActiveEmployeeCount() {
		return employeeRepository.countEmployeesByStatus("108"); // Fetch count for status 108 (Active)
	}

	// Method to get the count of inactive employees
	public long getInactiveEmployeeCount() {
		return employeeRepository.countEmployeesByStatus("117"); // Fetch count for status 117 (Inactive)
	}

	public List<EmployeeForGoal> searchEmployeeForGoal(String input) {
			try {
				return employeeRepository.searchEmployeeForGoal(input);
			} catch (Exception e) {
				throw new ResourceNotFoundException("Error fetching employee details",
						e.fillInStackTrace());
			}
		}
}

