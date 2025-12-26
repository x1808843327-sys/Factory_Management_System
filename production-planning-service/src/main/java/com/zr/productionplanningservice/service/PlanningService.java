package com.zr.productionplanningservice.service;

import com.zr.productionplanningservice.dto.CreatePlanRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PlanningService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 根据订单查询生产计划（模拟）
     */
    public Map<String, Object> getPlanByOrder(String orderId) {
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", orderId);
        result.put("planId", "PLAN-001");
        result.put("status", "CREATED");
        return result;
    }



    /**
     * 创建生产计划
     */
    public Map<String, Object> createPlan(CreatePlanRequest request) {

        Map<String, Object> result = new HashMap<>();

        // 1️⃣ 检查计划容量
        if (!checkCapacity()) {
            result.put("orderId", request.getOrderId());
            result.put("status", "FAILED_CAPACITY");
            return result;
        }

        // 2️⃣ 调库存服务
        Map<String, Object> materialRequest = new HashMap<>();
        materialRequest.put("materialId", "M001");
        materialRequest.put("quantity", 10);

        Map inventoryResult = restTemplate.postForObject(
                "http://inventory-service/inventory/materials/check",
                materialRequest,
                Map.class
        );

        // 3️⃣ 调设备服务（负载均衡生效点）
        Map equipmentResult = restTemplate.getForObject(
                "http://equipment-monitoring-service/equipment/{equipmentId}/status",
                Map.class,
                1001L
        );

        // 4️⃣ 返回结果
        result.put("planId", UUID.randomUUID().toString());
        result.put("orderId", request.getOrderId());
        result.put("inventoryCheck", inventoryResult);
        result.put("equipmentCheck", equipmentResult);
        result.put("status", "CREATED");

        return result;
    }

    /**
     * 🔒 内部容量检查（不暴露为接口）
     */
    private boolean checkCapacity() {
        // 这里是模拟逻辑，真实项目可能查数据库/规则引擎
        return true;
    }

    /**
     * 设备异常处理流程：
     * 根据设备 ID 查询生产计划，并进行重排
     */
    public Map<String, Object> handleDeviceException(Long equipmentId) {

        Map<String, Object> result = new HashMap<>();

        // 1️⃣ 模拟：根据设备 ID 查询生产计划
        String planId = "PLAN-EX-001";
        String orderId = "ORDER-1001";

        result.put("equipmentId", equipmentId);
        result.put("planId", planId);
        result.put("orderId", orderId);

        // 2️⃣ 模拟判断是否有可用设备
        boolean hasAlternativeDevice = false;

        if (hasAlternativeDevice) {
            // 有其他设备，可重排
            result.put("planStatus", "RESCHEDULED");
            result.put("message", "检测到设备异常，已切换至备用设备重新排产");

            // 3️⃣ 通知订单服务：已重新排产
            Map<String, Object> orderUpdate = new HashMap<>();
            orderUpdate.put("status", "RESCHEDULED");

            restTemplate.postForObject(
                    "http://order-service/order/" + orderId + "/status",
                    orderUpdate,
                    Map.class
            );
        } else {
            // 无可用设备，只能等待
            result.put("planStatus", "WAITING");
            result.put("message", "当前无可用设备，生产计划已暂停");

            // 3️⃣ 通知订单服务：等待设备
            Map<String, Object> orderUpdate = new HashMap<>();
            orderUpdate.put("status", "WAITING_FOR_DEVICE");

            restTemplate.postForObject(
                    "http://order-service/order/" + orderId + "/status",
                    orderUpdate,
                    Map.class
            );
        }

        return result;
    }

}

