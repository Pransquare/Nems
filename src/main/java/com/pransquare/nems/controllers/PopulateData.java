package com.pransquare.nems.controllers;

import java.time.LocalDate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import com.pransquare.nems.entities.EmployeeLeaveEntity;
import com.pransquare.nems.models.EmployeeLeaveModel;
import com.pransquare.nems.repositories.EmployeeRepository;
import com.pransquare.nems.services.EmployeeLeaveService;

//@Component
public class PopulateData implements ApplicationRunner {

	private static final Logger logger = LogManager.getLogger(PopulateData.class);
	

	private final EmployeeLeaveService employeeLeaveService;

	public PopulateData( EmployeeLeaveService employeeLeaveService) {
		this.employeeLeaveService = employeeLeaveService;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		EmployeeLeaveModel employeeLeaveModel = new EmployeeLeaveModel();
		employeeLeaveModel.setEmployeeId(1l);
		employeeLeaveModel.setLeaveFrom(LocalDate.now());
		employeeLeaveModel.setLeaveTo(LocalDate.now());
		employeeLeaveModel.setFromLeaveType("Test");
		employeeLeaveModel.setToLeaveType("Test");
		// employeeLeaveModel.set
		EmployeeLeaveEntity employeeLeaveDetails = employeeLeaveService
				.createOrUpdateEmployeeLeaveEntity(employeeLeaveModel);
		logger.info("PopulateData.run() and Created/Updated EmployeeLeaveEntity: {}",employeeLeaveDetails );
	}

}
