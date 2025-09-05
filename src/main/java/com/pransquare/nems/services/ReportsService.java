package com.pransquare.nems.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.pransquare.nems.entities.AttendanceEntity;
import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.entities.EmployeeLeaveEntity;
import com.pransquare.nems.entities.ExpenseEntity;
import com.pransquare.nems.entities.ProjectReportEntity;
import com.pransquare.nems.entities.UserApproverConfigurationsEntity;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.models.AttendanceDetailsSearchModel;
import com.pransquare.nems.models.HolidayModel;
import com.pransquare.nems.models.ProjectReportModel;
import com.pransquare.nems.repositories.ApproverConfigRepository;
import com.pransquare.nems.repositories.AttendanceRepository;
import com.pransquare.nems.repositories.EmployeeLeaveRepository;
import com.pransquare.nems.repositories.EmployeeProjectConfigRepository;
import com.pransquare.nems.repositories.EmployeeRepository;
import com.pransquare.nems.repositories.ExpenseRepository;
import com.pransquare.nems.repositories.ProjectReportRepository;
import com.pransquare.nems.utils.CurrencySymbols;
import com.pransquare.nems.utils.IntegerUtils;

import jakarta.persistence.EntityManager;

@Service
public class ReportsService {

	private static final Logger logger = LogManager.getLogger(ReportsService.class);
	private RestTemplate restTemplate = new RestTemplate();
	private final EmployeeProjectConfigRepository employeeProjectConfigRepository;
	private final EmployeeRepository employeeRepository;
	private final ExpenseRepository expenseRepository;
	private final AttendanceRepository attendanceRepository;
	private final EmployeeLeaveRepository employeeLeaveRepository;
	private final EmployeeTimeSheetService employeeTimeSheetService;
	private final ApproverConfigRepository approverConfigRepository;
    private ProjectReportRepository projectReportRepository;

	@Value("${master-config-service.url}")
	private String masterConfigUrl;

	@Autowired
	EntityManager entityManager;

	ReportsService(EmployeeProjectConfigRepository employeeProjectConfigRepository,
			EmployeeRepository employeeRepository, ExpenseRepository expenseRepository,
			AttendanceRepository attendanceRepository, EmployeeLeaveRepository employeeLeaveRepository,
			EmployeeTimeSheetService employeeTimeSheetService, ApproverConfigRepository approverConfigRepository,
			ProjectReportRepository projectReportRepository) {
		this.employeeProjectConfigRepository = employeeProjectConfigRepository;
		this.employeeRepository = employeeRepository;
		this.expenseRepository = expenseRepository;
		this.attendanceRepository = attendanceRepository;
		this.employeeLeaveRepository = employeeLeaveRepository;
		this.employeeTimeSheetService = employeeTimeSheetService;
		this.approverConfigRepository = approverConfigRepository;
		this.projectReportRepository = projectReportRepository;
	}



