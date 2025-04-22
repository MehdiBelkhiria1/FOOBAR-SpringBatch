package com.example.demo.writer;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.utils.Utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Component
@StepScope
public class Writer implements ItemWriter<String> {


	@Value("${app.output.dir}") 
	String outputFilePath;
	
    @Override
    public void write(Chunk<? extends String> chunk) throws Exception {
        // ajouter chaque element au output file
        Files.write(Paths.get(outputFilePath+Utils.OUTPUT_FILE),
                chunk.getItems(),
                StandardOpenOption.CREATE, 
                StandardOpenOption.APPEND);
    }
}
