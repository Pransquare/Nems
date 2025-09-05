package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.GroupEmailEntity;

@Repository
public interface GroupEmailRepository extends JpaRepository<GroupEmailEntity, Integer> {

    GroupEmailEntity findByGroupName(String groupName);

    boolean existsByEmailId(String emailId);
}
