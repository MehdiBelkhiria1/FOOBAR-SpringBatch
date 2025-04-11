package com.example.demo.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.core.step.tasklet.Tasklet;

public class PrintMessageTasklet implements Tasklet {
    private final int messageId;

    public PrintMessageTasklet(int messageId) {
        this.messageId = messageId;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        System.out.println("Thread " + Thread.currentThread().getName() +
                " processing message " + messageId);
        return RepeatStatus.FINISHED;
    }
}