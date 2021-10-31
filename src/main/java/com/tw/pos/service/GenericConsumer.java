package com.tw.pos.service;

import com.tw.pos.AppConfigs;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GenericConsumer<T> {

    private KafkaConsumer<String, T> kafkaConsumer;

    public GenericConsumer(String topicName){
        kafkaConsumer = new KafkaConsumer<>(consumerConfigs());
        kafkaConsumer.subscribe(Collections.singleton(topicName));
    }
    public static Map<String, Object> consumerConfigs() {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        consumerProps.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG,AppConfigs.schemaRegistryServers);
        consumerProps.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG,true);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, AppConfigs.groupID);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return consumerProps;
    }

    public ConsumerRecords<String, T> poll(){
        return kafkaConsumer.poll(Duration.ofSeconds(3));
    }

}
