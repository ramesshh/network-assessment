package com.softql.apicem.model;

import java.io.Serializable;

public class DeviceDetails implements Serializable {

	private DiscoveryDevices response;

	private String version;

	public DiscoveryDevices getResponse() {
		return response;
	}

	public void setResponse(DiscoveryDevices response) {
		this.response = response;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "ClassPojo [response = " + response + ", version = " + version + "]";
	}
}