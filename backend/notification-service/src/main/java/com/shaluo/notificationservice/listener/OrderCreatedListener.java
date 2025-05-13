package com.shaluo.notificationservice.listener;

import com.shaluo.notificationservice.model.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedListener {

    @Autowired
    private JavaMailSender mailSender;

    @RabbitListener(queues = "order.created.queue")
    public void handleOrderCreated(OrderCreatedEvent event) {
        System.out.println("📨 接收到订单消息: " + event.getOrderId()
                + " 用户：" + event.getUsername()
                + " 餐厅：" + event.getRestaurantId()
                + " 金额：" + event.getTotalPrice());

        // 构建邮件
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lasa1015@163.com");    // 发件人
        message.setTo("luosha1015@gmail.com");  // 收件人（此处先填你自己的邮箱测试）
        message.setSubject("🍽️ 有新订单啦！");
        message.setText("用户 " + event.getUsername()
                + " 刚刚下了一笔订单\n餐厅ID: " + event.getRestaurantId()
                + "\n订单金额: " + event.getTotalPrice()
                + "\n订单ID: " + event.getOrderId());

        mailSender.send(message);
    }
}
