package com.pransquare.nems.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.TdsDetails;

@Repository
public interface TdsDetailsRepository extends JpaRepository<TdsDetails, Integer> {

	List<TdsDetails> findByEmployeeCode(String employeeCode);

	@Modifying
	@Query("UPDATE TdsDetails t SET t.status = :status WHERE t.employeeCode = :employeeCode")
	int updateTdsStatus(@Param("employeeCode") String employeeCode, @Param("status") String status);
}
