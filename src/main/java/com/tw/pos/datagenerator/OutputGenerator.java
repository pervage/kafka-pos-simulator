package com.tw.pos.datagenerator;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import java.nio.file.Paths;
import java.util.List;

public class OutputGenerator {
    @SneakyThrows
    public void addReportList(List<String> outputList){
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        writer.writeValue(Paths.get("src/main/resources/output/output"+System.nanoTime()+".json").toFile(), String.valueOf(outputList));
    }
}
