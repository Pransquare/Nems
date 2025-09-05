package com.pransquare.nems.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmpGoalSetupEntity;

@Repository
public interface GoalSetUpRepository extends JpaRepository<EmpGoalSetupEntity, Long>{

	Page<EmpGoalSetupEntity> findAll(Specification<EmpGoalSetupEntity> spec, Pageable pageable);


	EmpGoalSetupEntity findByEmpBasicDetailIdAndStatusNot(long emplBasicId, String string);

}
