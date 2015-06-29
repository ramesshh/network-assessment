package com.cisco.apicem.service;

import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.cisco.apicem.domain.User;

@Service
public class MongoDBAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Autowired
	private MongoCollection users;

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		UserDetails loadedUser = null;

		try {
			User client = users.findOne("{#: #}", User.USERNAME, username).as(User.class);
			loadedUser = new org.springframework.security.core.userdetails.User(client.getUsername(),
					client.getPassword(), client.getRoles());
		} catch (Exception repositoryProblem) {
			throw new InternalAuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
		}

		return loadedUser;
	}
}
