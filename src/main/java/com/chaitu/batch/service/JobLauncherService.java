package com.chaitu.batch.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.chaitu.batch.models.JobEvent;
import com.chaitu.batch.models.JobHistory;
import com.chaitu.batch.repo.JobHistoryRepo;

@Service
public class JobLauncherService {
	public final Map<String,SseEmitter> sses = new ConcurrentHashMap<>();
	@Autowired
	JobLauncher jobLauncher;
	
	@Autowired
	Job job;
	
	@Autowired
	JobHistoryRepo jobHistoryRepo;
	
	public void launchJob(String jobName) {
		LocalDateTime startTime;
		LocalDateTime endTime;
		String status;
		startTime = LocalDateTime.now();
		JobParameters j = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
				.addString("jobName", jobName).toJobParameters();
		sendEvent(new JobEvent(jobName, startTime.toString()));
		try {
			jobLauncher.run(job, j);
			status = "Success";
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			status = "Failed : " + e.getMessage();
		}
		endTime = LocalDateTime.now();
		sendEvent(new JobEvent(jobName,endTime.toString(),status));
		JobHistory jh = new JobHistory();
		jh.setJobName(jobName);
		jh.setEndTime(endTime);
		jh.setStartTime(startTime);
		jh.setStatus(status);
		jobHistoryRepo.save(jh);
	}

	public void sendEvent( JobEvent je){
		sses.forEach( (key ,sse) -> {
			try {
				sse.send(je);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	public List<JobHistory> getJobHistory(){
		return jobHistoryRepo.findAllByOrderByStartTime();
	}
	
}
