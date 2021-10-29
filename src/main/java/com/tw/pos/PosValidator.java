package com.tw.pos;

import com.tw.pos.service.GenericConsumer;
import com.tw.pos.types.PosInvoice;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PosValidator {
    private static final Logger logger = LogManager.getLogger();

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        GenericConsumer<PosInvoice> consumer = new GenericConsumer<>(AppConfigs.sourceTopicName);
        while (true) {
            ConsumerRecords<String, PosInvoice> records = consumer.poll();
            for (ConsumerRecord<String, PosInvoice> record : records) {
                if (record.value().getDeliveryType().equals("HOME-DELIVERY") &&
                    record.value().getDeliveryAddress().getContactNumber().equals("")) {
                    //Invalid
                    logger.info("invalid record - " + record.value().getInvoiceNumber());
                } else {
                    //Valid
                    logger.info("valid record - " + record.value().getInvoiceNumber());
                }
            }
        }
    }
}
