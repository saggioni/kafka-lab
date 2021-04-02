package br.com.saggioni.lab.springapplication.config;

import br.com.saggioni.lab.domain.Proposal;
import br.com.saggioni.lab.springapplication.util.CompletableReplyingKafkaOperations;
import br.com.saggioni.lab.springapplication.util.CompletableReplyingPartitionKafkaTemplate;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfigRequestReplyConsumer {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${kafka.topic.proposal.request}")
    private String requestTopic;

    @Value("${kafka.topic.proposal.reply}")
    private String replyTopic;

    @Value("${kafka.request-reply.timeout-ms}")
    private Long replyTimeout;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return props;
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    /* ----------------------------------------
    Left side (sync API to async Kafka) start
    requestReplyClient.requestReply()
    ------------------------------------------- */
    @Bean //Request-Reply (Entrypoint Sync to Async)
    public CompletableReplyingKafkaOperations<String, String, Proposal> requestReplyClient() {
        var requestReply = new CompletableReplyingPartitionKafkaTemplate<>(requestProducerFactory(), replyListenerContainer());
        requestReply.setDefaultTopic(requestTopic);
        requestReply.setReplyTimeout(replyTimeout);
        return requestReply;
    }

    @Bean //Command-Request Producer - step 1
    public ProducerFactory<String, String> requestProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean //Reply Consumer - step 4
    public ConsumerFactory<String, Proposal> replyConsumerFactory() {
        JsonDeserializer<Proposal> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages(Proposal.class.getPackage().getName());
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(),
                jsonDeserializer);
    }

    @Bean //Reply Consumer - step 4
    public KafkaMessageListenerContainer<String, Proposal> replyListenerContainer() {
        ContainerProperties containerProperties = new ContainerProperties(replyTopic);
        return new KafkaMessageListenerContainer<>(replyConsumerFactory(), containerProperties);
    }

    /* ----------------------------------------
    Right side (sync API to async Kafka) start
    @KafkaListener/@SendTo
    ------------------------------------------- */
    @Bean //Request-Command Consumer - Async side (step 2)
    public ConsumerFactory<String, String> requestConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(), new StringDeserializer(), new JsonDeserializer<>());
    }

    @Bean //Reply Producer Async side (step 3)
    public ProducerFactory<String, Proposal> replyProducerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean //Reply Producer Async side (step 3)
    public KafkaTemplate<String, Proposal> replyTemplate() {
        return new KafkaTemplate<>(replyProducerFactory());
    }

    @Bean //Request-Reply Async side
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> requestReplyListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(requestConsumerFactory());
        factory.setReplyTemplate(replyTemplate());
        return factory;
    }
}