package com.chaitu.batch.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chaitu.batch.models.JobHistory;

public interface JobHistoryRepo extends JpaRepository<JobHistory, Long>{
	List<JobHistory> findAllByOrderByStartTimeDesc();
}
