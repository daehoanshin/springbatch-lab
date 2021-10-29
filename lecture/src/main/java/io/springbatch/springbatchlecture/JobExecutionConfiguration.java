package io.springbatch.springbatchlecture;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

//@Configuration
@RequiredArgsConstructor
public class JobExecutionConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobExecutionListener jobExecutionListener;
//    @Bean
    public Job batchJob() {
        return jobBuilderFactory.get("batchJob")
                .start(step1())
                .next(step2())
                .listener(jobExecutionListener)
                .build();
    }

//    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("step1 was executed");
                        JobParameters jobParameters = contribution.getStepExecution().getJobExecution().getJobParameters();
                        ExecutionContext jobExecutionContext = contribution.getStepExecution().getJobExecution().getExecutionContext();
                        ExecutionContext stepExecutionContext = contribution.getStepExecution().getExecutionContext();

                        String jobName = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getJobName();
                        String stepName = chunkContext.getStepContext().getStepExecution().getStepName();

                        if(jobExecutionContext.get("jobName") == null) {
                            jobExecutionContext.put("jobName", jobName);
                        }

                        if(stepExecutionContext.get("stepName") == null) {
                            stepExecutionContext.put("stepName", stepName);
                        }

                        String name = jobParameters.getString("name");
                        Long seq = jobParameters.getLong("seq");
                        Date date = jobParameters.getDate("date");
                        Double age = jobParameters.getDouble("age");

                        System.out.println("name = " + name);
                        System.out.println("seq = " + seq);
                        System.out.println("date = " + date);
                        System.out.println("age = " + age);
                        System.out.println("age = " + age);


                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

//    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(new CustomTasklet())
                .build();
    }
}

