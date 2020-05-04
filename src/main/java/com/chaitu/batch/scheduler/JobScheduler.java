package com.chaitu.batch.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.chaitu.batch.service.JobLauncherService;
import com.chaitu.schema.repo.TableSchemaRepo;

@Component
public class JobScheduler {
	
	@Autowired
	TableSchemaRepo tableSchemaRepo;
	
	@Autowired
	JobLauncher jobLauncher;
	
	@Autowired
	Job job;
	
	@Autowired
	JobLauncherService jobLauncherService;
	
	@Scheduled(cron = "${scheduler.cron}")
	public void launchJobs(){
		System.out.println("starting");
		tableSchemaRepo.findAllTables()
		.forEach( table -> {
			jobLauncherService.launchJob(table);
		});
	}
}
