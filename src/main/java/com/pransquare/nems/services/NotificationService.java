package com.pransquare.nems.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pransquare.nems.entities.NotificationEntity;
import com.pransquare.nems.repositories.NotificationRepository;
import com.pransquare.nems.utils.IntegerUtils;

import reactor.core.publisher.Mono;

@Service
public class NotificationService {

    NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
		this.notificationRepository = notificationRepository;
	}

	public Mono<Integer> getNotificationCount(Long employeeId) {
        // Call the blocking repository method and wrap it in Mono
        return Mono.fromCallable(() -> notificationRepository.countByEmployeeIdAndStatus(employeeId, "115"));
    }

    public List<NotificationEntity> getNotifications(Long employeeId) {
        try {
            return notificationRepository.findByEmployeeIdAndStatus(employeeId, "115");
        } catch (Exception e) {
            throw new IllegalStateException(e.fillInStackTrace());
        }
    }

//    public String markAsRead(Long notificationId, Long employeeId) {
//        try {
//            if (IntegerUtils.isNotNull(employeeId)) {
//                List<NotificationEntity> notificationEntities = notificationRepository
//                        .findByEmployeeIdAndStatus(employeeId, "115");
//                notificationRepository.deleteAll(notificationEntities);
//                return "Notification marked as read successfully.";
//            }
//            notificationRepository.delete(notificationRepository.findById(notificationId).map(notification -> {
//                notification.setStatus("116");
//                return notification;
//            }).get());
//            return "Notification marked as read successfully.";
//        } catch (Exception e) {
//            throw new IllegalStateException(e.fillInStackTrace());
//        }
//
//    }
    
    public String markAsRead(Long notificationId, Long employeeId) {
        try {
            if (IntegerUtils.isNotNull(employeeId)) {
                List<NotificationEntity> notificationEntities = notificationRepository
                        .findByEmployeeIdAndStatus(employeeId, "115");
                notificationRepository.deleteAll(notificationEntities);
                return "Notification marked as read successfully.";
            }
            
            // Handle case when notificationId is provided
            Optional<NotificationEntity> notificationEntity = notificationRepository.findById(notificationId);
            if (notificationEntity.isPresent()) {
                NotificationEntity notification = notificationEntity.get();
                notification.setStatus("116");
                notificationRepository.delete(notification);
                return "Notification marked as read successfully.";
            } else {
                throw new RuntimeException("Notification not found for the given ID");
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.fillInStackTrace());
        }
    }
}