    public InputStream generateExcelForProjectReport(ProjectReportModel projectReportModel) {
        List<ProjectReportEntity> rows = projectReportRepository.findFiltered(
                isEmpty(projectReportModel.getClientCode()) ? null : projectReportModel.getClientCode(),
                isEmpty(projectReportModel.getProjectCode()) ? null : projectReportModel.getProjectCode(),
                projectReportModel.getFromDate(),
                projectReportModel.getToDate()
        );

        if (rows == null || rows.isEmpty()) {
            throw new ResourceNotFoundException("No data found for the selected filters.");
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Project Report");
            int rowNum = 0;

            // Styles
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            CellStyle tableStyle = workbook.createCellStyle();
            tableStyle.setAlignment(HorizontalAlignment.CENTER);
            tableStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            tableStyle.setBorderTop(BorderStyle.THIN);
            tableStyle.setBorderBottom(BorderStyle.THIN);
            tableStyle.setBorderLeft(BorderStyle.THIN);
            tableStyle.setBorderRight(BorderStyle.THIN);
            tableStyle.setWrapText(true);

            // Header
            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = { "Sr. No.", "Project ID", "Project Name", "Employee Code", "Employee Name", "Status",
                    "Start Date", "End Date" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data
            int srNo = 1;
            LocalDate today = LocalDate.now();

            for (ProjectReportEntity row : rows) {
                Row dataRow = sheet.createRow(rowNum++);
                int col = 0;

                dataRow.createCell(col++).setCellValue(srNo++);
                dataRow.createCell(col++).setCellValue(row.getProjectId());
                dataRow.createCell(col++).setCellValue(row.getProjectName());
                dataRow.createCell(col++).setCellValue(row.getEmployeeCode());
                dataRow.createCell(col++).setCellValue(row.getEmployeeName());

                String status = row.getEndDate() != null && row.getEndDate().isBefore(today) ? "Expired" : "Active";
                dataRow.createCell(col++).setCellValue(status);
                dataRow.createCell(col++).setCellValue(row.getStartDate() != null ? row.getStartDate().toString() : "");
                dataRow.createCell(col++).setCellValue(row.getEndDate() != null ? row.getEndDate().toString() : "");

                for (int i = 0; i < col; i++) {
                    dataRow.getCell(i).setCellStyle(tableStyle);
                }
            }

            // Autosize
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new ResourceNotFoundException("Failed to generate report: " + e.getMessage(), e);
        }
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean isEmpty(List<String> list) {
        return list == null || list.isEmpty();
    }
	private Map<String, String> getEmployeeDetails(Long employeeId) {
		// Replace with actual DB/service call to get employee code and name
		EmployeeEntity employee = employeeRepository.findById(employeeId).orElse(null);
		if (employee == null)
			return Map.of("employeeCode", "N/A", "employeeName", "Unknown");

		return Map.of("employeeCode", employee.getEmployeeCode(), "employeeName",
				employee.getFirstName() + " " + employee.getLastName());
	}

// Method to fetch employee full name
	private String getEmployeeFullName(Long employeeId) {
		EmployeeEntity employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
		return employee.getFirstName() + " " + employee.getLastName();
	}

	public ByteArrayResource generateExpenseReport(Long employeeId, LocalDate date) throws IOException {
		// Calculate start and end of the month
		LocalDate startOfMonth = date.withDayOfMonth(1);
		LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());
		List<ExpenseEntity> expenses;
		// Fetch expenses
		if (IntegerUtils.isNotNull(employeeId)) {
			expenses = expenseRepository.findExpensesForReport(employeeId, startOfMonth, endOfMonth, "102");
		} else {
			expenses = expenseRepository.findExpensesForReportAll(startOfMonth, endOfMonth, "102");
		}

		// Create Excel file
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Expense Report");

			// Header row
			Row header = sheet.createRow(0);
			header.createCell(0).setCellValue("S.No.");
			header.createCell(1).setCellValue("Employee Name");
			header.createCell(2).setCellValue("Expense Entry Date");
			header.createCell(3).setCellValue("Expense name");
			header.createCell(4).setCellValue("Expense From Date");
			header.createCell(5).setCellValue("Expense To Date");
			header.createCell(6).setCellValue("Amount");

			// Data rows
			int rowIndex = 1;
			for (int i = 0; i < expenses.size(); i++) {
				ExpenseEntity expense = expenses.get(i);
				Row row = sheet.createRow(rowIndex++);
				row.createCell(0).setCellValue(i + 1); // S.No.
				row.createCell(1).setCellValue(expense.getEmpoyeeName());
				row.createCell(2).setCellValue(expense.getEntryDate().toString());
				row.createCell(3).setCellValue(expense.getExpenseName());
				row.createCell(4).setCellValue(expense.getFrom().toString());
				row.createCell(5).setCellValue(expense.getTo().toString());
				row.createCell(6)
						.setCellValue(CurrencySymbols.getCurrencySymbol(expense.getExpenseAmtType()) != null
								? CurrencySymbols.getCurrencySymbol(expense.getExpenseAmtType()) + " "
										+ expense.getExpenseAmount().toString()
								: expense.getExpenseAmount().toString());
			}
			// Auto-size columns
			for (int i = 0; i < 6; i++) {
				sheet.autoSizeColumn(i);
			}
			workbook.write(out);
		}
		return new ByteArrayResource(out.toByteArray());
	}

