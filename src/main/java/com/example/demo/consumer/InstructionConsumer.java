package com.example.demo.consumer;

import com.example.demo.models.JobInstruction;
import com.example.demo.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class InstructionConsumer {
	private final ObjectMapper mapper;

	private final JobLauncher jobLauncher;

	private final Job fooBarJob;
	private final Job multiMessageJob;

	private final JobExplorer jobExplorer;

	public InstructionConsumer(ObjectMapper mapper, JobLauncher jobLauncher, @Qualifier(Utils.JOB_FOOBAR) Job fooBarJob,
			@Qualifier(Utils.JOB_MULTI_MESSAGE) Job multiMessageJob, JobExplorer jobExplorer) {
		this.mapper = mapper;
		this.jobLauncher = jobLauncher;
		this.fooBarJob = fooBarJob;
		this.multiMessageJob = multiMessageJob;
		this.jobExplorer = jobExplorer;
	}

	@KafkaListener(topics = "job-instructions", groupId = "batch-group")
	public void onMessage(String json, Acknowledgment ack) throws Exception {
        JobInstruction instr = mapper.readValue(json, JobInstruction.class);
        
        Job job=null;
        String jobName=instr.getJobName();
        if(Utils.JOB_FOOBAR.equals(jobName)) {
        	job=fooBarJob;
        }else {
        	job=multiMessageJob;
        }
        
		JobParameters jobParameters = new JobParametersBuilder(jobExplorer).getNextJobParameters(job)
				.toJobParameters();

		JobExecution exec = jobLauncher.run(job, jobParameters);
		
		//Only if the job COMPLETES successfully do we commit the offset:
	    if (exec.getStatus() == BatchStatus.COMPLETED) {
	      ack.acknowledge();
	    } else {
	      // you could choose to retry, send to DLQ, etc.
	      throw new RuntimeException("Batch job failed; not committing offset");
	    }
	}
}