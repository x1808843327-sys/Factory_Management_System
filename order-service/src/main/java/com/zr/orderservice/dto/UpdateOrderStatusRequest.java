package com.zr.orderservice.dto;

public class UpdateOrderStatusRequest {

    /**
     * CREATED
     * WAITING_MATERIAL
     * WAITING_EQUIPMENT
     * PLANNED
     * QUALITY_FAIL
     */
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
