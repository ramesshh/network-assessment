/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.softql.apicem.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.net.ssl.SSLContext;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.softql.apicem.model.ApicEmLoginForm;
import com.softql.apicem.model.DeviceDetails;
import com.softql.apicem.model.DeviceTag;
import com.softql.apicem.model.DeviceTagResponse;
import com.softql.apicem.model.DiscoveryDevices;
import com.softql.apicem.model.Ticket;
import com.softql.apicem.model.TicketForm;
import com.softql.apicem.util.ApicemUtils;

/**
 *
 * @author Ramesh
 */
@Service
public class ApicEmService {

	private static final Logger log = LoggerFactory.getLogger(ApicEmService.class);

	private RestTemplate restTemplate;

	@Inject
	private MongoOperations mongoTemplate;

	private void getRestTemplate(String username, String password) {
		try {
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy())
					.useTLS().build();
			SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext,
					new AllowAllHostnameVerifier());
			BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
			HttpClient httpClient = HttpClientBuilder.create().setSSLSocketFactory(connectionFactory)
					.setDefaultCredentialsProvider(credentialsProvider).build();

			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
					httpClient);
			restTemplate = new RestTemplate(requestFactory);
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getToken(ApicEmLoginForm form, String url) {
		TicketForm ticketForm = new TicketForm();
		ticketForm.setUsername(form.getUsername());
		ticketForm.setPassword(form.getPassword());
		Ticket ticket = new Ticket();
		getRestTemplate(form.getUsername(), form.getPassword());
		ticket = restTemplate.postForObject(url, ticketForm, Ticket.class);
		return ticket.getResponse().getServiceTicket();
	}

	public List<DiscoveryDevices> getDevices(String url) {
		DeviceDetails deviceDetails = restTemplate.getForObject(url, DeviceDetails.class);
		List<DiscoveryDevices> arrayToList = CollectionUtils.arrayToList(deviceDetails.getResponse());
		return arrayToList;
	}

	public void onBoardApicem(ApicEmLoginForm form) {
		mongoTemplate.save(form, "apicems");
		System.out.println("apicem onboarded");

	}

	public List<ApicEmLoginForm> getApicEms(String userName) {
		// find
		List<ApicEmLoginForm> apicems = mongoTemplate.find(new Query(Criteria.where("userId").is(userName)),
				ApicEmLoginForm.class, "apicems");

		if (CollectionUtils.isEmpty(apicems)) {
			apicems = new ArrayList<ApicEmLoginForm>();
		}
		return apicems;
	}

	public Map<String, String> getTags(String url) {
		Map<String, String> tagsMap = new HashMap<String, String>();
		DeviceTagResponse deviceTagRes = restTemplate.getForObject(url, DeviceTagResponse.class);
		List<DeviceTag> arrayToList = CollectionUtils.arrayToList(deviceTagRes.getResponse());

		for (DeviceTag deviceTag : arrayToList) {
			String[] s = { deviceTag.getTag(), tagsMap.get(deviceTag.getNetworkDeviceId()) };
			tagsMap.put(deviceTag.getNetworkDeviceId(), ApicemUtils.join(',', s));
		}

		return tagsMap;

	}

}
