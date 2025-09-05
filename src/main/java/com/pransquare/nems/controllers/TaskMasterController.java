package com.pransquare.nems.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pransquare.nems.entities.TaskMasterEntity;
import com.pransquare.nems.services.TaskMasterService;

@RestController
@RequestMapping("/Pransquare/nems/taskMaster")
public class TaskMasterController {

	private final TaskMasterService taskMasterService;

	public TaskMasterController(TaskMasterService taskMasterService) {
		this.taskMasterService = taskMasterService;
	}

	@GetMapping("/getAllTasks")
	public List<TaskMasterEntity> getAllTasks() {
		return taskMasterService.getAllTasks();
	}
}