
package com.pransquare.nems.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.dto.VendorApprovalDto;
import com.pransquare.nems.dto.VendorInputDto;
import com.pransquare.nems.dto.VendorSearchDto;
import com.pransquare.nems.entities.Vendor;
import com.pransquare.nems.services.VendorService;

@RestController
@RequestMapping("/Pransquare/nems/api/vendor")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping("/saveVendor")
    public ResponseEntity<Vendor> createVendor(@RequestBody VendorInputDto vendorInputDto) {
        Vendor createdVendor = vendorService.createVendor(vendorInputDto);
        return ResponseEntity.ok(createdVendor);
    }

    @PostMapping("/approveOrRejectVendor")
    public ResponseEntity<String> approveOrRejectVendor(@RequestBody VendorApprovalDto approvalDto) {
        String response = vendorService.approveOrRejectVendor(approvalDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<Vendor>> searchVendors(@RequestBody VendorSearchDto searchDto) {
        Page<Vendor> vendors = vendorService.searchVendors(
                searchDto.getVendorName(),
                searchDto.getClient(),
                searchDto.getWorkflowStatuses(),
                searchDto.getManagerId(),
                searchDto.getPage(),
                searchDto.getSize(),
                searchDto.getStartDate(),
                searchDto.getEndDate(),
                searchDto.getResource(),
                searchDto.getVendorStatus());
        return ResponseEntity.ok(vendors);
    }


    @PostMapping("/vendorReport")
    public ResponseEntity<ByteArrayResource> exportVendorsToExcel(@RequestBody VendorSearchDto searchDto)
            throws IOException {
        ByteArrayResource excelResource = vendorService.exportVendorsToExcel(searchDto.getVendorName(),

                searchDto.getClient(),
                searchDto.getWorkflowStatuses(), searchDto.getManagerId(), searchDto.getStartDate(),
                searchDto.getEndDate(),searchDto.getVendorStatus());

        // ByteArrayResource resource = new ByteArrayResource(excelData);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=expense_report.xlsx");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(excelResource.contentLength())
                .body(excelResource);
    }
    
    @DeleteMapping("/deleteVendor/{id}")
    public ResponseEntity<String> deleteVendor(@PathVariable Long id) {
        boolean isDeleted = vendorService.deleteVendorById(id);
        if (isDeleted) {
            return ResponseEntity.ok("Vendor deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
