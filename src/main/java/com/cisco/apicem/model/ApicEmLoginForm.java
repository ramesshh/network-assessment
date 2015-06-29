package com.cisco.apicem.model;

import java.io.Serializable;

public class ApicEmLoginForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId;

	private String username;

	private String password;

	private String apicemIP;

	private String version;

	private String location;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getApicemIP() {
		return apicemIP;
	}

	public void setApicemIP(String apicemIP) {
		this.apicemIP = apicemIP;
	}

	public String getVersion() {
		return version;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
