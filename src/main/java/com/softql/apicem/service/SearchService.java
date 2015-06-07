/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softql.apicem.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.softql.apicem.model.DeviceDetails;
import com.softql.apicem.model.DeviceQuestionare;
import com.softql.apicem.model.DiscoveryDevices;
import com.softql.apicem.model.Questionare;
import com.softql.apicem.model.ReachabilityDevice;
import com.softql.apicem.model.ReachabilityInfo;
import com.softql.apicem.util.IPAddressUtils;

/**
 *
 * @author
 */
@Service
public class SearchService {

	private static final Logger log = LoggerFactory.getLogger(SearchService.class);

	private static final int READ_TIME_OUT = 1000000;

	private static final int CONNECTION_TIME_OUT = 1000000;

	public List<DiscoveryDevices> searchDevicesByIP(String fromIp, String toIP) {
		List<DiscoveryDevices> deviceList = new ArrayList<DiscoveryDevices>();
		try {
			RestTemplate restTemplate = getRestTemplate();

			boolean ignore = StringUtils.isBlank(toIP) || StringUtils.isBlank(fromIp);

			ReachabilityInfo reachabilityInfo = restTemplate.getForObject(
					"https://sandboxapic.cisco.com:443/api/v0/reachability-info", ReachabilityInfo.class);

			if (reachabilityInfo != null && ArrayUtils.isNotEmpty(reachabilityInfo.getResponse())) {
				ReachabilityDevice[] response = reachabilityInfo.getResponse();
				for (ReachabilityDevice reachabilityDevice : response) {
					String ipAddress = reachabilityDevice.getMgmtIp();
					boolean isInRange = IPAddressUtils.isInRange(fromIp, toIP, ipAddress);
					if (isInRange || ignore) {
						try {
							DeviceDetails deviceDetails = getDeviceDetails(reachabilityDevice.getId());
							log.info("Device details {}", deviceDetails);
							// deviceList.add(deviceDetails.getResponse());
						} catch (Exception e) {
							log.info("Failed to fetch details id {}", reachabilityDevice.getId());
							e.printStackTrace();
							continue;
						}
					}
				}
			}
			log.info("Device list {}", deviceList);
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return deviceList;
	}

	public List<DiscoveryDevices> getDevices() {
		RestTemplate restTemplate = getRestTemplate();
		List<DiscoveryDevices> deviceList = new ArrayList<DiscoveryDevices>();
		DeviceDetails deviceDetails = restTemplate.getForObject(
				"https://sandboxapic.cisco.com/api/v0/network-device/1/10000000", DeviceDetails.class);
		List<DiscoveryDevices> arrayToList = CollectionUtils.arrayToList(deviceDetails.getResponse());
		deviceList.addAll(arrayToList);
		return deviceList;
	}

	private DeviceDetails getDeviceDetails(String deviceId) {
		RestTemplate restTemplate = getRestTemplate();
		DeviceDetails deviceDetails = restTemplate.getForObject(
				"https://sandboxapic.cisco.com:443/api/v0/network-device/" + deviceId, DeviceDetails.class);
		return deviceDetails;
	}

	private RestTemplate getRestTemplate() {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setReadTimeout(READ_TIME_OUT);
		requestFactory.setConnectTimeout(CONNECTION_TIME_OUT);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		return restTemplate;
	}

	public DeviceQuestionare getQuestionare(String discoveryId) {
		return testGetQuestionare(discoveryId);
	}

	private DeviceQuestionare testGetQuestionare(String discoveryId) {

		DeviceQuestionare deviceQuestionare = new DeviceQuestionare();
		deviceQuestionare.setDiscoveryId(discoveryId);
		deviceQuestionare.setDiscoveryType("Router");
		List<Questionare> questionare = new ArrayList<Questionare>();

		Questionare q1 = new Questionare();
		q1.setQuestion("Do you need Redundant power supply?");
		q1.setOptions(new String[] { "Yes", "No" });

		Questionare q2 = new Questionare();
		q2.setQuestion("Do you need integrated switch?");
		q2.setOptions(new String[] { "Yes", "No" });

		Questionare q3 = new Questionare();
		q3.setQuestion("How many ports?");
		q3.setOptions(new String[] { "10", "20", "30" });

		Questionare q4 = new Questionare();
		q4.setQuestion("DO you need to host applications ?");
		q4.setOptions(new String[] { "Yes", "No" });

		Questionare q5 = new Questionare();
		q5.setQuestion("Do you need to Secure WAN?");
		q5.setOptions(new String[] { "Yes", "No" });

		Questionare q6 = new Questionare();
		q6.setQuestion("Are you planning to deploy Intelligent WAN Solution?");
		q6.setOptions(new String[] { "Yes", "No" });

		Questionare q7 = new Questionare();
		q7.setQuestion("Are you going to enable Cisco Unified Communication solution?");
		q7.setOptions(new String[] { "Yes", "No" });

		Questionare[] q = { q1, q2, q3, q4, q5, q6, q7 };
		questionare.addAll(Arrays.asList(q));

		deviceQuestionare.setQuestionare(questionare);
		return deviceQuestionare;
	}

	public List<DiscoveryDevices> replaceDevices(String deviceId) {
		List<DiscoveryDevices> deviceDetails = new ArrayList<DiscoveryDevices>();
		// deviceDetails.add(getDeviceDetails(deviceId).getResponse());
		return deviceDetails;
	}

}
