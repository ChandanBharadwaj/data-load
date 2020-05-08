package com.chaitu.batch.writer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.chaitu.schema.model.TableSchema;

@Component
public class CustomItemWriter implements ItemWriter {

	TableSchema ts = new TableSchema();

	@Autowired
	JdbcTemplate jdbcTemplate;

	public CustomItemWriter(TableSchema ts) {
		this.ts = ts;
	}

	@Override
	public void write(List list) throws Exception {
		try {
			GsonJsonParser j = new GsonJsonParser();
			Map<String,String> map = (Map<String,String>)(Object)j.parseMap(ts.getSchema());
			List<String> types = new ArrayList<String>();
			map.values().forEach(item ->{
				types.add(item);
			});
			insertIntoDb(ts.getTableName(), new ArrayList<String>(map.keySet()),types, list);	
		}catch (Exception e){
			if( e instanceof IndexOutOfBoundsException) {				
				throw new RuntimeException("Error while writing the data to db, due to number fields mismatch");
			}
			throw new RuntimeException("Error while writing the data to db, Please check the field types in the schema");
		}
		
	}

	private int[] insertIntoDb(String tableName, List<String> headers, List<String> types, List<String[]> list) throws SQLException {
		int[] updateCounts = jdbcTemplate.batchUpdate(prepareQuery(tableName, headers),
				new BatchPreparedStatementSetter() {
					@Override
					public int getBatchSize() {
						return list.size();
					}
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						String[] intoDbValues = list.get(i);
						for (int j = 0; j < intoDbValues.length; j++) {
							switch(types.get(j)) {
							case "string":
								ps.setString(j + 1, intoDbValues[j]);
								break;
							case "number":
								if(!intoDbValues[j].isEmpty()) {
									ps.setLong(j + 1, Long.valueOf(intoDbValues[j]));									
								}else {									
									ps.setLong(j + 1, 0);
								}
								break;
							case "date":
								if(!intoDbValues[j].isEmpty()) {									
									ps.setDate(j + 1,  new java.sql.Date(Long.valueOf(intoDbValues[j])));
								}
								break;
							}
							
						}
					}
				});
		return updateCounts;

	}

	private String prepareQuery(String tableName, List<String> headers) {
		String query = "INSERT INTO " + tableName + " ( ";
		for (int i = 0; i < headers.size(); i++) {
			query = query + headers.get(i) + ", ";
		}
		query = StringUtils.removeEnd(query, ", ");
		query = query + " ) VALUES (";
		for (int i = 0; i < headers.size(); i++) {
			query = query + "?" + " , ";
		}
		query = StringUtils.removeEnd(query, ", ");
		query = query + " )";
		return query;
	}

}
