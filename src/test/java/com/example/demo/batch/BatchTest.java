package com.example.demo.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.example.demo.config.BatchConfig;
import com.example.demo.processor.Processor;
import com.example.demo.reader.Reader;
import com.example.demo.utils.Utils;
import com.example.demo.writer.Writer;


@SpringBatchTest
@ContextConfiguration(classes = {BatchConfig.class, Reader.class, Processor.class, Writer.class})
@SpringBootTest
public class BatchTest {
	
	@Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    
    @Test
    public void testHello(@Autowired @Qualifier(Utils.JOB_FOOBAR) Job job) throws Exception {
    	this.jobLauncherTestUtils.setJob(job);
        var jobExecution =
                jobLauncherTestUtils.launchJob();
        while (jobExecution.isRunning()) {
            Thread.sleep(500); // poll every 500ms
        }
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    
    }

}
