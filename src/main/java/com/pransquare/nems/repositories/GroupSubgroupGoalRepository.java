package com.pransquare.nems.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.GroupSubgroupGoalEntity;

@Repository
public interface GroupSubgroupGoalRepository extends JpaRepository<GroupSubgroupGoalEntity, Long>{

	GroupSubgroupGoalEntity findByGoalAndGroupSubgroupConfigId(String goal, Long configId);

	List<GroupSubgroupGoalEntity> findByGroupSubgroupConfigId(Long configId);

}
