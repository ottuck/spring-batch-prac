package com.example.SpringBatchTutorial.job.FileDataReadWrite;

import com.example.SpringBatchTutorial.job.FileDataReadWrite.dto.Player;
import com.example.SpringBatchTutorial.job.FileDataReadWrite.dto.PlayerYears;
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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/*
 * desc : fileDataReadWriteJob 을 실행하기 위한 설정 파일
 * Player DTO 를 읽어 PlayerYears DTO 로 변환하는 작업을 수행
 * run arguments : --spring.batch.job.names=fileDataReadWriteJob
 * */

@Configuration
public class FileDataReadWriteConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job fileDataReadWriteJob(Step fileDataReadWriteStep) {
        return jobBuilderFactory.get("fileDataReadWriteJob")
                .incrementer(new RunIdIncrementer())
                .start(fileDataReadWriteStep)
                .build();
    }

    @JobScope
    @Bean
    public Step fileDataReadWriteStep(ItemReader playerItemReader, ItemProcessor playerItemProcessor, ItemWriter playerItemWriter) {
        return stepBuilderFactory.get("fileDataReadWriteStep")
                .<Player, PlayerYears>chunk(5)
                .reader(playerItemReader)
                .processor(playerItemProcessor)
//                .writer(items -> items.forEach(System.out::println))
                .writer(playerItemWriter)
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Player, PlayerYears> playerItemProcessor() {
        return new ItemProcessor<Player, PlayerYears>() {
            @Override
            public PlayerYears process(Player item) throws Exception {
                return new PlayerYears(item);
            }
        };
    }

    @StepScope
    @Bean
    public FlatFileItemReader<Player> playerItemReader() {
        return new FlatFileItemReaderBuilder<Player>()
                .name("playerItemReader")
                .resource(new FileSystemResource("Players.csv"))
                .lineTokenizer(new DelimitedLineTokenizer() {{
                    setDelimiter(DelimitedLineTokenizer.DELIMITER_COMMA);
                    setNames("ID", "LastName", "FirstName", "Position", "BirthYear", "DebutYear");
                }})
                .fieldSetMapper(new PlayerFieldSetMapper())
                .linesToSkip(1)
                .build();
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<PlayerYears> playerItemWriter() {
        BeanWrapperFieldExtractor<PlayerYears> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[] {"ID", "LastName", "FirstName", "Position", "BirthYear", "DebutYear", "YearsExperience"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<PlayerYears> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);

        FileSystemResource outputResource = new FileSystemResource("Players_output.csv");

        return new FlatFileItemWriterBuilder<PlayerYears>()
                .name("playerItemWriter")
                .resource(outputResource)
                .lineAggregator(lineAggregator)
                .build();
    }

}
