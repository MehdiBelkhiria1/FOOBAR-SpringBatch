package com.example.demo.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class ParallelMessagesController {

	    private final JobLauncher jobLauncher;
	    private final Job multiMessageJob;

	    public ParallelMessagesController(JobLauncher jobLauncher,
	                                 @Qualifier("multiMessageJob") Job multiMessageJob) {
	        this.jobLauncher = jobLauncher;
	        this.multiMessageJob = multiMessageJob;
	    }

	    @PostMapping("/run")
	    public ResponseEntity<String> runJob(@RequestParam int messageCount) {
	        try {
	            JobParameters params = new JobParametersBuilder()
	                    .addLong("time", System.currentTimeMillis())  
	                    .addLong("messageCount", (long) messageCount)
	                    .toJobParameters();
	            jobLauncher.run(multiMessageJob, params);
	            return ResponseEntity.ok("Job launched with messageCount: " + messageCount);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                 .body("Job failed: " + e.getMessage());
	        }
	    }
}
