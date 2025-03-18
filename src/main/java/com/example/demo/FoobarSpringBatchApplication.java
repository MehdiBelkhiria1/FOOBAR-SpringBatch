package com.example.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.demo")
public class FoobarSpringBatchApplication implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job myBatchJob;
    private final JobExplorer jobExplorer;
    
    public FoobarSpringBatchApplication(JobLauncher jobLauncher, Job myBatchJob, JobExplorer jobExplorer) {
        this.jobLauncher = jobLauncher;
        this.myBatchJob = myBatchJob;
        this.jobExplorer=jobExplorer;
    }

	public static void main(String[] args) {
		SpringApplication.run(FoobarSpringBatchApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {

        JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
        		.getNextJobParameters(myBatchJob)
                .toJobParameters();

        System.out.println("Starting Batch Job...");
        JobExecution jobExecution = jobLauncher.run(myBatchJob, jobParameters);

        while (jobExecution.isRunning()) {
            System.out.println("Job is still running...");
            Thread.sleep(3000);
        }

        System.out.println("Batch Job Status: " + jobExecution.getStatus());
		
	}

}
