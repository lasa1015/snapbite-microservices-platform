package com.shaluo.notificationservice.listener;

import com.shaluo.notificationservice.model.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {

    @RabbitListener(queues = "order.created.queue")
    public void handleOrderCreated(OrderCreatedEvent event) {
        System.out.println("📨 接收到订单消息: " + event.getOrderId()
                + " 用户：" + event.getUsername()
                + " 餐厅：" + event.getRestaurantId()
                + " 金额：" + event.getTotalPrice());

        // 在这里添加发邮件、推送通知等逻辑
    }
}
