package com.pransquare.nems.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmployeeProjectConfigEntity;
import com.pransquare.nems.models.ProjectAndClientDTO;

@Repository
public interface EmployeeProjectConfigRepository extends JpaRepository<EmployeeProjectConfigEntity, Long> {

    List<EmployeeProjectConfigEntity> findByEmployeeIdAndStatus(Long employeeId, String status);

    @Query(value = "SELECT DISTINCT pm.PROJECT_NAME AS projectName, cm.CLIENT_NAME AS clientName " +
            "FROM emsportal.employee_project_config epc " +
            "INNER JOIN masterconfiguration.project_master pm ON epc.project_code = pm.PROJECT_CODE " +
            "INNER JOIN masterconfiguration.client_master cm ON pm.CLIENT_CODE = cm.CLIENT_CODE " +
            "WHERE epc.status = '108' AND epc.employee_id = 36", nativeQuery = true)
    List<ProjectAndClientDTO> getProjects();

    @Query("SELECT DISTINCT e.employeeId FROM EmployeeProjectConfigEntity e WHERE e.projectCode like :projectCode")
    List<Long> findDistinctEmployeeIdsByProjectCode(String projectCode);

    List<EmployeeProjectConfigEntity> findByStatus(String status);
}