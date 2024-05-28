package com.example.SpringBatchTutorial.job.HelloWorld;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobLoggerListener implements JobExecutionListener {

    private static final String BEGIN_MESSAGE = "{} Job is Running!";
    private static final String END_MESSAGE = "{} Job is Done! Status: {}";

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info(BEGIN_MESSAGE, jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info(END_MESSAGE, jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());

        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.info("JOB FAILED!");
        }
    }

}
