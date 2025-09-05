package com.pransquare.nems.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pransquare.nems.batch.TimesheetReport;
import com.pransquare.nems.dto.TimesheetReportRequestDto;
import com.pransquare.nems.entities.AttendanceEntity;
import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.entities.EmployeeLeaveEntity;
import com.pransquare.nems.entities.EmployeeProjectConfigEntity;
import com.pransquare.nems.entities.NotificationEntity;
import com.pransquare.nems.entities.TimesheetDatesApprovalEntity;
import com.pransquare.nems.entities.TimesheetMasterEntity;
import com.pransquare.nems.entities.UserApproverConfigurationsEntity;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.models.AttendanceApprovalModel;
import com.pransquare.nems.models.AttendanceDetailsSearchModel;
import com.pransquare.nems.models.AttendanceIdModel;
import com.pransquare.nems.models.EmailModel;
import com.pransquare.nems.models.ProjectAndClientDTO;
import com.pransquare.nems.models.SaveOrUpdateAttendanceModel;
import com.pransquare.nems.models.TimesheetReportModel;
import com.pransquare.nems.models.WeekendApprovalModel;
import com.pransquare.nems.repositories.ApproverConfigRepository;
import com.pransquare.nems.repositories.AttendanceRepository;
import com.pransquare.nems.repositories.EmployeeLeaveRepository;
import com.pransquare.nems.repositories.EmployeeProjectConfigRepository;
import com.pransquare.nems.repositories.EmployeeRepository;
import com.pransquare.nems.repositories.NotificationRepository;
import com.pransquare.nems.repositories.TimesheetDatesApprovalRepository;
import com.pransquare.nems.repositories.TimesheetMasterRepository;
import com.pransquare.nems.utils.Constants;
import com.pransquare.nems.utils.IntegerUtils;
import com.pransquare.nems.utils.StringUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class EmployeeTimeSheetService {

	private static final Logger logger = LogManager.getLogger(EmployeeTimeSheetService.class);
	public static final String RESPONSE_KEY = "response";

	private final AttendanceRepository attendanceRepository;

	private final EntityManager entityManager;

	private final TimesheetMasterRepository timesheetMasterRepository;

	private final ApproverConfigRepository approverConfigRepository;

	private final EmployeeLeaveRepository employeeLeaveRepository;

	private final EmailService emailService;

	private final ApproverConfigRepository userApproverConfigurationRepository;

	private final EmployeeRepository employeeRepository;

	private final NotificationRepository notificationRepository;

	private final EmployeeProjectConfigRepository employeeProjectConfigRepository;

	private final TimesheetReport timesheetReport;

	private final TimesheetDatesApprovalRepository timesheetDatesApprovalRepository;

	public EmployeeTimeSheetService(AttendanceRepository attendanceRepository, EntityManager entityManager,
			TimesheetMasterRepository timesheetMasterRepository, ApproverConfigRepository approverConfigRepository,
			EmployeeLeaveRepository employeeLeaveRepository, EmailService emailService,
			ApproverConfigRepository userApproverConfigurationRepository, EmployeeRepository employeeRepository,
			NotificationRepository notificationRepository,
			EmployeeProjectConfigRepository employeeProjectConfigRepository, TimesheetReport timesheetReport,
			TimesheetDatesApprovalRepository timesheetDatesApprovalRepository) {
		this.attendanceRepository = attendanceRepository;
		this.entityManager = entityManager;
		this.timesheetMasterRepository = timesheetMasterRepository;
		this.approverConfigRepository = approverConfigRepository;
		this.employeeLeaveRepository = employeeLeaveRepository;
		this.emailService = emailService;
		this.userApproverConfigurationRepository = userApproverConfigurationRepository;
		this.employeeRepository = employeeRepository;
		this.notificationRepository = notificationRepository;
		this.employeeProjectConfigRepository = employeeProjectConfigRepository;
		this.timesheetReport = timesheetReport;
		this.timesheetDatesApprovalRepository = timesheetDatesApprovalRepository;
	}

	public Map<String, Object> saveOrUpdateAttendance(SaveOrUpdateAttendanceModel saveOrUpdateAttendanceModel) {
		Map<String, Object> response = new HashMap<>();
		LocalDate taskDate = saveOrUpdateAttendanceModel.getTaskDate();
		String projectCode = saveOrUpdateAttendanceModel.getProjectCode();
		List<String> status = new ArrayList<>();
		status.add("109");
		status.add("103");
		List<AttendanceEntity> listOfAttendanceEntities = attendanceRepository
				.findByEmpBasicDetailIdAndTaskDateAndStatusNotIn(saveOrUpdateAttendanceModel.getEmpBasicDetailId(),
						taskDate, status);
		int totalTimeInvested = 0;
		Optional<EmployeeEntity> employeeEntity = employeeRepository
				.findById(saveOrUpdateAttendanceModel.getEmpBasicDetailId());

		if (employeeEntity.isPresent()) {
		totalTimeInvested = validateTotalTimeInvested(listOfAttendanceEntities.stream()
				.filter(d -> !Objects.equals(d.getAttendanceId(), saveOrUpdateAttendanceModel.getAttendanceId()))
					.toList(), saveOrUpdateAttendanceModel.getTimeInvested(),
					saveOrUpdateAttendanceModel.getIsBillable(), employeeEntity.get().getWorkLocationCode(),
					employeeEntity.get().getWorkType());
		} else {
			throw new ResourceNotFoundException("Employee not found for the given ID");
		}
		switch (timesheetAndLeaveValidation(saveOrUpdateAttendanceModel)) {
			case "allow":
				break;
			case "half":
				if (totalTimeInvested > 6 * 60) {
					throw new ResourceNotFoundException(
							"Half day Leave is applied for this task date. Total time invested shouldn't be more than 6 hours.");
				}
				break;
			case "full":
				throw new ResourceNotFoundException("Leave is applied for this task date.");
			default:
				throw new IllegalArgumentException("Something went wrong. Please check with Admin.");
		}
		if (saveOrUpdateAttendanceModel.getAttendanceId() != 0) {
			AttendanceEntity updateAttendanceEntity = attendanceRepository
					.findById(saveOrUpdateAttendanceModel.getAttendanceId()).orElse(null);
			if (updateAttendanceEntity != null) {
				updateAttendanceEntity.setApproverComments(saveOrUpdateAttendanceModel.getApprovercomments());
				updateAttendanceEntity.setStatus(saveOrUpdateAttendanceModel.getStatus());
				updateAttendanceEntity.setWorkflowStatus(saveOrUpdateAttendanceModel.getWorkflowStatus());
				updateAttendanceEntity.setClientCode(saveOrUpdateAttendanceModel.getClientCode());
				updateAttendanceEntity.setProjectCode(saveOrUpdateAttendanceModel.getProjectCode());
				updateAttendanceEntity.setTaskName(saveOrUpdateAttendanceModel.getTaskName());
				updateAttendanceEntity.setLocation(saveOrUpdateAttendanceModel.getLocation());
				updateAttendanceEntity.setTimeInvested(saveOrUpdateAttendanceModel.getTimeInvested());
				updateAttendanceEntity.setTaskDate(saveOrUpdateAttendanceModel.getTaskDate());
				updateAttendanceEntity.setIsBillable(saveOrUpdateAttendanceModel.getIsBillable());
				updateAttendanceEntity.setStatus("100");
				attendanceRepository.save(updateAttendanceEntity);
				response.put(RESPONSE_KEY, "Attendance details saved successfully");

			} else {
				throw new ResourceNotFoundException("Attendance details not found");
			}
		} else {
			AttendanceEntity attendanceEntity = new AttendanceEntity();
			attendanceEntity.setClientCode(saveOrUpdateAttendanceModel.getClientCode());
			attendanceEntity.setProjectCode(saveOrUpdateAttendanceModel.getProjectCode());
			attendanceEntity.setComments(saveOrUpdateAttendanceModel.getComments());
			attendanceEntity.setCreatedBy(saveOrUpdateAttendanceModel.getCreatedBy());
			attendanceEntity.setCreatedDate(LocalDateTime.now());
			attendanceEntity.setStatus(saveOrUpdateAttendanceModel.getStatus());
			attendanceEntity.setWorkflowStatus(saveOrUpdateAttendanceModel.getWorkflowStatus());
			attendanceEntity.setTaskName(saveOrUpdateAttendanceModel.getTaskName());
			attendanceEntity.setEmpBasicDetailId(saveOrUpdateAttendanceModel.getEmpBasicDetailId());
			attendanceEntity.setLocation(saveOrUpdateAttendanceModel.getLocation());
			attendanceEntity.setTimeInvested(saveOrUpdateAttendanceModel.getTimeInvested());
			attendanceEntity.setTaskDate(saveOrUpdateAttendanceModel.getTaskDate());
			attendanceEntity.setIsBillable(saveOrUpdateAttendanceModel.getIsBillable());
			attendanceEntity.setStatus("100");
			attendanceEntity.setApproverId(approverConfigRepository
					.findByEmpBasicDetailIdAndModuleName(saveOrUpdateAttendanceModel.getEmpBasicDetailId(), "timesheet")
					.getApproverId());
			String[] splitString = saveOrUpdateAttendanceModel.getTimeInvested().split(":");

			attendanceEntity.setHours(Integer.parseInt(splitString[0]));
			attendanceEntity.setMinutes(Integer.parseInt(splitString[1]));
			attendanceEntity.setSubmittedDate(saveOrUpdateAttendanceModel.getSubmittedDate());
			attendanceRepository.save(attendanceEntity);
			response.put(RESPONSE_KEY, "Attendance details saved successfully");

		}
		return response;
	}

	public int validateTotalTimeInvested(List<AttendanceEntity> listOfAttendanceEntities, String newTimeInvested,
			Boolean billable, String workLocationCode, String employementType) {
		int totalMinutes = 0;
		int maxMinutes = 12 * 60; // 12 hours in minutes
		int totalBillableMinutes = 0;
		// Sum time invested for all the attendance entries
		for (AttendanceEntity entity : listOfAttendanceEntities) {
			String timeInvested = entity.getTimeInvested();
			totalMinutes += convertTimeToMinutes(timeInvested);
		}

		for (AttendanceEntity entity : listOfAttendanceEntities.stream().filter(d -> d.getIsBillable()).toList()) {
			String timeInvested = entity.getTimeInvested();
			totalBillableMinutes += convertTimeToMinutes(timeInvested);
		}

		// Add the new timeInvested (time from the current save operation)
		totalMinutes += convertTimeToMinutes(newTimeInvested);
		totalBillableMinutes += convertTimeToMinutes(newTimeInvested);
		// Check if the total billable time exceeds 8 hours (480 minutes)
		if (Boolean.TRUE.equals(
				Boolean.TRUE.equals(workLocationCode.equals("WL003")) && employementType.equals("contract") && billable)
				&& totalBillableMinutes > 8 * 60) {
			throw new IllegalArgumentException("Total time invested for billable exceeds the allowed 8-hours limit.");
		} else if (Boolean.TRUE.equals(billable) && totalBillableMinutes > 9 * 60) {
			throw new IllegalArgumentException("Total time invested for billable exceeds the allowed 9-hours limit.");
		}

		// Check if the total exceeds 12 hours (720 minutes)
		if (totalMinutes > maxMinutes) {
			throw new IllegalArgumentException("Total time invested exceeds the allowed 12-hours limit.");
		}

		return totalMinutes;
	}

	public int convertTimeToMinutes(String timeInvested) {
		try {
			String[] parts = timeInvested.split(":");
			int hours = Integer.parseInt(parts[0]);
			int minutes = Integer.parseInt(parts[1]);
			return (hours * 60) + minutes; // Convert to total minutes
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Invalid time format: " + timeInvested);
		}
	}

	public boolean validateTimeInvested(String timeInvested, int maxHours) {
		try {
			// Split the timeInvested string by ":"
			String[] parts = timeInvested.split(":");
			int hours = Integer.parseInt(parts[0]); // Extract the hours
			int minutes = Integer.parseInt(parts[1]); // Extract the minutes

			// Validate if hours exceed maxHours or if exactly maxHours but minutes exceed 0
			if (hours > maxHours || (hours == maxHours && minutes > 0)) {
				return false; // Time exceeds the provided maxHours limit
			}

			// If time is valid
			return true;
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			// In case the string is not formatted properly
			return false;
		}
	}

	// public String timesheetAndLeaveValidation(SaveOrUpdateAttendanceModel inp) {
	// try {
	// List<EmployeeLeaveEntity> employeeLeaveEntities = employeeLeaveRepository
	// .findOverlappingLeaves(inp.getEmpBasicDetailId(), inp.getTaskDate(),
	// inp.getTaskDate());
	// switch (employeeLeaveEntities.size()) {
	// case 0:
	// return "allow";
	// case 1:
	// if
	// (employeeLeaveEntities.get(0).getLeaveFrom().equals(employeeLeaveEntities.get(0).getLeaveTo()))
	// {
	// if (!employeeLeaveEntities.get(0).getFromLeaveType().equals("full")) {
	// return "half";
	// }
	// return "full";
	// } else if
	// (!inp.getTaskDate().equals(employeeLeaveEntities.get(0).getLeaveFrom())
	// && !inp.getTaskDate().equals(employeeLeaveEntities.get(0).getLeaveTo())) {
	// return "full";
	// } else if
	// (inp.getTaskDate().equals(employeeLeaveEntities.get(0).getLeaveFrom())) {
	// if (employeeLeaveEntities.get(0).getFromLeaveType().equals("full")) {
	// return "full";
	// }
	// return "half";
	// } else if
	// (inp.getTaskDate().equals(employeeLeaveEntities.get(0).getLeaveTo())) {
	// if (employeeLeaveEntities.get(0).getToLeaveType().equals("full")) {
	// return "full";
	// }
	// return "half";
	// }
	// return "half";
	// case 2:
	// return "full";
	// default:
	// return "invalid";
	// }
	// } catch (Exception e) {
	// throw new ResourceNotFoundException(e.fillInStackTrace());
	// }
	// }
	// public String timesheetAndLeaveValidation(SaveOrUpdateAttendanceModel inp) {
	// try {
	// List<EmployeeLeaveEntity> employeeLeaveEntities = employeeLeaveRepository
	// .findOverlappingLeaves(inp.getEmpBasicDetailId(), inp.getTaskDate(),
	// inp.getTaskDate());
	// switch (employeeLeaveEntities.size()) {
	// case 0:
	// return "allow";
	// case 1:
	// if
	// (employeeLeaveEntities.get(0).getLeaveFrom().equals(employeeLeaveEntities.get(0).getLeaveTo()))
	// {
	// if (!employeeLeaveEntities.get(0).getFromLeaveType().equals("full")) {
	// return "half";
	// }
	// return "full";
	// } else if
	// (!inp.getTaskDate().equals(employeeLeaveEntities.get(0).getLeaveFrom())
	// && !inp.getTaskDate().equals(employeeLeaveEntities.get(0).getLeaveTo())) {
	// return "full";
	// } else if
	// (inp.getTaskDate().equals(employeeLeaveEntities.get(0).getLeaveFrom())) {
	// if (employeeLeaveEntities.get(0).getFromLeaveType().equals("full")) {
	// return "full";
	// }
	// return "half";
	// } else if
	// (inp.getTaskDate().equals(employeeLeaveEntities.get(0).getLeaveTo())) {
	// if (employeeLeaveEntities.get(0).getToLeaveType().equals("full")) {
	// return "full";
	// }
	// return "half";
	// }
	// return "half";
	// case 2:
	// return "full";
	// default:
	// return "invalid";
	// }
	// } catch (Exception e) {
	// throw new ResourceNotFoundException(e.fillInStackTrace());
	// }
	// }

	public String timesheetAndLeaveValidation(SaveOrUpdateAttendanceModel inp) {
		try {
			List<EmployeeLeaveEntity> employeeLeaveEntities = employeeLeaveRepository
					.findOverlappingLeaves(inp.getEmpBasicDetailId(), inp.getTaskDate(), inp.getTaskDate());

			switch (employeeLeaveEntities.size()) {
				case 0:
					return "allow";
				case 1:
					return handleSingleLeaveEntity(inp, employeeLeaveEntities.get(0));
				case 2:
					return "full";
				default:
					return "invalid";
			}
		} catch (Exception e) {
			throw new ResourceNotFoundException(e.fillInStackTrace());
		}
	}

	private String handleSingleLeaveEntity(SaveOrUpdateAttendanceModel inp, EmployeeLeaveEntity leaveEntity) {
		if (leaveEntity.getLeaveFrom().equals(leaveEntity.getLeaveTo())) {
			return handleSingleDayLeave(leaveEntity);
		} else if (!inp.getTaskDate().equals(leaveEntity.getLeaveFrom())
				&& !inp.getTaskDate().equals(leaveEntity.getLeaveTo())) {
			return "full";
		} else if (inp.getTaskDate().equals(leaveEntity.getLeaveFrom())) {
			return handleStartDayLeave(leaveEntity);
		} else if (inp.getTaskDate().equals(leaveEntity.getLeaveTo())) {
			return handleEndDayLeave(leaveEntity);
		}
		return "half";
	}

	private String handleSingleDayLeave(EmployeeLeaveEntity leaveEntity) {
		return leaveEntity.getFromLeaveType().equals("full") ? "full" : "half";
	}

	private String handleStartDayLeave(EmployeeLeaveEntity leaveEntity) {
		return leaveEntity.getFromLeaveType().equals("full") ? "full" : "half";
	}

	private String handleEndDayLeave(EmployeeLeaveEntity leaveEntity) {
		return leaveEntity.getToLeaveType().equals("full") ? "full" : "half";
	}

	public Map<String, Object> sendForApprovalAndApprove(AttendanceApprovalModel attendanceApprovalModel) {
		Map<String, Object> returnResponse = new HashMap<>();
		Map<Long, Set<LocalDate>> approvedTimesheets = new HashMap<>();
		Map<Long, Set<LocalDate>> requestTimesheets = new HashMap<>();
		Map<Long, Set<LocalDate>> rejectTimesheets = new HashMap<>();
		Set<LocalDate> dates = new HashSet<>();
		String specification = "";
		try {
			List<AttendanceIdModel> attendanceIdModels = attendanceApprovalModel.getAttendanceEmpIdModels();
			for (AttendanceIdModel attendance : attendanceIdModels) {
				AttendanceEntity attendanceEntity = attendanceRepository.findById(attendance.getAttendanceId())
						.orElse(null);
				if (attendanceEntity != null) {
					dates.add(attendanceEntity.getTaskDate());
					attendanceEntity.setStatus(attendance.getStatus());
					if (attendance.getStatus().equals("101")) {
						requestTimesheets.computeIfAbsent(attendanceEntity.getEmpBasicDetailId(), k -> new HashSet<>())
								.add(attendanceEntity.getTaskDate());
					}

					if (attendance.getStatus().equals("102")) {
						saveToTimesheetMaster(attendanceApprovalModel);
						approvedTimesheets.computeIfAbsent(attendanceEntity.getEmpBasicDetailId(), k -> new HashSet<>())
								.add(attendanceEntity.getTaskDate());

					} else if (attendance.getStatus().equals("103")) {
						attendanceEntity.setApproverComments(attendanceApprovalModel.getComments());
						rejectTimesheets.computeIfAbsent(attendanceEntity.getEmpBasicDetailId(), k -> new HashSet<>())
								.add(attendanceEntity.getTaskDate());
					}
					attendanceRepository.save(attendanceEntity);
				}

			}
			NotificationEntity notificationEntity = new NotificationEntity();
			AttendanceEntity attendanceEntity = attendanceRepository
					.findById(attendanceIdModels.get(0).getAttendanceId())
					.orElseThrow(() -> new IllegalArgumentException(
							"Attendance entity not found for id: " + attendanceIdModels.get(0).getAttendanceId()));
			notificationEntity.setEmployeeId(attendanceEntity.getEmpBasicDetailId());
			notificationEntity.setModule("Timesheet");
			notificationEntity.setPath(Constants.TIMESHEET_PATH);
			notificationEntity.setStatus("115");
			switch (attendanceIdModels.get(0).getStatus()) {
				case "101":
					// send notification to manager
					notificationEntity.setEmployeeId(attendanceEntity.getApproverId());
					notificationEntity
							.setMessage("Timesheet Request from " + attendanceEntity.getEmployeeEntity().getFullName());
					notificationRepository.save(notificationEntity);
					break;
				case "102":
					// send notification to employee for approval
					notificationEntity.setMessage("Timesheet Request has been approved");
					notificationRepository.save(notificationEntity);
					break;
				case "103":
					// send notification to employee for rejection
					notificationEntity.setMessage("Timesheet Request has been rejected");
					notificationRepository.save(notificationEntity);
					break;
				default:
					break;
			}
			if (attendanceApprovalModel.getAttendanceEmpIdModels().get(0).getStatus().equals("102")) {
				// Use Java 8 streams to build the specification string
				for (Map.Entry<Long, Set<LocalDate>> entry : approvedTimesheets.entrySet()) {
					Long empBasicDetailId = entry.getKey();
					Set<LocalDate> taskDates = entry.getValue();

					// Prepare email content: format task dates
					String formattedDates = taskDates.stream()
							.map(date -> date.getDayOfMonth() + "-" + date.getMonth() + "-" + date.getYear())
							.collect(Collectors.joining(", "));

					// Update the model for email
					attendanceApprovalModel.setBasicEmpDetailId(empBasicDetailId);
					attendanceApprovalModel.setComments(formattedDates);

					// Send approval email
					sendTimesheetApprovalEmail(attendanceApprovalModel);
				}
			} else if (attendanceApprovalModel.getAttendanceEmpIdModels().get(0).getStatus().equals("101")) {
				for (Map.Entry<Long, Set<LocalDate>> entry : requestTimesheets.entrySet()) {
					Long empBasicDetailId = entry.getKey();
					Set<LocalDate> taskDates = entry.getValue();

					// Prepare email content: format task dates
					String formattedDates = taskDates.stream()
							.map(date -> date.getDayOfMonth() + "-" + date.getMonth() + "-" + date.getYear())
							.collect(Collectors.joining(", "));

					// Update the model for email
					attendanceApprovalModel.setBasicEmpDetailId(empBasicDetailId);
					attendanceApprovalModel.setComments(formattedDates);

					// Send approval email
					sendTimesheetRequestEmail(attendanceApprovalModel);
				}

			} else if (attendanceApprovalModel.getAttendanceEmpIdModels().get(0).getStatus().equals("103")) {
				for (Map.Entry<Long, Set<LocalDate>> entry : rejectTimesheets.entrySet()) {
					Long empBasicDetailId = entry.getKey();
					Set<LocalDate> taskDates = entry.getValue();

					// Prepare email content: format task dates
					String formattedDates = taskDates.stream()
							.map(date -> date.getDayOfMonth() + "-" + date.getMonth() + "-" + date.getYear())
							.collect(Collectors.joining(", "));

					// Update the model for email
					attendanceApprovalModel.setBasicEmpDetailId(empBasicDetailId);
					attendanceApprovalModel.setComments(formattedDates);

					// Send approval email
					sendTimesheetRejectEmail(attendanceApprovalModel);
				}

			}

			returnResponse.put(RESPONSE_KEY, "Response Recorded Successfully");
		} catch (Exception e) {
			throw new RuntimeException("Error Occurred: " + e.getMessage());
		}

		return returnResponse;
	}

	public String saveToTimesheetMaster(AttendanceApprovalModel attendanceApprovalModel) {

		List<AttendanceIdModel> attendanceEmpIdModels = attendanceApprovalModel.getAttendanceEmpIdModels();

		for (AttendanceIdModel attendance : attendanceEmpIdModels) {
			AttendanceEntity attendanceEntity = attendanceRepository
					.findByAttendanceIdAndStatus(attendance.getAttendanceId(), "102");
			if (attendanceEntity != null) {
				TimesheetMasterEntity timesheetMasterEntity = new TimesheetMasterEntity();
				timesheetMasterEntity.setEmployeeCode(attendanceEntity.getEmployeeEntity().getEmployeeCode());
				timesheetMasterEntity.setCreatedBy("System");
				timesheetMasterEntity.setCreatedDate(LocalDateTime.now());
				timesheetMasterEntity.setProjectCode(attendanceEntity.getProjectCode());
				timesheetMasterEntity.setTaskName(attendanceEntity.getTaskName());
				timesheetMasterEntity.setClientName(attendanceEntity.getClientCode());
				timesheetMasterEntity.setEmpBasicDetailId(attendanceEntity.getEmpBasicDetailId());
				timesheetMasterRepository.save(timesheetMasterEntity);
			}
		}
		return "Successfully saved to TimesheetMaster";
	}

	public Page<AttendanceEntity> searchAttendanceDetails(AttendanceDetailsSearchModel attendanceDetailsSearchModel) {
		logger.info("-----------serchAttendanceDetails service Starts Here---------------------");
		try {
			Long empBasicDetailId = attendanceDetailsSearchModel.getEmpBasicDetailId();
			String projectCode = attendanceDetailsSearchModel.getProjectCode();
			Long approverId = attendanceDetailsSearchModel.getApproverId();
			int page = attendanceDetailsSearchModel.getPage();
			int size = attendanceDetailsSearchModel.getSize();
			int year = 0;
			int month = 0;
			if (Boolean.TRUE.equals(StringUtil.isNotNull(attendanceDetailsSearchModel.getYearAndMonth()))) {
				LocalDate localDate = LocalDate.parse(attendanceDetailsSearchModel.getYearAndMonth());

				year = localDate.getYear();
				month = localDate.getMonthValue();
				logger.info("year and month: {} and {}", year, month);
			}

			Specification<AttendanceEntity> spec = Specification.where(null);

			if (StringUtil.isNotNull(projectCode)) {
				spec = spec.and(hasProjectCode(projectCode));
			}
			if (IntegerUtils.isNotNull(empBasicDetailId)) {
				spec = spec.and(hasEmpBasicDetailId(empBasicDetailId));
			}
			if (IntegerUtils.isNotNull(approverId)) {
				spec = spec.and(hasApproverId(approverId));
			}
			if (StringUtil.isNotNull(attendanceDetailsSearchModel.getYearAndMonth())) {
				spec = spec.and(hasTaskDate(month, year));
				spec = spec.and(hasTaskMonth(month, year));

			}

			if (attendanceDetailsSearchModel.getStatus() != null
					&& !attendanceDetailsSearchModel.getStatus().isEmpty()) {
				List<Specification<AttendanceEntity>> statusSpecs = new ArrayList<>();
				for (String status : attendanceDetailsSearchModel.getStatus()) {
					statusSpecs.add(hasStatus(status));
				}
				Specification<AttendanceEntity> combinedSpec = statusSpecs.stream().reduce(Specification::and)
						.orElse(null);
				if (combinedSpec != null) {
					spec = spec.and(combinedSpec);
				}

			}
			if (attendanceDetailsSearchModel.getTaskDate() != null) {
				spec = spec.and(hasExactTaskDate(attendanceDetailsSearchModel.getTaskDate()));
			}
			spec = spec.and(hasStatusNot("109"));
			Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "taskDate"));
			// Pageable pageable = PageRequest.of(page, size);
			return attendanceRepository.findAll(spec, pageable);
		} catch (IllegalArgumentException e) {
			throw new ResourceNotFoundException(e.fillInStackTrace());
		}
	}

	public static Specification<AttendanceEntity> hasExactTaskDate(LocalDate taskDate) {
		return (Root<AttendanceEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
				.equal(root.get("taskDate"), taskDate);
	}

	public static Specification<AttendanceEntity> hasStatusNot(String status) {
		return (Root<AttendanceEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
				.notEqual(root.get("status"), status);
	}

	public static Specification<AttendanceEntity> hasProjectCode(String projectCode) {
		return (Root<AttendanceEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
				.equal(root.get("projectCode"), projectCode);
	}

	public static Specification<AttendanceEntity> hasEmpBasicDetailId(Long empBasicDetailId) {
		return (Root<AttendanceEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
				.equal(root.get("empBasicDetailId"), empBasicDetailId);
	}

	public static Specification<AttendanceEntity> hasApproverId(Long approverId) {
		return (Root<AttendanceEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
				.equal(root.get("approverId"), approverId);
	}

	public static Specification<AttendanceEntity> hasTaskDate(Integer month, Integer year) {
		return (Root<AttendanceEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
			// Use the YEAR function to extract the year part from taskDate and compare it
			// with the provided year
			return criteriaBuilder.equal(criteriaBuilder.function("YEAR", Integer.class, root.get("taskDate")), year);
		};
	}

	public static Specification<AttendanceEntity> hasTaskMonth(Integer month, Integer year) {
		return (Root<AttendanceEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
			// Use the YEAR function to extract the year part from taskDate and compare it
			// with the provided year
			return criteriaBuilder.equal(criteriaBuilder.function("MONTH", Integer.class, root.get("taskDate")), month);
		};

	}

	public static Specification<AttendanceEntity> hasStatus(String status) {
		return (Root<AttendanceEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> criteriaBuilder
				.equal(root.get("status"), status);

	}

	public List<AttendanceEntity> searchAttendanceDetails1(AttendanceDetailsSearchModel attendanceDetailsSearchModel) {
		List<AttendanceEntity> userRoles = null;
		TypedQuery<AttendanceEntity> typedQuery = null;
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AttendanceEntity> criteriaQuery = criteriaBuilder.createQuery(AttendanceEntity.class);
		Root<AttendanceEntity> adjustment = criteriaQuery.from(AttendanceEntity.class);
		List<Predicate> predicates = new ArrayList<>();

		Long empBasicDetailId = attendanceDetailsSearchModel.getEmpBasicDetailId();
		String projectCode = attendanceDetailsSearchModel.getProjectCode();
		Long approverId = attendanceDetailsSearchModel.getApproverId();
		if (empBasicDetailId != 0) {
			predicates.add(criteriaBuilder.equal(adjustment.get("empBasicDetailId"), empBasicDetailId));
		}

		if (approverId != 0) {
			predicates.add(criteriaBuilder.equal(adjustment.get("approverId"), approverId));
		}

		criteriaQuery.select(adjustment).distinct(true);
		criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
		typedQuery = entityManager.createQuery(criteriaQuery);
		userRoles = typedQuery.getResultList();
		// List<AttendanceEntity> roleTypeUsersResponseModel =
		// UserRoleMapper.toDTOList(userRoles);

		return userRoles;
	}

	public void sendTimesheetRequestEmail(AttendanceApprovalModel attendanceApprovalModel) {
		Long employeeId = attendanceApprovalModel.getBasicEmpDetailId();
		// EmailService emailService = new EmailService();
		EmailModel emailModel = new EmailModel();
		UserApproverConfigurationsEntity approverConfigurationEntity = userApproverConfigurationRepository
				.findByEmpBasicDetailIdAndModuleName(employeeId, "timesheet");

		if (approverConfigurationEntity != null) {
			Long approver = approverConfigurationEntity.getApproverId();

			Optional<EmployeeEntity> approverEntity = employeeRepository.findById(approver);
			if (approverEntity.isPresent()) {
				emailModel.setAddressTo(approverEntity.get().getFullName());
				emailModel.setToEmail(approverEntity.get().getEmailId());
				emailModel.setSubject("Timesheet Approval Request");

				// Find the employee entity
				Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(employeeId);
				if (employeeEntity.isPresent()) {
					emailModel.setRequestor(employeeEntity.get().getFullName());
					emailModel.setEmployeeCode(employeeEntity.get().getEmployeeCode());

					// Send the timesheet approval request email
					emailService.sendTimesheetMailRequest(emailModel);
				} else {
					throw new RuntimeException("No employee found with the given ID");
				}
			} else {
				throw new RuntimeException("No approver found for the employee");
			}
		} else {
			throw new RuntimeException("No approver configuration found for the employee");
		}

	}

	public void sendTimesheetRejectEmail(AttendanceApprovalModel attendanceApprovalModel) {
		Long employeeId = attendanceApprovalModel.getBasicEmpDetailId();
		EmailModel emailModel = new EmailModel();
		logger.info("attendanceApprovalModel657 {}", attendanceApprovalModel);
		// Find the employee entity
		Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(employeeId);

		if (employeeEntity.isPresent()) {
			EmployeeEntity employee = employeeEntity.get();
			emailModel.setAddressTo(employee.getFullName());
			emailModel.setToEmail(employee.getEmailId());
			emailModel.setSubject("Timesheet Approval Confirmation");
			emailModel.setBody(attendanceApprovalModel.getComments());

			emailModel.setRequestor(employee.getFullName());
			emailModel.setEmployeeCode(employee.getEmployeeCode());

			// Send the timesheet approval confirmation email
			logger.info("emailModel672 {}", emailModel);
			emailService.sendTimesheetRejectMail(emailModel);
		} else {
			throw new RuntimeException("No employee found with the given ID");
		}
	}

	public void sendTimesheetApprovalEmail(AttendanceApprovalModel attendanceApprovalModel) {
		Long employeeId = attendanceApprovalModel.getBasicEmpDetailId();
		EmailModel emailModel = new EmailModel();
		logger.info("attendanceApprovalModel657 {}", attendanceApprovalModel);
		// Find the employee entity
		Optional<EmployeeEntity> employeeEntity = employeeRepository.findById(employeeId);

		if (employeeEntity.isPresent()) {
			EmployeeEntity employee = employeeEntity.get();
			emailModel.setAddressTo(employee.getFullName());
			emailModel.setToEmail(employee.getEmailId());
			emailModel.setSubject("Timesheet Approval Confirmation");
			emailModel.setBody(attendanceApprovalModel.getComments());

			emailModel.setRequestor(employee.getFullName());
			emailModel.setEmployeeCode(employee.getEmployeeCode());

			// Send the timesheet approval confirmation email
			logger.info("emailModel672 {}", emailModel);
			emailService.sendTimesheetApprovalMail(emailModel);
		} else {
			throw new RuntimeException("No employee found with the given ID");
		}
	}

	public String createTimesheetReport(TimesheetReportModel timesheetReportModel) {
		try {
			String project = "";
			String filepath = null;
			List<AttendanceEntity> timesheetdata = searchAttendanceDetailsforReport(timesheetReportModel);
			logger.info("timesheetdata {}", timesheetdata);

			EmployeeEntity employee = employeeRepository.findByEmployeeCode(timesheetReportModel.getEmployeeCode());
			if (employee == null) {
				throw new ResourceNotFoundException("Employee Doesn't exist with this code");
			}
			logger.info("employee {}", employee);

			List<EmployeeProjectConfigEntity> employeeProjectConfig = employeeProjectConfigRepository
					.findByEmployeeIdAndStatus(employee.getEmployeeBasicDetailId(), "108");
			if (!employeeProjectConfig.isEmpty()) {
				for (EmployeeProjectConfigEntity employeeProjectConfigEntity : employeeProjectConfig) {
					logger.info("employeeProjectConfigEntity.getProjectCode(): {}",
							employeeProjectConfigEntity.getProjectCode());
					project = project + employeeProjectConfigEntity.getProjectCode();
				}
				LocalDate localDate = timesheetReportModel.getFromDate();

				int year = localDate.getYear();
				int month = localDate.getMonthValue();
				String workLocationCode = fetchWorklocationCode();
				List<ProjectAndClientDTO> projects = fetchCodeAndDescription(employee.getEmployeeBasicDetailId(),
						timesheetReportModel.getFromDate(), timesheetReportModel.getToDate());
				List<LocalDate> dates = getDatesBetween(timesheetReportModel.getFromDate(),
						timesheetReportModel.getToDate());
				String formattedDate = YearMonth.of(year, month)
						.format(DateTimeFormatter.ofPattern("MMM-yyyy"));
				filepath = timesheetReport.generatePdfReport(timesheetdata, dates, projects, workLocationCode, employee,
						formattedDate);
			}
			return filepath;
		} catch (Exception e) {
			throw new ResourceNotFoundException(e.fillInStackTrace());
		}

	}

	public List<AttendanceEntity> searchAttendanceDetailsforReport(TimesheetReportModel timesheetReportModel) {
		logger.info("-----------searchAttendanceDetails service Starts Here---------------------");

		// Check if employee exists
		EmployeeEntity employee = employeeRepository.findByEmployeeCode(timesheetReportModel.getEmployeeCode());
		logger.info("employee: {}", employee);
		if (employee == null) {
			throw new ResourceNotFoundException("Employee doesn't exist with this code");
		}

		Long empBasicDetailId = employee.getEmployeeBasicDetailId();
		logger.info("empBasicDetailId: {}", empBasicDetailId);
		List<String> status = new ArrayList<>();
		status.add("102");
		status.add("101");
		status.add("108");
		List<AttendanceEntity> attendanceList = null;
		TypedQuery<AttendanceEntity> typedQuery = null;

		// Initialize CriteriaBuilder and CriteriaQuery
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<AttendanceEntity> criteriaQuery = criteriaBuilder.createQuery(AttendanceEntity.class);
		Root<AttendanceEntity> attendance = criteriaQuery.from(AttendanceEntity.class);

		// Create a list of predicates (conditions)
		List<Predicate> predicates = new ArrayList<>();

		// Add a predicate for employee basic detail ID
		if (empBasicDetailId != 0) {
			predicates.add(criteriaBuilder.equal(attendance.get("empBasicDetailId"), empBasicDetailId));
		}

		// Handle date range filtering
		if (timesheetReportModel.getFromDate() != null && timesheetReportModel.getToDate() != null) {
			predicates.add(criteriaBuilder.between(attendance.<LocalDate>get("taskDate"),
					timesheetReportModel.getFromDate(), timesheetReportModel.getToDate()));
		} else if (timesheetReportModel.getFromDate() != null) {
			// If only fromDate is provided, fetch attendance from that date onwards
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(attendance.<LocalDate>get("taskDate"),
					timesheetReportModel.getFromDate()));
		} else if (timesheetReportModel.getToDate() != null) {
			// If only toDate is provided, fetch attendance up to that date
			predicates.add(criteriaBuilder.lessThanOrEqualTo(attendance.<LocalDate>get("taskDate"),
					timesheetReportModel.getToDate()));
		}
		predicates.add(attendance.get("status").in(status));

		// Select distinct attendance records
		criteriaQuery.select(attendance).distinct(true);

		// Apply predicates
		criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

		// Execute the query and retrieve results
		typedQuery = entityManager.createQuery(criteriaQuery);
		attendanceList = typedQuery.getResultList();

		return attendanceList;
	}

	public List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
		List<LocalDate> datesBetween = new ArrayList<>();

		// Ensure startDate is before or equal to endDate
		if (startDate.isAfter(endDate)) {
			throw new IllegalArgumentException("Start date must be before or equal to end date");
		}

		// Iterate from startDate to endDate and add each date to the list
		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			datesBetween.add(date);
		}

		return datesBetween;
	}

	public List<ProjectAndClientDTO> fetchCodeAndDescription(Long empId, LocalDate fromDate, LocalDate toDate) {

		String projectCode = employeeProjectConfigRepository.findByEmployeeIdAndStatus(empId, "108").stream()
				.map(EmployeeProjectConfigEntity::getProjectCode).collect(Collectors.joining("','", "'", "'"));
		String sql = "SELECT pm.project_name as projectName, cm.client_name as clientName,pm.project_code as projectCode FROM masterconfigurationprod.project_master pm JOIN masterconfigurationprod.client_master cm ON pm.client_code = cm.client_code WHERE pm.project_code IN ("
				+ projectCode + ")";
		logger.info(sql);
		// Execute native query
		Query query = entityManager.createNativeQuery(sql);

		// Fetch results
		List<Object[]> resultList = query.getResultList();
		List<ProjectAndClientDTO> projectAndClientList = new ArrayList<>();

		// Iterate over results and map to DTO
		for (Object[] result : resultList) {
			String projectName = (String) result[0];
			String clientName = (String) result[1];
			String project = (String) result[2];
			projectAndClientList.add(new ProjectAndClientDTO(projectName, clientName, project));
		}

		return projectAndClientList;
	}

	public String fetchWorklocationCode() {
		String sql = "SELECT work_location_code FROM masterconfigurationprod.work_location_master WHERE work_location = 'U.S.A'";

		// Execute native query
		Query query = entityManager.createNativeQuery(sql);

		// Fetch result as a single string
		String workLocationCode = (String) query.getSingleResult();

		return workLocationCode;
	}

	public ResponseEntity<TimesheetDatesApprovalEntity> weekendValidation(Long employeeId, LocalDate taskDate) {
		try {
			TimesheetDatesApprovalEntity timesheetDatesApprovalEntity = timesheetDatesApprovalRepository
					.findByEmployeeIdAndTaskDate(employeeId, taskDate);
			if (timesheetDatesApprovalEntity != null) {
				return new ResponseEntity<>(timesheetDatesApprovalEntity, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			logger.error("Error occurred while fetching timesheet dates approval entity", e);
			throw new ResourceNotFoundException(e.fillInStackTrace());
		}
	}

	public ResponseEntity<Map<String, Page<TimesheetDatesApprovalEntity>>> getWeekendApprovals(
			WeekendApprovalModel weekendApprovalModel) {
		try {
			Map<String, Page<TimesheetDatesApprovalEntity>> response = new HashMap<>();
			Specification<TimesheetDatesApprovalEntity> spec = Specification.where(null);
			if (IntegerUtils.isNotNull(weekendApprovalModel.getEmpBasicDetailId())) {
				spec = spec.and(hasEmployeeId(weekendApprovalModel.getEmpBasicDetailId()));
			}
			if (IntegerUtils.isNotNull(weekendApprovalModel.getApproverID())) {
				spec = spec.and(hasApproverIdForWeekendCheck(weekendApprovalModel.getApproverID()));
			}
			if (Boolean.TRUE.equals(!weekendApprovalModel.getStatus().isEmpty())) {
				spec = spec.and(hasStatusForWeekendCheck(weekendApprovalModel.getStatus()));
			}
			Pageable pageable = PageRequest.of(weekendApprovalModel.getPage(), weekendApprovalModel.getSize(),
					Sort.by(Sort.Direction.DESC, "taskDate"));
			Page<TimesheetDatesApprovalEntity> timesheetDatesApprovalEntities = timesheetDatesApprovalRepository
					.findAll(spec, pageable);
			response.put("data", timesheetDatesApprovalEntities);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			throw new ResourceNotFoundException(e.fillInStackTrace());
		} catch (Exception e) {
			logger.error("Error occurred while fetching timesheet dates approval entity", e);
			throw new ResourceNotFoundException(e.fillInStackTrace());
		}
	}

	private Specification<TimesheetDatesApprovalEntity> hasEmployeeId(Long employeeId) {
		return (Root<TimesheetDatesApprovalEntity> root, CriteriaQuery<?> query,
				CriteriaBuilder criteriaBuilder) -> criteriaBuilder
						.equal(root.get("employeeId"), employeeId);
	}

	private Specification<TimesheetDatesApprovalEntity> hasApproverIdForWeekendCheck(Long approverId) {
		return (Root<TimesheetDatesApprovalEntity> root, CriteriaQuery<?> query,
				CriteriaBuilder criteriaBuilder) -> criteriaBuilder
						.equal(root.get("approverId"), approverId);
	}

	private Specification<TimesheetDatesApprovalEntity> hasStatusForWeekendCheck(List<String> status) {
		return (Root<TimesheetDatesApprovalEntity> root, CriteriaQuery<?> query,
				CriteriaBuilder criteriaBuilder) -> root.get("status").in(status);
	}

	public ResponseEntity<String> saveAndApproveWeekend(TimesheetDatesApprovalEntity timesheetDatesApprovalEntity) {
		try {
			UserApproverConfigurationsEntity approverConfigurationEntity = userApproverConfigurationRepository
					.findByEmpBasicDetailIdAndModuleName(timesheetDatesApprovalEntity.getEmployeeId(), "timesheet");
			if (approverConfigurationEntity != null) {
				timesheetDatesApprovalEntity.setApproverId(approverConfigurationEntity.getApproverId());
			} else {
				throw new ResourceNotFoundException("Approver not found for the employee");
			}
			timesheetDatesApprovalRepository.save(timesheetDatesApprovalEntity);
			return new ResponseEntity<>("Weekend approval saved successfully", HttpStatus.OK);
		} catch (Exception e) {
			throw new ResourceNotFoundException(e.fillInStackTrace());
		}
	}
	
	public List<AttendanceEntity> searchAttendanceForGrid(TimesheetReportRequestDto dto) {
	    logger.info("-----------searchAttendanceDetails service Starts Here---------------------");

	    EmployeeEntity employee = employeeRepository.findByEmployeeCode(dto.getEmployeeCode());
	    logger.info("employee: {}", employee);
	    if (employee == null) {
	        throw new ResourceNotFoundException("Employee doesn't exist with this code");
	    }

	    Long empBasicDetailId = employee.getEmployeeBasicDetailId();
	    logger.info("empBasicDetailId: {}", empBasicDetailId);

	    List<String> status = List.of("102", "101", "108");

	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    CriteriaQuery<AttendanceEntity> cq = cb.createQuery(AttendanceEntity.class);
	    Root<AttendanceEntity> attendance = cq.from(AttendanceEntity.class);

	    List<Predicate> predicates = new ArrayList<>();

	    if (empBasicDetailId != 0) {
	        predicates.add(cb.equal(attendance.get("empBasicDetailId"), empBasicDetailId));
	    }

	    if (dto.getFromDate() != null && dto.getToDate() != null) {
	        predicates.add(cb.between(attendance.get("taskDate"), dto.getFromDate(), dto.getToDate()));
	    } else if (dto.getFromDate() != null) {
	        predicates.add(cb.greaterThanOrEqualTo(attendance.get("taskDate"), dto.getFromDate()));
	    } else if (dto.getToDate() != null) {
	        predicates.add(cb.lessThanOrEqualTo(attendance.get("taskDate"), dto.getToDate()));
	    }

	    predicates.add(attendance.get("status").in(status));

	    cq.select(attendance).distinct(true);
	    cq.where(cb.and(predicates.toArray(new Predicate[0])));
	    cq.orderBy(cb.desc(attendance.get("taskDate"))); // optional ordering

	    TypedQuery<AttendanceEntity> query = entityManager.createQuery(cq)
	            .setFirstResult(dto.getPage() * dto.getSize())
	            .setMaxResults(dto.getSize());

	    return query.getResultList();
	}
}
