package com.zr.inventoryservice.service;

import com.zr.inventoryservice.dto.MaterialCheckRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryService {

    /**
     * 检查物料是否可用（模拟）
     */
    public Map<String, Object> checkMaterial(MaterialCheckRequest request) {

        Map<String, Object> result = new HashMap<>();

        // 模拟规则：数量 <= 100 即认为库存充足
        boolean available = request.getQuantity() <= 100;

        result.put("materialId", request.getMaterialId());
        result.put("requiredQuantity", request.getQuantity());
        result.put("available", available);

        return result;
    }
}
