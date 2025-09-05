package com.pransquare.nems.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.entities.SowDetails;
import com.pransquare.nems.models.SowInputDTO;
import com.pransquare.nems.models.SowSearchInputModel;
import com.pransquare.nems.services.SowDetailsService;

@RestController
@RequestMapping("/Pransquare/nems/sow")
public class SowDetailsController {

    private SowDetailsService service;

    public SowDetailsController(SowDetailsService service) {
        this.service = service;
    }

    @PostMapping("/saveSowDetails")
    public ResponseEntity<SowDetails> createSow(@RequestBody SowDetails sowDetails) {
        return ResponseEntity.ok(service.createSow(sowDetails));
    }

    @PutMapping("/updateSowDetails/{id}")
    public ResponseEntity<SowDetails> updateSow(@PathVariable Long id, @RequestBody SowDetails sowDetails) {
        return ResponseEntity.ok(service.updateSow(id, sowDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSow(@PathVariable Long id) {
        service.deleteSow(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllSowDetails")
    // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Page<SowDetails>> getAllActiveSows(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<SowDetails> result = service.getAllActiveSows(pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getSowDetails/{deliveryManagerId}")
    public ResponseEntity<Page<SowDetails>> getSowsByDeliveryManager(
            @PathVariable Integer deliveryManagerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<SowDetails> result = service.getSowsByDeliveryManagerId(deliveryManagerId, pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<SowDetails>> searchSows(@RequestBody SowSearchInputModel sowSearchInputModel) {

        Page<SowDetails> result = service.searchSowsByCriteria(sowSearchInputModel);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/generateSowReport")
    public ResponseEntity<byte[]> generateReport(@RequestBody SowInputDTO sowInputDTO) {
        try {
            byte[] report = service.generateSowReport(sowInputDTO);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "sow_details_report.xlsx");
            return new ResponseEntity<>(report, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/getSowIds")
    public ResponseEntity<List<String>> getSowIds(@RequestParam String account, @RequestParam String status) {
        return ResponseEntity.ok(service.getSowIdsByAccountAndStatus(account, status));
    }
}
