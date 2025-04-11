package com.example.demo.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.utils.Utils;

@Component
@EnableScheduling
public class JobScheduler {

    private volatile boolean enabled = false;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier(Utils.JOB_NAME)
    private Job myJob;

    @Scheduled(fixedDelay = 10000) // Runs every 10 sec (or adjust to your use case)
    public void scheduledJob() throws Exception {
        if (!enabled) return;

        JobParameters params = new JobParametersBuilder()
                .addLong("run.id", System.currentTimeMillis())
                .toJobParameters();

        System.out.println("Scheduled job triggered...");
        jobLauncher.run(myJob, params);
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
