package com.atlantbh.auctionappbackend.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.atlantbh.auctionappbackend.utils.Constants.*;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue auctionQueue() {
        return new Queue(AUCTION_QUEUE);
    }

    @Bean
    public DirectExchange auctionExchange() {
        return new DirectExchange(AUCTION_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue auctionQueue, DirectExchange auctionExchange) {
        return BindingBuilder.bind(auctionQueue).to(auctionExchange).with(ROUTING_KEY);
    }

}
