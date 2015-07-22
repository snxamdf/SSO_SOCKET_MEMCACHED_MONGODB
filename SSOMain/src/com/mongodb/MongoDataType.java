package com.mongodb;

public class MongoDataType {
	public MongoDataType() {

	}

	private Object value;

	public MongoDataType(Object value) {
		this.value = value;
	}

	public Integer toInteger() {
		return value == null ? 0 : Integer.parseInt(value.toString());
	}

	public String toString() {
		return value == null ? null : value.toString();
	}
}
