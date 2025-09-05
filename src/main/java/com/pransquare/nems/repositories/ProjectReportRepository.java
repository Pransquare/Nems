package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pransquare.nems.entities.ProjectReportEntity;

import java.time.LocalDate;
import java.util.List;

public interface ProjectReportRepository extends JpaRepository<ProjectReportEntity, Long> {

    @Query("""
        SELECT p FROM ProjectReportEntity p
        WHERE (:clientCode IS NULL OR p.clientCode = :clientCode)
          AND (:projectCodes IS NULL OR p.projectId IN (:projectCodes))
          AND (:fromDate IS NULL OR p.startDate >= :fromDate)
          AND (:toDate IS NULL OR p.endDate <= :toDate)
    """)
    List<ProjectReportEntity> findFiltered(
        @Param("clientCode") String clientCode,
        @Param("projectCodes") List<String> projectCodes,
        @Param("fromDate") LocalDate fromDate,
        @Param("toDate") LocalDate toDate
    );
}
