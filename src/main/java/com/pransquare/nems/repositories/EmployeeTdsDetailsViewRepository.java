package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmployeeTdsDetailsView;

@Repository
public interface EmployeeTdsDetailsViewRepository extends JpaRepository<EmployeeTdsDetailsView, Integer>, JpaSpecificationExecutor<EmployeeTdsDetailsView> {
}