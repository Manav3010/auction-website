package com.auction.auctionservice.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.queue.expired}")
    private String expiredQueue;

    @Value("${rabbitmq.routing.expired}")
    private String expiredRoutingKey;

    @Bean
    public TopicExchange auctionExchange() {
        return new TopicExchange(exchangeName);
    }

    // Queue to listen for expired auctions
    @Bean
    public Queue expiredAuctionQueue() {
        return new Queue(expiredQueue);
    }

    @Bean
    public Binding expiredAuctionBinding(Queue expiredAuctionQueue, TopicExchange auctionExchange) {
        return BindingBuilder
                .bind(expiredAuctionQueue)
                .to(auctionExchange)
                .with(expiredRoutingKey);
    }
}
