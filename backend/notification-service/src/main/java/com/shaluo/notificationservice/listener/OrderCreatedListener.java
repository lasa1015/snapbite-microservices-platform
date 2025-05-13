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
        System.out.println("📨 Received order event: " + event.getOrderId()
                + " | User: " + event.getUsername()
                + " | Restaurant: " + event.getRestaurantId()
                + " | Total: " + event.getTotalPrice());

        // Construct email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lasa1015@163.com");    // Sender
        message.setTo("luosha1015@gmail.com");  // Recipient (currently set to developer's email for testing)
        message.setSubject("🍽️ New Order Received!");
        message.setText("Hello,\n\nA new order has just been placed:\n\n"
                + "Customer Username: " + event.getUsername()
                + "\nRestaurant ID: " + event.getRestaurantId()
                + "\nOrder Amount: €" + event.getTotalPrice()
                + "\nOrder ID: " + event.getOrderId()
                + "\n\nPlease process this order as soon as possible.\n\nBest regards,\nSnapBite System");

        mailSender.send(message);
    }
}
