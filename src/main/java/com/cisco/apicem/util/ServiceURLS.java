package com.cisco.apicem.util;

public enum ServiceURLS {
	TICKET("/ticket");

	private String value;

	ServiceURLS(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