	public ByteArrayResource generateTimesheetComplianceReport(Long employeeId, LocalDate fromDate, LocalDate toDate,
			String workLocationCode, String type) throws IOException {
		try {
			List<EmployeeEntity> employees = fetchEmployees(employeeId, workLocationCode);
			if (employees.isEmpty()) {
				throw new ResourceNotFoundException("No data found for the given date range or employee status");
			}
			List<AttendanceEntity> attendanceEntities = fetchAttendanceRecords(employeeId, fromDate, toDate);
			List<HolidayModel> holidays = new ArrayList<>();
			List<EmployeeLeaveEntity> employeeLeaveEntities = new ArrayList<>();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			if (type.equals("compliance")) {
				holidays = fetchHolidays();
				employeeLeaveEntities = fetchEmployeeLeaves(employeeId, fromDate, toDate);
				try (Workbook workbook = new XSSFWorkbook()) {
					Sheet sheet = workbook.createSheet("Timesheet Compliance Report");
					createHeaderRow(sheet, fromDate, toDate);
					fillEmployeeData(sheet, employees, attendanceEntities, employeeLeaveEntities, fromDate, toDate,
							holidays);
					workbook.write(out);
				}
			} else {
				try (Workbook workbook = new XSSFWorkbook()) {
					Sheet sheet = workbook.createSheet("Timesheet Effort Report");
					Row row = sheet.createRow(0);
					row.createCell(0).setCellValue("S.No");
					row.createCell(1).setCellValue("Project Code");
					row.createCell(2).setCellValue("Project Name");
					row.createCell(3).setCellValue("Employee Code");
					row.createCell(4).setCellValue("Employee Name");
					row.createCell(5).setCellValue("Date");
					row.createCell(6).setCellValue("Task");
					row.createCell(7).setCellValue("Effort");
					row.createCell(8).setCellValue("Status");

					createBodyForEffortReport(sheet, attendanceEntities, fromDate, toDate, employees);
					workbook.write(out);
				}

			}
			return new ByteArrayResource(out.toByteArray());
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	private Sheet createBodyForEffortReport(Sheet sheet, List<AttendanceEntity> attendanceEntities, LocalDate fromDate,
			LocalDate toDate, List<EmployeeEntity> employees) {

		Map<String, String> projectCodeNameMap = fetchProjectCodeNameMap();

		int serialNo = 1;
		LocalDate date = fromDate;

		while (!date.isAfter(toDate)) {
			for (EmployeeEntity employee : employees) {
				final LocalDate currentDate = date;

				List<AttendanceEntity> entriesForDay = attendanceEntities.stream()
						.filter(att -> att.getTaskDate().equals(currentDate)
								&& att.getEmpBasicDetailId().equals(employee.getEmployeeBasicDetailId()))
						.collect(Collectors.toList());

				for (AttendanceEntity entry : entriesForDay) {
					int hours = entry.getHours();
					int minutes = entry.getMinutes();
					String timeFormatted = String.format("%d.%02d", hours + minutes / 60, minutes % 60);

					String projectCode = entry.getProjectCode();
					String projectName = projectCodeNameMap.getOrDefault(projectCode, "");

					Row row = sheet.createRow(sheet.getLastRowNum() + 1);
					row.createCell(0).setCellValue(serialNo++);
					row.createCell(1).setCellValue(projectCode != null ? projectCode : "");
					row.createCell(2).setCellValue(projectName);
					row.createCell(3).setCellValue(employee.getEmployeeCode());
					row.createCell(4).setCellValue(employee.getFullName());
					row.createCell(5).setCellValue(currentDate.toString());
					row.createCell(6)
							.setCellValue(entry.getTaskMasterEntity() != null
									? entry.getTaskMasterEntity().getTaskDescription()
									: "");
					row.createCell(7).setCellValue(timeFormatted);
					row.createCell(8)
							.setCellValue(entry.getStatusMasterEntity() != null
									? entry.getStatusMasterEntity().getDescription()
									: "");
				}
			}
			date = date.plusDays(1);
		}

		return sheet;
	}

	public List<HolidayModel> fetchHolidays() {
		List<HolidayModel> holidayList = new ArrayList<>();
		try {
			URL url = new URL("http://localhost:8081/pransquare/MasterConfiguration/holiday/getAllHolidays");
			holidayList = restTemplate.exchange(url.toString(), HttpMethod.GET, null,
					new ParameterizedTypeReference<List<HolidayModel>>() {
					}).getBody();
		} catch (Exception e) {
			logger.error("An error occurred while fetching leave types: {}", e.getMessage(), e);
		}
		return holidayList;
	}

	private List<AttendanceEntity> fetchAttendanceRecords(Long employeeId, LocalDate fromDate, LocalDate toDate) {
		List<String> status = new ArrayList<>();
		status.add("101");
		status.add("102");
		if (IntegerUtils.isNotNull(employeeId)) {
			return attendanceRepository.findByEmpBasicDetailIdAndTaskDateBetweenAndStatusIn(employeeId, fromDate,
					toDate, status);
		} else {
			return attendanceRepository.findByTaskDateBetweenAndStatusIn(fromDate, toDate, status);
		}
	}

	private List<EmployeeLeaveEntity> fetchEmployeeLeaves(Long employeeId, LocalDate fromDate, LocalDate toDate) {
		if (IntegerUtils.isNotNull(employeeId)) {
			return employeeLeaveRepository.findOverlappingLeaves(employeeId, fromDate, toDate);
		} else {
			return employeeLeaveRepository.findByStatusAndLeaveFromLessThanEqualAndLeaveToGreaterThanEqual("102",
					toDate, fromDate);
		}
	}

	private List<EmployeeEntity> fetchEmployees(Long employeeId, String workLocationCode) {
		if (IntegerUtils.isNotNull(employeeId)) {
			return employeeRepository.findByEmployeeBasicDetailIdAndStatus(employeeId, "108").stream()

					.filter(a -> !Boolean.TRUE.equals(a.getTestProfile())
							&& !Boolean.TRUE.equals(a.getGenericProfile()))
					.toList();
		} else {
			return employeeRepository.findByStatus("108").stream()
					.filter(a -> workLocationCode.equals(a.getWorkLocationCode())).toList();
		}
	}

	private void createHeaderRow(Sheet sheet, LocalDate fromDate, LocalDate toDate) {
		Row headerRow = sheet.createRow(0);
		Cell headerCell = headerRow.createCell(0);
		headerCell.setCellValue("Employee Name");

		headerCell = headerRow.createCell(1);
		headerCell.setCellValue("Employee Code");

		int colIndex = 2;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
			headerCell = headerRow.createCell(colIndex++);
			headerCell.setCellValue(date.format(formatter));
		}
	}

//	private void fillEmployeeData(Sheet sheet, List<EmployeeEntity> employees,
//			List<AttendanceEntity> attendanceEntities, List<EmployeeLeaveEntity> employeeLeaveEntities,
//			LocalDate fromDate, LocalDate toDate, List<HolidayModel> holidays) {
//		int rowIndex = 1;
//		for (EmployeeEntity employee : employees) {
//			Row row = sheet.createRow(rowIndex++);
//			row.createCell(0).setCellValue(employee.getFullName() != null ? employee.getFullName() : "Unknown");
//			row.createCell(1).setCellValue(employee.getEmployeeCode());
//
//			int colIndex = 2;
//			CellStyle submittedStyle = sheet.getWorkbook().createCellStyle();
//			submittedStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
//			submittedStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//			CellStyle leaveStyle = sheet.getWorkbook().createCellStyle();
//			leaveStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
//			leaveStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//			CellStyle holidayStyle = sheet.getWorkbook().createCellStyle();
//			holidayStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
//			holidayStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//			CellStyle weekendStyle = sheet.getWorkbook().createCellStyle();
//			weekendStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
//			weekendStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//			CellStyle pendingFromManagerStyle = sheet.getWorkbook().createCellStyle();
//			pendingFromManagerStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
//			pendingFromManagerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//
//			for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
//				String status = determineStatus(attendanceEntities, employeeLeaveEntities, employee, date, holidays);
//				Cell cell = row.createCell(colIndex++);
//				cell.setCellValue(status);
//
//				switch (status) {
//				case "Submitted":
//					cell.setCellStyle(submittedStyle);
//					break;
//				case "Leave":
//					cell.setCellStyle(leaveStyle);
//					break;
//				case "Holiday":
//					cell.setCellStyle(holidayStyle);
//					break;
//				case "Weekend":
//					cell.setCellStyle(weekendStyle);
//					break;
//				case "Pending From Manager":
//					cell.setCellStyle(pendingFromManagerStyle);
//					break;
//				default:
//					// No specific style for "Pending"
//					break;
//				}
//			}
//		}
//	}
	
	private void fillEmployeeData(Sheet sheet, List<EmployeeEntity> employees,
	        List<AttendanceEntity> attendanceEntities, List<EmployeeLeaveEntity> employeeLeaveEntities,
	        LocalDate fromDate, LocalDate toDate, List<HolidayModel> holidays) {

	    int rowIndex = 1;
	    for (EmployeeEntity employee : employees) {
	        Row row = sheet.createRow(rowIndex++);
	        row.createCell(0).setCellValue(employee.getFullName() != null ? employee.getFullName() : "Unknown");
	        row.createCell(1).setCellValue(employee.getEmployeeCode());

	        int colIndex = 2;

	        CellStyle submittedStyle = sheet.getWorkbook().createCellStyle();
	        submittedStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
	        submittedStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	        CellStyle leaveStyle = sheet.getWorkbook().createCellStyle();
	        leaveStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
	        leaveStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	        CellStyle holidayStyle = sheet.getWorkbook().createCellStyle();
	        holidayStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
	        holidayStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	        CellStyle weekendStyle = sheet.getWorkbook().createCellStyle();
	        weekendStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
	        weekendStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	        CellStyle pendingFromManagerStyle = sheet.getWorkbook().createCellStyle();
	        pendingFromManagerStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
	        pendingFromManagerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        
	        CellStyle notSubmittedStyle = sheet.getWorkbook().createCellStyle();
	        pendingFromManagerStyle.setFillForegroundColor(IndexedColors.DARK_RED.getIndex());
	        pendingFromManagerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	        for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
	            String status = determineStatus(attendanceEntities, employeeLeaveEntities, employee, date, holidays);
	            Cell cell = row.createCell(colIndex++);

	            if ("Submitted".equals(status)) {
	                // Set the date as string value (formatted as dd-MM-yyyy)
	                cell.setCellValue(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
	                cell.setCellStyle(submittedStyle);
	            } else {
	                cell.setCellValue(status);
	                switch (status) {
	                    case "Leave":
	                        cell.setCellStyle(leaveStyle);
	                        break;
	                    case "Holiday":
	                        cell.setCellStyle(holidayStyle);
	                        break;
	                    case "Weekend":
	                        cell.setCellStyle(weekendStyle);
	                        break;
	                    case "Pending From Manager":
	                        cell.setCellStyle(pendingFromManagerStyle);
	                        break;
	                    case "Not Entered":
	                    	cell.setCellStyle(notSubmittedStyle);
	                    default:
	                        // No specific style for "Pending"
	                        break;
	                }
	            }
	        }
	    }
	}


	private String determineStatus(List<AttendanceEntity> attendanceEntities, List<EmployeeLeaveEntity> leaveEntities,
			EmployeeEntity employee, LocalDate date, List<HolidayModel> holidays) {
		for (AttendanceEntity attendance : attendanceEntities) {
			if (attendance.getEmpBasicDetailId().equals(employee.getEmployeeBasicDetailId())
					&& attendance.getTaskDate().equals(date)) {
				if ("102".equals(attendance.getStatus())) {
					return "Submitted";
				} else if ("101".equals(attendance.getStatus())) {
					return "Pending From Manager";
				}
			}

		}
		for (EmployeeLeaveEntity leave : leaveEntities) {
			if (leave.getEmployeeId().equals(employee.getEmployeeBasicDetailId())
					&& (leave.getLeaveFrom().equals(date) || leave.getLeaveTo().equals(date)
							|| (leave.getLeaveFrom().isBefore(date) && leave.getLeaveTo().isAfter(date)))) {
				return "Leave";
			}
		}
		if (holidays.stream().anyMatch(a -> a.getWorkLocationCode().equals(employee.getWorkLocationCode())
				&& a.getHolidayDate().equals(date))) {
			return "Holiday";
		}
		if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
			return "Weekend";
		}
		return "Not Submitted";
	}

