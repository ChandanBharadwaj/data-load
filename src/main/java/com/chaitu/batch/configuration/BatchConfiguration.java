package com.chaitu.batch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.chaitu.batch.writer.CustomItemWriter;
import com.chaitu.poi.PassThroughRowMapper;
import com.chaitu.poi.PoiItemReader;
import com.chaitu.schema.model.TableSchema;
import com.chaitu.schema.repo.TableSchemaRepo;

@Configuration
public class BatchConfiguration {

	@Autowired
	TableSchemaRepo tableSchemaRepo;
	
	@Value("${job.chunk}")
	Integer chunk;
	
	@Autowired
    private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
    private StepBuilderFactory stepBuilderFactory;
	
	 @Bean
	 public Job readExcelFileJob() {
	        return jobBuilderFactory
	                .get("ReadCSVFileJob")
	                .incrementer(new RunIdIncrementer())
	                .start(step(null))
	                .build();
	 }
	@Bean
	@JobScope
	public Step step(@Value("#{jobParameters[jobName]}") String jobName) {
		return stepBuilderFactory.get("step")
				.chunk(chunk)
				.reader(reader(jobName))
				.writer(writer(jobName))
				.build();
	}
	
	@Bean
	@StepScope
	ItemStreamReader reader(@Value("#{jobParameters[jobName]}") String jobName) {
		TableSchema ts =tableSchemaRepo.findByTableName(jobName);
		if(ts.getFileName().endsWith(".csv")) {			
			FlatFileItemReader freader = new FlatFileItemReader();
			freader.setResource(new ClassPathResource(ts.getFileName()));
			freader.setLinesToSkip(1);
			freader.setLineMapper(new CustomLineMapper());
			return freader;
		}else if(ts.getFileName().endsWith(".xlsx")) {
			PoiItemReader reader = new PoiItemReader();
			reader.setResource(new ClassPathResource(ts.getFileName()));
			reader.setRowMapper(new PassThroughRowMapper());
			reader.setLinesToSkip(1);
			return reader;	
		}
		return null;
	}

	@Bean
	@StepScope
	public ItemWriter writer(@Value("#{jobParameters[jobName]}") String jobName) {
		TableSchema ts =tableSchemaRepo.findByTableName(jobName);
		return new CustomItemWriter(ts);
	}
	@Bean
	TableSchema ts() {
		return new TableSchema();
	}
}
