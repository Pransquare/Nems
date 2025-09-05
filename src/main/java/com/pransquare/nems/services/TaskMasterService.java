package com.pransquare.nems.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.pransquare.nems.entities.TaskMasterEntity;
import com.pransquare.nems.repositories.TaskMasterRepository;

@Service
public class TaskMasterService {

    private TaskMasterRepository taskMasterRepository;

    public TaskMasterService(TaskMasterRepository taskMasterRepository) {
		this.taskMasterRepository = taskMasterRepository;
	}

    public List<TaskMasterEntity> getAllTasks() {
        return taskMasterRepository.findAll(Sort.by(Sort.Direction.ASC, "taskDescription"));
    }
}