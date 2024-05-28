package com.example.SpringBatchTutorial.job.ValidatedParam.Validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

// JobParameters를 검증하는데 사용하는 JobParametersValidator 인터페이스를 구현하여 validate 메소드를 오버라이드
// validate 메소드는 JobParameters를 받아서 검증을 수행하고, 문제가 있으면 JobParametersInvalidException을 던짐
// 파일 이름이 .csv로 끝나지 않으면 예외를 던짐

public class FileParamValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
        String fileName = jobParameters.getString("fileName");

        if (!StringUtils.endsWithIgnoreCase(fileName, ".csv")) {
            throw new JobParametersInvalidException("This file is not a CSV file");

        }
    }
}
