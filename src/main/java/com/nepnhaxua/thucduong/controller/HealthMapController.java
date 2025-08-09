package com.nepnhaxua.thucduong.controller;

import com.nepnhaxua.thucduong.api.ApiResponse;
import com.nepnhaxua.thucduong.entity.BodyHealthMap;
import com.nepnhaxua.thucduong.service.BodyHealthMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/health-map")
@RequiredArgsConstructor
public class HealthMapController {

    private final BodyHealthMapService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BodyHealthMap>>> list() {
        return ResponseEntity.ok(ApiResponse.ok(service.getAllBodyParts()));
    }

    @GetMapping("/{part}")
    public ResponseEntity<ApiResponse<BodyHealthMap>> detail(@PathVariable("part") String part) {
        return ResponseEntity.ok(ApiResponse.ok(service.getBodyPartDetails(part)));
    }
}


