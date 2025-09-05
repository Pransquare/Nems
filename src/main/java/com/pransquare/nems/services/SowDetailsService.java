package com.pransquare.nems.services;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pransquare.nems.entities.SowDetails;
import com.pransquare.nems.models.SowInputDTO;
import com.pransquare.nems.models.SowSearchInputModel;
import com.pransquare.nems.repositories.SowDetailsRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class SowDetailsService {

	 public static final String STATUS_ACTIVE = "Active";
    private SowDetailsRepository repository;
    
    public SowDetailsService(SowDetailsRepository repository) {
		this.repository = repository;
	}

	@PersistenceContext
    private EntityManager entityManager;

    public Page<SowDetails> searchSowsByCriteria(SowSearchInputModel searchInput) {
        Pageable pageable = PageRequest.of(searchInput.getPage(), searchInput.getSize());
        
        String status = (searchInput.getStatus() != null && !searchInput.getStatus().isEmpty()) ? searchInput.getStatus() : null;  

        String milestoneMonth = (searchInput.getMilestoneMonth() != null && !searchInput.getMilestoneMonth().isEmpty()) ? searchInput.getMilestoneMonth() : null;
        
        String account = (searchInput.getAccount() != null && !searchInput.getAccount().isEmpty()) ? searchInput.getAccount() : null;
        
        String sowId = (searchInput.getSowId() != null && !searchInput.getSowId().isEmpty()) ? searchInput.getSowId() : null;

        return repository.searchSowDetails(
                account,
                milestoneMonth,
                status,
                searchInput.getManagerId(),
                sowId,
                pageable
        );
    }

    public SowDetails createSow(SowDetails sowDetails) {
        sowDetails.setStatus(STATUS_ACTIVE);
        return repository.save(sowDetails);
    }
    

    public SowDetails updateSow(Long id, SowDetails sowDetails) {
        SowDetails existingSow = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SOW not found"));
        
        // Update fields
        existingSow.setAccount(sowDetails.getAccount());
        existingSow.setSowId(sowDetails.getSowId());
        existingSow.setSowName(sowDetails.getSowName());
        existingSow.setPo(sowDetails.getPo());
        existingSow.setMilestoneMonth(sowDetails.getMilestoneMonth());
        existingSow.setCurrency(sowDetails.getCurrency());
        existingSow.setMilestoneAmount(sowDetails.getMilestoneAmount());
        existingSow.setApproxAmount(sowDetails.getApproxAmount());
        existingSow.setSowStartDate(sowDetails.getSowStartDate());
        existingSow.setSowEndDate(sowDetails.getSowEndDate());
        existingSow.setDeliveryManagerId(sowDetails.getDeliveryManagerId());
        
        return repository.save(existingSow);
    }

    public void deleteSow(Long id) {
        SowDetails existingSow = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("SOW not found"));
        
        existingSow.setStatus("Inactive"); // Mark as inactive instead of deleting
        repository.save(existingSow);
    }

    public Page<SowDetails> getAllActiveSows(Pageable pageable) {
        return repository.findByStatus(STATUS_ACTIVE, pageable); // Assuming you have a method for this
    }

    public Page<SowDetails> getSowsByDeliveryManagerId(Integer deliveryManagerId, Pageable pageable) {
        return repository.findByDeliveryManagerIdAndStatus(deliveryManagerId,STATUS_ACTIVE, pageable); // Adjust the method in the repository
    }
    
    public byte[] generateSowReport(SowInputDTO sowInputDTO) throws IOException {
        // Fetch SOW records from the database
        List<SowDetails> sowRecords = repository.findByCriteria(sowInputDTO.getFromDate(),sowInputDTO.getToDate(),sowInputDTO.getStatus(),sowInputDTO.getDeliveryManagerId());

        // Create Excel workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        var sheet = workbook.createSheet("SOW Report");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("SOW ID");
        headerRow.createCell(1).setCellValue("SOW Name");
        headerRow.createCell(2).setCellValue("Account");
        headerRow.createCell(3).setCellValue("PO");
        headerRow.createCell(4).setCellValue("Milestone Month");
        headerRow.createCell(5).setCellValue("Currency");
        headerRow.createCell(6).setCellValue("Milestone Amount");
        headerRow.createCell(7).setCellValue("Approx Amount");
        headerRow.createCell(8).setCellValue("SOW Start Date");
        headerRow.createCell(9).setCellValue("SOW End Date");
        headerRow.createCell(10).setCellValue("Delivery Manager");
        headerRow.createCell(11).setCellValue("Status");

        // Populate data rows
        int rowNum = 1;
        for (SowDetails sow : sowRecords) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(sow.getSowId());
            row.createCell(1).setCellValue(sow.getSowName());
            row.createCell(2).setCellValue(sow.getAccount());
            row.createCell(3).setCellValue(sow.getPo());
            row.createCell(4).setCellValue(sow.getMilestoneMonth());
            row.createCell(5).setCellValue(sow.getCurrency());
            row.createCell(6).setCellValue(sow.getMilestoneAmount());
            row.createCell(7).setCellValue(sow.getApproxAmount());
            row.createCell(8).setCellValue(sow.getSowStartDate().toString());
            row.createCell(9).setCellValue(sow.getSowEndDate().toString());
            row.createCell(10).setCellValue(sow.getDeliveryManager() != null ? sow.getDeliveryManager().getFullName() : "");
            row.createCell(11).setCellValue(sow.getStatus());
        }

        // Write to a byte array output stream
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } finally {
            workbook.close();
        }
    }
    
    public List<String> getSowIdsByAccountAndStatus(String account, String status) {
        List<SowDetails> sowDetailsList = repository.findByAccountAndStatus(account, status);

        // Return the list of sow_ids from the retrieved records
        return sowDetailsList.stream()
                .map(SowDetails::getSowId)
                .toList();
    }
}
