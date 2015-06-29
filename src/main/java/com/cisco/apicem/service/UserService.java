/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cisco.apicem.service;

import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.cisco.apicem.DTOUtils;
import com.cisco.apicem.domain.User;
import com.cisco.apicem.exception.ResourceNotFoundException;
import com.cisco.apicem.model.UserDetails;

/**
 *
 * @author
 */
@Service
@Transactional
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private MongoCollection users;

	public UserDetails findByUsername(String name) {
		Assert.notNull(name, "user id can not be null");
		if (log.isDebugEnabled()) {
			log.debug("find user by id @" + name);
		}
		User user = users.findOne("{#: #}", User.USERNAME, name).as(User.class);
		if (user == null) {
			throw new ResourceNotFoundException(name);
		}

		return DTOUtils.map(user, UserDetails.class);
	}

}
