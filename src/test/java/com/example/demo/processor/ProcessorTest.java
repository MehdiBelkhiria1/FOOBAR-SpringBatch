package com.example.demo.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ProcessorTest {

	 private final Processor processor = new Processor();

	    @ParameterizedTest(name = "processor({0}) should return \"{1}\"")
	    @CsvSource({
	        "1, 1",
	        "3, FOOFOO",
	        "5, BARBAR",
	        "7, QUIX",
	        "9, FOO",
	        "51, FOOBAR",
	        "53, BARFOO",
	        "33, FOOFOOFOO",
	        "15, FOOBARBAR"
	    })
	    void testProcessor(int input, String expectedOutput) {
	        assertEquals(expectedOutput, processor.process(input));
	    }
}
