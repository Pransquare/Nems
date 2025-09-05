package com.pransquare.nems.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.UserApproverConfigurationsEntity;

@Repository
public interface ApproverConfigRepository extends JpaRepository<UserApproverConfigurationsEntity, Long> {

	UserApproverConfigurationsEntity findByEmpBasicDetailIdAndModuleName(Long employeeBasicDetailId, String moduleName);

	List<UserApproverConfigurationsEntity> findByEmpBasicDetailId(Long employeeBasicDetailId);

	List<UserApproverConfigurationsEntity> findByApproverIdAndModuleName(Long approverId, String module);

}
