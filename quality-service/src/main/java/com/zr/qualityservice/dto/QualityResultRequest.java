package com.zr.qualityservice.dto;

public class QualityResultRequest {

    /**
     * PASS / FAIL
     */
    private String result;

    /**
     * 关联订单编号（用于通知订单服务）
     */
    private String orderId;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
