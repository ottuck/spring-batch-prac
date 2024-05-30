package com.example.SpringBatchTutorial.job.DbDataReadWrite;

import com.example.SpringBatchTutorial.core.domain.accounts.Accounts;
import com.example.SpringBatchTutorial.core.domain.accounts.AccountsRepository;
import com.example.SpringBatchTutorial.core.domain.orders.OrdersRepository;
import com.example.SpringBatchTutorial.core.domain.orders.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

/*
 * desc : 주문테이블 정보 -> 정산 테이블로 이관 배치 작업
 * run arguments : --spring.batch.job.names=trMigrationJob
 * */

@Configuration
@RequiredArgsConstructor
public class TrMigrationConfig {

    @Autowired
    private OrdersRepository orderRepository;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job trMigrationJob(Step trMigrationStep) {
        return jobBuilderFactory.get("trMigrationJob")
                .incrementer(new RunIdIncrementer())
                .start(trMigrationStep)
                .build();
    }

    @JobScope
    @Bean
    public Step trMigrationStep(
            ItemReader trOrdersReader,
            ItemProcessor trOrderProcessor,
            ItemWriter trOrdersWriter) {

        return stepBuilderFactory.get("trMigrationStep")
                .<Orders, Accounts>chunk(5)
                .reader(trOrdersReader)
                .processor(trOrderProcessor)
//                .writer(items -> items.forEach(System.out::println))
                .writer(trOrdersWriter)
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Orders, Accounts> trMigrationProcessor() {
        return new ItemProcessor<Orders, Accounts>() {
            @Override
            public Accounts process(Orders item) throws Exception {
                return new Accounts(item);
            }
        };
    }

    @StepScope
    @Bean
    public RepositoryItemReader<Orders> trOrderReader() {
        return new RepositoryItemReaderBuilder<Orders>()
                .name("trOrderReader")
                .repository(orderRepository)
                .methodName("findAll")
                .pageSize(5)
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

// RepositoryItemWriter 사용시
    @StepScope
    @Bean
    public RepositoryItemWriter<Accounts> trMigrationWriter() {
        return new RepositoryItemWriterBuilder<Accounts>()
                .repository(accountsRepository)
                .methodName("save")
                .build();
    }

// ItemWriter 사용시
//    @StepScope
//    @Bean
//    public ItemWriter<Accounts> trOrdersWriter() {
//        return new ItemWriter<Accounts>() {
//            @Override
//            public void write(List<? extends Accounts> items) throws Exception {
//                items.forEach(item -> accountsRepository.save(item));
//            }
//        };
//    }
}