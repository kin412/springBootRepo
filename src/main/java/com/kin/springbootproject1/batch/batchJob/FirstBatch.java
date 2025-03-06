package com.kin.springbootproject1.batch.batchJob;

import com.kin.springbootproject1.batch.entity.AfterEntity;
import com.kin.springbootproject1.batch.entity.BeforeEntity;
import com.kin.springbootproject1.batch.repository.AfterRepository;
import com.kin.springbootproject1.batch.repository.BeforeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Slf4j
@Configuration
public class FirstBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final BeforeRepository beforeRepository;
    private final AfterRepository afterRepository;

    //생성자 방식으로 객체 주입
    public FirstBatch(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, BeforeRepository beforeRepository, AfterRepository afterRepository) {

        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.beforeRepository = beforeRepository;
        this.afterRepository = afterRepository;
    }

    //메서드 실행 순서대로 아래에 작성했음.

    @Bean
    public Job firstJob() {

        log.info("first job");

        return new JobBuilder("firstJob", jobRepository) // tracking 할 repository
                .start(firstStep())
                // .on().to() 다음스텝이 수행될 조건
                //.next() 스텝이 한개 이상일 경우
                .build();
    }
    //job 수행 전후에 실행될 특정작업 - jobListener

    @Bean
    public Step firstStep() {

        log.info("first step");

        return new StepBuilder("firstStep", jobRepository)
                //chunk() 최소단위지정, 대량의 데이터를 끊어서 처리하기위해
                //더 간단한 방식으로 Tasklet방식이 있는데, 거의 사용하지 않음
                //chunk단위 처리는 read 10번 -> process 10번 -> write 순으로 진행됨
                .<BeforeEntity, AfterEntity> chunk(10, platformTransactionManager)
                .reader(beforeReader())
                .processor(middleProcessor())
                .writer(afterWriter())
                //.faultTolerant() .skip() .noSkip() .skipLimit() .skipPolish() .retry 등도 있음
                .build();
    }

    //읽어올 entity
    @Bean
    public RepositoryItemReader<BeforeEntity> beforeReader() {

        return new RepositoryItemReaderBuilder<BeforeEntity>()
                .name("beforeReader")
                .pageSize(10)
                .methodName("findAll")
                .repository(beforeRepository)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    //중간에 할일
    @Bean
    public ItemProcessor<BeforeEntity, AfterEntity> middleProcessor() {

        return new ItemProcessor<BeforeEntity, AfterEntity>() {

            @Override
            public AfterEntity process(BeforeEntity item) throws Exception {

                AfterEntity afterEntity = new AfterEntity();
                afterEntity.setUsername(item.getUsername());

                return afterEntity;
            }
        };
    }

    //넣어줄 entity
    //writer 작업의 경우 jpa로 구성할 경우 jdbc보다 처리속도가 엄청나게 저하 됨
    @Bean
    public RepositoryItemWriter<AfterEntity> afterWriter() {

        return new RepositoryItemWriterBuilder<AfterEntity>()
                .repository(afterRepository)
                .methodName("save")
                .build();
    }

}
