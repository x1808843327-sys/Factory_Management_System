//package com.zr.gatewayservice;
//
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import javax.annotation.Resource;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api")
//public class GatewayForwardController {
//
//    用于实现随机负载均衡
//    @Resource
//    private RestTemplate restTemplate;
//
//    @PostMapping("/order")
//    public Object forwardOrder(@RequestBody Map<String, Object> body) {
//        return restTemplate.postForObject(
//                "http://order-service/order",
//                body,
//                Object.class
//        );
//    }
//}
