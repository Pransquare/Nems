package com.pransquare.nems.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.PayrollEntity;

@Repository
public interface PayrollRepository extends JpaRepository<PayrollEntity, Long> {

	PayrollEntity findByEmpBasicDetailId(Long id);

	PayrollEntity findByEmpBasicDetailIdAndPayPeriodMonthAndPayPeriodYear(Long empBasicDetailId, String month,
			String year);

	@Query("SELECT p FROM PayrollEntity p WHERE p.empBasicDetailId = :empId ORDER BY p.payPeriodYear DESC, p.payPeriodMonth DESC")
	List<PayrollEntity> findByEmpBasicDetailIdOrderByPayPeriod(@Param("empId") Long empId);

}
