package com.zr.qualityservice.service;

import com.zr.qualityservice.dto.CreateQualityIssueRequest;
import com.zr.qualityservice.dto.QualityResultRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class QualityService {

    @Resource
    private RestTemplate restTemplate;

//    public Map<String, Object> feedbackResult(
//            String issueId,
//            QualityResultRequest request) {
//            已将业务逻辑合并到 /issues接口中
//    }

    /**
     * 创建质量问题（人工 / 外部系统触发）
     */
    public Map<String, Object> createQualityIssue(CreateQualityIssueRequest request) {

        Map<String, Object> result = new HashMap<>();

        // 1️⃣ 创建质量问题
        String issueId = UUID.randomUUID().toString();
        result.put("issueId", issueId);
        result.put("orderId", request.getOrderId());
        result.put("description", request.getDescription());
        result.put("qualityResult", "FAIL");
        result.put("status", "REPORTED");

        // 2️⃣ 立即反馈订单服务（自动）
        Map<String, Object> orderFeedback = new HashMap<>();
        orderFeedback.put("issueId", issueId);
        orderFeedback.put("qualityResult", "FAIL");

        Map orderResult = restTemplate.postForObject(
                "http://order-service/order/"
                        + request.getOrderId()
                        + "/quality-feedback",
                orderFeedback,
                Map.class
        );

        result.put("orderServiceResult", orderResult);
        result.put("message", "质量问题已确认，已自动反馈订单服务");

        return result;
    }


}

