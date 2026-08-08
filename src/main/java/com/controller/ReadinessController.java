package com.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReadinessController {

    private final DataSource dataSource;

    public ReadinessController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/readiness")
    public ResponseEntity<Map<String, Object>> readiness() {
        Map<String, Object> m = new LinkedHashMap<>();
        try (Connection c = dataSource.getConnection()) {
            m.put("db", "UP");
            m.put("status", "OK");
            return ResponseEntity.ok(m);
        } catch (Exception e) {
            m.put("db", "DOWN");
            m.put("status", "FAIL");
            m.put("error", e.getMessage());
            return ResponseEntity.status(503).body(m);
        }
    }
}
