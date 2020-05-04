package com.chaitu.batch.models;

import java.io.Serializable;

public class Cron implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	String cron;
	public Cron(String name, String cron){
		this.name = name;
		this.cron = cron;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}
	
}