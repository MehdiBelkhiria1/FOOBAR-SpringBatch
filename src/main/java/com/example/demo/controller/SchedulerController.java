package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.scheduler.JobScheduler;

@RestController
@RequestMapping("/schedule")
public class SchedulerController {

    @Autowired
    private JobScheduler jobScheduler;

    @PostMapping("/start")
    public ResponseEntity<String> startScheduler() {
        jobScheduler.enable();
        return ResponseEntity.ok("Scheduler started.");
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopScheduler() {
        jobScheduler.disable();
        return ResponseEntity.ok("Scheduler stopped.");
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Scheduler is " + (jobScheduler.isEnabled() ? "ENABLED" : "DISABLED"));
    }
}
