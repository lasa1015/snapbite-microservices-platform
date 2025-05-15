package com.shaluo.notificationservice.listener;

import com.shaluo.notificationservice.model.OrderCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderCreatedListenerTest {

    private JavaMailSender mailSender;
    private OrderCreatedListener listener;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        listener = new OrderCreatedListener(mailSender); // ✅ 注意构造器注入
    }

    @Test
    void testHandleOrderCreated_shouldSendEmail() {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(java.util.UUID.randomUUID());
        event.setUsername("john");
        event.setRestaurantId("R123");
        event.setTotalPrice(23.5);

        // 调用 listener
        listener.handleOrderCreated(event);

        // 验证邮件是否发送
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage sentMessage = captor.getValue();
        assertNotNull(sentMessage);
        assertEquals("🍽️ New Order Received!", sentMessage.getSubject());
        assertTrue(sentMessage.getText().contains("john"));
        assertTrue(sentMessage.getText().contains("R123"));
    }
}
