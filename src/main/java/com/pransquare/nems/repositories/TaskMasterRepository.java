package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.TaskMasterEntity;

@Repository
public interface TaskMasterRepository extends JpaRepository<TaskMasterEntity, Integer> {
}