	public ByteArrayResource generateManagerTimesheetReport(Long employeeId, LocalDate fromDate, LocalDate toDate,
			List<String> workLocationCodes, Long managerId) throws IOException {
		try {
			// Fetch employees based on managerId from UserApproverConfigurationsEntity
			List<EmployeeEntity> employees = fetchEmployeesForManager(employeeId, workLocationCodes, managerId);
			if (employees.isEmpty()) {
				throw new ResourceNotFoundException("No employees found for the given manager or filters");
			}

			// Fetch attendance records for all relevant employees
			List<AttendanceEntity> attendanceEntities = fetchAttendanceRecordsForManager(employeeId, fromDate, toDate,
					employees);
			List<HolidayModel> holidays = fetchHolidays();
			List<EmployeeLeaveEntity> employeeLeaveEntities = fetchEmployeeLeavesForManager(employeeId, fromDate,
					toDate, employees);

			// Generate Excel
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try (Workbook workbook = new XSSFWorkbook()) {
				Sheet sheet = workbook.createSheet("Manager Timesheet Report");
				createHeaderRow(sheet, fromDate, toDate);
				fillEmployeeData(sheet, employees, attendanceEntities, employeeLeaveEntities, fromDate, toDate,
						holidays);

				// Auto-size columns
				for (int i = 0; i <= (toDate.toEpochDay() - fromDate.toEpochDay() + 2); i++) {
					sheet.autoSizeColumn(i);
				}

				workbook.write(out);
			}

			return new ByteArrayResource(out.toByteArray());
		} catch (Exception e) {
			logger.error("Error generating manager timesheet report: {}", e.getMessage(), e);
			throw new IOException("Failed to generate manager timesheet report: " + e.getMessage());
		}
	}

