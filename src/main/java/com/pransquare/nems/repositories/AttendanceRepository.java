package com.pransquare.nems.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.AttendanceEntity;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, Long>,
		PagingAndSortingRepository<AttendanceEntity, Long>, JpaSpecificationExecutor<AttendanceEntity> {

	AttendanceEntity findByAttendanceIdAndStatus(Long attendanceId, String string);

	AttendanceEntity findByTaskNameAndTaskDate(String taskName, LocalDate taskDate);

	List<AttendanceEntity> findByEmpBasicDetailIdAndTaskDateAndStatusNot(Long empBasicDetailId, LocalDate taskDate,
			String string);

	List<AttendanceEntity> findByEmpBasicDetailIdAndTaskNameAndTaskDateAndProjectCodeAndStatusNot(Long empBasicDetailId,
			String taskName, LocalDate taskDate, String projectCode, String string);

	List<AttendanceEntity> findByEmpBasicDetailIdAndTaskNameAndTaskDateAndProjectCodeAndStatusNotAndAttendanceIdNot(
			Long empBasicDetailId, String taskName, LocalDate taskDate, String projectCode, String string,
			Long attendanceId);

	AttendanceEntity findByEmpBasicDetailIdAndTaskDateAndProjectCode(Long empBasicDetailId, LocalDate taskDate,
			String projectCode);

	AttendanceEntity findByEmpBasicDetailIdAndTaskDateAndProjectCodeAndTaskNameAndStatusNotInAndIsBillable(
			Long empBasicDetailId,
			LocalDate taskDate, String projectCode, String taskName, List<String> status, Boolean isBillable);

	List<AttendanceEntity> findByEmpBasicDetailIdAndTaskDateAndStatusNotIn(Long empBasicDetailId, LocalDate taskDate,
			List<String> statuList);

	List<AttendanceEntity> findByEmpBasicDetailIdAndTaskDateBetweenAndStatusIn(Long empBasicDetailId, LocalDate fromDate,
			LocalDate toDate,List<String> status);

	List<AttendanceEntity> findByTaskDateBetweenAndStatusIn(LocalDate fromDate, LocalDate toDate, List<String> status);
	

    List<AttendanceEntity> findByEmpBasicDetailIdInAndTaskDateBetweenAndStatusIn(
            List<Long> empBasicDetailIds, LocalDate fromDate, LocalDate toDate, List<String> status);
    
    Page<AttendanceEntity> findByEmpBasicDetailIdAndTaskDateBetweenAndStatusIn(
            Long empBasicDetailId, LocalDate fromDate, LocalDate toDate, List<String> status, Pageable pageable);

    Page<AttendanceEntity> findByEmpBasicDetailIdInAndTaskDateBetweenAndStatusIn(
            List<Long> empBasicDetailIds, LocalDate fromDate, LocalDate toDate, List<String> status, Pageable pageable);


}