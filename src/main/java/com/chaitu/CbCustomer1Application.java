package com.chaitu;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBatchProcessing
@EnableJpaRepositories
@EnableScheduling
public class CbCustomer1Application {

	public static void main(String[] args) {
		SpringApplication.run(CbCustomer1Application.class, args);
	}
	  
}
