package com.example.demo.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReaderTest {

	private Reader reader;

	@BeforeEach
	void setUp() throws Exception {
		reader = new Reader("src/test/resources");
	}

	@Test
	void testReader() throws Exception {
		assertEquals(1, reader.read());
		assertEquals(3, reader.read());
	}
}
