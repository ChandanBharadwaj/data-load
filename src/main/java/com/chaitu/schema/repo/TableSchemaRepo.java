package com.chaitu.schema.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.chaitu.schema.model.TableSchema;

@Repository
public interface TableSchemaRepo extends JpaRepository<TableSchema, Long>{

	TableSchema findByTableName(String tableName);
	
	@Query(value = "SELECT TABLE_NAME FROM C_TABLE_SCHEMA",nativeQuery = true)
	List<String> findAllTables();
	
	
}
