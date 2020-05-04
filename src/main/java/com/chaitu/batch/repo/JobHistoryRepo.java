package com.chaitu.batch.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.chaitu.batch.models.JobHistory;

public interface JobHistoryRepo extends CrudRepository<JobHistory, Long>{
	List<JobHistory> findAllByOrderByStartTime();
}
