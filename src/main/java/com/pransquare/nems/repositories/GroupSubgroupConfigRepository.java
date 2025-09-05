package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.GroupSubgroupConfigEntity;

@Repository
public interface GroupSubgroupConfigRepository extends JpaRepository<GroupSubgroupConfigEntity, Long>,
        PagingAndSortingRepository<GroupSubgroupConfigEntity, Long>,
        JpaSpecificationExecutor<GroupSubgroupConfigEntity> {

    GroupSubgroupConfigEntity findByGroupAndSubGroup(String group, String subGroup);

    GroupSubgroupConfigEntity findByGroupAndSubGroupAndStatus(String group, String subGroup, String string);

}
