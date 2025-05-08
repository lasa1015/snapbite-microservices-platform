package com.shaluo.orderservice.dto.request;

import lombok.Data;

// 前端提交结算信息（目前只需要地址）
@Data
public class CheckoutRequest {

    private String recipient;  // 收件人姓名
    private String phone;      // 收件人电话
    private String address;    // 地址



}
