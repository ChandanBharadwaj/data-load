package com.chaitu.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.opencsv.CSVReader;

@RestController
public class SampleController {

	@Value("${aws.credentials.access-key}")
	private String accessKey;

	@Value("${aws.credentials.secretKey}")
	private String secretkey;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@GetMapping("/loadfroms3")
	public String load() throws IOException, SQLException {
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretkey);
		AmazonS3 s3client = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.AP_SOUTH_1).build();
		ObjectListing objects = s3client.listObjects("saichandanaws");
		System.out.println(objects.getObjectSummaries());
		S3Object s3object = s3client.getObject("saichandanaws", "ST.csv");
		S3ObjectInputStream inputStream = s3object.getObjectContent();

		@SuppressWarnings("resource")
		CSVReader reader = new CSVReader(new InputStreamReader(inputStream, "UTF-8"));
		List<String[]> ls= reader.readAll();
		String[] header =ls.get(0);
		ls.remove(0);
		insertIntoDb("emp", header, ls);
		return "Done";
	}

	private int[] insertIntoDb(String tableName, String[] headers, List<String[]> ls) throws SQLException {
		ResultSet a= jdbcTemplate.getDataSource().getConnection().getMetaData().getColumns(null, null, tableName, headers[1]);
		if (a.next()) {
		      System.out.println("Exists !");
		    }else {
		    	jdbcTemplate.execute("create table "+tableName +" "+perpareCreateQuery(headers));
		    }
		  
		int[] updateCounts = jdbcTemplate.batchUpdate(prepareQuery(tableName, headers), new BatchPreparedStatementSetter() {

					@Override
					public int getBatchSize() {
						return ls.size();
					}
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						 String[] intoDbValues  =ls.get(i);
						for (int j = 0; j < intoDbValues.length; j++) {
							ps.setString(j+1, intoDbValues[j]);
						}
					}
				});
		return updateCounts;

	}

	private String perpareCreateQuery(String[] headers) {
	String s = "( ";
			for (int i = 0; i < headers.length; i++) {
				s = s + headers[i] + " VARCHAR(100), ";
			}
			s = StringUtils.removeEnd(s, ", ");
			s=s+" ) ";
		return s;
	}

	private String prepareQuery(String tableName, String[] headers) {
		String query = "INSERT INTO " + tableName + " ( ";
		for (int i = 0; i < headers.length; i++) {
			query = query + headers[i] + ", ";
		}
		query = StringUtils.removeEnd(query, ", ");
		query = query + " ) VALUES (";
		for (int i = 0; i < headers.length; i++) {
			query = query + "?" + " , ";
		}
		query = StringUtils.removeEnd(query, ", ");
		query = query+" )";
		System.out.println(query);
		return query;
	}
}
