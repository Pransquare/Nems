package com.pransquare.nems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
public class NEMSPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(NEMSPortalApplication.class, args);
	}

}
