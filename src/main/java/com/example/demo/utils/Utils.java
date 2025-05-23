package com.example.demo.utils;


public class Utils {

	public final static String INPUT_FILE="/input.txt";
	public final static String OUTPUT_FILE="/output.txt";
	

	public static final String JOB_FOOBAR = "fooBarJob";
	public static final String JOB_MULTI_MESSAGE = "multiMessageJob";
	public static final String STEP_NAME = "fooBarStep";

	public static String processNumber(Integer number) {
		StringBuilder result = new StringBuilder();

		if (number % 3 == 0) {
			result.append("FOO");
		}
		if (number % 5 == 0) {
			result.append("BAR");
		}

		for (char digit : number.toString().toCharArray()) {
			if (digit == '3') {
				result.append("FOO");
			} else if (digit == '5') {
				result.append("BAR");
			} else if (digit == '7') {
				result.append("QUIX");
			}
		}

		return result.length() > 0 ? result.toString() : String.valueOf(number);
	}
}
