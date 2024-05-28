package com.example.SpringBatchTutorial.job.ValidatedParam;

import com.example.SpringBatchTutorial.job.ValidatedParam.Validator.FileParamValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/*
 * desc : 파일 이름을 파라미터로 전달 후 검증
 * run arguments : --spring.batch.job.names=validatedParamJob -fileName=test.csv
 * */

@Configuration
@RequiredArgsConstructor
public class ValidatedParam {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    // Job 생성
    // incrementer : 동일한 Job Parameter로 여러번 실행할 때 Job Parameter를 증가시키는 역할
    // validator : Job Parameter를 검증하는 역할
    @Bean
    public Job validatedParamJob(Step validatedParamStep) {
        return jobBuilderFactory.get("validatedParamJob")
                .incrementer(new RunIdIncrementer())
//                .validator(new FileParamValidator())
                .validator(multipleValidator())
                .start(validatedParamStep)
                .build();
    }

    // CompositeJobParametersValidator : 여러개의 JobParametersValidator를 사용할 수 있도록 함
    private CompositeJobParametersValidator multipleValidator() {
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
        validator.setValidators(Arrays.asList(new FileParamValidator()));
        return validator;
    }

    // Step 생성
    // tasklet : 실제 Step 안에서 수행될 기능을 정의
    @JobScope
    @Bean
    public Step validatedParamStep(Tasklet validatedParamTasklet) {
        return stepBuilderFactory.get("validatedParamStep")
                .tasklet(validatedParamTasklet)
                .build();
    }

    // Tasklet 생성
    // execute : 실제 Step 안에서 수행될 기능을 정의
    // RepeatStatus.FINISHED : Step이 성공적으로 끝났음을 알림
    @StepScope
    @Bean
    public Tasklet validatedParamTasklet(@Value("#{jobParameters['fileName']}") String fileName) {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contributlion, ChunkContext chunkContext) throws Exception {
                System.out.println("validated Param Tasklet");
                return RepeatStatus.FINISHED;
            }
        };
    }
}
