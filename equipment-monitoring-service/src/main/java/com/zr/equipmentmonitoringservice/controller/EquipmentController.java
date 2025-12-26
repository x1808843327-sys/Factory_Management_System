package com.zr.equipmentmonitoringservice.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    @Value("${server.port}")
    private String port;
    @Resource
    private RestTemplate restTemplate;


    @PostMapping("/{equipmentId}/report-exception")
    public Map<String, Object> reportException(@PathVariable Long equipmentId) {
        // 1) 查询异常信息（本服务内部）
        Map<String, Object> ex = getEquipmentExceptions(String.valueOf(equipmentId)).getBody();

        // 2) 调用生产计划服务：按设备id查计划并调整
        Map planHandle = restTemplate.postForObject(
                "http://production-planning-service/planning/device/{equipmentId}/exception",
                null,
                Map.class,
                equipmentId
        );

        Map<String, Object> res = new HashMap<>();
        res.put("equipmentId", equipmentId);
        res.put("exceptions", ex);
        res.put("planningHandle", planHandle);
        return res;
    }


    /* =======================
       查询设备状态（核心接口）
       ======================= */

    @GetMapping("/{equipmentId}/status")
    @SentinelResource(
            value = "equipmentStatus",
            blockHandler = "equipmentStatusBlockHandler"
    )
    public ResponseEntity<Map<String, Object>> getStatus(@PathVariable Long equipmentId) {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("equipmentId", equipmentId);
        result.put("status", "RUNNING");
        result.put("serverPort", port);

        return ResponseEntity.ok(result);
    }

    /* =======================
       查询设备异常信息
       ======================= */

    @GetMapping("/{equipmentId}/exceptions")
    public ResponseEntity<Map<String, Object>> getEquipmentExceptions(
            @PathVariable String equipmentId) {

        Map<String, Object> result = new HashMap<>();
        result.put("equipmentId", equipmentId);
        result.put("exceptions", new String[]{"OVERHEAT"});

        return ResponseEntity.ok(result);
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello from equipment-monitoring-service, port=" + port;
    }

    /* =======================
       Sentinel 降级处理
       ======================= */

    public ResponseEntity<Map<String, Object>> equipmentStatusBlockHandler(
            Long equipmentId,
            BlockException ex) {

        Map<String, Object> fallback = new HashMap<>();
        fallback.put("equipmentId", equipmentId);
        fallback.put("status", "UNKNOWN");
        fallback.put("message", "设备监控服务响应超时，已触发熔断");

        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(fallback);
    }
}
