package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.demo.processor.Processor;
import com.example.demo.reader.Reader;
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

	    @Bean
	    public Job fooBarJob(Step fooBarQuixStep,JobRepository jobRepository) {
	        return new JobBuilder(Utils.JOB_NAME, jobRepository)
	        		.incrementer(new RunIdIncrementer())
	                .start(fooBarQuixStep)
	                .build();
	    }
	    
	    //Async JobLauncher pour ameliorer les performances et permettre les call api rest (asynchrones)
	    @Bean
	    public JobLauncher createJobLauncher(JobRepository jobRepository) throws Exception {
	      TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
	      jobLauncher.setJobRepository(jobRepository);
	      jobLauncher.setTaskExecutor( new SimpleAsyncTaskExecutor());
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
    
}
