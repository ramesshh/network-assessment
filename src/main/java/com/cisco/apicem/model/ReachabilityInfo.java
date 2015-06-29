package com.cisco.apicem.model;

public class ReachabilityInfo {
	private ReachabilityDevice[] response;

	private String version;

	public ReachabilityDevice[] getResponse() {
		return response;
	}

	public void setResponse(ReachabilityDevice[] response) {
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