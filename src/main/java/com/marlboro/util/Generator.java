package com.marlboro.util;

public class Generator {
	public static String makeSynthesisKey(String ... values) {
		String synthesisKey = "";
		for (int i = 0; i < values.length; i ++) {
			if (i < values.length - 1)
				synthesisKey += (values[i] + "/");
			else
				synthesisKey += (values[i]);
		}
		
		return synthesisKey;
	}
}