	private List<EmployeeEntity> fetchEmployeesForManager(Long employeeId, List<String> workLocationCodes,
			Long managerId) {
		List<EmployeeEntity> employees;

		if (IntegerUtils.isNotNull(employeeId)) {
			employees = employeeRepository.findByEmployeeBasicDetailIdAndStatus(employeeId, "108").stream()

					.filter(e -> !Boolean.TRUE.equals(e.getTestProfile())
							&& !Boolean.TRUE.equals(e.getGenericProfile()))
					.toList();

		} else {
			List<UserApproverConfigurationsEntity> approverConfigs = approverConfigRepository
					.findByApproverIdAndModuleName(managerId, "timesheet");

			List<Long> employeeIds = approverConfigs.stream().map(UserApproverConfigurationsEntity::getEmpBasicDetailId)
					.distinct().toList();

			employees = employeeRepository.findByEmployeeBasicDetailIdsAndStatus(employeeIds, "108").stream()

					.filter(e -> !Boolean.TRUE.equals(e.getTestProfile())
							&& !Boolean.TRUE.equals(e.getGenericProfile()))
					.toList();

			if (workLocationCodes != null && !workLocationCodes.isEmpty()) {
				employees = employees.stream().filter(e -> workLocationCodes.contains(e.getWorkLocationCode()))
						.toList();
			}
		}

		return employees;
	}

