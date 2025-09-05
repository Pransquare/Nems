package com.pransquare.nems.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.entities.ExpenseDetails;
import com.pransquare.nems.entities.ExpenseEntity;
import com.pransquare.nems.entities.GroupEmailEntity;
import com.pransquare.nems.entities.UserApproverConfigurationsEntity;
import com.pransquare.nems.exceptions.ResourceNotFoundException;
import com.pransquare.nems.models.ApproverSearchModel;
import com.pransquare.nems.models.ExpenseUpdateModal;
import com.pransquare.nems.repositories.EmployeeRepository;
import com.pransquare.nems.repositories.ExpenseDetailsRepository;
import com.pransquare.nems.repositories.ExpenseRepository;
import com.pransquare.nems.repositories.GroupEmailRepository;
import com.pransquare.nems.utils.PageableResponse;
import com.pransquare.nems.utils.StringUtil;

import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Service
public class ExpenseService {

    @Value("${billsfilepath}")
    private String billsFilePath;

    @Value("${timesheetEmailId}")
    private String mailId;

    @Value("${app.path}")
    private String appPath;

    private final ExpenseRepository expenseRepository;
    private final EntityManager entityManager;
    private static final Logger logger = LogManager.getLogger(ExpenseService.class);
    private final ExpenseDetailsRepository expenseDetailsRepository;
    private final JavaMailSender javaMailSender;
    private final ApproverConfigService approverConfigService;
    private final GroupEmailRepository groupEmailRepository;
    private final EmployeeRepository employeeRepository;

    ExpenseService(ExpenseRepository expenseRepository, EntityManager entityManager,
            ExpenseDetailsRepository expenseDetailsRepository, JavaMailSender javaMailSender,
            ApproverConfigService approverConfigService, GroupEmailRepository groupEmailRepository,
            EmployeeRepository employeeRepository) {
        this.expenseRepository = expenseRepository;
        this.entityManager = entityManager;
        this.expenseDetailsRepository = expenseDetailsRepository;
        this.javaMailSender = javaMailSender;
        this.approverConfigService = approverConfigService;
        this.groupEmailRepository = groupEmailRepository;
        this.employeeRepository = employeeRepository;
    }

    // Save or update expense
    public ExpenseEntity saveOrUpdateExpenses(ExpenseEntity entity) {
        try {
            ExpenseEntity result = expenseRepository.save(entity);
            emailSendingHandler(result);
            return result;
        } catch (Exception e) {
            logger.error("Error while saving expense:", e.fillInStackTrace());
            throw new ResourceNotFoundException("Error while saving expense:", e.fillInStackTrace());
        }
    }

    // Get all expenses by employeeId
    public List<ExpenseEntity> getAllExpenses(Long employeeId) {
        try {
            return expenseRepository.findByEmployeeId(employeeId);
        } catch (Exception e) {
            logger.error("Error while fetching expenses:", e.fillInStackTrace());
            throw new ResourceNotFoundException("Error while fetching expenses:");
        }
    }

