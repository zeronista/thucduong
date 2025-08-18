package com.nepnhaxua.thucduong.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RootController {

    @GetMapping("/status")
    public String status() {
        return "Thuc Duong API is running";
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
