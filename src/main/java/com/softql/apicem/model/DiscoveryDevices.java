package com.softql.apicem.model;

import java.io.Serializable;

/**
 *
 * @author
 *
 */
public class DiscoveryDevices implements Serializable {

	private static final long serialVersionUID = 1L;

	private String memorySize;

	private String imageName;

	private String roleSource;

	private String vendor;

	private String portRange;

	private String lastUpdated;

	private String hostname;

	private String serialNumber;

	private String managementIpAddress;

	private String macAddress;

	private String type;

	private String reachabilityFailureReason;

	private String platformId;

	private String reachabilityStatus;

	private String interfaceCount;

	private String id;

	private String lineCardId;

	private String upTime;

	private String family;

	private String lineCardCount;

	private String role;

	private String softwareVersion;

	private String numUpdates;

	private String avgUpdateFrequency;

	private String deviceType;

	private String locationName;

	private int qty = 1;

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	private boolean nonCiscoDevice;

	public String getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(String memorySize) {
		this.memorySize = memorySize;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getRoleSource() {
		return roleSource;
	}

	public void setRoleSource(String roleSource) {
		this.roleSource = roleSource;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getPortRange() {
		return portRange;
	}

	public void setPortRange(String portRange) {
		this.portRange = portRange;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getManagementIpAddress() {
		return managementIpAddress;
	}

	public void setManagementIpAddress(String managementIpAddress) {
		this.managementIpAddress = managementIpAddress;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReachabilityFailureReason() {
		return reachabilityFailureReason;
	}

	public void setReachabilityFailureReason(String reachabilityFailureReason) {
		this.reachabilityFailureReason = reachabilityFailureReason;
	}

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public String getReachabilityStatus() {
		return reachabilityStatus;
	}

	public void setReachabilityStatus(String reachabilityStatus) {
		this.reachabilityStatus = reachabilityStatus;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public boolean isNonCiscoDevice() {
		return nonCiscoDevice;
	}

	public void setNonCiscoDevice(boolean nonCiscoDevice) {
		this.nonCiscoDevice = nonCiscoDevice;
	}

	public String getInterfaceCount() {
		return interfaceCount;
	}

	public void setInterfaceCount(String interfaceCount) {
		this.interfaceCount = interfaceCount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLineCardId() {
		return lineCardId;
	}

	public void setLineCardId(String lineCardId) {
		this.lineCardId = lineCardId;
	}

	public String getUpTime() {
		return upTime;
	}

	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getLineCardCount() {
		return lineCardCount;
	}

	public void setLineCardCount(String lineCardCount) {
		this.lineCardCount = lineCardCount;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSoftwareVersion() {
		return softwareVersion;
	}

	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	public String getNumUpdates() {
		return numUpdates;
	}

	public void setNumUpdates(String numUpdates) {
		this.numUpdates = numUpdates;
	}

	public String getAvgUpdateFrequency() {
		return avgUpdateFrequency;
	}

	public void setAvgUpdateFrequency(String avgUpdateFrequency) {
		this.avgUpdateFrequency = avgUpdateFrequency;
	}

	@Override
	public String toString() {
		return "ClassPojo [memorySize = " + memorySize + ", imageName = " + imageName + ", roleSource = " + roleSource
				+ ", vendor = " + vendor + ", portRange = " + portRange + ", lastUpdated = " + lastUpdated
				+ ", hostname = " + hostname + ", serialNumber = " + serialNumber + ", managementIpAddress = "
				+ managementIpAddress + ", macAddress = " + macAddress + ", type = " + type
				+ ", reachabilityFailureReason = " + reachabilityFailureReason + ", platformId = " + platformId
				+ ", reachabilityStatus = " + reachabilityStatus + ", interfaceCount = " + interfaceCount + ", id = "
				+ id + ", lineCardId = " + lineCardId + ", upTime = " + upTime + ", family = " + family
				+ ", lineCardCount = " + lineCardCount + ", role = " + role + ", softwareVersion = " + softwareVersion
				+ ", numUpdates = " + numUpdates + ", avgUpdateFrequency = " + avgUpdateFrequency + "]";
	}

}
