package com.example.demo.processor;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.demo.utils.Utils;

@Component
@StepScope
public class Processor implements ItemProcessor<Integer, String> {

    @Override
    public String process(Integer number) {
        return Utils.processNumber(number);
    }
}
