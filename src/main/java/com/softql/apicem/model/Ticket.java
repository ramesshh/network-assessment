package com.softql.apicem.model;

public class Ticket {

	public class ServiceTicketResponse {

		String serviceTicket;

		public String getServiceTicket() {
			return serviceTicket;
		}

		public void setServiceTicket(String serviceTicket) {
			this.serviceTicket = serviceTicket;
		}
	}

	ServiceTicketResponse response;

	String version;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public ServiceTicketResponse getResponse() {
		return response;
	}

	public void setResponse(ServiceTicketResponse response) {
		this.response = response;
	}

}
