package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.NewsEntity;

@Repository
public interface NewsRepository extends JpaRepository<NewsEntity, Long> {
}