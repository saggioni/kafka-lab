package br.com.saggioni.lab.springapplication.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SimpleConsumer {
    @KafkaListener(topics = "${kafka.topic.proposal.request}", containerFactory = "kafkaListenerContainerFactory" )
    public void consume(String message) {
        System.out.println(String.format("%s: %s", SimpleConsumer.class.getName(), message));
    }
}
