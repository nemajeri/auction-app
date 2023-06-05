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
    public Queue auctionFinishedQueue() {
        return new Queue(AUCTION_FINISHED_QUEUE);
    }

    @Bean
    public DirectExchange auctionFinishedExchange() {
        return new DirectExchange(AUCTION_FINISHED_EXCHANGE);
    }

    @Bean
    public Binding auctionFinishedBinding(Queue auctionFinishedQueue, DirectExchange auctionFinishedExchange) {
        return BindingBuilder.bind(auctionFinishedQueue).to(auctionFinishedExchange).with(AUCTION_FINISHED_ROUTING_KEY);
    }

    @Bean
    public Queue outbidQueue() {
        return new Queue(OUTBID_QUEUE);
    }

    @Bean
    public DirectExchange outbidExchange() {
        return new DirectExchange(OUTBID_EXCHANGE);
    }

    @Bean
    public Binding outbidBinding(Queue outbidQueue, DirectExchange outbidExchange) {
        return BindingBuilder.bind(outbidQueue).to(outbidExchange).with(OUTBID_ROUTING_KEY);

    }
}