	private List<AttendanceEntity> fetchAttendanceRecordsForManager(Long employeeId, LocalDate fromDate,
			LocalDate toDate, List<EmployeeEntity> employees) {
		List<String> status = new ArrayList<>();
		status.add("101");
		status.add("102");
		status.add("103");

		if (IntegerUtils.isNotNull(employeeId)) {
			return attendanceRepository.findByEmpBasicDetailIdAndTaskDateBetweenAndStatusIn(employeeId, fromDate,
					toDate, status);
		} else {
			List<Long> employeeIds = employees.stream().map(EmployeeEntity::getEmployeeBasicDetailId).toList();
			return attendanceRepository.findByEmpBasicDetailIdInAndTaskDateBetweenAndStatusIn(employeeIds, fromDate,
					toDate, status);
		}
	}

	private List<EmployeeLeaveEntity> fetchEmployeeLeavesForManager(Long employeeId, LocalDate fromDate,
			LocalDate toDate, List<EmployeeEntity> employees) {
		if (IntegerUtils.isNotNull(employeeId)) {
			return employeeLeaveRepository
					.findByEmployeeIdAndLeaveFromLessThanEqualAndLeaveToGreaterThanEqual(employeeId, toDate, fromDate);
		} else {
			List<Long> employeeIds = employees.stream().map(EmployeeEntity::getEmployeeBasicDetailId).toList();
			return employeeLeaveRepository.findByEmployeeIdInAndLeaveFromLessThanEqualAndLeaveToGreaterThanEqual(
					employeeIds, toDate, fromDate);
		}
	}

