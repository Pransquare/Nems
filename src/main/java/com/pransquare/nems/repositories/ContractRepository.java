package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.ContractEntity;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long>{

	void findByContractId(String contractId);

}
