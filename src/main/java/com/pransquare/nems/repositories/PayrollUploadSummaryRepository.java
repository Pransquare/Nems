package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.PayrollUploadSummaryEntity;


@Repository
public interface PayrollUploadSummaryRepository extends JpaRepository<PayrollUploadSummaryEntity, Long>{

	PayrollUploadSummaryEntity findByFileId(String fileId);

	PayrollUploadSummaryEntity findByFileName(String originalFilename);

}
