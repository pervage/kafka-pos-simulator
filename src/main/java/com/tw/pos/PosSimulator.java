package com.tw.pos;

import com.tw.pos.datagenerator.InvoiceGenerator;
import com.tw.pos.service.GenericProducer;
import com.tw.pos.types.PosInvoice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PosSimulator {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        String topicName = AppConfigs.sourceTopicName;
        InvoiceGenerator invoiceGenerator = InvoiceGenerator.getInstance();
        GenericProducer<PosInvoice> posInvoiceProducer = new GenericProducer<>();
        while (invoiceGenerator.getNextInvoice() != null) {
            PosInvoice posInvoice = invoiceGenerator.getNextInvoice();
            posInvoiceProducer.sendMessage(topicName, posInvoice.getStoreID().toString(), posInvoice);
        }
        logger.info("Data Produced on topic {}", topicName);
    }
}
