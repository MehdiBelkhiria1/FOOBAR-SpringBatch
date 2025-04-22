package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.models.JobInstruction;
import com.example.demo.producer.InstructionProducer;

@RestController
@RequestMapping("/kafka")
public class InstructionController {
  private final InstructionProducer producer;
  public InstructionController(InstructionProducer producer) {
    this.producer = producer;
  }

  @PostMapping("/launch")
  public ResponseEntity<String> launch(@RequestBody JobInstruction instr) {
    producer.send(instr);
    return ResponseEntity.accepted().body("Instruction sent to Kafka");
  }
}
