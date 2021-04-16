package com.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AWSHealthCheckController {

    @GetMapping("/checkStatus")
    public void checkStatus() {
    }

}
