package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pransquare.nems.entities.ExpenseDetails;

public interface ExpenseDetailsRepository extends JpaRepository<ExpenseDetails, Integer> {
}
