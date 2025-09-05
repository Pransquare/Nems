package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.TdsConfig;

@Repository
public interface TdsConfigRepository extends JpaRepository<TdsConfig, Integer> {
    // Custom query methods can be added here
}
