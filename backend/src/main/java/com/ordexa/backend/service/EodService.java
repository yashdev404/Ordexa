package com.ordexa.backend.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class EodService {

    public Map<String, Object> getEodSummary(UUID factoryId, LocalDate date) {
        Map<String, Object> summary = new HashMap<>();
        summary.put("shiftId", "SHIFT-" + date + "-A");
        summary.put("date", date);
        summary.put("factoryId", factoryId);
        summary.put("totalInboundGrnUnits", 7600);
        summary.put("totalOutboundShippedUnits", 4400);
        summary.put("totalDamagedUnits", 12);
        summary.put("pendingApprovalsCount", 3);
        summary.put("isSealed", false);
        return summary;
    }
}
