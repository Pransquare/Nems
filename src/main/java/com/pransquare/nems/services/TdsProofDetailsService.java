package com.pransquare.nems.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pransquare.nems.entities.TdsProofDetails;
import com.pransquare.nems.repositories.TdsProofDetailsRepository;

@Service
public class TdsProofDetailsService {

    private final TdsProofDetailsRepository tdsProofDetailsRepository;

    public TdsProofDetailsService(TdsProofDetailsRepository tdsProofDetailsRepository) {
        this.tdsProofDetailsRepository = tdsProofDetailsRepository;
    }

    @Transactional
    public List<TdsProofDetails> saveAll(List<TdsProofDetails> tdsProofDetailsList) {
        return tdsProofDetailsRepository.saveAll(tdsProofDetailsList);
    }

    @Transactional
    public ResponseEntity<String> deleteTdsProofById(Long tdsProofId) {
        // Check if the record exists
        return tdsProofDetailsRepository.findById(tdsProofId)
                .map(tdsProofDetails -> {
                    String path = tdsProofDetails.getDocumentPath();

                    // Delete the file if it exists
                    if (path != null && !path.isEmpty()) {
                        Path filePath = Paths.get(path);
                        if (Files.exists(filePath)) {
                            try {
                                Files.delete(filePath);
                            } catch (Exception e) {
                                // Handle exception if file deletion fails
                                return ResponseEntity.status(500).body("Failed to delete the file: " + e.getMessage());
                            }
                        }
                    }

                    // Delete the record
                    tdsProofDetailsRepository.deleteById(tdsProofId);
                    return ResponseEntity.ok("TDS Proof Details deleted successfully");
                })
                .orElse(ResponseEntity.status(404).body("TDS Proof Details not found"));
    }

}
