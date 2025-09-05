package com.pransquare.nems.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.entities.AttendanceEntity;
import com.pransquare.nems.models.AttendanceDetailsSearchModel;
import com.pransquare.nems.models.ProjectReportModel;
import com.pransquare.nems.services.ReportsService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/Pransquare/nems/reports")
public class ReportsController {

    private ReportsService reportsService;

    ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    @PostMapping("/projectReport")//over
    public ResponseEntity<InputStreamResource> postMethodName(
            @Valid @RequestBody ProjectReportModel projectReportModel) {
        try {
            // Generate the Excel file
            InputStream stream = reportsService.generateExcelForProjectReport(projectReportModel);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=project_report.xlsx");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(stream));
        } catch (Exception e) {
            // Handle exception (e.g., log it)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/expense-report")//over
    public ResponseEntity<ByteArrayResource> downloadExpenseReport(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = true) LocalDate date) throws IOException {
        // Generate Excel file
        ByteArrayResource excelResource = reportsService.generateExpenseReport(employeeId, date);

        // Prepare response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=expense_report.xlsx");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(excelResource.contentLength())
                .body(excelResource);
    }

    @GetMapping("/attendance-compliance-effort-report")
    public ResponseEntity<ByteArrayResource> downloadAttendanceComplianceReport(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = true) String workLocationCode,
            @RequestParam(required = true) String type) throws IOException {
        // Generate Excel file
        ByteArrayResource excelResource;
        try {
            excelResource = reportsService.generateTimesheetComplianceReport(employeeId, fromDate, toDate,
                    workLocationCode, type);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Prepare response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attendance_compliance_report.xlsx");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelResource);
    }

    @GetMapping("/manager-timesheet-report")
    public ResponseEntity<ByteArrayResource> downloadManagerTimesheetReport(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) List<String> workLocationCodes,
            @RequestParam(required = true) Long managerId) {

        try {
            // Call the service to generate the Excel report
            ByteArrayResource resource = reportsService.generateManagerTimesheetReport(
                    employeeId, fromDate, toDate, workLocationCodes, managerId);

            // Set up HTTP headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "manager_timesheet_report.xlsx");
            headers.setContentLength(resource.contentLength());

            // Return the response with the Excel file
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (IOException e) {
            // Handle any IO exceptions during report generation
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/managerTimesheetGrid")
    public ResponseEntity<Page<AttendanceEntity>> managerTimesheetGrid(@RequestBody AttendanceDetailsSearchModel attendanceDetailsSearchModel) throws IOException {
    	
    		Page<AttendanceEntity> resource = reportsService.managerTimesheetGrid(attendanceDetailsSearchModel);
    		
    		return ResponseEntity.ok()
    				.body(resource);
    		

    }
    @GetMapping("/attendance-compliance-effort-reportNew")
    public ResponseEntity<ByteArrayResource> downloadAttendanceComplianceReportNew(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = true) List<String> workLocationCodes,
            @RequestParam(required = true) String type) throws IOException {
        // Generate Excel file
        ByteArrayResource excelResource;
        try {
            excelResource = reportsService.generateTimesheetComplianceReportNew(employeeId, fromDate, toDate,
                    workLocationCodes, type);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Prepare response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attendance_compliance_report.xlsx");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelResource);
    }

}
