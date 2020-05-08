package com.chaitu.batch.configuration;

import org.springframework.batch.item.file.LineMapper;

public class CustomLineMapper implements LineMapper<String[]> {

	@Override
	public String[] mapLine(String line, int lineNumber) throws Exception {
		return line.split(",");
	}

}
