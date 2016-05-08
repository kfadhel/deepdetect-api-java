package com.deepdetect.api.enums;

public enum Operation {
	INFO("info"),SERVICES("services"),TRAIN("train"),PREDICT("predict");

	private String value;

	public String getValue() {
		return value;
	}

	private Operation(String val) {
		value = val;
	}
}
