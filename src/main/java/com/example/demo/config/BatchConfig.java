package com.example.demo.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.demo.processor.Processor;
import com.example.demo.reader.Reader;
import com.example.demo.tasklet.PrintMessageTasklet;
import com.example.demo.utils.Utils;
import com.example.demo.writer.Writer;

@Configuration
public class BatchConfig{
	
	
	    //config JobRepository obligatoire pour les tests unitaires depuis SpringBatch 5.0
	    @Bean
	    public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
	        JobRepositoryFactoryBean factoryBean = new JobRepositoryFactoryBean();
	        factoryBean.setDataSource(dataSource);
	        factoryBean.setTransactionManager(transactionManager);
	        factoryBean.afterPropertiesSet();
	        return factoryBean.getObject();
	    }
	
	    //in memory H2 database
		@Bean
	    public DataSource batchDataSource() {
	        return new EmbeddedDatabaseBuilder()
	            .setType(EmbeddedDatabaseType.H2)
	            .addScript("/org/springframework/batch/core/schema-h2.sql")
	            .generateUniqueName(true)
	            .build();
	    }
	
	    @Bean
	    public JdbcTransactionManager batchTransactionManager(DataSource dataSource) {
	        return new JdbcTransactionManager(dataSource);
	    }
	    
	    @Bean
	    public Step fooBarStep(Reader reader, Processor processor, Writer writer, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
	        return new StepBuilder(Utils.STEP_NAME, jobRepository)
	                .<Integer, String>chunk(5, transactionManager)
	                .reader(reader)
	                .processor(processor)
	                .writer(writer)
	                .allowStartIfComplete(true)
	                .build();
	    }

	    @Bean(Utils.JOB_FOOBAR)
	    public Job fooBarJob(Step fooBarQuixStep,JobRepository jobRepository) {
	        return new JobBuilder(Utils.JOB_FOOBAR, jobRepository)
	        		.incrementer(new RunIdIncrementer())
	                .start(fooBarQuixStep)
	                .build();
	    }
	    
	    //Async JobLauncher pour ameliorer les performances et permettre les call api rest (asynchrones)
	    @Bean
	    public JobLauncher createJobLauncher(ThreadPoolTaskExecutor taskExecutor,JobRepository jobRepository) throws Exception {
	      TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
	      jobLauncher.setJobRepository(jobRepository);
	      jobLauncher.setTaskExecutor(taskExecutor);
	      jobLauncher.afterPropertiesSet();
	      return jobLauncher;
	    }
	    
	    //pour les @StepScope
	    @Bean
	    public static CustomScopeConfigurer scopeConfigurer() {
	        CustomScopeConfigurer configurer = new CustomScopeConfigurer();
	        Map<String, Object> scopes = new HashMap<>();
	        scopes.put("step", new SimpleThreadScope());
	        configurer.setScopes(scopes);
	        return configurer;
	    }
	    
	    @Bean
	    public ThreadPoolTaskExecutor taskExecutor() {
		    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		    taskExecutor.setCorePoolSize(8);
		    taskExecutor.setMaxPoolSize(16);
		    taskExecutor.setQueueCapacity(32);
		    return taskExecutor;
		}
	    
	    
	    // Job-scoped bean: the split flow is built at job execution time using the job parameter.
	    @Bean
	    @JobScope
	    public Flow splitFlow(@Value("#{jobParameters['messageCount']}") Long messageCount,
	                          TaskExecutor taskExecutor, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
	        Flow[] flows = createFlowsForMessages(messageCount.intValue(), jobRepository, transactionManager);
	        return new FlowBuilder<Flow>("splitFlow")
	                .split(taskExecutor)
	                .add(flows)
	                .build();
	    }

	    // Create one flow (with one step) for each message.
	    public Flow[] createFlowsForMessages(int messageCount, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
	        List<Flow> flows = new ArrayList<>();
	        for (int i = 1; i <= messageCount; i++) {
	            String stepName = "printMessageStep-" + i;
	            Step step = new  StepBuilder(stepName, jobRepository)
	                    .tasklet(new PrintMessageTasklet(i),transactionManager)
	                    .build();
	            Flow flow = new FlowBuilder<Flow>("flow-" + i)
	                    .start(step)
	                    .build();
	            flows.add(flow);
	        }
	        return flows.toArray(new Flow[0]);
	    }

	    
	    @Bean(Utils.JOB_MULTI_MESSAGE)
	    public Job multiMessageJob(JobRepository jobRepository,
	                               Flow splitFlow) {
	        return new JobBuilder("multiMessageJob", jobRepository)
	        		.incrementer(new RunIdIncrementer())
	                .start(splitFlow)
	                .build()        
	                .build(); 
	    }

}
