package com.zr.qualityservice.controller;

import com.zr.qualityservice.dto.CreateQualityIssueRequest;
import com.zr.qualityservice.dto.QualityResultRequest;
import com.zr.qualityservice.service.QualityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/quality")
public class QualityController {

    @Value("${server.port}")
    private String port;

    @Resource
    private QualityService qualityService;


    @GetMapping("/hello")
    public String hello() {
        return "hello from quality-service, port=" + port;
    }


    /**
     * 创建质量问题（人工 / 外部系统入口）
     */
    @PostMapping("/issues")
    public Map<String, Object> createQualityIssue(
            @RequestBody CreateQualityIssueRequest request) {
        return qualityService.createQualityIssue(request);
    }

    /**
     * 反馈质检结果
     * 在内部调用订单服务，触发订单状态变化
     */
//    @PostMapping("/issues/{issueId}/result")
//    public Map<String, Object> feedbackQualityResult(
//
//    }
}
