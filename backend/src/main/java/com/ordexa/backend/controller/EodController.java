package com.ordexa.backend.controller;

import com.ordexa.backend.service.EodService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/eod")
public class EodController {

    private final EodService eodService;

    public EodController(EodService eodService) {
        this.eodService = eodService;
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getEodSummary(
            @RequestParam UUID factoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate targetDate = date != null ? date : LocalDate.now();
        return ResponseEntity.ok(eodService.getEodSummary(factoryId, targetDate));
    }
}