    // Upload bill (file handling logic)
    public List<String> uploadBill(List<MultipartFile> fileList, String employeeCode, String type) throws IOException {
        List<String> result = new ArrayList<>();
        for (MultipartFile file : fileList) {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("Uploaded file is empty.");
            }

            // Create employee-specific directory if it doesn't exist
            Path employeeDir = Paths.get(billsFilePath + employeeCode);
            if (!Files.exists(employeeDir)) {
                Files.createDirectories(employeeDir); // Creates directory if it doesn't exist
            }

            // Create a unique file name by appending a timestamp to the original file name
            String originalFileName = file.getOriginalFilename();
            String fileNameWithPrefix = employeeCode + "_" + System.currentTimeMillis() + "_" + originalFileName;

            // Define the file path within the employee's directory
            Path filePath = employeeDir.resolve(fileNameWithPrefix);

            // Save the file
            file.transferTo(filePath.toFile());

            result.add(filePath.toString());
        }
        if (Boolean.TRUE.equals(StringUtil.isNotNull(type)) && type.equals("profilePic")) {
            // Update employee profile picture
            EmployeeEntity employeeEntity = employeeRepository.findByEmployeeCode(employeeCode);
            employeeEntity.setProfilePicPath(result.get(0));
            employeeRepository.save(employeeEntity);
        }
        return result;
    }

    public boolean removeBillUrl(String employeeCode) {
        EmployeeEntity employeeEntity = employeeRepository.findByEmployeeCode(employeeCode);
        if (employeeEntity != null) {
            employeeEntity.setProfilePicPath(null);
            employeeRepository.save(employeeEntity);
            return true;
        } else {
            return false;
        }
    }

    public PageableResponse<ExpenseEntity> getExpensesWithFilters(Integer employeeId, Integer managerId,
            List<String> status,
            Pageable pageable) {
        // Construct the base query with filters
        StringBuilder queryBuilder = new StringBuilder("SELECT e FROM ExpenseEntity e WHERE 1=1");

        // List for holding parameters dynamically
        Map<String, Object> params = new HashMap<>();

        if (employeeId != null) {
            queryBuilder.append(" AND e.employeeId = :employeeId");
            params.put("employeeId", employeeId);
        }

        if (managerId != null) {
            queryBuilder.append(" AND e.managerId = :managerId");
            params.put("managerId", managerId);
        }

        if (status != null && !status.isEmpty()) {
            queryBuilder.append(" AND e.status in :status");
            params.put("status", status);
        }

        // Add sorting dynamically
        if (pageable.getSort().isSorted()) {
            queryBuilder.append(" ORDER BY ");
            List<String> sortOrders = pageable.getSort().stream()
                    .map(order -> "e." + order.getProperty() + " " + (order.isAscending() ? "ASC" : "DESC"))
                    .toList();
            queryBuilder.append(String.join(", ", sortOrders));
        }

        // Create query
        TypedQuery<ExpenseEntity> query = entityManager.createQuery(queryBuilder.toString(), ExpenseEntity.class);

        // Set parameters dynamically
        params.forEach(query::setParameter);

        // Handle pagination
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // Fetch results
        List<ExpenseEntity> resultList = query.getResultList();

        // Count total items (without pagination)
        TypedQuery<Long> countQuery = entityManager.createQuery(
                queryBuilder.toString().replace("SELECT e", "SELECT COUNT(e)"), Long.class);
        params.forEach(countQuery::setParameter);
        long total = countQuery.getSingleResult();

        // Wrap in PageableResponse
        return new PageableResponse<>(resultList, total, pageable.getPageNumber(), pageable.getPageSize());
    }

    public Optional<ExpenseEntity> getExpenseById(Integer id) {
        return expenseRepository.findById(id);
    }

    public ExpenseEntity updateExpense(ExpenseUpdateModal updateModal) {
        try {
            Optional<ExpenseEntity> expense = expenseRepository.findById(updateModal.getId());
            if (expense.isPresent()) {
                expense.get().setStatus(updateModal.getStatus());
                expense.get().setComments(updateModal.getComments());
                expense.get().setApprovedBy(updateModal.getUser());
                emailSendingHandler(expense.get());
                expenseRepository.save(expense.get());
            }
            return expense.get();
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getCause());
        }
    }

    public ExpenseDetails updateBillStatus(ExpenseUpdateModal updateModal) {
        try {
            Optional<ExpenseDetails> expenseDetails = expenseDetailsRepository.findById(updateModal.getId());
            if (expenseDetails.isPresent()) {
                expenseDetails.get().setStatus(updateModal.getStatus());
                expenseDetails.get().setComments(updateModal.getComments());
                expenseDetails.get().setModifiedBy(updateModal.getUser());
                expenseDetails.get().setModifiedDate(LocalDateTime.now());
                expenseDetailsRepository.save(expenseDetails.get());
            }
            return expenseDetails.get();
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getCause());
        }
    }

    public String emailSendingHandler(ExpenseEntity expenseEntity) {
        try {
            // Initialize subject and body
            String name = employeeRepository.findById(Long.valueOf(expenseEntity.getEmployeeId())).get().getFullName();
            String subject = "Expense Approval Request";
            String body = "";
            String recipientMailId = "";
            ApproverSearchModel approverSearchModel = new ApproverSearchModel();
            approverSearchModel.setEmployeeBasicDetailId(Long.valueOf(expenseEntity.getEmployeeId()));
            UserApproverConfigurationsEntity userApproverConfigurationsEntity;
            GroupEmailEntity groupEmailEntity;

            // Determine the recipient and email content based on the status
            switch (expenseEntity.getStatus()) {
                case "113":
                    // To manager
                    approverSearchModel.setModuleName(EmployeeService.MODULE_APPROVAL_JOBMANAGER);
                    userApproverConfigurationsEntity = approverConfigService
                            .getApproverByEmpIdAndModule(approverSearchModel);
                    recipientMailId = userApproverConfigurationsEntity.getApproverEmailId();
                    body = "<html><body>" +
                            "Dear " + userApproverConfigurationsEntity.getApproverName() + ",<br><br>" +
                            "An expense request has been submitted by " + name + " for your approval.<br><br>" +
                            "<b>Expense Details</b><br>" +
                            "Employee Name: " + name + "<br>" +
                            "Expense Name: " + expenseEntity.getExpenseName() + "<br>" +
                            "Amount: " + expenseEntity.getExpenseAmount() + "<br><br>" +
                            "Please review and take the necessary action." +
                            "</body></html>";
                    break;

                case "121":
                    // To Accounts
                    groupEmailEntity = groupEmailRepository.findByGroupName("accounts");
                    recipientMailId = groupEmailEntity.getEmailId();
                    body = "<html><body>" +
                            "Dear Accounts Team,<br><br>" +
                            "An expense request has been approved by the Manager and is forwarded to you for your verification.<br><br>"
                            +
                            "<b>Expense Details</b><br>" +
                            "Employee Name: " + name + "<br>" +
                            "Expense Name: " + expenseEntity.getExpenseName() + "<br>" +
                            "Amount: " + expenseEntity.getExpenseAmount() + "<br><br>" +
                            "Please review and take the necessary action." +
                            "</body></html>";
                    break;

                case "122":
                    // To Management
                    groupEmailEntity = groupEmailRepository.findByGroupName("management");
                    recipientMailId = groupEmailEntity.getEmailId();
                    body = "<html><body>" +
                            "Dear Management,<br><br>" +
                            "An expense request is forwarded to you for further verify and approval.<br><br>" +
                            "<b>Expense Details</b><br>" +
                            "Employee Name: " + name + "<br>" +
                            "Expense Name: " + expenseEntity.getExpenseName() + "<br>" +
                            "Amount: " + expenseEntity.getExpenseAmount() + "<br><br>" +
                            "</body></html>";
                    break;

                case "123":
                    // To Finance
                    groupEmailEntity = groupEmailRepository.findByGroupName("finance");
                    recipientMailId = groupEmailEntity.getEmailId();
                    body = "<html><body>" +
                            "Dear Finance Team,<br><br>" +
                            "An expense request has been approved by the management and is forwarded to you for processing.<br><br>"
                            +
                            "<b>Expense Details</b><br>" +
                            "Employee Name: " + name + "<br>" +
                            "Expense Name: " + expenseEntity.getExpenseName() + "<br>" +
                            "Amount: " + expenseEntity.getExpenseAmount() + "<br><br>" +
                            "</body></html>";
                    break;

                case "124":
                    // Sent back to Employee
                    recipientMailId = expenseEntity.getEmailId();
                    body = "<html><body>" +
                            "Dear " + expenseEntity.getEmpoyeeName() + ",<br><br>" +
                            "Your expense request has been sent back for additional information or corrections.<br><br>"
                            +
                            "<b>Expense Details</b><br>" +
                            "Expense Name: " + expenseEntity.getExpenseName() + "<br>" +
                            "Amount: " + expenseEntity.getExpenseAmount() + "<br><br>" +
                            "Please update the required details and resubmit." +
                            "</body></html>";
                    break;

                case "102":
                    // Approved by Finance
                    subject = expenseEntity.getExpenseName() + " Expense Approved";
                    recipientMailId = expenseEntity.getEmailId();
                    body = "<html><body>" +
                            "Dear " + expenseEntity.getEmpoyeeName() + ",<br><br>" +
                            "Your expense request has been approved. The amount will be credited in the next month's salary.<br><br>"
                            +
                            "<b>Expense Details</b><br>" +
                            "Expense Name: " + expenseEntity.getExpenseName() + "<br>" +
                            "Amount: " + expenseEntity.getExpenseAmount() + "<br><br>" +
                            "Thank you." +
                            "</body></html>";
                    break;

                case "103":
                    // Rejected
                    subject = expenseEntity.getExpenseName() + " Expense Rejected";
                    recipientMailId = expenseEntity.getEmailId();
                    body = "<html><body>" +
                            "Dear " + expenseEntity.getEmpoyeeName() + ",<br><br>" +
                            "Your expense request has been rejected.<br><br>" +
                            "<b>Expense Details</b><br>" +
                            "Expense Name: " + expenseEntity.getExpenseName() + "<br>" +
                            "Amount: " + expenseEntity.getExpenseAmount() + "<br><br>" +
                            "Please contact your manager for further details." +
                            "</body></html>";
                    break;

                default:
                    throw new IllegalArgumentException("Invalid status: " + expenseEntity.getStatus());
            }

            body += "\n<a href='http://103.77.26.221/home/finance/expense/submission/" + expenseEntity.getId()
                    + "'>Click here to view expense details</a>";
            // Send the email (make sure you are using HTML content type)
            return emailProcessing(recipientMailId, subject, body);

        } catch (Exception e) {
            throw new ResourceNotFoundException("Error processing email for expense entity: " + expenseEntity.getId(),
                    e);
        }
    }

    public String emailProcessing(String recipientMailId, String subject, String text) {
        try {
            // Create a MimeMessage
            MimeMessage message = javaMailSender.createMimeMessage();

            // Use MimeMessageHelper to handle the email content
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true indicates it's an HTML email
            helper.setTo(recipientMailId); // Dynamic recipient
            helper.setSubject(subject); // Step-specific subject
            helper.setText(text, true); // Email content with HTML support
            helper.setFrom(mailId);

            // Send the email
            javaMailSender.send(message);

            return "Email sent successfully to " + recipientMailId;
        } catch (Exception e) {
            // Log the error and rethrow
            e.printStackTrace();
            throw new ResourceNotFoundException("Failed to send email to recipient: " + recipientMailId, e);
        }
    }
}
