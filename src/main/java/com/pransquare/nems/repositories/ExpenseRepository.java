package com.pransquare.nems.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.entities.ExpenseEntity;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer>,
        PagingAndSortingRepository<ExpenseEntity, Integer>, JpaSpecificationExecutor<ExpenseEntity> {

    List<ExpenseEntity> findByEmployeeId(Long employeeId);

    @Query("SELECT e FROM ExpenseEntity e WHERE e.employeeId = :employeeId " +
            "AND e.entryDate BETWEEN :startOfMonth AND :endOfMonth " +
            "AND e.status = :status")
    List<ExpenseEntity> findExpensesForReport(@Param("employeeId") Long employeeId,
            @Param("startOfMonth") LocalDate startOfMonth,
            @Param("endOfMonth") LocalDate endOfMonth,
            @Param("status") String status);

    @Query("SELECT e FROM ExpenseEntity e WHERE " +
            "e.entryDate BETWEEN :startOfMonth AND :endOfMonth " +
            "AND e.status = :status")
    List<ExpenseEntity> findExpensesForReportAll(
            @Param("startOfMonth") LocalDate startOfMonth,
            @Param("endOfMonth") LocalDate endOfMonth,
            @Param("status") String status);
}
