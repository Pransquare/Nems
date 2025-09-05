package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmployeeGoal;

@Repository
public interface EmployeeGoalRepository extends JpaRepository<EmployeeGoal, Integer> {
}
