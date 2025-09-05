package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.StatusMasterEntity;

@Repository
public interface StatusMasterRepository extends JpaRepository<StatusMasterEntity, Long> {
    // Add any custom methods or queries here if needed
}