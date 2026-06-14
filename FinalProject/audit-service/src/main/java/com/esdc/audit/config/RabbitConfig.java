package com.esdc.audit.config;

import com.esdc.events.EventRouting;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String AUDIT_QUEUE = "audit.q";

    @Bean
    public TopicExchange bookingEvents() {
        return new TopicExchange(EventRouting.EXCHANGE, true, false);
    }

    @Bean
    public Queue auditQueue() {
        return new Queue(AUDIT_QUEUE, true);
    }

    @Bean
    public Binding auditBookingBinding(Queue auditQueue, TopicExchange bookingEvents) {
        return BindingBuilder.bind(auditQueue).to(bookingEvents).with(EventRouting.PATTERN_ALL);
    }

    @Bean
    public Binding auditPaymentBinding(Queue auditQueue, TopicExchange bookingEvents) {
        return BindingBuilder.bind(auditQueue).to(bookingEvents).with(EventRouting.PATTERN_PAYMENT);
    }
}
