package com.shaluo.orderservice.model.postgres;

public enum OrderStatus {
    CREATED,     // 用户下单，等待商家处理
    ACCEPTED,    // 商家已接单
    SHIPPED,     // 商家已发货或派送中
    COMPLETED,   // 用户确认收货
    CANCELED     // 用户或商家取消
}
