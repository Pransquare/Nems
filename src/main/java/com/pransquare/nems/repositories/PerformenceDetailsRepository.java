package com.pransquare.nems.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.PerformanceDetailsEntity;

@Repository
public interface PerformenceDetailsRepository extends JpaRepository<PerformanceDetailsEntity, Long> {

	@Query("SELECT e.empBasicDetailId, e.performanceGroup, e.performanceSubGroup, p.performanceParameter, "
			+ "p.comments, p.selfRating, p.managerRating, p.managerComments, p.finalRating, p.finalComments "
			+ "FROM EmployeeDesignationAndManagerDetailsEntity e "
			+ "INNER JOIN PerformanceDetailsEntity p ON e.empBasicDetailId = p.employeeBasicDetais "
			+ "WHERE e.empBasicDetailId = :empId")
	List<Object[]> findEmployeeAndPerformanceDetailsByEmpId(@Param("empId") Long empId);

}
