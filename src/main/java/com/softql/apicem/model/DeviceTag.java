package com.softql.apicem.model;

public class DeviceTag {
	private String id;

	private String networkDeviceId;

	/* private String attributeInfo; */

	private String tag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNetworkDeviceId() {
		return networkDeviceId;
	}

	public void setNetworkDeviceId(String networkDeviceId) {
		this.networkDeviceId = networkDeviceId;
	}

	/*
	 * public String getAttributeInfo() { return attributeInfo; }
	 * 
	 * public void setAttributeInfo(String attributeInfo) { this.attributeInfo =
	 * attributeInfo; }
	 */

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

}
