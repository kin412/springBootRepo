package com.kin.springbootproject1.batch.batchJob;

import com.kin.springbootproject1.batch.entity.WinEntity;
import com.kin.springbootproject1.batch.repository.WinRepository;
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

import java.util.Collections;
import java.util.Map;

@Configuration
public class SecondBatch {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final WinRepository winRepository;

    public SecondBatch(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, WinRepository winRepository) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.winRepository = winRepository;
    }

    @Bean
    public Job secondJob() {

        return new JobBuilder("secondJob", jobRepository)
                .start(secondStep())
                .build();
    }

    @Bean
    public Step secondStep() {

        return new StepBuilder("secondStep", jobRepository)
                .<WinEntity, WinEntity> chunk(10, platformTransactionManager)
                .reader(winReader())
                .processor(trueProcessor())
                .writer(winWriter())
                .build();
    }

    @Bean
    public RepositoryItemReader<WinEntity> winReader() {

        return new RepositoryItemReaderBuilder<WinEntity>()
                .name("winReader")
                .pageSize(10)
                .methodName("findByWinGreaterThanEqual")
                .arguments(Collections.singletonList(10L))
                .repository(winRepository)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<WinEntity, WinEntity> trueProcessor() {

        return item -> {
            item.setReward(true);
            return item;
        };
    }

    @Bean
    public RepositoryItemWriter<WinEntity> winWriter() {

        return new RepositoryItemWriterBuilder<WinEntity>()
                .repository(winRepository)
                .methodName("save")
                .build();
    }

    /* jpa가 아닌 jdbc 구현법

        //JdbcCursorItemReader
        db자체의 cursor를 사용하여 전체테이블에서 cursor가 한칸씩 이동하며 데이터를 가져옴. (대세아닌듯)
        @Bean
        public JdbcCursorItemReader<CustomerCredit> itemReader(DataSource dataSource) {
            String sql = "select ID, NAME, CREDIT from CUSTOMER";
            return new JdbcCursorItemReaderBuilder<CustomerCredit>().name("customerReader")
                    .dataSource(dataSource)
                    .sql(sql)
                    .rowMapper(new CustomerCreditRowMapper())
                    .build();
        }

        //JdbcPagingItemReader
        //데이터 테이블에서 묶음 단위로 데이터를 가져오는 방식으로 sql 구문 생성시 offset과 limit를 배치단에서 자동으로 조합하여 쿼리를 날림 (이게 대세인듯)
        @Bean
        @StepScope
        public JdbcPagingItemReader<CustomerCredit> itemReader(DataSource dataSource,
                                                               @Value("#{jobParameters['credit']}") Double credit) {
            Map<String, Object> parameterValues = new HashMap<>();
            parameterValues.put("statusCode", "PE");
            parameterValues.put("credit", credit);
            parameterValues.put("type", "COLLECTION");

            return new JdbcPagingItemReaderBuilder<CustomerCredit>().name("customerReader")
                    .dataSource(dataSource)
                    .selectClause("select NAME, ID, CREDIT")
                    .fromClause("FROM CUSTOMER")
                    .whereClause("WHERE CREDIT > :credit")
                    .sortKeys(Map.of("ID", Order.ASCENDING))
                    .rowMapper(new CustomerCreditRowMapper())
                    .pageSize(2)
                    .parameterValues(parameterValues)
                    .build();
        }

        //processor는 데이터베이스 및 인터페이스에 상관이 없기때문에 생략

        //JdbcBatchItemWriter
        @Bean
        public JdbcBatchItemWriter<CustomerCredit> itemWriter(DataSource dataSource) {
            String sql = "UPDATE CUSTOMER set credit = :credit where id = :id";
            return new JdbcBatchItemWriterBuilder<CustomerCredit>().dataSource(dataSource)
                    .sql(sql)
                    .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                    .assertUpdates(true)
                    .build();
        }


        이외에도 mongodb, file 등등 다양한 방식이 있음.
     */

}
