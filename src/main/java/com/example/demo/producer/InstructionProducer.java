package com.example.demo.producer;

import com.example.demo.models.JobInstruction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class InstructionProducer {
  private final KafkaTemplate<String,String> tpl;
  private final ObjectMapper mapper;

  public InstructionProducer(KafkaTemplate<String,String> tpl, ObjectMapper mapper) {
    this.tpl = tpl;
    this.mapper = mapper;
  }

  public void send(JobInstruction instr) {
    try {
      String json = mapper.writeValueAsString(instr);
      tpl.send("job-instructions", instr.getJobName(), json);
      System.out.println("Producer sent message to kafka");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}