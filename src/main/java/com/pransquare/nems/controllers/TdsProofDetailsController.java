package com.pransquare.nems.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.entities.TdsProofDetails;
import com.pransquare.nems.services.TdsProofDetailsService;

@RestController
@RequestMapping("/Pransquare/nems/tdsProofDetails")
public class TdsProofDetailsController {

    private final TdsProofDetailsService tdsProofDetailsService;

    public TdsProofDetailsController(TdsProofDetailsService tdsProofDetailsService) {
        this.tdsProofDetailsService = tdsProofDetailsService;
    }

    @PostMapping("/saveProofDetails")
    public ResponseEntity<List<TdsProofDetails>> saveTdsProofDetails(
            @RequestBody List<TdsProofDetails> tdsProofDetailsList) {
        List<TdsProofDetails> savedDetails = tdsProofDetailsService.saveAll(tdsProofDetailsList);
        return ResponseEntity.ok(savedDetails);
    }

    @PostMapping("/deleteProofDetails")
    public ResponseEntity<String> deleteTdsProofDetails(@RequestParam(required = true) Long tdsProofId) {
        return tdsProofDetailsService.deleteTdsProofById(tdsProofId);
    }
}
