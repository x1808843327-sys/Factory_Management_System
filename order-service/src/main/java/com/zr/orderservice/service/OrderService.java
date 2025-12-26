package com.zr.orderservice.service;

import com.zr.orderservice.dto.CreateOrderRequest;
import com.zr.orderservice.dto.QualityFeedbackRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderService {

    @Resource
    private RestTemplate restTemplate;

    /**
     * 创建订单（模拟实现）
     */
    public Map<String, Object> createOrder(CreateOrderRequest request) {
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", UUID.randomUUID().toString());
        result.put("productId", request.getProductId());
        result.put("quantity", request.getQuantity());
        result.put("status", "CREATED");
        return result;
    }

    /**
     * 更新订单状态（模拟实现）
     */
    public Map<String, Object> updateOrderStatus(String orderId, String status) {
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", orderId);
        result.put("status", status);
        return result;
    }

    /**
     * 处理质量反馈（模拟实现）
     */
    public Map<String, Object> handleQualityFlow(
            String orderId,
            QualityFeedbackRequest request) {

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", orderId);
        result.put("issueId", request.getIssueId());
        result.put("qualityResult", request.getQualityResult());

        // 1️⃣ 更新订单状态
        String newStatus = "FAIL".equalsIgnoreCase(request.getQualityResult())
                ? "QUALITY_FAIL"
                : "PLANNED";

        result.put("newStatus", newStatus);

        // 2️⃣ 查询生产计划
        Map planResult = restTemplate.getForObject(
                "http://production-planning-service/plans/by-order/{orderId}",
                Map.class,
                orderId
        );

        result.put("productionPlan", planResult);

        return result;
    }
}
