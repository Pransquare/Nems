package com.pransquare.nems.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pransquare.nems.entities.NotificationEntity;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    Integer countByEmployeeIdAndStatus(Long employeeId, String status);

    List<NotificationEntity> findByEmployeeIdAndStatus(Long employeeId, String string);
}
