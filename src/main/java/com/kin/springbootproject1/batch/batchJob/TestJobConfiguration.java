package com.kin.springbootproject1.batch.batchJob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestJobConfiguration {
    @Bean
    public Job testJob(
            JobRepository jobRepository,
            Step testStep
    ) {
        return new JobBuilder("testJob", jobRepository)
                .start(testStep)
                .build();
    }
}