	public Page<AttendanceEntity> managerTimesheetGrid(AttendanceDetailsSearchModel attendanceDetailsSearchModel)
			throws IOException {
		try {
			List<EmployeeEntity> employees = fetchEmployeesForManager(
					attendanceDetailsSearchModel.getEmpBasicDetailId(), attendanceDetailsSearchModel.getWorklocation(),
					attendanceDetailsSearchModel.getApproverId());

			if (employees.isEmpty()) {
				throw new ResourceNotFoundException("No employees found for the given manager or filters");
			}

			// Construct Pageable
			int page = attendanceDetailsSearchModel.getPage();
			int size = attendanceDetailsSearchModel.getSize();
			Pageable pageable = PageRequest.of(page, size, Sort.by("taskDate").descending());

			// Extract date range
			LocalDate fromDate = attendanceDetailsSearchModel.getFromDate();
			LocalDate toDate = attendanceDetailsSearchModel.getToDate();

			// Assuming fetchAttendanceRecordsForManagerGrid is updated to return
			// Page<AttendanceEntity>
			return fetchAttendanceRecordsForManagerGrid(attendanceDetailsSearchModel.getEmpBasicDetailId(), fromDate,
					toDate, employees, pageable);
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException(e.getMessage());
		} catch (Exception e) {
			logger.error("Error generating manager timesheet report: {}", e.getMessage(), e);
			throw new IOException("Failed to fetch data: " + e.getMessage());
		}
	}

