package com.tw.pos.service;

import com.tw.pos.AppConfigs;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.HashMap;
import java.util.Map;

public class GenericProducer<T> {
    private final KafkaProducer<String,T> kafkaProducer;

    public GenericProducer(){
        this.kafkaProducer = new KafkaProducer<>(producerConfigs());
    }
    public Map<String, Object> producerConfigs(){
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        properties.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, AppConfigs.schemaRegistryServers);
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,true);
        return properties;
    }

    public void sendMessage(String topicName, String Key, T value){
        ProducerRecord<String, T> producerRecord = new ProducerRecord<>(topicName, Key, value);
        kafkaProducer.send(producerRecord, (recordMetadata, e) -> {
            if (e == null) {
                System.out.println(recordMetadata.toString());
            } else {
                e.printStackTrace();
            }
        });
        kafkaProducer.flush();
    }
}
