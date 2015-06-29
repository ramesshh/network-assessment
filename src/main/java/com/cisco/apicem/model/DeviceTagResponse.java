package com.cisco.apicem.model;

public class DeviceTagResponse {

	private DeviceTag[] response;

	private String version;

	public DeviceTag[] getResponse() {
		return response;
	}

	public void setResponse(DeviceTag[] response) {
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