	private Page<AttendanceEntity> fetchAttendanceRecordsForManagerGrid(Long employeeId, LocalDate fromDate,
			LocalDate toDate, List<EmployeeEntity> employees, Pageable pageable) {
		List<String> status = new ArrayList<>();
		status.add("101");
		status.add("102");
		status.add("103");
		if (IntegerUtils.isNotNull(employeeId)) {
			return attendanceRepository.findByEmpBasicDetailIdAndTaskDateBetweenAndStatusIn(employeeId, fromDate,
					toDate, status, pageable);
		} else {
			List<Long> employeeIds = employees.stream().map(EmployeeEntity::getEmployeeBasicDetailId).toList();
			return attendanceRepository.findByEmpBasicDetailIdInAndTaskDateBetweenAndStatusIn(employeeIds, fromDate,
					toDate, status, pageable);
		}
	}

	public Map<String, String> fetchProjectCodeNameMap() {
		String query = "SELECT PROJECT_CODE, PROJECT_NAME FROM masterconfigurationprod.project_master";
		List<Object[]> resultList = entityManager.createNativeQuery(query).getResultList();

		return resultList.stream().filter(row -> row[0] != null && row[1] != null)
				.collect(Collectors.toMap(row -> row[0].toString(), row -> row[1].toString()));
	}
	public ByteArrayResource generateTimesheetComplianceReportNew(Long employeeId, LocalDate fromDate, LocalDate toDate,
			List<String> workLocationCode, String type) throws IOException {
		try {
			List<EmployeeEntity> employees = fetchEmployeesNew(employeeId, workLocationCode);
			if (employees.isEmpty()) {
				throw new ResourceNotFoundException("No data found for the given date range or employee status");
			}
			List<AttendanceEntity> attendanceEntities = fetchAttendanceRecords(employeeId, fromDate, toDate);
			List<HolidayModel> holidays = new ArrayList<>();
			List<EmployeeLeaveEntity> employeeLeaveEntities = new ArrayList<>();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			if (type.equals("compliance")) {
				holidays = fetchHolidays();
				employeeLeaveEntities = fetchEmployeeLeaves(employeeId, fromDate, toDate);
				try (Workbook workbook = new XSSFWorkbook()) {
					Sheet sheet = workbook.createSheet("Timesheet Compliance Report");
					createHeaderRow(sheet, fromDate, toDate);
					fillEmployeeData(sheet, employees, attendanceEntities, employeeLeaveEntities, fromDate, toDate,
							holidays);
					workbook.write(out);
				}
			} else {
				try (Workbook workbook = new XSSFWorkbook()) {
					Sheet sheet = workbook.createSheet("Timesheet Effort Report");
					Row row = sheet.createRow(0);
					row.createCell(0).setCellValue("S.No");
					row.createCell(1).setCellValue("Project Code");
					row.createCell(2).setCellValue("Project Name");
					row.createCell(3).setCellValue("Employee Code");
					row.createCell(4).setCellValue("Employee Name");
					row.createCell(5).setCellValue("Date");
					row.createCell(6).setCellValue("Task");
					row.createCell(7).setCellValue("Effort");
					row.createCell(8).setCellValue("Status");

					createBodyForEffortReport(sheet, attendanceEntities, fromDate, toDate, employees);
					workbook.write(out);
				}

			}
			return new ByteArrayResource(out.toByteArray());
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}
	private List<EmployeeEntity> fetchEmployeesNew(Long employeeId, List<String> workLocationCodes) {
	    if (IntegerUtils.isNotNull(employeeId)) {
	        return employeeRepository.findByEmployeeBasicDetailIdAndStatus(employeeId, "108").stream()
	                .filter(a -> !Boolean.TRUE.equals(a.getTestProfile())
	                        && !Boolean.TRUE.equals(a.getGenericProfile()))
	                .toList();
	    } else {
	        return employeeRepository.findByStatus("108").stream()
	                .filter(a -> workLocationCodes.contains(a.getWorkLocationCode()))
	                .toList();
	    }
	}
}
