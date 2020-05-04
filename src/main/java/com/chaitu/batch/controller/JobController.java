package com.chaitu.batch.controller;

import java.util.List;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.chaitu.batch.models.Cron;
import com.chaitu.batch.models.JobHistory;
import com.chaitu.batch.service.JobLauncherService;

@RestController
@CrossOrigin
public class JobController {
	
	@Autowired
	JobLauncherService jobLauncherService;
	
	@Value("${scheduler.cron}")
	String cron;
	
	@Value("${scheduler.name}")
	String name;
	
	@GetMapping(value="/api/jobs/launchJob/{jobName}")
	public void launch(@PathVariable("jobName") String jobName ) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		jobLauncherService.launchJob(jobName);
	}
	
	@GetMapping(value="/api/schedule/getschedule")
	public ResponseEntity<Cron>  getSchedule( ) {
			return ResponseEntity.ok(new Cron(name,cron));
	}
	
	@GetMapping("/api/jobs/feed")
	public SseEmitter feed() {
		SseEmitter sse = new SseEmitter(Long.MAX_VALUE);
		jobLauncherService.sses.put("jobs",sse);
		return sse;
	}
	@GetMapping("/api/jobs/getjobhistory")
	public List<JobHistory> getJobHistory() {
		return jobLauncherService.getJobHistory();
	}
}

