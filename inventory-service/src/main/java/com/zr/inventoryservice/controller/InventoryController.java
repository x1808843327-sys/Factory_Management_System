package com.zr.inventoryservice.controller;

import com.zr.inventoryservice.dto.MaterialCheckRequest;
import com.zr.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Value("${server.port}")
    private String port;

    @Resource
    private InventoryService inventoryService;

    @GetMapping("/hello")
    public String hello() {
        return "hello from inventory-service, port=" + port;
    }


    /**
     * 检查物料可用性
     */
    @PostMapping("/materials/availability")
    public Map<String, Object> checkMaterialAvailability(
            @RequestBody MaterialCheckRequest request) {
        return inventoryService.checkMaterial(request);
    }
}
