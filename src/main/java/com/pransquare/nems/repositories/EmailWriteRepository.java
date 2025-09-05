package com.pransquare.nems.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.EmailWrite;

@Repository
public interface EmailWriteRepository extends JpaRepository<EmailWrite, Long>{

	EmailWrite findByMessageId(String messageId);

}
