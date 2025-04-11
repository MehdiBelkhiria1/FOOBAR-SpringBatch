package com.example.demo.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.utils.Utils;

@RestController
@RequestMapping("/api")
public class Controller {
    
    private final JobLauncher jobLauncher;
    
    private final Job myBatchJob;
    
    private final JobExplorer jobExplorer;

    public Controller(JobLauncher jobLauncher,  @Qualifier(Utils.JOB_NAME) Job myBatchJob, JobExplorer jobExplorer) {
        this.jobLauncher = jobLauncher;
        this.myBatchJob = myBatchJob;
        this.jobExplorer=jobExplorer;
    }

    //retourne string foobar a partir d`un numero
    @GetMapping("/convert/{number}")
    public String convertNumber(@PathVariable Integer number) {
        return Utils.processNumber(number);
    }
    
    //si on veut lancer le job a partir d`un webservice
    @PostMapping("/start")
    @Async
    public ResponseEntity<String> startBatchJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
            		.getNextJobParameters(myBatchJob)
                    .toJobParameters();

            jobLauncher.run(myBatchJob, jobParameters);
            return ResponseEntity.accepted().body("Job successfully started.");
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(500).body("Could not start job.");
        }
    }
}
