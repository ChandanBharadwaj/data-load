package com.chaitu.batch.models;

public class JobEvent {
	
	String name;
	boolean isFailed;
	
	public JobEvent(String name, String endTime, String status, boolean isFailed) {
		super();
		this.name = name;
		this.endTime = endTime;
		this.status = status;
		this.isFailed = isFailed;
	}
	public JobEvent(String name, String startTime) {
		super();
		this.name = name;
		this.startTime = startTime;
	}
	String startTime;
	String endTime;
	String status;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean isFailed() {
		return isFailed;
	}
	public void setFailed(boolean isFailed) {
		this.isFailed = isFailed;
	}
	
}
