package com.pransquare.nems.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.PayrollStagingEntity;

@Repository
public interface PayrollStagingRepository extends JpaRepository<PayrollStagingEntity,Long> {

	List<PayrollStagingEntity> findByFileId(String fileId);


}
