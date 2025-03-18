package com.example.demo.processor;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class Processor implements ItemProcessor<Integer, String> {

    @Override
    public String process(Integer number) {
        StringBuilder result = new StringBuilder();

        if (number % 3 == 0) {
        	result.append("FOO");
        } 
        if (number % 5 == 0) {
        	result.append("BAR");
        } 

        for (char digit : number.toString().toCharArray()) {
            if (digit == '3') {
            	result.append("FOO");
            }else if (digit == '5') {
            	result.append("BAR");
            }else if (digit == '7') {
            	result.append("QUIX");
            }
        }

        return result.length() > 0 ? result.toString() : String.valueOf(number);
    }
}
