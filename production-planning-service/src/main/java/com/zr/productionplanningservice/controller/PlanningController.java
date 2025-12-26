package com.zr.productionplanningservice.controller;

import com.zr.productionplanningservice.dto.CreatePlanRequest;
import com.zr.productionplanningservice.service.PlanningService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plans")
public class PlanningController {

    @Value("${server.port}")
    private String port;

    @Resource
    private PlanningService planningService;
    @Resource
    private RestTemplate restTemplate;


    @GetMapping("/hello")
    public String hello() {
        return "hello from production-planning-service, port=" + port;
    }

    /**
     * 创建生产计划
     * 内部会完成：
     * 1. 计划容量检查
     * 2. 物料可用性检查
     * 3. 设备可用性检查
     */
    @PostMapping
    public Map<String, Object> createPlan(
            @RequestBody CreatePlanRequest request) {

        return planningService.createPlan(request);
    }


    /**
     * 根据订单查询生产计划
     */
    @GetMapping("/by-order/{orderId}")
    public Map<String, Object> getPlanByOrder(
            @PathVariable String orderId) {
        return planningService.getPlanByOrder(orderId);
    }


    /**
     * 正常生产流程：检查设备运行状态
     */
    @GetMapping("/{planId}/equipment/availability")
    public Map<String, Object> checkEquipment(@PathVariable String planId) {

        Long equipmentId = 1001L;

        return restTemplate.getForObject(
                "http://equipment-monitoring-service/equipment/{equipmentId}",
                Map.class,
                equipmentId
        );
    }

    /**
     * 正常生产流程：检查物料是否充足
     */
    @PostMapping("/{planId}/materials/availability")
    public Map<String, Object> checkMaterials(
            @PathVariable String planId,
            @RequestBody Map<String, Object> request) {
        List<Map<String, Object>> materialList = (List<Map<String, Object>>) request.get("materialList");
        Map<String, Object> result = new HashMap<>();
        result.put("planId", planId);
        result.put("materialAvailable", true);
        result.put("unavailableMaterials", new HashMap<>());

        for (Map<String, Object> material : materialList) {
            String materialId = (String) material.get("materialId");
            Integer quantity = (Integer) material.get("quantity");

            Map<String, Object> inventoryRequest = new HashMap<>();
            inventoryRequest.put("materialId", materialId);
            inventoryRequest.put("quantity", quantity);

            Map<String, Object> inventoryResult = restTemplate.postForObject(
                    "http://inventory-service/inventory/materials/availability",
                    inventoryRequest,
                    Map.class
            );

            boolean available = (Boolean) inventoryResult.get("available");
            if (!available) {
                result.put("materialAvailable", false);
                ((Map<String, Object>) result.get("unavailableMaterials")).put(
                        materialId, "需求数量：" + quantity + "，当前库存不足"
                );
            }
        }
        return result;
    }

    /**
     * 质量问题追溯：查询设备异常信息
     */
    @GetMapping("/{planId}/equipment/exceptions")
    public Map<String, Object> checkEquipmentExceptions(
            @PathVariable String planId) {

        Long equipmentId = 1001L;

        return restTemplate.getForObject(
                "http://equipment-monitoring-service/equipment/{equipmentId}/exceptions",
                Map.class,
                equipmentId
        );
    }

    /**
     * 设备异常处理入口
     * 由设备监控服务调用
     */
    @PostMapping("/equipment/{equipmentId}/adjustments")
    public Map<String, Object> handleDeviceException(
            @PathVariable Long equipmentId) {

        return planningService.handleDeviceException(equipmentId);
    }





}


