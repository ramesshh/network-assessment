package com.softql.apicem.model;

import java.io.Serializable;
import java.util.List;

public class DeviceQuestionare implements Serializable {

	private String discoveryId;

	private String discoveryType;

	private List<Questionare> questionare;

	public String getDiscoveryId() {
		return discoveryId;
	}

	public void setDiscoveryId(String discoveryId) {
		this.discoveryId = discoveryId;
	}

	public String getDiscoveryType() {
		return discoveryType;
	}

	public void setDiscoveryType(String discoveryType) {
		this.discoveryType = discoveryType;
	}

	public List<Questionare> getQuestionare() {
		return questionare;
	}

	public void setQuestionare(List<Questionare> questionare) {
		this.questionare = questionare;
	}
}
