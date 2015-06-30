package com.cisco.apicem.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cisco.apicem.Constants;
import com.cisco.apicem.security.SecurityUtil;

@RestController
@RequestMapping(value = Constants.URI_API + Constants.URI_SELF)
public class CurrentUserController {

	private static final Logger log = LoggerFactory.getLogger(CurrentUserController.class);

	@Autowired
	private UserDetailsService userDetailsService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public UserDetails currentUser() {
		if (log.isDebugEnabled()) {
			log.debug("get current user info");
		}
		UserDetails details = userDetailsService.loadUserByUsername(SecurityUtil.currentUser().getUsername());
		if (log.isDebugEnabled()) {
			log.debug("current user value @" + details);
		}

		return details;
	}

}
