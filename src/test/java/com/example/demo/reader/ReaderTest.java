package com.example.demo.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ReaderTest {

	    @Mock
	    private Iterator<String> mockIterator;
	
	    @InjectMocks
	    private Reader reader;
	
	    @BeforeEach
	    void setUp() throws Exception {
	        MockitoAnnotations.openMocks(this);
	        when(mockIterator.hasNext()).thenReturn(true, true, false);
	        when(mockIterator.next()).thenReturn("3", "5");
	    }
	
	    @Test
	    void testReader() throws Exception {
	        assertEquals(3, reader.read());
	        assertEquals(5, reader.read());
	        assertNull(reader.read());  
	    }
}
