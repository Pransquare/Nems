package com.pransquare.nems.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmployeeLeaveEntity;

@Repository
public interface EmployeeLeaveRepository extends JpaRepository<EmployeeLeaveEntity, Long> {

	List<EmployeeLeaveEntity> findByEmployeeId(Integer employeeId);
	// Add custom queries or methods here if needed

	List<EmployeeLeaveEntity> findByEmployeeIdAndStatusNot(Integer employeeId, String string);

	List<EmployeeLeaveEntity> findByApprover1AndStatus(Long approverId, String status);

	List<EmployeeLeaveEntity> findByEmployeeIdAndStatus(Integer employeeId, String string);

	@Query("SELECT e FROM EmployeeLeaveEntity e WHERE e.employeeId = :employeeId " + "AND e.status in('101','102')"
			+ "AND (e.leaveFrom <= :toDate AND e.leaveTo >= :fromDate)")
	List<EmployeeLeaveEntity> findOverlappingLeaves(@Param("employeeId") Long employeeId,
			@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

	List<EmployeeLeaveEntity> findByLeaveFromGreaterThanEqualAndLeaveToIsLessThanEqual(LocalDate leaveFrom,
			LocalDate leaveTo);

	List<EmployeeLeaveEntity> findByStatusAndLeaveFromGreaterThanEqualAndLeaveToIsLessThanEqual(String string,
			LocalDate taskDate, LocalDate taskDate2);

	List<EmployeeLeaveEntity> findByStatusAndLeaveFromLessThanEqualAndLeaveToGreaterThanEqual(String string,
			LocalDate taskDate, LocalDate taskDate2);

	List<EmployeeLeaveEntity> findByStatusNotInAndLeaveFromLessThanEqualAndLeaveToGreaterThanEqual(List<String> status,
			LocalDate taskDate, LocalDate taskDate2);

	List<EmployeeLeaveEntity> findByLeaveFromLessThanEqualAndLeaveToGreaterThanEqualAndStatusNotIn(LocalDate taskDate,
			LocalDate taskDate2, List<String> status);

	List<EmployeeLeaveEntity> findByEmpLeaveIdAndLeaveFromLessThanEqualAndLeaveToGreaterThanEqualAndStatusNotIn(
			Long empBasicDetailId, LocalDate taskDate, LocalDate taskDate2, List<String> status);
	List<EmployeeLeaveEntity> findByEmployeeIdAndLeaveFromLessThanEqualAndLeaveToGreaterThanEqual(
            Long employeeId, LocalDate toDate, LocalDate fromDate);

    List<EmployeeLeaveEntity> findByEmployeeIdInAndLeaveFromLessThanEqualAndLeaveToGreaterThanEqual(
            List<Long> employeeIds, LocalDate toDate, LocalDate fromDate);

	List<EmployeeLeaveEntity> findByEmployeeIdAndStatus(Long employeeId, String string);

}