package com.example.demo.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ProcessorTest {

	 private final Processor processor = new Processor();

	    @Test
	    void testProcessor() {
	        assertEquals("1", processor.process(1));
	        assertEquals("FOOFOO", processor.process(3));  
	        assertEquals("BARBAR", processor.process(5));  
	        assertEquals("QUIX", processor.process(7));   
	        assertEquals("FOO", processor.process(9));   
	        assertEquals("FOOBAR", processor.process(51));
	        assertEquals("BARFOO", processor.process(53)); 
	        assertEquals("FOOFOOFOO", processor.process(33));
	        assertEquals("FOOBARBAR", processor.process(15)); 
	    }
}
