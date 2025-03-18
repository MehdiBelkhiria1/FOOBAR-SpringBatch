package com.example.demo.writer;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.example.demo.utils.Utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Component
@StepScope
public class Writer implements ItemWriter<String> {


    @Override
    public void write(Chunk<? extends String> chunk) throws Exception {
        // ajouter chaque element au output file
        Files.write(Paths.get(Utils.OUTPUT_FILE),
                chunk.getItems(),
                StandardOpenOption.CREATE, 
                StandardOpenOption.APPEND);
    }
}
