package com.pransquare.nems.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.PerformenceReviewEntity;

@Repository
public interface PerformenceReviewRepository extends JpaRepository<PerformenceReviewEntity, Long> {
    PerformenceReviewEntity findByEmployeeBasicDetailIdAndStatusNot(Long empId, String string);

    PerformenceReviewEntity findByEmployeeBasicDetailIdAndStatus(Long empId, String status);

    List<PerformenceReviewEntity> findByEmployeeBasicDetailId(Long empId);

}
