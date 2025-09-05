package com.pransquare.nems.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmpGoalDetailsEntity;

@Repository
public interface GoalDetailsRepository extends JpaRepository<EmpGoalDetailsEntity, Long> {

	List<EmpGoalDetailsEntity> findByEmpGoalSetupId(Long empGoalSetupId);

}
