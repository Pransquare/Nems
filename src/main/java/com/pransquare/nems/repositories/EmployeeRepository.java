package com.pransquare.nems.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmployeeEntity;
import com.pransquare.nems.models.EmployeeNameCode;

@Repository
public interface EmployeeRepository extends PagingAndSortingRepository<EmployeeEntity, Long>,
                JpaRepository<EmployeeEntity, Long>, JpaSpecificationExecutor<EmployeeEntity> {

        EmployeeEntity findByEmployeeCode(String employeeCode);

        Page<EmployeeEntity> findAllByStatusNot(String status, Pageable pageable);

        EmployeeEntity findByEmployeeCodeAndStatusNot(String employeeCode, String status);

        // List<EmployeeEntity> (String status);

        @Query("SELECT e FROM EmployeeEntity e WHERE e.status = :status AND (e.genericProfile = FALSE OR e.genericProfile IS NULL)")
        List<EmployeeEntity> findByStatus(@Param("status") String status);
        

        List<EmployeeEntity> findByGroupAndSubGroupAndStatus(String group, String subGroup, String status);

        @Query("SELECT e FROM EmployeeEntity e WHERE e.employeeBasicDetailId IN (SELECT d.empBasicDetailId FROM EmployeeDesignationAndManagerDetailsEntity d WHERE d.performanceGroup = :performanceGroup AND d.performanceSubGroup = :performanceSubGroup AND d.reportingLevel1 = :managerId)")
        List<EmployeeEntity> findByManager(@Param("performanceGroup") String performanceGroup,
                        @Param("performanceSubGroup") String performanceSubGroup, @Param("managerId") Long managerId);

        Page<EmployeeEntity> findAllByStatus(String status, Pageable page);

        @Query(nativeQuery = true, value = "SELECT e.employee_code AS employeeCode, " +
                        "e.full_name AS fullName, e.email_id AS emailId, " +
                        "e.employee_basic_detail_id AS employeeBasicDetailId, " +
                        "ew.work_location_code AS workLocationCode " + // Space before FROM
                        "FROM employee_basic_details e " +
                        "JOIN employee_work_location ew ON e.employee_basic_detail_id = ew.employee_id " +
                        "WHERE e.status='108' and LOWER(e.full_name) LIKE LOWER(CONCAT('%', :input, '%'))")
        List<EmployeeNameCode> findByFullNameStartingWithIgnoreCase(@Param("input") String input);

        @Query(value = """
        	    SELECT first_name AS firstName, middle_name AS middleName, last_name AS lastName, dob
        	    FROM employee_basic_details
        	    WHERE status = '108' AND (
        	        DATE_FORMAT(dob, '%m-%d') BETWEEN DATE_FORMAT(:today, '%m-%d') AND DATE_FORMAT(:nextWeek, '%m-%d')
        	        OR (
        	            DATE_FORMAT(:today, '%m-%d') > DATE_FORMAT(:nextWeek, '%m-%d') AND 
        	            (DATE_FORMAT(dob, '%m-%d') >= DATE_FORMAT(:today, '%m-%d') OR DATE_FORMAT(dob, '%m-%d') <= DATE_FORMAT(:nextWeek, '%m-%d'))
        	        )
        	    )
        	    """, nativeQuery = true)
        	List<EmployeeBirthdayProjection> findBirthdaysWithinNextWeek(
        	        @Param("today") LocalDate today,
        	        @Param("nextWeek") LocalDate nextWeek);



        // Projection interface
        public interface EmployeeBirthdayProjection {
                String getFirstName();

                String getMiddleName();

                String getLastName();

                LocalDate getDob();
        }

        public interface EmployeeNameCode {
                String getEmployeeCode();

                String getFullName();

                String getEmailId();

                Long getEmployeeBasicDetailId();

                String getWorkLocationCode();
        }

        List<EmployeeEntity> findByPersonalEmail(String email);

        @Modifying
        @Query("UPDATE EmployeeEntity e SET e.isProofdeclarationEnabled = 1,e.proofSubmissionEnabledDate = CURRENT_TIMESTAMP WHERE e.status = '108'")
        int enableProofDeclarationForAllActiveEmployees();

        @Modifying
        @Query("UPDATE EmployeeEntity e SET e.isProofdeclarationEnabled = 1, e.proofSubmissionEnabledDate = CURRENT_TIMESTAMP WHERE e.employeeCode = :employeeCode")
        int enableProofDeclarationforEmployee(@Param("employeeCode") String employeeCode);

        List<EmployeeEntity> findByEmployeeBasicDetailIdAndStatus(Long employeeId, String string);

        @Modifying
        @Query("UPDATE EmployeeEntity e SET e.isTaxdeclarationEnabled = 1, e.taxDeclarationEnabledDate = CURRENT_TIMESTAMP WHERE e.status = '108'")
        int enableTaxDeclarationForAllActiveEmployees();

        @Modifying
        @Query("UPDATE EmployeeEntity e SET e.isTaxdeclarationEnabled = 1, e.taxDeclarationEnabledDate = CURRENT_TIMESTAMP WHERE e.employeeCode = :employeeCode")
        int enableTaxDeclarationforEmployee(@Param("employeeCode") String employeeCode);

        @Query("SELECT e FROM EmployeeEntity e WHERE e.employeeBasicDetailId IN :employeeIds AND e.status = :status")
        List<EmployeeEntity> findByEmployeeBasicDetailIdsAndStatus(@Param("employeeIds") List<Long> employeeIds,
                        @Param("status") String status);

        EmployeeEntity findByEmployeeBasicDetailId(Long employeeId);

        @Query(value = "SELECT COUNT(*) FROM employee_basic_details " +
                        "WHERE STATUS = :status AND (generic_profile = FALSE OR generic_profile IS NULL)", nativeQuery = true)
        long countEmployeesByStatus(@Param("status") String status);
        
        public interface EmployeeForGoal {
            String getEmployeeCode();

            String getFullName();

            String getEmailId();

            Long getEmployeeBasicDetailId();

            String getWorkLocationCode();
            
            String getGoalSetup();
    }
        @Query(nativeQuery = true, value = "SELECT e.employee_code AS employeeCode, " +
                "e.full_name AS fullName, e.email_id AS emailId, " +
                "e.employee_basic_detail_id AS employeeBasicDetailId, " +
                "ew.work_location_code AS workLocationCode, e.goal_setup AS goalSetup " + // Space before FROM
                "FROM employee_basic_details e " +
                "JOIN employee_work_location ew ON e.employee_basic_detail_id = ew.employee_id " +
                "WHERE e.status='108' and LOWER(e.full_name) LIKE LOWER(CONCAT('%', :input, '%'))")
List<EmployeeForGoal> searchEmployeeForGoal(@Param("input") String input);


		List<EmployeeEntity> findByGroupAndSubGroupAndStatusAndGenericProfile(String performanceGroup,
				String performanceSubGroup, String string, boolean b);

		List<EmployeeEntity> findByGroupAndSubGroupAndStatusAndGenericProfileNot(String performanceGroup,
				String performanceSubGroup, String string, boolean b);

		List<EmployeeEntity> findByEmailId(String createdBy);

		EmployeeEntity findByFullName(String string);
}
