package com.example.demo.reader;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import com.example.demo.utils.Utils;


@Component
@StepScope
public class Reader  implements ItemReader<Integer> {

    private Iterator<String> lines;

    public Reader() throws Exception {
        List<String> content = Files.readAllLines(Paths.get(Utils.INPUT_FILE));
       lines = content.iterator();
    }

    @Override
    public Integer read() {
        return lines.hasNext() ? Integer.parseInt(lines.next()) : null;
    }
}
