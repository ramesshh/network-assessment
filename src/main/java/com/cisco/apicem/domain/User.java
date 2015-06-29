package com.cisco.apicem.domain;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Immutable
public class User {

	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String ROLES = "roles";

	@JsonProperty(USERNAME)
	private String username;

	@JsonProperty(PASSWORD)
	private String password;

	@JsonProperty(ROLES)
	@JsonSerialize(contentUsing = GrantedAuthoritySerializer.class)
	@JsonDeserialize(contentUsing = GrantedAuthorityDeserializer.class)
	private List<GrantedAuthority> roles;

	public User(String username, String password, String role) {
		this.username = username;
		this.password = password;
		if (roles == null) {
			roles = new ArrayList<GrantedAuthority>();
		}

	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public List<GrantedAuthority> getRoles() {
		return roles;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}