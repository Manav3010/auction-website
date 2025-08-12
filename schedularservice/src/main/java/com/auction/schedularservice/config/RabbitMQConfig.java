package com.auction.schedularservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.queue.created}")
    private String createdQueue;

    @Value("${rabbitmq.routing.created}")
    private String createdRoutingKey;

    @Bean
    public TopicExchange auctionExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Queue createdQueue() {
        return new Queue(createdQueue);
    }

    @Bean
    public Binding bindingCreatedQueue() {
        return BindingBuilder
                .bind(createdQueue())
                .to(auctionExchange())
                .with(createdRoutingKey);
    }
}

