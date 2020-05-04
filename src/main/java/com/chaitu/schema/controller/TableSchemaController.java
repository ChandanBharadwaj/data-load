package com.chaitu.schema.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chaitu.schema.model.TableSchema;
import com.chaitu.schema.repo.TableSchemaRepo;

@SuppressWarnings("deprecation")
@RestController
@CrossOrigin
public class TableSchemaController {

	@Autowired
	TableSchemaRepo tableSchemaRepo;

	@Autowired
	JdbcTemplate jd;
	
	@PostMapping(value = "/api/schema/save", consumes = "application/json")
	public ResponseEntity<String> save(@RequestBody TableSchema request) {
		request.setSchema(StringEscapeUtils.unescapeJson(request.getSchema()));
		request.setLastUpdated(LocalDateTime.now());
		tableSchemaRepo.save(request);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/api/schema/update", consumes = "application/json")
	public ResponseEntity<String> update(@RequestBody TableSchema request) {
		request.setSchema(StringEscapeUtils.unescapeJson(request.getSchema()));
		String sql = "UPDATE c_table_schema c SET c.file_name=? , c.file_path = ?,"
				+ " c.last_updated= ?, c.table_schema= ? WHERE c.table_name = ?";
		jd.update(sql, request.getFileName(),
		request.getFilePath(), LocalDateTime.now(), request.getSchema(),
		request.getTableName());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/api/schema/findall")
	public ResponseEntity<List<TableSchema>> getAllSchema() {
		return ResponseEntity.ok().body(
				StreamSupport.stream(tableSchemaRepo.findAll().spliterator(), false).collect(Collectors.toList()));
	}

	@GetMapping("/api/schema/findAllTables")
	public ResponseEntity<List<String>> getAllTables() {
		return ResponseEntity.ok().body(tableSchemaRepo.findAllTables());
	}
}
