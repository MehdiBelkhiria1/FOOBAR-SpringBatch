package com.example.demo.models;

import java.util.Map;

public class JobInstruction {

	private String jobName;
	private Map<String, String> params;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	  
}
