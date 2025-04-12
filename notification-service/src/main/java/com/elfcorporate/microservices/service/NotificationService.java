package com.elfcorporate.microservices.service;

import com.elfcorporate.microservices.order.event.OrderPlacedEvent;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.Span;


@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final Tracer tracer;

    private final JavaMailSender javaMailSender;

    @Observed(name = "notification.kafka.listener")
    @KafkaListener(topics = "order-placed", groupId = "notification-service")
    public void listen(OrderPlacedEvent orderPlacedEvent){
        Span span = this.tracer.nextSpan().name("order-placed-listener").start();
        try (Tracer.SpanInScope scope = this.tracer.withSpan(span)) {
            // Business logic here
            System.out.println("Got message: " + orderPlacedEvent);

        log.info("Got message from order-placed topic {}", orderPlacedEvent);

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springshop@email.com");
            messageHelper.setTo(orderPlacedEvent.getEmail().toString());
            messageHelper.setSubject(String.format("Your Order with OrderNumber %s is placed successfully", orderPlacedEvent.getOrderNumber()));
            messageHelper.setText(String.format("""
                            Hi %s, %s

                            Your order with order number %s is now placed successfully.
                            
                            Best Regards
                            Spring Shop
                            """,
                    orderPlacedEvent.getFirstName().toString(),
                    orderPlacedEvent.getLastName().toString(),
                    orderPlacedEvent.getOrderNumber()));
        };
        try {
            javaMailSender.send(messagePreparator);
            log.info("Order Notifcation email sent!!");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new RuntimeException("Exception occurred when sending mail to springshop@email.com", e);
        }
        } finally {
            span.end();
        }
    }

}
