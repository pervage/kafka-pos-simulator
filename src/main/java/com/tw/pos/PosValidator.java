package com.tw.pos;

import com.tw.pos.datagenerator.OutputGenerator;
import com.tw.pos.service.GenericConsumer;
import com.tw.pos.types.KafkaOutput;
import com.tw.pos.types.PosInvoice;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PosValidator {
    private static final Logger logger = LogManager.getLogger();

    @SneakyThrows
    public static void main(String[] args) {
        GenericConsumer<PosInvoice> consumer = new GenericConsumer<>(AppConfigs.sourceTopicName);
        OutputGenerator outputGenerator = new OutputGenerator();
        List<String> kafkaOutputs = new ArrayList<>();
        long endTime = System.nanoTime() + TimeUnit.NANOSECONDS.convert(Duration.ofMinutes(1));
        while (System.nanoTime() < endTime) {
            ConsumerRecords<String, PosInvoice> records = consumer.poll();
            for (ConsumerRecord<String, PosInvoice> record : records) {
                if (record.value().getDeliveryType().equals("HOME-DELIVERY") &&
                    record.value().getDeliveryAddress().getContactNumber().equals("")) {
                    logger.info("invalid record - " + record.value().getInvoiceNumber());
                    System.out.println("invalid record - " + record.value().getInvoiceNumber());
                    kafkaOutputs.add(KafkaOutput.builder()
                            .invoiceNumber(record.value().getInvoiceNumber().toString())
                            .status("FAIL")
                            .build().toString());
                } else {
                    logger.info("valid record - " + record.value().getInvoiceNumber());
                    System.out.println("valid record - " + record.value().getInvoiceNumber());
                    kafkaOutputs.add(KafkaOutput.builder()
                            .invoiceNumber(record.value().getInvoiceNumber().toString())
                            .status("PASS")
                            .build().toString());;
                }
            }
        }
        outputGenerator.addReportList(kafkaOutputs);
    }
}
