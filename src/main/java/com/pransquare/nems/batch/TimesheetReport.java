package com.pransquare.nems.batch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pransquare.nems.entities.AttendanceEntity;
import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.models.ProjectAndClientDTO;
import com.pransquare.nems.services.PayrollService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimesheetReport {

    private static final String TIME_FORMAT = "%02d:%02d";
    private static final Logger logger = LogManager.getLogger(TimesheetReport.class);

    @Value("${payrollfilepath}")
    private String uploadDirectory;

    @Value("${company.logo}")
    private String companyLogoPath;

    @Autowired
    private PayrollService payrollService;

    public String generatePdfReport(List<AttendanceEntity> attendanceList, List<LocalDate> dates,
            List<ProjectAndClientDTO> projects, String workLocationCode,
            EmployeeEntity employee, String month) {
        String filepath = createFilePath(employee);

        try (OutputStream outputStream = new FileOutputStream(filepath)) {
            // Generate the HTML content without footer
            String htmlContent = generateHtmlContent(attendanceList, dates, projects, workLocationCode, employee,
                    month);

            // Convert HTML to PDF
            payrollService.convertHtmlToPdf(htmlContent, filepath);

            // Add logo to the bottom of every page in the PDF
            addLogoToPdf(filepath);

            logger.info("PDF file created successfully at: {}", filepath);
        } catch (Exception e) {
            logger.error("Failed to create PDF report: {}", filepath, e);
            return null;
        }

        return filepath;
    }

    private void addLogoToPdf(String filepath) {
        try (PDDocument document = PDDocument.load(new File(filepath))) {
            for (PDPage page : document.getPages()) {
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page,
                        PDPageContentStream.AppendMode.APPEND, true, true)) {
                    // Load the company logo
                    PDImageXObject logo = PDImageXObject.createFromFile(companyLogoPath, document);

                    float y = 10;
                    float logoWidth = logo.getWidth() / 4;
                    float x = page.getMediaBox().getWidth() - logoWidth - 30;
                    float logoHeight = logo.getHeight() / 4;

                    contentStream.drawImage(logo, x, y, logoWidth, logoHeight);
                }
            }
            document.save(filepath);
        } catch (IOException e) {
            logger.error("Failed to add logo to PDF pages", e);
        }
    }

    private String createFilePath(EmployeeEntity employee) {
        return uploadDirectory + employee.getEmployeeCode() + "_Employee_Time_Sheet_" + System.currentTimeMillis()
                + ".pdf";
    }

    private String generateHtmlContent(List<AttendanceEntity> attendanceList, List<LocalDate> dates,
            List<ProjectAndClientDTO> projects, String workLocationCode,
            EmployeeEntity employee, String month) {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<html><head><style>")
                .append(getStyles())
                .append("</style></head><body>")
                .append("<h1>Employee Time Sheet</h1>")
                .append(employee.getWorkType().equals("permanent") ? "<p>Employee Name: " : "<p>Contractor Name:")
                .append(employee.getFullName()).append("<br />")
                .append("Client Name: ").append(getClientDetails(projects)).append("<br />")
                .append("Project ID: ").append(getProjectDetails(projects)).append("<br />")
                .append("Month: ").append(month).append("</p>")
                .append("<div class='table-container'>")
                .append(buildTableContent(attendanceList, dates))
                .append("</div>") // End of table content
                .append("</body></html>"); // End HTML

        return htmlContent.toString();
    }

    private String getStyles() {
        return "@page { margin: 10mm 10mm 10mm 10mm; size: A4; }" // Adjust bottom margin
                + "body { font-family: Arial, sans-serif; margin: 0; padding: 0;} "
                + "h1 { text-align: center; margin: 0; font-size: 20px; } "
                + "p { font-size: 12px; margin: 5px 0; } "
                + "table { width: 100%; border-collapse: collapse; margin-top: 10px; margin-bottom: 100px; page-break-inside:auto} "
                + "th, td { border: 1px solid #000; padding: 6px; text-align: center; font-size: 10px; } "
                + "th { background-color: #f2f2f2; } "
                + "tr { page-break-inside: avoid; }";
    }

    private int calculateTotalBillableMinutes(List<AttendanceEntity> attendanceList) {
        return attendanceList.stream()
                .mapToInt(attendance -> attendance.getHours() * 60 + attendance.getMinutes())
                .sum();
    }

    private String buildTableContent(List<AttendanceEntity> attendanceList, List<LocalDate> dates) {
        StringBuilder tableContent = new StringBuilder("<table class='table'>")
                .append("<tr><th>Date</th><th>Day</th><th>Billable Hrs</th><th>Task</th><th>Task Description</th><th>Status</th></tr>");

        for (LocalDate date : dates) {
            List<AttendanceEntity> entriesForDate = attendanceList.stream()
                    .filter(attendance -> attendance.getTaskDate().equals(date))
                    .toList();

            if (entriesForDate.isEmpty()) {
                tableContent.append(buildEmptyRow(date));
            } else {
                entriesForDate.forEach(attendance -> tableContent.append(buildDataRow(date, attendance)));
            }
        }
        int totalBillableMinutes = calculateTotalBillableMinutes(attendanceList);
        tableContent
                .append("<tfoot>")
                .append(buildTotalBillableHoursRow(totalBillableMinutes))
                .append("</tfoot>");

        return tableContent.append("</table>").toString();
    }

    private String buildEmptyRow(LocalDate date) {
        return new StringBuilder()
                .append("<tr>")
                .append("<td>").append(date).append("</td>")
                .append("<td>").append(date.getDayOfWeek()).append("</td>")
                .append("<td>0</td>")
                .append(date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                        ? "<td>Weekend</td>"
                        : "<td></td>")
                .append("<td></td><td></td>")
                .append("</tr>")
                .toString();
    }

    private String buildDataRow(LocalDate date, AttendanceEntity attendance) {
        int totalMinutes = attendance.getHours() * 60 + attendance.getMinutes();
        return new StringBuilder()
                .append("<tr>")
                .append("<td>").append(date).append("</td>")
                .append("<td>").append(date.getDayOfWeek()).append("</td>")
                .append("<td>").append(formatTime(totalMinutes)).append("</td>")
                .append("<td>").append(attendance.getTaskMasterEntity().getTaskDescription()).append("</td>")
                .append("<td>").append(attendance.getComments()).append("</td>")
                .append("<td>").append(attendance.getStatusMasterEntity().getDescription()).append("</td>")
                .append("</tr>")
                .toString();
    }

    private String buildTotalBillableHoursRow(int totalBillableMinutes) {
        return new StringBuilder()
                .append("<tr>")
                .append("<td colspan='2' style='text-align: right;'><strong>Total Billable Hours:</strong></td>")
                .append("<td colspan='4'><strong>").append(formatTime(totalBillableMinutes)).append("</strong></td>")
                .append("</tr>")
                .toString();
    }

    private String formatTime(int totalMinutes) {
        return String.format(TIME_FORMAT, totalMinutes / 60, totalMinutes % 60);
    }

    private String getClientDetails(List<ProjectAndClientDTO> projects) {
        return projects.stream()
                .map(ProjectAndClientDTO::getClientName)
                .distinct()
                .collect(Collectors.joining(", "));
    }

    private String getProjectDetails(List<ProjectAndClientDTO> projects) {
        return projects.stream()
                .map(ProjectAndClientDTO::getProjectCode)
                .distinct()
                .collect(Collectors.joining(", "));
    }
}
