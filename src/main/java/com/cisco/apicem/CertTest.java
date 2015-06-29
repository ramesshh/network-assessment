package com.cisco.apicem;

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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class CertTest {

	public void main(String[] args) {

		try {

			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy())
					.useTLS().build();
			SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext,
					new AllowAllHostnameVerifier());
			BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("admin", "cisco1234"));

			HttpClient httpClient = HttpClientBuilder.create().setSSLSocketFactory(connectionFactory)
					.setDefaultCredentialsProvider(credentialsProvider).build();

			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
					httpClient);
			RestTemplate restTemplate = new RestTemplate(requestFactory);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			String requestJson = "{\"username\": \"admin\",  \"password\": \"cisco1234\"}";
			HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

			String str = restTemplate.postForObject("https://91.222.6.228/api/v1/ticket", entity, String.class);
			System.out.println("Str:::" + str);

			HttpHeaders headers1 = new HttpHeaders();
			headers1.add("X-Auth-Token", "ST-45-fu2vcIp2eb4kKgde1nUt-cas");

			HttpEntity entity1 = new HttpEntity(headers1);

			ResponseEntity<String> user = restTemplate.exchange("https://91.222.6.228/api/v1/network-device/1/100",
					HttpMethod.GET, entity1, String.class);
			System.out.println("Hello:::" + user.getBody());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
