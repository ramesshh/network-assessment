package com.softql.apicem.model;

public class ReachabilityDevice {
	private String enablePassword;

	private String id;

	private String protocolList;

	private String protocolUsed;

	private String mgmtIp;

	private String userName;

	private String password;

	private String reachabilityFailureReason;

	private String discoveryStartTime;

	private String reachabilityStatus;

	public String getEnablePassword() {
		return enablePassword;
	}

	public void setEnablePassword(String enablePassword) {
		this.enablePassword = enablePassword;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProtocolList() {
		return protocolList;
	}

	public void setProtocolList(String protocolList) {
		this.protocolList = protocolList;
	}

	public String getProtocolUsed() {
		return protocolUsed;
	}

	public void setProtocolUsed(String protocolUsed) {
		this.protocolUsed = protocolUsed;
	}

	public String getMgmtIp() {
		return mgmtIp;
	}

	public void setMgmtIp(String mgmtIp) {
		this.mgmtIp = mgmtIp;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getReachabilityFailureReason() {
		return reachabilityFailureReason;
	}

	public void setReachabilityFailureReason(String reachabilityFailureReason) {
		this.reachabilityFailureReason = reachabilityFailureReason;
	}

	public String getDiscoveryStartTime() {
		return discoveryStartTime;
	}

	public void setDiscoveryStartTime(String discoveryStartTime) {
		this.discoveryStartTime = discoveryStartTime;
	}

	public String getReachabilityStatus() {
		return reachabilityStatus;
	}

	public void setReachabilityStatus(String reachabilityStatus) {
		this.reachabilityStatus = reachabilityStatus;
	}

	@Override
	public String toString() {
		return "ClassPojo [enablePassword = " + enablePassword + ", id = " + id + ", protocolList = " + protocolList
				+ ", protocolUsed = " + protocolUsed + ", mgmtIp = " + mgmtIp + ", userName = " + userName
				+ ", password = " + password + ", reachabilityFailureReason = " + reachabilityFailureReason
				+ ", discoveryStartTime = " + discoveryStartTime + ", reachabilityStatus = " + reachabilityStatus + "]";
	}
}
