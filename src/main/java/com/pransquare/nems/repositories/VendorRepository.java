
package com.pransquare.nems.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Page<Vendor> findByVendorNameContainingIgnoreCase(String vendorName, Pageable pageable);
    
    @Query("SELECT v FROM Vendor v WHERE "
        + "(:vendorName IS NULL OR LOWER(v.vendorName) LIKE LOWER(CONCAT('%', :vendorName, '%'))) "
        + "AND (:client IS NULL OR LOWER(v.client) LIKE LOWER(CONCAT('%', :client, '%'))) "
        + "AND (:managerId IS NULL OR v.managerId = :managerId) "
        + "AND (:workflowStatuses IS NULL OR v.workflowStatus IN :workflowStatuses) "
        + "AND (:startDate IS NULL OR v.startDate >= :startDate) "
        + "AND (:endDate IS NULL OR v.endDate <= :endDate) "
        + "AND (:resource IS NULL OR LOWER(v.resource) LIKE LOWER(CONCAT('%', :resource, '%'))) "
        + "AND (:vendorStatus IS NULL OR LOWER(v.vendorStatus) = LOWER(:vendorStatus))")
Page<Vendor> searchVendors(
        @Param("vendorName") String vendorName,
        @Param("client") String client,
        @Param("workflowStatuses") List<String> workflowStatuses,
        @Param("managerId") Long managerId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("resource") String resource,
        @Param("vendorStatus") String vendorStatus,
        Pageable pageable);

    
    @Query("SELECT v FROM Vendor v WHERE "
            + "(:vendorName IS NULL OR LOWER(v.vendorName) LIKE LOWER(CONCAT('%', :vendorName, '%'))) "
            + "AND (:client IS NULL OR LOWER(v.client) LIKE LOWER(CONCAT('%', :client, '%'))) "
            + "AND (:managerId IS NULL OR v.managerId = :managerId) "
            + "AND (:workflowStatuses IS NULL OR v.workflowStatus IN :workflowStatuses) "
            + "AND (:startDate IS NULL OR v.startDate >= :startDate) "
            + "AND (:endDate IS NULL OR v.endDate <= :endDate)"
            + "AND (:vendorStatus IS NULL OR LOWER(v.vendorStatus) = LOWER(:vendorStatus))")
    List<Vendor> searchVendorsWithoutPagination(
            @Param("vendorName") String vendorName,
            @Param("client") String client,
            @Param("workflowStatuses") List<String> workflowStatuses,
            @Param("managerId") Long managerId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("vendorStatus") String vendorStatus);


}

