package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pransquare.nems.entities.GoalCommentsEntity;

public interface GoalCommentsRepository extends JpaRepository<GoalCommentsEntity, Long> {

}
