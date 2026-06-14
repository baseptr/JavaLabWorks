package com.esdc.booking.config;

import com.esdc.events.EventRouting;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String SAGA_QUEUE = "booking.saga.q";

    @Bean
    public TopicExchange bookingEvents() {
        return new TopicExchange(EventRouting.EXCHANGE, true, false);
    }

    @Bean
    public Queue sagaQueue() {
        return new Queue(SAGA_QUEUE, true);
    }

    @Bean
    public Binding paymentConfirmedBinding(Queue sagaQueue, TopicExchange bookingEvents) {
        return BindingBuilder.bind(sagaQueue).to(bookingEvents).with(EventRouting.RK_PAYMENT_CONFIRMED);
    }

    @Bean
    public Binding paymentFailedBinding(Queue sagaQueue, TopicExchange bookingEvents) {
        return BindingBuilder.bind(sagaQueue).to(bookingEvents).with(EventRouting.RK_PAYMENT_FAILED);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("com.esdc.events");
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}
