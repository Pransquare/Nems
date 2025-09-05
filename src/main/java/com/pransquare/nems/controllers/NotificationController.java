package com.pransquare.nems.controllers;

import java.time.Duration;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.entities.NotificationEntity;
import com.pransquare.nems.services.NotificationService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/Pransquare/nems/notification")
public class NotificationController {

	private final NotificationService notificationService;

	public NotificationController(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@GetMapping(value = "/count/{employeeId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Integer> streamNotificationCount(@PathVariable Long employeeId) {
		return Flux.interval(Duration.ofSeconds(5)) // Poll the notification count every 5 seconds
				.flatMap(i -> notificationService.getNotificationCount(employeeId)).distinctUntilChanged(); // Only emit
																											// if the
																											// count has
																											// changed
	}

	@GetMapping(value = "/getNotifications/{employeeId}")
	public List<NotificationEntity> getNotifications(@PathVariable Long employeeId) {
		return notificationService.getNotifications(employeeId);
	}

	@PutMapping("/markAsRead")
	public ResponseEntity<String> markAsRead(@RequestParam(required = false) Long notificationId,
			@RequestParam(required = false) Long employeeId) {
		try {
			// Call the service method to mark the notifications as read
			String response = notificationService.markAsRead(notificationId, employeeId);

			// Return success response
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			// Handle any exceptions
			return ResponseEntity.status(500).body("Error marking notification as read: " + e.getMessage());
		}
	}
}
