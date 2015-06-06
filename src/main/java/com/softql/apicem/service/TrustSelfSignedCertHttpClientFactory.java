package com.apicem.network.discovery;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.AbstractVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.FactoryBean;

/**
 * Factory class to create HttpClient that allows self-signed certificate.
 */
public class TrustSelfSignedCertHttpClientFactory implements FactoryBean<HttpClient> {

  @Override
  public boolean isSingleton() {
    return true;
  }

  @Override
  public Class<?> getObjectType() {
    return HttpClient.class;
  }

  @Override
  public HttpClient getObject() throws Exception {

	  /*
	  // Create a trust manager that does not validate certificate chains
	    TrustManager[] trustAllCerts = new TrustManager[] { 
	      new X509TrustManager() {
	        public X509Certificate[] getAcceptedIssuers() { 
	          return new X509Certificate[0]; 
	        }
	        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
	        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
	    }};
	    
	    HostnameVerifier hv = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
	    
    // provide SSLContext that allows self-signed certificate
    SSLContext sc =
      new SSLContextBuilder()
        .loadTrustMaterial(null, new TrustSelfSignedStrategy())
        .build();
    
    sc.init(null, trustAllCerts, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    HttpsURLConnection.setDefaultHostnameVerifier(hv);

    SSLConnectionSocketFactory sslConnectionSocketFactory
      = new SSLConnectionSocketFactory(sc);

    // based on HttpClients.createSystem()
    return HttpClientBuilder.create()
      .useSystemProperties()
      .setSSLSocketFactory(sslConnectionSocketFactory)  // add custom config
      .build();*/
	  
	  // Create a trust manager that does not validate certificate chains
	    TrustManager[] trustAllCerts = new TrustManager[] { 
	      new X509TrustManager() {
	        public X509Certificate[] getAcceptedIssuers() { 
	          return new X509Certificate[0]; 
	        }
	        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
	        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
	    }};

	    // Ignore differences between given hostname and certificate hostname
	    	X509HostnameVerifier hv = new X509HostnameVerifier() {
	      public boolean verify(String hostname, SSLSession session) { return true; }
	      
	      @Override
	      public void verify(String host, SSLSocket ssl) throws IOException {
	         return;
	      }

	      @Override
	      public void verify(String host, X509Certificate cert) throws SSLException {
	    	  return;
	      }

	      @Override
	      public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
	    	  return;
	      }
	    };
	    
	    MyHostNameVerifier mv = new MyHostNameVerifier();

	    HttpClient client = null;
	    SSLConnectionSocketFactory sslConnectionSocketFactory =null;
	    // Install the all-trusting trust manager
	    try {
	      SSLContext sc = SSLContext.getInstance("SSL");
	      sc.init(null, trustAllCerts, new SecureRandom());
	      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	      HttpsURLConnection.setDefaultHostnameVerifier(mv);
	     
	    
	   sslConnectionSocketFactory
	      = new SSLConnectionSocketFactory(sc);

	    // based on HttpClients.createSystem()
	     client=HttpClientBuilder.create()
	      .useSystemProperties()
	      .setSSLSocketFactory(sslConnectionSocketFactory).setHostnameVerifier(mv)  // add custom config
	      .build();
	    
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    
	    return client;
  }
  
  public HttpClient disableCertificateValidation() {
	    // Create a trust manager that does not validate certificate chains
	    TrustManager[] trustAllCerts = new TrustManager[] { 
	      new X509TrustManager() {
	        public X509Certificate[] getAcceptedIssuers() { 
	          return new X509Certificate[0]; 
	        }
	        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
	        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
	    }};

	    // Ignore differences between given hostname and certificate hostname
	    	X509HostnameVerifier hv = new X509HostnameVerifier() {
	      public boolean verify(String hostname, SSLSession session) { return true; }
	      
	      @Override
	      public void verify(String host, SSLSocket ssl) throws IOException {
	         return;
	      }

	      @Override
	      public void verify(String host, X509Certificate cert) throws SSLException {
	    	  return;
	      }

	      @Override
	      public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
	    	  return;
	      }
	    };
	    
	    MyHostNameVerifier mv = new MyHostNameVerifier();

	    HttpClient client = null;
	    SSLConnectionSocketFactory sslConnectionSocketFactory =null;
	    // Install the all-trusting trust manager
	    try {
	      SSLContext sc = SSLContext.getInstance("SSL");
	      sc.init(null, trustAllCerts, new SecureRandom());
	      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	      HttpsURLConnection.setDefaultHostnameVerifier(mv);
	     
	    
	   sslConnectionSocketFactory
	      = new SSLConnectionSocketFactory(sc);

	    // based on HttpClients.createSystem()
	     client=HttpClientBuilder.create()
	      .useSystemProperties()
	      .setSSLSocketFactory(sslConnectionSocketFactory).setHostnameVerifier(mv)  // add custom config
	      .build();
	    
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    
	    return client;
	  }
}

class MyHostNameVerifier extends AbstractVerifier{

	@Override
	public void verify(String host, String[] cns, String[] subjectAlts)
			throws SSLException {
		return;
		
	}
	
}