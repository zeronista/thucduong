package com.nepnhaxua.thucduong.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @GetMapping("/")
    public String ping() {
        return "Thuc Duong API is running";
    }
}


