package com.util;

import java.util.UUID;

public class ToKen {
	public static String generate() {
		return UUID.randomUUID().toString();
	}
}
