package com.pransquare.nems.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pransquare.nems.entities.SowDetails;

public interface SowDetailsRepository extends JpaRepository<SowDetails, Long> {

	Page<SowDetails> findByStatus(String status, Pageable pageable);

	// Method to find SOWs by delivery manager ID
	Page<SowDetails> findByDeliveryManagerId(Integer deliveryManagerId, Pageable pageable);

	Page<SowDetails> findByDeliveryManagerIdAndStatus(Integer deliveryManagerId, String status, Pageable pageable);

	@Query("SELECT s FROM SowDetails s WHERE s.sowStartDate >= :startDate AND s.sowEndDate <= :endDate AND s.status = :status"
			+ " AND (s.deliveryManagerId in :deliveryManagerId)")
	List<SowDetails> findByCriteria(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
			@Param("status") String status, @Param("deliveryManagerId") List<Integer> deliveryManagerId);

	List<SowDetails> findByAccountAndStatus(String account, String status);

	@Query("SELECT s FROM SowDetails s " + "WHERE (:account IS NULL OR s.account = :account) "
			+ "AND (:milestoneMonth IS NULL OR s.milestoneMonth = :milestoneMonth) "
			+ "AND (:status IS NULL OR s.status = :status) "
			+ "AND (:managerId IS NULL OR s.deliveryManagerId IN :managerId)"
			+ "AND (:sowId IS NULL OR s.sowId = :sowId)")
	Page<SowDetails> searchSowDetails(@Param("account") String account, @Param("milestoneMonth") String milestoneMonth,
			@Param("status") String status, @Param("managerId") List<Long> managerId, String sowId, Pageable pageable);
}
