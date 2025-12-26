package com.zr.orderservice.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zr.orderservice.dto.CreateOrderRequest;
import com.zr.orderservice.dto.QualityFeedbackRequest;
import com.zr.orderservice.dto.UpdateOrderStatusRequest;
import com.zr.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Value("${server.port}")
    private String port;

    @Resource
    private OrderService orderService;

    @Resource
    private RestTemplate restTemplate;


    @GetMapping("/hello")
    @SentinelResource(
            value = "orderHello",
            blockHandler = "orderHelloBlockHandler"
    )
    public ResponseEntity<String> hello() throws InterruptedException {
        Thread.sleep(3000);
        return ResponseEntity.ok("hello from order-service, port=" + port);
    }

    /**
     * Sentinel 流控降级处理
     */
    public ResponseEntity<String> orderHelloBlockHandler(BlockException ex) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body("当前订单请求过多，系统已进行限流，请稍后再试");
    }

    /**
     * 创建订单
     */
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) {

        Map<String, Object> orderResult = orderService.createOrder(request);

        Map<String, Object> planRequest = new HashMap<>();
        planRequest.put("orderId", UUID.randomUUID().toString());
        planRequest.put("productId", "P001");
        planRequest.put("quantity", 10);

        Map<String, Object> planResult = restTemplate.postForObject(
                "http://production-planning-service/plans",
                planRequest,
                Map.class
        );

        orderResult.put("plan", planResult);
        return ResponseEntity.ok(orderResult);
    }


    /**
     * 更新订单状态
     */
    @PutMapping("/{orderId}")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable String orderId,
            @RequestBody Map<String, Object> body) {

        String status = body.get("status").toString();
        return ResponseEntity.ok(
                orderService.updateOrderStatus(orderId, status)
        );
    }


    /**
     * 接收质量反馈
     */
    @PostMapping("/{orderId}/quality-feedbacks")
    public ResponseEntity<?> receiveQualityFeedback(
            @PathVariable String orderId,
            @RequestBody QualityFeedbackRequest request) {

        return ResponseEntity.ok(
                orderService.handleQualityFlow(orderId, request)
        );
    }

}
